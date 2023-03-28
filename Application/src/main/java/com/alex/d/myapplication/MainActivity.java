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


//    String[] images = {
//            "https://www.valutar.md/assets/7dcc2c93/img/banks/icons/banca-nationala.png",
//            "https://www.valutar.md/ru/banks/moldova-agroindbank",
//            "https://www.valutar.md/ru/banks/moldindconbank"
//    };

    int[] img = {R.drawable.nationala, R.drawable.agroindbank, R.drawable.moldindconbank,
            R.drawable.victoriabank, R.drawable.mobiasbanca, R.drawable.eximbank,
            R.drawable.procredit, R.drawable.fincombank, R.drawable.energbank,
            R.drawable.bcr, R.drawable.comertbank, R.drawable.eurocreditbank,
            R.drawable.nologo, R.drawable.nologo,R.drawable.nologo,R.drawable.nologo,R.drawable.nologo,R.drawable.nologo,R.drawable.nologo,R.drawable.nologo,R.drawable.nologo,
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
//        imageView = findViewById(R.id.imageView);
        arrayList = new ArrayList<>();
//        adapter = new CustomArrayAdapter(this, R.layout.row, arrayList, getLayoutInflater(), urls, images);
        adapter = new CustomArrayAdapter(this, R.layout.row2, arrayList, getLayoutInflater(), urls, img);
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


//            for (int i = 0; i < our_table.childrenSize(); i++) {
            for (int i = 0; i < 21; i++) {

                ListItemClass item = new ListItemClass();
                item.setData1(our_table.children().get(i).child(0).text());
                item.setData2(our_table.children().get(i).child(1).text());
                item.setData3(our_table.children().get(i).child(2).text());
                item.setData4(our_table.children().get(i).child(3).text());
                item.setData5(our_table.children().get(i).child(4).text());
                item.setData6(our_table.children().get(i).child(7).text());
                item.setData7(our_table.children().get(i).child(8).text());
                item.setData11(our_table.children().get(i).child(11).text());
                item.setData12(our_table.children().get(i).child(12).text());

                arrayList.add(item);

            }

            runOnUiThread(() -> adapter.notifyDataSetChanged());

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
