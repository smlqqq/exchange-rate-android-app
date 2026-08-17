package com.alex.d.myapplication;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

/**
 * Periodically re-scrapes valutar.md in the background so the cached data
 * (and therefore the calculator) stays reasonably fresh even if the app
 * hasn't been opened in a while. WorkManager always runs doWork() off the
 * main thread, so the blocking Jsoup calls in WebScrapingService are safe
 * to call directly here.
 */
public class ExchangeRatesWorker extends Worker {

    public ExchangeRatesWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        List<ListItemClass> data = new WebScrapingService().scrapeData();
        if (data.isEmpty()) {
            // network hiccup or the site is temporarily unreachable — let
            // WorkManager retry with its default backoff instead of
            // overwriting a good cache with nothing
            return Result.retry();
        }
        ExchangeRatesRepository.getInstance().setItems(data);
        ExchangeRatesRepository.getInstance().persist(getApplicationContext());
        return Result.success();
    }
}
