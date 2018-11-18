package com.technorio.master.techoriosmsgateway3.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.technorio.master.techoriosmsgateway3.Model.Message;
import com.technorio.master.techoriosmsgateway3.Model.Sms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ujjwal on 11/15/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    static String name = "TechnorioSMSDB";
    static int version = 1;

    String sqlCreateSmsHistoryTable = "CREATE TABLE if not exists `sms_history` (\n" +
            "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`number`\tTEXT,\n" +
            "\t`message_id`\tINTEGER\n" +
            ");";

    String sqlCreateMessageTable = "CREATE TABLE if not exists `message` (\n" +
            "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`message`\tTEXT,\n" +
            "\t`date`\tTEXT\n" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
        getWritableDatabase().execSQL(sqlCreateSmsHistoryTable);
        getWritableDatabase().execSQL(sqlCreateMessageTable);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertRecord(String number, long message_id){

        ContentValues contentValues = new ContentValues();
        contentValues.put("number", number);
        contentValues.put("message_id", message_id);

        getWritableDatabase().insert("sms_history", "", contentValues);

    }

    public void deleteRecord(){

    }

    public ArrayList<String> getNumberList(int message_id){

        ArrayList<String> numberList = new ArrayList<>();

        String sql = "select number from sms_history where message_id ="+message_id;
        Cursor c = getReadableDatabase().rawQuery(sql, null);

        while (c.moveToNext()){

            String number = c.getString(c.getColumnIndex("number"));
            numberList.add(number);
        }
        return numberList;
    }

    public long insertMessage(String message){

        Calendar cal = Calendar.getInstance();
        String date = new SimpleDateFormat("YYY-MMM-dd").format(cal.getTime());

        ContentValues contentValues = new ContentValues();
        contentValues.put("message", message);
        contentValues.put("date", date);

        return getWritableDatabase().insert("message", "", contentValues);
    }

    public void deleteMessage(){

    }

    public Message getMessageById(int message_id){

        Message message = new Message();

        String sql = "select * from message where id="+message_id;
        Cursor c = getReadableDatabase().rawQuery(sql, null);

        while(c.moveToNext()){
            message.setMessage(c.getString(c.getColumnIndex("message")));
            message.setDate(c.getString(c.getColumnIndex("date")));
        }
        return message;

    }

    public ArrayList<Message> getMessageList(){

        ArrayList<Message> messageList = new ArrayList<>();

        String sql = "select * from message ORDER BY id DESC";
        Cursor c = getReadableDatabase().rawQuery(sql, null);

        while (c.moveToNext()){
            Message message = new Message();
            int id = c.getInt(c.getColumnIndex("id"));
            String msg = c.getString(c.getColumnIndex("message"));
            String date = c.getString(c.getColumnIndex("date"));
            message.setId(id);
            message.setMessage(msg);
            message.setDate(date);
            messageList.add(message);
        }
        c.close();
        return messageList;
    }

    public int getSMSNoByMessageId(int id){

        String sql = "select * from sms_history where message_id = "+id;
        Cursor c = getReadableDatabase().rawQuery(sql, null);

        int row = 0;
        while(c.moveToNext()){
            row++;
        }

        return row;
    }


}
