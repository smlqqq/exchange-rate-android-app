package com.alex.d.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

public class CalculatorActivity extends AppCompatActivity {

    private enum Direction { CURRENCY_TO_MDL, MDL_TO_CURRENCY }

    /** Currencies the calculator supports, with their rate accessors on ListItemClass. */
    private enum Currency {
        USD("USD", ListItemClass::getUsdB, ListItemClass::getUsdS),
        EUR("EUR", ListItemClass::getEuroB, ListItemClass::getEuroS),
        RON("RON", ListItemClass::getRoLeuB, ListItemClass::getRoLeuS),
        GBP("GBP", ListItemClass::getGbpB, ListItemClass::getGbpS);

        private final String label;
        private final RateExtractor buyExtractor;
        private final RateExtractor sellExtractor;

        Currency(String label, RateExtractor buyExtractor, RateExtractor sellExtractor) {
            this.label = label;
            this.buyExtractor = buyExtractor;
            this.sellExtractor = sellExtractor;
        }

        String getLabel() {
            return label;
        }

        String getBuy(ListItemClass item) {
            return buyExtractor.get(item);
        }

        String getSell(ListItemClass item) {
            return sellExtractor.get(item);
        }
    }

    /** Local functional interface (not java.util.function) to stay safe on older minSdk. */
    private interface RateExtractor {
        String get(ListItemClass item);
    }

    private Spinner currencySpinner;
    private Spinner bankSpinner;
    private MaterialButtonToggleGroup directionToggle;
    private TextInputEditText amountInput;
    private TextView resultText;
    private TextView rateInfoText;
    private View emptyState;
    private View formContainer;

    private List<ListItemClass> banks;
    private Direction direction = Direction.CURRENCY_TO_MDL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_calculator);

        View mainView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return windowInsets;
        });

        MaterialToolbar toolbar = findViewById(R.id.calcToolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        currencySpinner = findViewById(R.id.currencySpinner);
        bankSpinner = findViewById(R.id.bankSpinner);
        directionToggle = findViewById(R.id.directionToggle);
        amountInput = findViewById(R.id.amountInput);
        resultText = findViewById(R.id.resultText);
        rateInfoText = findViewById(R.id.rateInfoText);
        emptyState = findViewById(R.id.calcEmptyState);
        formContainer = findViewById(R.id.calcFormContainer);

        banks = ExchangeRatesRepository.getInstance().getItems();

        if (banks.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            formContainer.setVisibility(View.GONE);
            return;
        }

        emptyState.setVisibility(View.GONE);
        formContainer.setVisibility(View.VISIBLE);

        setupCurrencySpinner();
        setupBankSpinner();
        setupDirectionToggle();
        setupAmountInput();

        String bankName = getIntent().getStringExtra("BANK_NAME");
        String currencyCode = getIntent().getStringExtra("CURRENCY");

        if (bankName != null) {
            for (int i = 0; i < banks.size(); i++) {
                if (banks.get(i).getBank().equals(bankName)) {
                    bankSpinner.setSelection(i);
                    break;
                }
            }
        }

        if (currencyCode != null) {
            Currency[] vals = Currency.values();
            for (int i = 0; i < vals.length; i++) {
                if (vals[i].getLabel().equals(currencyCode)) {
                    currencySpinner.setSelection(i);
                    break;
                }
            }
        }

        recalculate();
    }

    private void setupCurrencySpinner() {
        Currency[] values = Currency.values();
        String[] labels = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            labels[i] = values[i].getLabel();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(adapter);
        currencySpinner.setOnItemSelectedListener(new SimpleSelectionListener(this::recalculate));
    }

    private void setupBankSpinner() {
        String[] names = new String[banks.size()];
        for (int i = 0; i < banks.size(); i++) {
            names[i] = banks.get(i).getBank();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankSpinner.setAdapter(adapter);
        bankSpinner.setOnItemSelectedListener(new SimpleSelectionListener(this::recalculate));
    }

    private void setupDirectionToggle() {
        directionToggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) return;
            direction = checkedId == R.id.btnCurrencyToMdl ? Direction.CURRENCY_TO_MDL : Direction.MDL_TO_CURRENCY;
            recalculate();
        });
    }

    private void setupAmountInput() {
        amountInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                recalculate();
            }
        });
    }

    private void recalculate() {
        int bankPos = bankSpinner.getSelectedItemPosition();
        int currencyPos = currencySpinner.getSelectedItemPosition();
        if (bankPos < 0 || bankPos >= banks.size() || currencyPos < 0) return;

        ListItemClass bank = banks.get(bankPos);
        Currency currency = Currency.values()[currencyPos];

        String buyStr = currency.getBuy(bank);
        String sellStr = currency.getSell(bank);

        if (isEmpty(buyStr) || isEmpty(sellStr)) {
            resultText.setText(getString(R.string.calc_no_rate));
            rateInfoText.setText("");
            return;
        }

        double buy = parseOrZero(buyStr);
        double sell = parseOrZero(sellStr);
        double amount = parseOrZero(amountInput.getText() != null ? amountInput.getText().toString() : "");

        String directionLabel = direction == Direction.CURRENCY_TO_MDL ? getString(R.string.buy) : getString(R.string.sell);
        double rate = direction == Direction.CURRENCY_TO_MDL ? buy : sell;

        if (direction == Direction.CURRENCY_TO_MDL) {
            double result = amount * buy;
            resultText.setText(String.format(Locale.getDefault(), "%.2f %s", result, getString(R.string.mdl)));
        } else {
            double result = sell != 0 ? amount / sell : 0;
            resultText.setText(String.format(Locale.getDefault(), "%.2f %s", result, currency.getLabel()));
        }

        rateInfoText.setText(getString(R.string.calc_rate_info_format,
                currency.getLabel(), rate, bank.getBank(), directionLabel.toLowerCase(Locale.getDefault())));
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static double parseOrZero(String s) {
        if (s == null) return 0;
        try {
            return Double.parseDouble(s.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** Adapts a Runnable to AdapterView.OnItemSelectedListener so spinners can share one lambda. */
    private static class SimpleSelectionListener implements AdapterView.OnItemSelectedListener {
        private final Runnable onSelected;

        SimpleSelectionListener(Runnable onSelected) {
            this.onSelected = onSelected;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            onSelected.run();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }
}
