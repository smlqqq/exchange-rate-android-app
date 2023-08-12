package com.alex.d.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.io.File;
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
            viewHolder.data1 = convertView.findViewById(R.id.tvData1);
            viewHolder.data2 = convertView.findViewById(R.id.tvData2);
            viewHolder.data3 = convertView.findViewById(R.id.tvData3);
            viewHolder.data4 = convertView.findViewById(R.id.tvData4);
            viewHolder.data5 = convertView.findViewById(R.id.tvData5);
            viewHolder.data6 = convertView.findViewById(R.id.tvData6);
            viewHolder.data7 = convertView.findViewById(R.id.tvData7);
            viewHolder.data11 = convertView.findViewById(R.id.tvData11);
            viewHolder.data12 = convertView.findViewById(R.id.tvData12);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



//        viewHolder.imageView.setImageResource(images[position]);
        viewHolder.gifImageView.setImageResource(images[position]);

        viewHolder.data1.setText(listItemMain.getData1());
        viewHolder.data2.setText(listItemMain.getData2());
        viewHolder.data3.setText(listItemMain.getData3());
        viewHolder.data4.setText(listItemMain.getData4());
        viewHolder.data5.setText(listItemMain.getData5());
        viewHolder.data6.setText(listItemMain.getData6());
        viewHolder.data7.setText(listItemMain.getData7());
        viewHolder.data11.setText(listItemMain.getData11());
        viewHolder.data12.setText(listItemMain.getData12());



        convertView.setOnClickListener(v -> {
            Intent openLinks = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position]));
            context.startActivity(openLinks);
        });


        return convertView;
    }


    private class ViewHolder {

//        ImageView imageView;
        GifImageView gifImageView;
        TextView data1;
        TextView data2;
        TextView data3;
        TextView data4;
        TextView data5;
        TextView data6;
        TextView data7;
        TextView data11;
        TextView data12;


        public ViewHolder(View v) {
            gifImageView = v.findViewById(R.id.gifView);
//            imageView = v.findViewById(R.id.imageView);
            data1 = v.findViewById(R.id.tvData1);
            data2 = v.findViewById(R.id.tvData2);
            data3 = v.findViewById(R.id.tvData3);
            data4 = v.findViewById(R.id.tvData4);
            data5 = v.findViewById(R.id.tvData5);
            data6 = v.findViewById(R.id.tvData6);
            data7 = v.findViewById(R.id.tvData7);
            data11 = v.findViewById(R.id.tvData11);
            data12 = v.findViewById(R.id.tvData12);


        }


    }
}
