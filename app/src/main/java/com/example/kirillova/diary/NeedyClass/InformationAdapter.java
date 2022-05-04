package com.example.kirillova.diary.NeedyClass;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kirillova.diary.R;

import java.util.ArrayList;

public class InformationAdapter extends ArrayAdapter<Information> {
    Context context;
    ArrayList<Information> ItemList;

    public InformationAdapter(@NonNull Context context, ArrayList<Information> ItemList) {
        super(context, 0,ItemList);
        this.context = context;
        this.ItemList = ItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){//если view = null тогда создаем новый view
            view= LayoutInflater.from(context).inflate(R.layout.listview_shape,parent,false);//для создания view
        }

        Information item = ItemList.get(position);

        //поиск компонента listview
        TextView subject = view.findViewById(R.id.subjectListViewShapeId);
        TextView date = view.findViewById(R.id.dateListViewShapeId);
        //return super.getView(position, convertView, parent);


        //устанавливаем listview компонент в arrryList
        subject.setText(item.getSubject());
        date.setText(item.getDateTime());

        return view;
    }
}
