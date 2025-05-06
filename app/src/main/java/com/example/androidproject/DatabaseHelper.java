package com.example.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.androidproject.modal.ChatItem;
import com.example.androidproject.modal.EmailItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(@Nullable Context context) {
        super(context, "EmailAssistant.db",null, 1);
        sqLiteDatabase = getWritableDatabase();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE_EMAILS =
                "CREATE TABLE IF NOT EXISTS emails (" +
                        "emailid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "email_text TEXT NOT NULL, " +
                        "date_created TEXT NOT NULL DEFAULT (strftime('%d/%m/%Y', 'now', 'localtime')), " +
                        "time_created TEXT NOT NULL DEFAULT (strftime('%H:%M:%S', 'now', 'localtime')), " +
                        "email_length INTEGER NOT NULL" +
                        ");";

        final String CREATE_TABLE_CHATS =
                "CREATE TABLE IF NOT EXISTS chats (" +
                        "chatid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "message TEXT NOT NULL, " +
                        "sender TEXT NOT NULL, " +
                        "message_length INTEGER NOT NULL, " +
                        "date_created TEXT NOT NULL DEFAULT (strftime('%d/%m/%Y', 'now', 'localtime')), " +
                        "time_created TEXT NOT NULL DEFAULT (strftime('%H:%M:%S', 'now', 'localtime'))" +
                        ");";
        db.execSQL(CREATE_TABLE_EMAILS);
        db.execSQL(CREATE_TABLE_CHATS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS emails");
//        db.execSQL("DROP TABLE IF EXISTS chats");
//        onCreate(db);
    }

    public boolean addEmail(String emailText, int emailLength) {

        ContentValues values = new ContentValues();
        values.put("email_text", emailText);
        values.put("email_length", emailLength);

        long result = sqLiteDatabase.insert("emails", null, values);
        return result != -1;
    }

    public void addChat(String message, String sender, int length) {
        ContentValues values = new ContentValues();
        values.put("message", message);
        values.put("sender", sender);
        values.put("message_length", length);

        sqLiteDatabase.insert("chats", null, values);
    }
    //sus code
    public List<String> getAllEmailDates() {
        List<String> dates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT date_created FROM emails ORDER BY date_created DESC", null);

        if (cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }

    public List<String> getAllChatDates() {
        List<String> dates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT date_created FROM chats ORDER BY date_created DESC", null);

        if (cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }

    public List<EmailItem> getEmailsOnDate(String date) {
        List<EmailItem> emails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("emails",
                new String[]{"emailid", "email_text", "time_created", "email_length"},
                "date_created = ?",
                new String[]{date},
                null, null, "time_created DESC");

        if (cursor.moveToFirst()) {
            do {
                emails.add(new EmailItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        date,
                        cursor.getString(2),
                        cursor.getInt(3)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return emails;
    }

    public List<ChatItem> getChatsOnDate(String date) {
        List<ChatItem> chats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("chats",
                new String[]{"chatid", "message", "sender", "time_created", "message_length"},
                "date_created = ?",
                new String[]{date},
                null, null, "time_created DESC");

        if (cursor.moveToFirst()) {
            do {
                chats.add(new ChatItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        date,
                        cursor.getString(3),
                        cursor.getInt(4)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return chats;
    }
}
