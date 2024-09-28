package com.alex.d.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExchangeRatesApi {
    @GET("/api/data/latest")
    Call<ExchangeRatesResponse> getExchangeRates();
}
