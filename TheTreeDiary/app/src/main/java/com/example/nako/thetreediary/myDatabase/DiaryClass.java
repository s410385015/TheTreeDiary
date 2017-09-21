package com.example.nako.thetreediary.myDatabase;

import java.util.ArrayList;

/**
 * Created by Nako on 2017/1/10.
 */

public class DiaryClass {
    private int ID = -1; // -1 -> 尚未存進資料庫
    public void SetID(int ID) {this.ID = ID;}
    public int GetID() {return this.ID;}

    //Setting

    private String TREE = "DailyLife";
    private String TYPE = "0";
    private String DATE = "1970-1-1";
    private String TIME = "9:41";
    private String PRIVATE = "0";
    private String MOOD = "0";
    private String WEATHER = "0";

    public void SetTree(String TREE) {this.TREE = TREE;}
    public void SetType(String TYPE) {this.TYPE = TYPE;}
    public void SetDate(String DATE) {this.DATE = DATE;}
    public void SetTime(String TIME) {this.TIME = TIME;}
    public void SetPrivate(String PRIVATE) {this.PRIVATE = PRIVATE;}
    public void SetMood(String MOOD) {this.MOOD = MOOD;}
    public void SetWeather(String WEATHER) {this.WEATHER = WEATHER;}

    public String GetTree() {return this.TREE;}
    public String GetType() {return this.TYPE;}
    public String GetDate() {return this.DATE;}
    public String GetTime() {return this.TIME;}
    public String GetPrivate() {return this.PRIVATE;}
    public String GetMood() {return this.MOOD;}
    public String GetWeather() {return this.WEATHER;}


    //Article

    private String TITLE = "TestSQL";
    private String CONTENT = "Hello!";

    public void SetTitle(String TITLE) {this.TITLE = TITLE;}
    public void SetContent(String CONTENT) {this.CONTENT = CONTENT;}

    public String GetTitle() {return this.TITLE;}
    public String GetContent() {return this.CONTENT;}

    public ArrayList<String> DiaryInfo()
    {
        ArrayList<String> info = new ArrayList<String>();
        info.add(String.valueOf(this.ID));
        info.add(this.TREE);
        info.add(this.TYPE);
        info.add(this.DATE);
        info.add(this.TIME);
        info.add(this.PRIVATE);
        info.add(this.MOOD);
        info.add(this.WEATHER);
        info.add(this.TITLE);
        info.add(this.CONTENT);
        return info;
    }
}
