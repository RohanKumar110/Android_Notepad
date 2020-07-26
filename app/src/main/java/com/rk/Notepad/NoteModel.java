package com.rk.Notepad;

public class NoteModel {

    private int id;
    private String title;
    private String Details;
    private String Date;
    private String Time;

    public NoteModel() {
    }

    public NoteModel(int id, String title, String details, String date, String time) {
        this.id = id;
        this.title = title;
        Details = details;
        Date = date;
        Time = time;
    }

    public NoteModel(String title, String details, String date, String time) {
        this.title = title;
        Details = details;
        Date = date;
        Time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
