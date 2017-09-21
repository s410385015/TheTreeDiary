package com.example.nako.thetreediary.myClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nako.thetreediary.R;

import java.util.ArrayList;

/**
 * Created by Nako on 2017/1/8.
 */

public class CustomListAdapter extends BaseAdapter{
    private ArrayList<Achievementdata> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<Achievementdata> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.processView = (TextView) convertView.findViewById(R.id.process);
            holder.rateView = (RatingBar) convertView.findViewById(R.id.ratingBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameView.setText(listData.get(position).getName());
        holder.processView.setText(listData.get(position).getProgress());
        holder.rateView.setRating(listData.get(position).getRate());
        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView processView;
        RatingBar rateView;
    }
}
