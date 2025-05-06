package com.example.androidproject.modal;

public class ChatItem {
    public int id;
    public String message;
    public String sender;
    public String date;
    public String time;
    public int length;

    public ChatItem(int id, String message, String sender, String date, String time, int length) {
        this.id = id;
        this.message = message;
        this.sender = sender;
        this.date = date;
        this.time = time;
        this.length = length;
    }
}
