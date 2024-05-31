package com.alex.d.myapplication;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private Document doc;
    private ListView listView;
    private CustomArrayAdapter adapter;
    private List<ListItemClass> arrayList;


    String[] urls = {
//            "https://valutar.md/ru/banks/banca-nationala",
//            "https://valutar.md/ru/banks/moldova-agroindbank",
//            "https://valutar.md/ru/banks/moldindconbank",
//            "https://valutar.md/ru/banks/victoriabank",
//            "https://valutar.md/ru/banks/mobiasbanca",
//            "https://valutar.md/ru/banks/eximbank",
//            "https://valutar.md/ru/banks/procredit-bank",
//            "https://valutar.md/ru/banks/fincombank",
//            "https://valutar.md/ru/banks/energbank",
//            "https://valutar.md/ru/banks/bcr-chisinau",
//            "https://valutar.md/ru/banks/comertbank",
//            "https://valutar.md/ru/banks/eurocreditbank",
//            "https://valutar.md/ru/exchange-offices/deghest-csv",
//            "https://valutar.md/ru/exchange-offices/clio-csv",
//            "https://valutar.md/ru/exchange-offices/orion-csv",
//            "https://valutar.md/ru/exchange-offices/profx-schimb-csv",
//            "https://valutar.md/ru/exchange-offices/ciocana-csv",
//            "https://valutar.md/ru/exchange-offices/calisto-ng-csv",
//            "https://valutar.md/ru/exchange-offices/nelus-grup-csv",
//            "https://valutar.md/ru/exchange-offices/protanir-csv",
//            "https://valutar.md/ru/exchange-offices/vadisan-csv"

            "https://www.bnm.md/",
            "https://www.maib.md/ro",
            "https://www.micb.md/",
            "https://www.victoriabank.md/ru/",
            "https://www.mobiasbanca.md/",
            "https://eximbank.md/ro",
            "https://www.procreditbank.md/",
            "https://fincombank.com/",
            "https://www.energbank.com",
            "https://www.bcr.md/",
            "https://comertbank.md/",
            "https://www.ecb.md/",
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

    int[] img = {R.drawable.nationala, R.drawable.agroindbank, R.drawable.moldindconbank,
            R.drawable.victoriabank, R.drawable.mobiasbanca, R.drawable.eximbank,
            R.drawable.procredit, R.drawable.fincombank, R.drawable.energbank,
            R.drawable.bcr, R.drawable.comertbank, R.drawable.eurocreditbank,
            R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block, R.drawable.block,
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getInfo();

        String jsonString = loadJsonFromFile("data.json");
        if (jsonString != null) {
            // Обновление данных из JSON
            updateDataFromJson(jsonString);
        }

    }

    private String loadJsonFromFile(String fileName) {
        String json = null;
        try {
            FileInputStream fis = openFileInput(fileName);
            int size = fis.available();
            byte[] buffer = new byte[size];
            fis.read(buffer);
            fis.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void updateDataFromJson(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ListItemClass>>(){}.getType();
        ArrayList<ListItemClass> dataFromJson = gson.fromJson(jsonString, type);

        // Обновите ваш arrayList и UI здесь
        arrayList.clear();
        arrayList.addAll(dataFromJson);
        adapter.notifyDataSetChanged();
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

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    @SuppressLint("SetTextI18n")
    public void getWeb() {
        try {
            doc = Jsoup.connect("https://valutar.md/ru").get();
            Elements tbody = doc.getElementsByTag("tbody");
            Element our_table = tbody.get(0);

            // Сначала парсим данные и добавляем их в arrayList
            for (int i = 0; i < 21; i++) {
                ListItemClass item = new ListItemClass();
                item.setBank(our_table.children().get(i).child(0).text());
                item.setUsdB(our_table.children().get(i).child(1).text());
                item.setUsdS(our_table.children().get(i).child(2).text());
                item.setEuroB(our_table.children().get(i).child(3).text());
                item.setEuroS(our_table.children().get(i).child(4).text());
                item.setRoLeuB(our_table.children().get(i).child(7).text());
                item.setRoLeuS(our_table.children().get(i).child(8).text());
                item.setGbpB(our_table.children().get(i).child(11).text());
                item.setGbpS(our_table.children().get(i).child(12).text());
                item.setDate(getCurrentDate());
                arrayList.add(item);
            }

            // Теперь конвертируем данные из arrayList в JSON
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonArray();
            for (ListItemClass item : arrayList) {
                JsonObject jsonItem = gson.toJsonTree(item).getAsJsonObject();
                jsonArray.add(jsonItem);
            }
            String jsonString = gson.toJson(jsonArray);
            Log.d(TAG, "Json файл создан " + jsonString);
            saveJsonToFile(jsonString, "data.json");
            // Обновляем UI в главном потоке
            runOnUiThread(() -> adapter.notifyDataSetChanged());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveJsonToFile(String json, String filename) {
        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

