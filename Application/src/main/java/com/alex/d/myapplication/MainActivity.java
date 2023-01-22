package com.alex.d.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Document doc;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getInfo();

    }

    public void getInfo() {

        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(this, R.layout.row, arrayList, getLayoutInflater());
        listView.setAdapter(adapter);

        new Thread(() -> {

            try {
                getWeb();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }


    @SuppressLint("SetTextI18n")
    public void getWeb() {

        try {

            doc = Jsoup.connect("https://valutar.md/ru").get();
            Elements tbody = doc.getElementsByTag("tbody");
            Element our_table = tbody.get(0);
            Elements elements_from_table = our_table.children();


            for (int i = 0; i < our_table.childrenSize(); i++) {
//            for (int i = 0; i < 12; i++) {

                ListItemClass item = new ListItemClass();
                item.setData1(our_table.children().get(i).child(0).text());
                item.setData2(our_table.children().get(i).child(1).text());
                item.setData3(our_table.children().get(i).child(2).text());
                arrayList.add(item);

            }

            runOnUiThread(() -> adapter.notifyDataSetChanged());

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
