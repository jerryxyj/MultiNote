package com.example.jerry.multinote;

/**
 * Created by jerry on 7/18/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView time;
    public TextView context;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        time = (TextView) view.findViewById(R.id.time);
        context = (TextView) view.findViewById(R.id.context);
    }

}