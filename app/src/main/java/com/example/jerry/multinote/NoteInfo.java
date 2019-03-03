package com.example.jerry.multinote;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jerry on 7/18/2017.
 */

public class NoteInfo implements Serializable {

    private String title;
    private long time;
    private String context;


    public NoteInfo(String title,String context, Long time) {
        this.title = title;
        this.time = time;
        this.context = context;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDataTimeFormatted(){
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(time));


    }
    public String toString() {
        return time + ": " + context;
    }

}
