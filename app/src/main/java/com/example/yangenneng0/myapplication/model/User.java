package com.example.yangenneng0.myapplication.model;

import com.example.yangenneng0.myapplication.smack.SmackManager;

import java.util.List;

public class User {//单实例例类
    String account;
    String password;
    String name;
    List<Person> friendList;
    //Information selfinformation;
    //Setting set;
    private static User user;

    private User(){
        account="";
        password="";
        name="";
        friendList=null;
    }
    public static User getInstance() {
        if(user == null) {
            synchronized (User.class) {
                if(user == null) {
                    user= new User();
                }
            }
        }
        return user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getFreiendList() {
        return friendList;
    }

    public void setFreiendList(List<Person> freiendList) {
        this.friendList = freiendList;
    }



}
