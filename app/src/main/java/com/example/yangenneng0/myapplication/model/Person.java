package com.example.yangenneng0.myapplication.model;

/**
 * User: yangenneng
 * DateTime: 2016/12/13 10:43
 * Description:好友实体类
 */
public class Person {

    private String name; //姓名
    private String pinyin;//拼音
    private String headerWord; //拼音首字母

    private String username;//用户名


    public  Person(){}

    public  Person(String name){
        this.name=name;

        this.username="";
        this.pinyin = "  ";
        headerWord = pinyin.substring(0, 1);
    }

    public Person(String name,String username) {
        this.username=username;

        this.name = name;
        this.pinyin = "  ";
        headerWord = pinyin.substring(0, 1);
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeaderWord() {
        return headerWord;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
