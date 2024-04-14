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

//import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class CustomArrayAdapter extends ArrayAdapter<ListItemClass> {
    private LayoutInflater inflater;
    private List<ListItemClass> listItem;
    private Context context;
    private String[] urls;
    //    private String[] images;
    private int[] images;

    public CustomArrayAdapter(@NonNull Context context, int resource, List<ListItemClass> listItem, LayoutInflater inflater, String[] urls, int[] images) {
        super(context, resource, listItem);
        this.inflater = inflater;
        this.listItem = listItem;
        this.context = context;
        this.urls = urls;
        this.images = images;
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

            viewHolder.gifImageView = convertView.findViewById(R.id.gifView);
//            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.bankName = convertView.findViewById(R.id.bankName);
            viewHolder.usdB = convertView.findViewById(R.id.usdBuy);
            viewHolder.usdS = convertView.findViewById(R.id.usdSell);
            viewHolder.euroB = convertView.findViewById(R.id.euroBuy);
            viewHolder.euroS = convertView.findViewById(R.id.euroSell);
            viewHolder.roLeuB = convertView.findViewById(R.id.roLeuBuy);
            viewHolder.roLeuS = convertView.findViewById(R.id.roLeuSell);
            viewHolder.gbpB = convertView.findViewById(R.id.gbpBuy);
            viewHolder.gbpS = convertView.findViewById(R.id.gbpSell);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.imageView.setImageResource(images[position]);
        viewHolder.gifImageView.setImageResource(images[position]);

        viewHolder.bankName.setText(listItemMain.getBank());
        viewHolder.usdB.setText(listItemMain.getUsdB());
        viewHolder.usdS.setText(listItemMain.getUsdS());
        viewHolder.euroB.setText(listItemMain.getEuroB());
        viewHolder.euroS.setText(listItemMain.getEuroS());
        viewHolder.roLeuB.setText(listItemMain.getRoLeuB());
        viewHolder.roLeuS.setText(listItemMain.getRoLeuS());
        viewHolder.gbpB.setText(listItemMain.getGbpB());
        viewHolder.gbpS.setText(listItemMain.getGbpS());

        convertView.setOnClickListener(v -> {
            Intent openLinks = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position]));
            context.startActivity(openLinks);
        });


        return convertView;
    }


    private class ViewHolder {

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
