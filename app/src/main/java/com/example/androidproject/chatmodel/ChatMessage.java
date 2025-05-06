package com.example.androidproject.chatmodel;

public class ChatMessage {
    public String text;
    public String sender;

    public ChatMessage(String text, String sender) {
        this.text = text;
        this.sender = sender;
    }

    public String getText() { return text; }
    public String getAuthor() { return sender; }
    public void setText(String text) { this.text = text; }
}
