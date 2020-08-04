package com.rk.Notepad;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetail extends AppCompatActivity {

    int id;
    NoteModel note;
    Database db;
    TextView txtDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtDetails = findViewById(R.id.txtNoteDetails);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();
        id = intent.getIntExtra("Id",0);
        db = new Database(this);
        note = db.getNote(id);
        getSupportActionBar().setTitle(note.getTitle());
        txtDetails.setText(note.getDetails());
        txtDetails.setMovementMethod(new ScrollingMovementMethod());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteNote(id);
                startActivity(new Intent(ShowDetail.this,MainActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.editBtn)
        {
            NoteActivity.check = false;
            Intent intent = new Intent(ShowDetail.this, NoteActivity.class);
            intent.putExtra("ID",id);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}