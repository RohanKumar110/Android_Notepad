package com.rk.Notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "notepad.db";
    public static String TABLE_NAME = "tbl_notes";
    public static String COL_Id = "id";
    public static String COL_title = "title";
    public static String COL_details = "details";
    public static String COL_date = "date";
    public static String COL_time = "time";

    public Database(Context context) {
        super(context,DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table "+TABLE_NAME+" (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, details TEXT, date TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public long addNote(NoteModel note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_title,note.getTitle());
        values.put(COL_details,note.getDetails());
        values.put(COL_date,note.getDate());
        values.put(COL_time,note.getTime());

        long result = db.insert(TABLE_NAME,null,values);
        db.close();
        return result;
    }

    public NoteModel getNote(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{COL_Id,COL_title,COL_details,COL_date,COL_time},COL_Id+"=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        db.close();
        return new NoteModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
    }

    public List<NoteModel> getNotes()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<NoteModel> ListofNotes = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select * from "+TABLE_NAME+" Order By "+COL_Id+" DESC",null);
        if(cursor != null)
            while(cursor.moveToNext())
            {
                NoteModel note = new NoteModel();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setDetails(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                ListofNotes.add(note);
            }
        db.close();
        return ListofNotes;
    }

    public void deleteNote(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COL_Id+"=?",new String[]{String.valueOf(id)});
        db.close();
    }

    public int updateNote(NoteModel note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_title,note.getTitle());
        values.put(COL_details,note.getDetails());
        values.put(COL_date,note.getDate());
        values.put(COL_time,note.getTime());
        return db.update(TABLE_NAME,values,COL_Id+"=?",new String[]{String.valueOf(note.getId())});
    }
}
