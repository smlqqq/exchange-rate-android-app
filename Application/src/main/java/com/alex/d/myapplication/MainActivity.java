package com.alex.d.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends AppCompatActivity {


    private Document doc;
    private Button update_button;
    private TextView first;
    private TextView second;
    private TextView third;
    private TextView fourth;
    private TextView fifth;
    private TextView sixth;
    private TextView seventh;
    private TextView eighth;
    private TextView ninth;
    private TextView tenth;
    private TextView eleventh;

    private TextView tvData1;
    private TextView tvData2;
    private TextView tvData3;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_1);

        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        fifth = findViewById(R.id.fifth);
        sixth = findViewById(R.id.sixth);
        seventh = findViewById(R.id.seventh);
        eighth = findViewById(R.id.eighth);
        ninth = findViewById(R.id.ninth);
        tenth = findViewById(R.id.tenth);
        eleventh = findViewById(R.id.eleventh);

        update_button = findViewById(R.id.update_button);

        update_button.setOnClickListener(view -> {
            getInfo();
        });
    }

    public void getInfo() {
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

//            Log.d("MyLog", "head: " + elements_from_table.get(0).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(1).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(2).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(3).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(4).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(5).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(6).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(7).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(8).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(9).text());
//            Log.d("MyLog", "head: " + elements_from_table.get(10).text());



           for(int i = 0; i < our_table.childrenSize() ; i++) {


               Element agroindbank = elements_from_table.get(1);
               Elements agroindbank_usd = agroindbank.children();
               Log.d("MyLog", "head: " + agroindbank_usd.get(0).text() + " " + agroindbank_usd.get(1).text() + " " + agroindbank_usd.get(2).text());
               tvData1.setText(agroindbank_usd.get(0).text());
               tvData2.setText(agroindbank_usd.get(1).text());
               tvData3.setText(agroindbank_usd.get(2).text());
           }

            Element moldindconbank = elements_from_table.get(2);
            Elements moldindconbank_usd = moldindconbank.children();
//            Log.d("MyLog", "head: " + moldindconbank_usd.get(0).text() + " " + moldindconbank_usd.get(1).text() + " " + moldindconbank_usd.get(2).text() );
            second.setText(moldindconbank_usd.get(0).text() + " " + moldindconbank_usd.get(1).text() + " " + moldindconbank_usd.get(2).text());

            Element victoriabank = elements_from_table.get(2);
            Elements victoriabank_usd = victoriabank.children();
//            Log.d("MyLog", "head: " + moldindconbank_usd.get(0).text() + " " + moldindconbank_usd.get(1).text() + " " + moldindconbank_usd.get(2).text() );
            third.setText(victoriabank_usd.get(0).text() + " " + victoriabank_usd.get(1).text() + " " + victoriabank_usd.get(2).text());


//            Element dollar = elements_from_table.get(0);
//            Elements dollar_elements = dollar.children();
//            firstView.setText(dollar_elements.get(0).text() + "  " + dollar_elements.get(1).text() + " / " + dollar_elements.get(2).text());



//            Elements thead = doc.getElementsByTag("thead");
//            Element hh = thead.get(0);
//            Elements elements_from_tag = hh.children();
//            Log.d("MyLog", "head: " + elements_from_tag.get(0).child(1).text());
//            textData.setText(elements_from_tag.get(0).child(1).text());

//
//            Element head2 = thead.get(0);
//            Elements cumpare_vanzare = head2.children();
//            textCump_Vanz.setText(cumpare_vanzare.get(1).child(1).text() + "      " + cumpare_vanzare.get(1).child(2).text());
//
//            Element dollar = elements_from_table.get(0);
//            Elements dollar_elements = dollar.children();
//            firstView.setText(dollar_elements.get(0).text() + "  " + dollar_elements.get(1).text() + " / " + dollar_elements.get(2).text());
//
//            Element euro = elements_from_table.get(1);
//            Elements euro_elements = euro.children();
//            secondView.setText(euro_elements.get(0).text() + "  " + euro_elements.get(1).text() + " / " + euro_elements.get(2).text());
//
//            Element ron = elements_from_table.get(3);
//            Elements ron_elements = ron.children();
//            thirdView.setText(ron_elements.get(0).text() + "  " + ron_elements.get(1).text() + " / " + ron_elements.get(2).text());
//
//            Element uah = elements_from_table.get(4);
//            Elements uah_elements = uah.children();
//            fourthView.setText(uah_elements.get(0).text() + "  " + uah_elements.get(1).text() + " / " + uah_elements.get(2).text());
//
//            Element gbp = elements_from_table.get(5);
//            Elements gbp_elements = gbp.children();
//            fifthView.setText(gbp_elements.get(0).text() + "  " + gbp_elements.get(1).text() + " / " + gbp_elements.get(2).text());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
