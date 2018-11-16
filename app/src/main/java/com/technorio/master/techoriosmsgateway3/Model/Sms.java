package com.technorio.master.techoriosmsgateway3.Model;

import java.util.Date;

/**
 * Created by Ujjwal on 11/15/2018.
 */

public class Sms {

    private int id;

    private String number;
    private String date;
    private int message_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }
}
