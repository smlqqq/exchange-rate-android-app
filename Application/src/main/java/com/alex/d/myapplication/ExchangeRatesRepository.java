package com.alex.d.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Tiny in-memory singleton holding the most recently fetched exchange rates.
 * MainActivity populates it after a successful fetch; CalculatorActivity reads
 * from it instead of hitting the network again. Data is lost on process death,
 * which is fine here — CalculatorActivity just shows an empty state asking the
 * user to open the main list first in that case.
 */
public class ExchangeRatesRepository {

    private static final ExchangeRatesRepository INSTANCE = new ExchangeRatesRepository();

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
}
