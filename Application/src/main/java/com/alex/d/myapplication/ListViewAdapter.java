//package com.alex.d.myapplication;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ListViewAdapter extends ArrayAdapter<JSONObject> {
//    int listlayout;
//    ArrayList<JSONObject> valutarList;
//    Context context;
//
//    public ListViewAdapter(Context context, int listlayout, ArrayList<JSONObject> valutarList) {
//        super(context, listlayout, valutarList);
//        this.listlayout = listlayout;
//        this.valutarList = valutarList;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//       LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//       View listViewItem = inflater.inflate(listlayout,null, false);
//       TextView nameOfBank = listViewItem.findViewById(R.id.tvData1);
//       TextView firstVal = listViewItem.findViewById(R.id.tvData2);
//       TextView secondVal = listViewItem.findViewById(R.id.tvData3);
//
//       try{
//           nameOfBank.setText(valutarList.get(position).getString("bank"));
//           firstVal.setText(valutarList.get(position).getString("firstVal"));
//           secondVal.setText(valutarList.get(position).getString("secondVal"));
//       } catch (JSONException e) {
//           e.printStackTrace();
//       }
//
//        return listViewItem;
//    }
//}
