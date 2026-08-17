package com.alex.d.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.d.myapplication.model.BankInfo;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankViewHolder> {

    private final Context context;
    private final Map<String, BankInfo> bankInfoByName = new HashMap<>();
    private List<ListItemClass> items = new ArrayList<>();

    public BankAdapter(Context context, List<BankInfo> bankInfoList) {
        this.context = context;
        for (BankInfo info : bankInfoList) {
            bankInfoByName.put(normalize(info.getMatchKey()), info);
        }
    }

    /**
     * Replaces the current list with newItems, computing a diff so the
     * RecyclerView animates only the rows that actually changed instead of
     * redrawing everything on every refresh.
     */
    public void submitList(List<ListItemClass> newItems) {
        List<ListItemClass> safeNewItems = newItems != null ? newItems : new ArrayList<>();
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new BankDiffCallback(items, safeNewItems));
        items = new ArrayList<>(safeNewItems);
        result.dispatchUpdatesTo(this);
    }

    // strips diacritics, lowercases, drops non-alphanumerics so
    // "Comerțbank", "comertbank", "Comerțbank " all match the same key
    private static String normalize(String s) {
        if (s == null) return "";
        String decomposed = Normalizer.normalize(s, Normalizer.Form.NFD);
        String stripped = decomposed.replaceAll("\\p{M}", "");
        return stripped.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {
        ListItemClass item = items.get(position);
        BankInfo info = bankInfoByName.get(normalize(item.getBank()));
        holder.bind(item, info, context);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class BankViewHolder extends RecyclerView.ViewHolder {
        private final ImageView logo;
        private final TextView bankName;
        private final CurrencyRow usdRow;
        private final CurrencyRow euroRow;
        private final CurrencyRow ronRow;
        private final CurrencyRow gbpRow;

        private final View containerUsd;
        private final View containerEuro;
        private final View containerRon;
        private final View containerGbp;

        BankViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.bankLogo);
            bankName = itemView.findViewById(R.id.bankName);
            usdRow = new CurrencyRow(itemView.findViewById(R.id.rowUsd), "USD");
            euroRow = new CurrencyRow(itemView.findViewById(R.id.rowEuro), "EUR");
            ronRow = new CurrencyRow(itemView.findViewById(R.id.rowRon), "RON");
            gbpRow = new CurrencyRow(itemView.findViewById(R.id.rowGbp), "GBP");

            containerUsd = itemView.findViewById(R.id.containerUsd);
            containerEuro = itemView.findViewById(R.id.containerEuro);
            containerRon = itemView.findViewById(R.id.containerRon);
            containerGbp = itemView.findViewById(R.id.containerGbp);
        }

        void bind(ListItemClass item, BankInfo info, Context context) {
            bankName.setText(item.getBank());
            logo.setImageResource(info != null ? info.getImageResId() : R.drawable.block);

            usdRow.bind(item.getUsdB(), item.getUsdS());
            euroRow.bind(item.getEuroB(), item.getEuroS());
            ronRow.bind(item.getRoLeuB(), item.getRoLeuS());
            gbpRow.bind(item.getGbpB(), item.getGbpS());

            setupClick(containerUsd, item, "USD", context);
            setupClick(containerEuro, item, "EUR", context);
            setupClick(containerRon, item, "RON", context);
            setupClick(containerGbp, item, "GBP", context);

            // Restore navigation to bank website on header click
            final String url = info != null ? info.getUrl() : null;
            View header = itemView.findViewById(R.id.bankHeader);
            if (header != null) {
                header.setOnClickListener(v -> {
                    if (url != null && !url.isEmpty()) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });
            }
        }

        private void setupClick(View view, ListItemClass item, String currency, Context context) {
            view.setOnClickListener(v -> {
                // Here we would open the specific currency view.
                // For now, let's open Calculator with that currency selected.
                Intent intent = new Intent(context, CalculatorActivity.class);
                intent.putExtra("BANK_NAME", item.getBank());
                intent.putExtra("CURRENCY", currency);
                context.startActivity(intent);
            });
        }
    }

    /** Wraps one <include layout="@layout/row_currency"/> block: code | buy | sell. */
    private static class CurrencyRow {
        private final View row;
        private final TextView buy;
        private final TextView sell;

        CurrencyRow(View row, String code) {
            this.row = row;
            TextView codeView = row.findViewById(R.id.currencyCode);
            this.buy = row.findViewById(R.id.buyValue);
            this.sell = row.findViewById(R.id.sellValue);
            codeView.setText(code);
        }

        void bind(String buyValue, String sellValue) {
            boolean hasData = buyValue != null && !buyValue.isEmpty()
                    && sellValue != null && !sellValue.isEmpty();
            row.setVisibility(hasData ? View.VISIBLE : View.GONE);
            if (hasData) {
                buy.setText(buyValue);
                sell.setText(sellValue);
            }
        }
    }

    private static class BankDiffCallback extends DiffUtil.Callback {
        private final List<ListItemClass> oldList;
        private final List<ListItemClass> newList;

        BankDiffCallback(List<ListItemClass> oldList, List<ListItemClass> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            String a = oldList.get(oldItemPosition).getBank();
            String b = newList.get(newItemPosition).getBank();
            return Objects.equals(a, b);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ListItemClass a = oldList.get(oldItemPosition);
            ListItemClass b = newList.get(newItemPosition);
            // compared field by field rather than relying on equals()/toString(),
            // since ListItemClass may not override them
            return Objects.equals(a.getBank(), b.getBank())
                    && Objects.equals(a.getUsdB(), b.getUsdB())
                    && Objects.equals(a.getUsdS(), b.getUsdS())
                    && Objects.equals(a.getEuroB(), b.getEuroB())
                    && Objects.equals(a.getEuroS(), b.getEuroS())
                    && Objects.equals(a.getRoLeuB(), b.getRoLeuB())
                    && Objects.equals(a.getRoLeuS(), b.getRoLeuS())
                    && Objects.equals(a.getGbpB(), b.getGbpB())
                    && Objects.equals(a.getGbpS(), b.getGbpS());
        }
    }
}
