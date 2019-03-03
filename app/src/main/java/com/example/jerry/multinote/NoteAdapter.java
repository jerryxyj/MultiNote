package com.example.jerry.multinote;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jerry on 7/18/2017.
 */




public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "NotesAdapter";
    private List<NoteInfo> noteList;
    private MainActivity mainAct;

    public NoteAdapter(List<NoteInfo> noteList, MainActivity ma ) {
        this.noteList = noteList;
        mainAct = ma;

    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoteInfo note = noteList.get(position);
        holder.title.setText(note.getTitle());
        String c=note.getContext();
        if(c.length()>80){
            c=c.substring(0,80)+"...";
        }
        holder.context.setText(c);
        holder.time.setText(note.getDataTimeFormatted());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }



}