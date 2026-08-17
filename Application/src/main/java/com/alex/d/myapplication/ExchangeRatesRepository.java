package com.alex.d.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory holder for the most recently scraped exchange rates, backed by a
 * small on-disk cache (SharedPreferences) so the last known rates survive
 * process death / app restarts. This replaces the old Redis cache that used
 * to live on the Spring backend — there's no server anymore, so the cache
 * lives on the device instead.
 */
public class ExchangeRatesRepository {

    private static final String PREFS_NAME = "exchange_rates_prefs";
    private static final String KEY_CACHED_JSON = "cached_rates_json";

    private static final ExchangeRatesRepository INSTANCE = new ExchangeRatesRepository();
    private static final Gson GSON = new Gson();

    private List<ListItemClass> items = new ArrayList<>();

    private ExchangeRatesRepository() {
    }

    public static ExchangeRatesRepository getInstance() {
        return INSTANCE;
    }

    public void setItems(List<ListItemClass> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public List<ListItemClass> getItems() {
        return items;
    }

    public void clearTempData() {
        // Implementation for clearing non-essential data to save memory
    }

    /** Writes the current in-memory list to disk. Call this after a successful scrape. */
    public void persist(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_CACHED_JSON, GSON.toJson(items)).apply();
    }

    /**
     * Loads whatever was last persisted into memory. Call once, early in
     * MainActivity.onCreate, before deciding whether to show a loading
     * spinner or the cached list.
     */
    public List<ListItemClass> loadCached(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_CACHED_JSON, null);
        if (json != null) {
            Type type = new TypeToken<List<ListItemClass>>() {
            }.getType();
            List<ListItemClass> cached = GSON.fromJson(json, type);
            if (cached != null) {
                items = cached;
            }
        }
        return items;
    }
}
