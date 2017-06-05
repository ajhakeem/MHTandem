package com.example.jaseem.tandem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jaseem on 5/4/17.
 */

public class FreeTimesAdapter extends BaseAdapter {

    Context context;
    ArrayList<FreeTimesClass> freeTimes;

    public FreeTimesAdapter(Context context, ArrayList<FreeTimesClass> freeTimes) {
        this.context = context;
        this.freeTimes = freeTimes;
    }

    @Override
    public int getCount() {
        return freeTimes.size();
    }

    @Override
    public Object getItem(int position) {
        return freeTimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_freetimeslist, parent, false);
        }

        TextView tvStartTime = (TextView) convertView.findViewById(R.id.tvStartTime);
        TextView tvEndTime = (TextView) convertView.findViewById(R.id.tvEndTime);


        final FreeTimesClass f = (FreeTimesClass) this.getItem(position);

        tvStartTime.setText(f.getTimeString1());
        tvEndTime.setText(f.getTimeString2());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Works", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
