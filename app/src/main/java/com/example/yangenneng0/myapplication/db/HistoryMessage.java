package com.example.yangenneng0.myapplication.db;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class HistoryMessage extends DataSupport {
    String name;//发起者
    String receiver;//接受者
    String content;
    String date;
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
