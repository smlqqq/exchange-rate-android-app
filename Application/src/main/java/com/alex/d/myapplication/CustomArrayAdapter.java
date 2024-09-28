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

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class CustomArrayAdapter extends ArrayAdapter<ListItemClass> {
    private LayoutInflater inflater;
    private List<ListItemClass> listItem;
    private Context context;
    private List<BankInfo> bankInfoList;

    public CustomArrayAdapter(@NonNull Context context, int resource, LayoutInflater inflater, List<BankInfo> bankInfoList, List<ListItemClass> listItem) {
        super(context, resource, listItem);
        this.inflater = inflater;
        this.listItem = listItem;
        this.context = context;
        this.bankInfoList = bankInfoList;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        ListItemClass listItemMain = listItem.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row2, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Устанавливаем изображение и текст
        viewHolder.gifImageView.setImageResource(bankInfoList.get(position).getImageResId());
        viewHolder.bankName.setText(listItemMain.getBank());
        viewHolder.usdB.setText(listItemMain.getUsdB());
        viewHolder.usdS.setText(listItemMain.getUsdS());
        viewHolder.euroB.setText(listItemMain.getEuroB());
        viewHolder.euroS.setText(listItemMain.getEuroS());
        viewHolder.roLeuB.setText(listItemMain.getRoLeuB());
        viewHolder.roLeuS.setText(listItemMain.getRoLeuS());
        viewHolder.gbpB.setText(listItemMain.getGbpB());
        viewHolder.gbpS.setText(listItemMain.getGbpS());

        // Открываем URL при нажатии
        convertView.setOnClickListener(v -> {
            Intent openLinks = new Intent(Intent.ACTION_VIEW, Uri.parse(bankInfoList.get(position).getUrl()));
            context.startActivity(openLinks);
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
        TextView timeStamp;

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
