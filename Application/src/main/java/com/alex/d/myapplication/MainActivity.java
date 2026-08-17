package com.alex.d.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.alex.d.myapplication.model.BankInfo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String REFRESH_WORK_NAME = "exchange_rates_refresh";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar loadingIndicator;
    private View errorState;
    private BankAdapter adapter;

    private final WebScrapingService scraper = new WebScrapingService();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        loadingIndicator = findViewById(R.id.loadingIndicator);
        errorState = findViewById(R.id.errorState);
        MaterialButton retryButton = findViewById(R.id.retryButton);
        FloatingActionButton calculatorFab = findViewById(R.id.calculatorFab);
        calculatorFab.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CalculatorActivity.class)));

        List<BankInfo> bankInfoList = Arrays.asList(BankInfo.values());
        adapter = new BankAdapter(this, bankInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::fetchData);
        retryButton.setOnClickListener(v -> fetchData());

        // show whatever we had cached from the last successful scrape immediately,
        // instead of a blank spinner, while a fresh scrape runs in the background
        List<ListItemClass> cached = ExchangeRatesRepository.getInstance().loadCached(this);
        if (!cached.isEmpty()) {
            adapter.submitList(cached);
            showContent();
        } else {
            showLoading();
        }

        scheduleBackgroundRefresh();
        fetchData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }

    /** Scrapes valutar.md on a background thread; safe to call repeatedly (pull-to-refresh, retry, startup). */
    private void fetchData() {
        executor.execute(() -> {
            List<ListItemClass> result = scraper.scrapeData();
            runOnUiThread(() -> {
                swipeRefresh.setRefreshing(false);
                if (!result.isEmpty()) {
                    adapter.submitList(result);
                    ExchangeRatesRepository.getInstance().setItems(result);
                    ExchangeRatesRepository.getInstance().persist(this);
                    showContent();
                } else {
                    showError();
                }
            });
        });
    }

    /**
     * Registers a periodic background scrape every 30 minutes so data stays
     * fresh even if the app isn't opened. 30 minutes is well above
     * WorkManager's 15-minute floor and matches how often bank rates
     * actually change in practice (a few times a day at most).
     * ExistingPeriodicWorkPolicy.KEEP means calling this on every app start
     * is harmless — it won't reschedule if a job is already queued.
     */
    private void scheduleBackgroundRefresh() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                ExchangeRatesWorker.class, 30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                REFRESH_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request);
    }

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        errorState.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.GONE);
    }

    private void showContent() {
        loadingIndicator.setVisibility(View.GONE);
        errorState.setVisibility(View.GONE);
        swipeRefresh.setVisibility(View.VISIBLE);
    }

    /**
     * Only shows the full-screen error state if we don't already have data on
     * screen (cached or freshly loaded). If a background refresh fails but
     * we're already showing a list, we keep the list visible instead of
     * yanking it away for a transient network hiccup.
     */
    private void showError() {
        loadingIndicator.setVisibility(View.GONE);
        if (adapter.getItemCount() == 0) {
            errorState.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
        }
    }
}
