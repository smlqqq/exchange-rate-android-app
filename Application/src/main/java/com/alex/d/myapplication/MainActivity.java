package com.alex.d.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alex.d.myapplication.model.BankInfo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar loadingIndicator;
    private View errorState;
    private BankAdapter adapter;
    private ExchangeRatesApi api;

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

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:9099/") // Base URL with trailing slash
                .baseUrl("https://exchange-rate-data-parser.onrender.com") // Base URL with trailing slash
                .addConverterFactory(GsonConverterFactory.create()) // JSON converter
                .build();

        api = retrofit.create(ExchangeRatesApi.class);

        showLoading();
        fetchData();
    }

    private void fetchData() {
        api.getExchangeRates().enqueue(new Callback<List<ListItemClass>>() {
            @Override
            public void onResponse(Call<List<ListItemClass>> call, Response<List<ListItemClass>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.submitList(response.body());
                    ExchangeRatesRepository.getInstance().setItems(response.body());
                    showContent();
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<List<ListItemClass>> call, Throwable t) {
                Log.e("Retrofit", "Failed to fetch exchange rates: " + t.getMessage());
                swipeRefresh.setRefreshing(false);
                showError();
            }
        });
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
     * screen. If a background refresh fails but we're already showing a list,
     * we keep the list visible instead of yanking it away for a transient
     * network hiccup.
     */
    private void showError() {
        loadingIndicator.setVisibility(View.GONE);
        if (adapter.getItemCount() == 0) {
            errorState.setVisibility(View.VISIBLE);
            swipeRefresh.setVisibility(View.GONE);
        }
    }
}
