package com.alex.d.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alex.d.myapplication.model.BankInfo;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class CustomArrayAdapter extends ArrayAdapter<ListItemClass> {
    private List<ListItemClass> listItem;
    private Context context;
    private final Map<String, BankInfo> bankInfoByName = new HashMap<>();

    public CustomArrayAdapter(@NonNull Context context, int resource, LayoutInflater layoutInflater, List<BankInfo> bankInfoList, List<ListItemClass> listItem) {
        super(context, resource, listItem);
        this.context = context;
        this.listItem = listItem;

        for (BankInfo info : bankInfoList) {
            bankInfoByName.put(normalize(info.getMatchKey()), info);
        }
    }

    // strips diacritics, lowercases, drops non-alphanumerics so
    // "Comerțbank", "comertbank", "Comerțbank " all match the same key
    private static String normalize(String s) {
        if (s == null) return "";
        String decomposed = Normalizer.normalize(s, Normalizer.Form.NFD);
        String stripped = decomposed.replaceAll("\\p{M}", "");
        return stripped.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        ListItemClass listItemMain = listItem.get(position);
        BankInfo bankInfo = bankInfoByName.get(normalize(listItemMain.getBank()));

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row2, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (bankInfo != null) {
            viewHolder.gifImageView.setImageResource(bankInfo.getImageResId());
        } else {
            // unknown/new bank name from backend — don't crash, just show a placeholder
            viewHolder.gifImageView.setImageResource(R.drawable.block);
        }

        viewHolder.bankName.setText(listItemMain.getBank());
        viewHolder.usdB.setText(listItemMain.getUsdB());
        viewHolder.usdS.setText(listItemMain.getUsdS());
        viewHolder.euroB.setText(listItemMain.getEuroB());
        viewHolder.euroS.setText(listItemMain.getEuroS());
        viewHolder.roLeuB.setText(listItemMain.getRoLeuB());
        viewHolder.roLeuS.setText(listItemMain.getRoLeuS());
        viewHolder.gbpB.setText(listItemMain.getGbpB());
        viewHolder.gbpS.setText(listItemMain.getGbpS());

        final String url = bankInfo != null ? bankInfo.getUrl() : null;
        convertView.setOnClickListener(v -> {
            if (url != null && !url.isEmpty()) {
                Intent openLinks = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(openLinks);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        GifImageView gifImageView;
        TextView bankName;
        TextView usdB;
        TextView usdS;
        TextView euroB;
        TextView euroS;
        TextView roLeuB;
        TextView roLeuS;
        TextView gbpB;
        TextView gbpS;

        public ViewHolder(View v) {
            gifImageView = v.findViewById(R.id.gifView);
            bankName = v.findViewById(R.id.bankName);
            usdB = v.findViewById(R.id.usdBuy);
            usdS = v.findViewById(R.id.usdSell);
            euroB = v.findViewById(R.id.euroBuy);
            euroS = v.findViewById(R.id.euroSell);
            roLeuB = v.findViewById(R.id.roLeuBuy);
            roLeuS = v.findViewById(R.id.roLeuSell);
            gbpB = v.findViewById(R.id.gbpBuy);
            gbpS = v.findViewById(R.id.gbpSell);
        }
    }
}