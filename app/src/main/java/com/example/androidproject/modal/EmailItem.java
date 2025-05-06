package com.example.androidproject.modal;

public class EmailItem {
    public int id;
    public String text;
    public String date;
    public String time;
    public int length;

    public EmailItem(int id, String text, String date, String time, int length) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.time = time;
        this.length = length;
    }
}
