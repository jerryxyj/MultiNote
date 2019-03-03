package com.example.jerry.multinote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;




public class EditActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText topic;
    private EditText content;

    NoteInfo note=new NoteInfo("","",System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        topic = (EditText) findViewById(R.id.topic);
        content = (EditText) findViewById(R.id.content);
        content.setMovementMethod(new ScrollingMovementMethod());
        content.setTextIsSelectable(true);

        Intent intent = getIntent();

        if (intent.hasExtra(NoteInfo.class.getName())) {

            NoteInfo n = (NoteInfo) intent.getSerializableExtra(NoteInfo.class.getName());
            topic.setText(n.getTitle());
            content.setText(n.getContext());
            note.setContext(n.getContext());
            note.setTitle(n.getTitle());
        }



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editmenu, menu);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                if(topic.getText().toString()==null|topic.getText().toString().isEmpty()){

                    Toast.makeText(this, "You must save with title", Toast.LENGTH_SHORT).show();



                }
                else {
                    Toast.makeText(this, "You want to save note", Toast.LENGTH_SHORT).show();
                    note.setTitle(topic.getText().toString());
                    note.setContext(content.getText().toString());
                    Intent data = new Intent();
                    data.putExtra("USER_TEXT", topic.getText().toString());
                    data.putExtra("USER_CONTENT", content.getText().toString());
                    setResult(RESULT_OK, data);
                    onBackPressed();


                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }




    public void onBackPressed() {

        if (note.getTitle().toString().equals(topic.getText().toString())&&note.getContext().toString().equals(content.getText().toString())|topic.getText().toString()==null|topic.getText().toString().isEmpty()) {
            super.onBackPressed();

        }
        else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You note is not saved! Save note " + topic.getText().toString())
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent data = new Intent();
                            data.putExtra("USER_TEXT", topic.getText().toString());
                            data.putExtra("USER_CONTENT", content.getText().toString());



                            setResult(RESULT_OK, data);


                            EditActivity.this.finish();
                            Log.d(TAG, "Testing the here ");

                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditActivity.this.finish();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }





    }


