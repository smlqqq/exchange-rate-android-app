package com.alex.d.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExchangeRatesApi {
    @GET("/api/data/latest")
    Call<List<ListItemClass>> getExchangeRates();
}
