package com.example.jerry.multinote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {
    private static final int A_REQ = 2;
    private static final int B_REQ = 1;
    private static final String TAG = "MainActivity";
    private List<NoteInfo>noteInfoList=new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    private int selectedPostion;
    private TextView title;
    private TextView context;
    private TextView time;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new NoteAdapter(noteInfoList, this);
        recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager mlayoutManger=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutManger);
        title=(TextView)findViewById(R.id.title);
        context=(TextView)findViewById(R.id.context);
        time=(TextView)findViewById(R.id.time);
        new MyAsyncTask(this).execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:
                Toast.makeText(this, "You want to add note", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
//                NoteInfo n=new NoteInfo("","",System.currentTimeMillis());
//                intent.putExtra(NoteInfo.class.getName(),n);
                intent.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivityForResult(intent, B_REQ);
//                noteInfoList.add(0,n);

                return true;
            case R.id.info:
                Toast.makeText(this, "show information", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivity.this, InforActivity.class);
                intent2.putExtra(Intent.EXTRA_TEXT, MainActivity.class.getSimpleName());
                startActivity(intent2);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        selectedPostion=recyclerView.getChildLayoutPosition(v);
        NoteInfo n=noteInfoList.get(selectedPostion);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra(NoteInfo.class.getName(),n);
        startActivityForResult(intent, A_REQ);





    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                noteInfoList.remove(pos);
                mAdapter.notifyDataSetChanged();
                saveNote();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMessage("Delete Note " +noteInfoList.get(pos).getTitle() + "?");


        builder.setTitle("Delete Note");

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == B_REQ) {
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("USER_TEXT");
                String text2 = data.getStringExtra("USER_CONTENT");
                NoteInfo n=new NoteInfo("","",System.currentTimeMillis());
                n.setTitle(text);
                n.setContext(text2);
                n.setTime(System.currentTimeMillis());
                noteInfoList.add(0,n);
                saveNote();

                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();

                Log.d(TAG, "onActivityResult: User Text: "+n.getTitle()+"and"+noteInfoList.size());
            }
            else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }


        }
        else if(requestCode == A_REQ){
            if (resultCode == RESULT_OK) {
                String text = data.getStringExtra("USER_TEXT");
                String text2 = data.getStringExtra("USER_CONTENT");
                NoteInfo n=noteInfoList.get(selectedPostion);
                noteInfoList.remove(selectedPostion);
                noteInfoList.add(0,n);
                n.setTitle(text);
                n.setContext(text2);
                n.setTime(System.currentTimeMillis());
                saveNote();
                Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();


                mAdapter.notifyDataSetChanged();

                Log.d(TAG, "onActivityResult: User Text: "+n.getTitle()+"and"+noteInfoList.size());
            }
            else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }


        }
        else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }


    }

    public void updateData(ArrayList<NoteInfo> nList) {
        Log.d(TAG,"updating MainActivity right now");

            Log.d(TAG, "after updating MainActivity");
            noteInfoList.addAll(nList);
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "after updating MainActivity");

    }

    private void saveNote() {

        Log.d(TAG, "saveProduct: Saving JSON File in MainActivity");

        try {

            FileOutputStream fos = getApplicationContext().openFileOutput("MultiNote.json", Context.MODE_PRIVATE);
            Log.d(TAG, "file open success");
//            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            BufferedWriter writer = new BufferedWriter((new OutputStreamWriter(fos)));




            Log.d(TAG, "noteInfoList size is "+noteInfoList.size());

            JSONArray jsonArr = new JSONArray();



            for(int i=0;i<noteInfoList.size();i++) {
                JSONObject objc=new JSONObject();
                objc.put("topic",noteInfoList.get(i).getTitle());
                objc.put("content",noteInfoList.get(i).getContext());
                objc.put("time",noteInfoList.get(i).getTime());
                jsonArr.put(objc);


                        }

            writer.write(jsonArr.toString());

            writer.close();


            Log.d(TAG, "out loop for testing save"+jsonArr.toString());


            Log.d(TAG, "saveProduct: Saving JSON File in MainActivity2"+jsonArr.toString());

        }

        catch (Exception e) {
            e.getStackTrace();
        }

    }

    class MyAsyncTask extends AsyncTask<String,Integer,String>{
        private MainActivity mainActivity;

        public MyAsyncTask(MainActivity ma) {
            mainActivity = ma;
        }


        protected void onPreExecute(){

            Toast.makeText(mainActivity, "Loading Note Data...", Toast.LENGTH_SHORT).show();
            Log.d("Start!!!!!","1");


        }
        protected void onPostExecute(String s){
            Log.d("it's onPostExcute!!!!!","1");
            if(s==null){
                Log.d("it' has no files!!!!!","1");

            }
            else{
                ArrayList<NoteInfo> noteList = parseJSON(s);
                Log.d("it's after reading!!!!!","1");
                mainActivity.updateData(noteList);

            }






        }
        protected String doInBackground(String... params){
            StringBuilder sb = new StringBuilder();
            try{
                InputStream is = getApplicationContext().openFileInput("MultiNote.json");

                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }




                Log.d(TAG, "parseJSON File exist: ");


                return sb.toString();





            }catch (Exception e){
                Log.d(TAG, "No parseJSON File exist: " + "sb's value is "+sb.toString()+e.getMessage());

            }

            return null;
        }


        private ArrayList<NoteInfo> parseJSON(String s) {

            ArrayList<NoteInfo> noteList = new ArrayList<>();
            if (s==null) {
                Log.d(TAG, "sb's length is "+s.length());

            } else {

                try {
                    JSONArray jObjMain = new JSONArray(s);
                    for (int i = 0; i < jObjMain.length(); i++) {

                        JSONObject jNote = (JSONObject) jObjMain.get(i);
                        String topic = jNote.getString("topic");
                        String content = jNote.getString("content");
                        Long time=jNote.getLong(("time"));
                        noteList.add(new NoteInfo(topic, content,time));
                    }
                    return noteList;

                } catch (Exception e) {
                    Log.d(TAG, "parseJSON: " + e.getMessage());
                    e.printStackTrace();
                }


            }
            return null;
        }




    }



    }

