package com.alex.d.myapplication;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ExchangeRatesResponse {
    @SerializedName("exchangeRates")
    private List<ListItemClass> exchangeRates;

    public List<ListItemClass> getExchangeRates() {
        return exchangeRates;
    }

}
