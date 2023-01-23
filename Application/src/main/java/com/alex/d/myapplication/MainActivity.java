package com.alex.d.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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


    String[] urls = {"https://valutar.md/ru/banks/banca-nationala",
            "https://valutar.md/ru/banks/moldova-agroindbank",
            "https://valutar.md/ru/banks/moldindconbank",
            "https://valutar.md/ru/banks/victoriabank",
            "https://valutar.md/ru/banks/mobiasbanca",
            "https://valutar.md/ru/banks/eximbank",
            "https://valutar.md/ru/banks/procredit-bank",
            "https://valutar.md/ru/banks/fincombank",
            "https://valutar.md/ru/banks/energbank",
            "https://valutar.md/ru/banks/bcr-chisinau",
            "https://valutar.md/ru/banks/comertbank",
            "https://valutar.md/ru/banks/eurocreditbank",
            "https://valutar.md/ru/exchange-offices/deghest-csv",
            "https://valutar.md/ru/exchange-offices/clio-csv",
            "https://valutar.md/ru/exchange-offices/orion-csv",
            "https://valutar.md/ru/exchange-offices/profx-schimb-csv",
            "https://valutar.md/ru/exchange-offices/ciocana-csv",
            "https://valutar.md/ru/exchange-offices/calisto-ng-csv",
            "https://valutar.md/ru/exchange-offices/nelus-grup-csv",
            "https://valutar.md/ru/exchange-offices/protanir-csv",
            "https://valutar.md/ru/exchange-offices/vadisan-csv"


    };

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
        adapter = new CustomArrayAdapter(this, R.layout.row, arrayList, getLayoutInflater(), urls);
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
