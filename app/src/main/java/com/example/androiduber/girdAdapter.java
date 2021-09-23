package com.example.androiduber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class girdAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> arrayList;

    public girdAdapter(Context context, ArrayList<String> arrayList){
        this.context =  context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_grid, null);
        }
        TextView txtPartidaR;
        txtPartidaR = convertView.findViewById(R.id.txtPartidaR);

        txtPartidaR.setText(arrayList.get(position));


        return convertView;
    }
}
