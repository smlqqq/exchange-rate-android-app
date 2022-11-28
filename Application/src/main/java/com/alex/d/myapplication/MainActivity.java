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
    private TextView firstView;
    private TextView secondView;
    private TextView thirdView;
    private TextView fourthView;
    private TextView fifthView;
    private TextView textData;
    private TextView textCump_Vanz;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstView = findViewById(R.id.firstView);
        secondView = findViewById(R.id.secondView);
        thirdView = findViewById(R.id.thirdView);
        fourthView = findViewById(R.id.fourthView);
        fifthView = findViewById(R.id.fifthView);
        textData = findViewById(R.id.textData);
        textCump_Vanz = findViewById(R.id.textCump_Vanz);

        update_button = findViewById(R.id.update_button);

        update_button.setOnClickListener(view -> {
            getInfo();
        });
    }

    public void getInfo(){
        new Thread(() -> {

            try {
                getWeb();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            runOnUiThread(() -> {
//                try {
//                    firstView.setText(String.valueOf(Valuta.USD()));
////                    secondView.setText(String.valueOf(Valuta.EUR()));
////                    thirdView.setText(String.valueOf(Valuta.RON()));
////                    fourthView.setText(String.valueOf(Valuta.UAH()));
////                    fifthView.setText(String.valueOf(Valuta.GBP()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
        }).start();


    }
    @SuppressLint("SetTextI18n")
    public void getWeb(){
        try {
            doc =  Jsoup.connect("https://www.micb.md/micb-ru/").get();

            Elements tt = doc.getElementsByTag("tbody");
            Element our_table = tt.get(0);
            Elements elements_from_table = our_table.children();

            Elements head = doc.getElementsByTag("thead");
            Element hh = head.get(0);
            Elements elements_from_tag = hh.children();
            Log.d("MyLog", "head: " + elements_from_tag.get(0).child(1).text());
            textData.setText(elements_from_tag.get(0).child(1).text());


            Element head2 = head.get(0);
            Elements cumpare_vanzare = head2.children();
            textCump_Vanz.setText(cumpare_vanzare.get(1).child(1).text() + "      " + cumpare_vanzare.get(1).child(2).text());

            Element dollar = elements_from_table.get(0);
            Elements dollar_elements = dollar.children();
            firstView.setText(dollar_elements.get(0).text() + "  " + dollar_elements.get(1).text() + " / " + dollar_elements.get(2).text());

            Element euro = elements_from_table.get(1);
            Elements euro_elements = euro.children();
            secondView.setText(euro_elements.get(0).text() + "  " + euro_elements.get(1).text() + " / " + euro_elements.get(2).text());

            Element ron = elements_from_table.get(3);
            Elements ron_elements = ron.children();
            thirdView.setText(ron_elements.get(0).text() + "  " + ron_elements.get(1).text() + " / " + ron_elements.get(2).text());

            Element uah = elements_from_table.get(4);
            Elements uah_elements = uah.children();
            fourthView.setText(uah_elements.get(0).text() + "  " + uah_elements.get(1).text() + " / " + uah_elements.get(2).text());

            Element gbp = elements_from_table.get(5);
            Elements gbp_elements = gbp.children();
            fifthView.setText(gbp_elements.get(0).text() + "  " + gbp_elements.get(1).text() + " / " + gbp_elements.get(2).text());



//            Log.d("MyLog","=> " + dollar_elements.get(0).text() + "   " + dollar_elements.get(1).text() + "  " + dollar_elements.get(2).text() + "  " + dollar_elements.get(3).text());
//            Log.d("MyLog","=> " + euro_elements.get(0).text() + "   " + euro_elements.get(1).text() + "  " + euro_elements.get(2).text() + "  " + euro_elements.get(3).text());
//            Log.d("MyLog","=> " + ron_elements.get(0).text() + "   " + ron_elements.get(1).text() + "  " + ron_elements.get(2).text() + "  " + ron_elements.get(3).text());



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
