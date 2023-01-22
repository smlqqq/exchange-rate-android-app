package com.alex.d.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<ListitemClass> {
     LayoutInflater inflater;
     List<ListitemClass> listItem = new ArrayList<>();

    public CustomArrayAdapter( Context context, int resource, List<ListitemClass> listItem, LayoutInflater inflater) {
        super(context,resource,listItem);
        this.inflater = inflater;
        this.listItem = listItem;
    }
}
