package com.alex.d.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.io.File;
import java.util.List;

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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        ListItemClass listItemMain = listItem.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null, false);
            viewHolder = new ViewHolder(convertView);

            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            viewHolder.data1 = convertView.findViewById(R.id.tvData1);
            viewHolder.data2 = convertView.findViewById(R.id.tvData2);
            viewHolder.data3 = convertView.findViewById(R.id.tvData3);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


//        Picasso.get().load(images[position].replace(" ", "%20")).resize(35,35).into(viewHolder.imageView);
//        Glide.with(context).load(images[position]).into(viewHolder.imageView);



//        Picasso.get().load(listItemMain.getImageUrl()).into(viewHolder.imageView);
//        Glide.with(context).load(Uri.parse(images[position])).into(iv);
//---------------------------------------------------------------------------------------------------------------------------------
//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.nologo)
//                .error(R.drawable.nologo);
//        Glide.with(context).load(new File(listItemMain.getImageUrl()).getPath()).apply(options).into(viewHolder.imageView)
//----------------------------------------------------------------------------------------------------------------------------------

        viewHolder.imageView.setImageResource(images[position]);

        viewHolder.data1.setText(listItemMain.getData1());
        viewHolder.data2.setText(listItemMain.getData2());
        viewHolder.data3.setText(listItemMain.getData3());
//        viewHolder.imageView.setImageURI(Uri.parse(images[position]));

//        Picasso.get().load(images[position]).into(viewHolder.imageView);


//        Picasso.get().load(images[position])
//                .into(imageView, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//                        if (progressBar != null) {
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//
//                });

        convertView.setOnClickListener(v -> {
            Intent openLinks = new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position]));
            context.startActivity(openLinks);
        });



        return convertView;
    }



    private class ViewHolder {

        ImageView imageView;
        TextView data1;
        TextView data2;
        TextView data3;

        public ViewHolder(View v){
            imageView = v.findViewById(R.id.imageView);
            data1 = v.findViewById(R.id.tvData1);
            data2 = v.findViewById(R.id.tvData2);
            data3 = v.findViewById(R.id.tvData3);

        }


    }
}
