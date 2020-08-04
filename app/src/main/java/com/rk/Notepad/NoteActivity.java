package com.rk.Notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class NoteActivity extends AppCompatActivity {

    int id;
    Toolbar toolbar;
    EditText editTitle, editDetails;
    String date,time;
    Database db;
    Calendar c;
    static boolean check = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_activity);

        toolbar = findViewById(R.id.toolbar_ID2);
        editTitle = findViewById(R.id.editTitle_ID);
        editDetails = findViewById(R.id.editDetails);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        db = new Database(this);
        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.length() != 0)
                        getSupportActionBar().setTitle(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        c = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:00"));
        date = c.get(Calendar.DAY_OF_MONTH)+"/"+ (c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
        time = pad(c.get(Calendar.HOUR))+":"+pad(c.get(Calendar.MINUTE));

        if(!check)
        {
            Intent intent = getIntent();
            id = intent.getIntExtra("ID",0);
            NoteModel note = db.getNote(id);
            editTitle.setText(note.getTitle());
            editDetails.setText(note.getDetails());
            getSupportActionBar().setTitle(note.getTitle());
        }

    }

    private String pad(int i) {
        if(i < 10)
            return "0"+i;
        else
        return String.valueOf(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.saveBtn)
        {
            validate();
        }

        return super.onOptionsItemSelected(item);
    }

    public void validate()
    {
        NoteModel note;
        if(editTitle.getText().toString().equals(""))
        {
            new Handler(Looper.myLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Enter title please",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(editDetails.getText().toString().equals(""))
        {
            new Handler(Looper.myLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Enter Details please",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            if(check){
                note = new NoteModel(editTitle.getText().toString(),editDetails.getText().toString(),date,time);
                db.addNote(note);
                gotoBack();
            }
            else
            {
                note = new NoteModel(id,editTitle.getText().toString(),editDetails.getText().toString(),date,time);
                db.updateNote(note);
                check = true;
                gotoBack();
            }


        }
    }

    public void gotoBack()
    {
        startActivity(new Intent(NoteActivity.this,MainActivity.class));
    }
}