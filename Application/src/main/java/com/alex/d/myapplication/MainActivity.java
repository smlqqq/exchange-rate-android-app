package com.alex.d.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;

String[] urls = {
        "https://www.bnm.md/",
        "https://www.maib.md/ro",
        "https://www.micb.md/",
        "https://www.victoriabank.md/ru/",
        "https://www.mobiasbanca.md/",
        "https://eximbank.md/ro",
        "https://www.procreditbank.md/",
        "https://fincombank.com/",
        "https://www.energbank.com",
        "https://www.bcr.md/",
        "https://comertbank.md/",
        "https://www.ecb.md/",
        "https://valutar.md/ru/exchange-offices/deghest-csv",
        "https://valutar.md/ru/exchange-offices/clio-csv",
        "https://valutar.md/ru/exchange-offices/orion-csv",
        "https://valutar.md/ru/exchange-offices/profx-schimb-csv",
        "https://valutar.md/ru/exchange-offices/ciocana-csv",
        "https://valutar.md/ru/exchange-offices/calisto-ng-csv",
        "https://valutar.md/ru/exchange-offices/nelus-grup-csv",
        "https://valutar.md/ru/exchange-offices/protanir-csv",
        "https://valutar.md/ru/exchange-offices/vadisan-csv"
};

    int[] img = {R.drawable.nationala, R.drawable.agroindbank, R.drawable.moldindconbank,
            R.drawable.victoriabank, R.drawable.mobiasbanca, R.drawable.eximbank,
            R.drawable.procredit, R.drawable.fincombank, R.drawable.energbank,
            R.drawable.bcr, R.drawable.comertbank, R.drawable.eurocreditbank,
            R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block,
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(this, R.layout.row2, arrayList, getLayoutInflater(), urls, img);
        listView.setAdapter(adapter);

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.100.5:8080") // Base URL
                .addConverterFactory(GsonConverterFactory.create()) // JSON converter
                .build();

        // Create API instance
        ExchangeRatesApi api = retrofit.create(ExchangeRatesApi.class);

        // Make network call asynchronously
        api.getExchangeRates().enqueue(new Callback<ExchangeRatesResponse>() {
            @Override
            public void onResponse(Call<ExchangeRatesResponse> call, Response<ExchangeRatesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ListItemClass> exchangeRates = response.body().getExchangeRates();
                    arrayList.clear();
                    arrayList.addAll(exchangeRates);
                    adapter.notifyDataSetChanged();
                } else {
                    // Handle error
                }
            }

            @Override
            public void onFailure(Call<ExchangeRatesResponse> call, Throwable t) {
                t.printStackTrace();
                // Handle failure
            }
        });
    }
}

