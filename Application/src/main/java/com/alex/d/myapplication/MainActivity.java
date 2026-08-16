package com.alex.d.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alex.d.myapplication.model.BankInfo;
import com.google.android.material.snackbar.Snackbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private FrameLayout errorOverlay;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;
    private ExchangeRatesApi api;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.main);

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();

        List<BankInfo> bankInfoList = Arrays.asList(BankInfo.values());


        adapter = new CustomArrayAdapter(this, R.layout.row2, getLayoutInflater(), bankInfoList, arrayList);
        listView.setAdapter(adapter);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:9099/") // Base URL with trailing slash
                .baseUrl("https://exchange-rate-data-parser.onrender.com") // Base URL with trailing slash
                .addConverterFactory(GsonConverterFactory.create()) // JSON converter
                .build();

        api = retrofit.create(ExchangeRatesApi.class);

        fetchData();
    }

    private void fetchData() {
        api.getExchangeRates().enqueue(new Callback<List<ListItemClass>>() {
            @Override
            public void onResponse(Call<List<ListItemClass>> call, Response<List<ListItemClass>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arrayList.clear();
                    arrayList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    hideErrorOverlay();
                } else {
                    showErrorOverlay();
                }
            }

            @Override
            public void onFailure(Call<List<ListItemClass>> call, Throwable t) {
                Log.e("Retrofit", "Failed to fetch exchange rates: " + t.getMessage());
                showErrorOverlay();
            }
        });
    }

    private void showErrorOverlay() {
        if (errorOverlay == null) {
            errorOverlay = new FrameLayout(this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            errorOverlay.setLayoutParams(params);

            LayoutInflater inflater = LayoutInflater.from(this);
            View overlayView = inflater.inflate(R.layout.overlay_error_layout, errorOverlay, false);

            ImageView imageView = overlayView.findViewById(R.id.image);
            TextView textView = overlayView.findViewById(R.id.text);
            Button refreshButton = overlayView.findViewById(R.id.refresh_button);

            imageView.setImageResource(R.drawable.ic_error);
            textView.setText("Невозможно получить данные.");

            refreshButton.setOnClickListener(v -> {
                errorOverlay.setVisibility(View.GONE);
                fetchData();
            });

            errorOverlay.addView(overlayView);
            addContentView(errorOverlay, params);
        }
        errorOverlay.setVisibility(View.VISIBLE);
    }

    private void hideErrorOverlay() {
        if (errorOverlay != null) {
            errorOverlay.setVisibility(View.GONE);
        }
    }
}