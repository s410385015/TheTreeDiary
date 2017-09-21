package com.example.nako.thetreediary.myDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Nako on 2017/1/10.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private SQLiteDatabase db;

    //Table info: Diary
    private static final String Table_DIARY =  "Diary";
    private static String[] DIARY_FIELD_NAME = {
            "_ID",
            "_TREE",
            "_TYPE",
            "_DATE",
            "_TIME",
            "_PRIVATE",
            "_MOOD",
            "_WEATHER",
            "_TITLE",
            "_CONTENT"
    };
    private static String CREATE_DIARY_TABLE = "CREATE table " + Table_DIARY + "("
            + "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + "_TREE VARCHAR, "
            + "_TYPE VARCHAR, "
            + "_DATE VARCHAR, "
            + "_TIME VARCHAR, "
            + "_PRIVATE VARCHAR, "
            + "_MOOD VARCHAR, "
            + "_WEATHER VARCHAR, "
            + "_TITLE VARCHAR, "
            + "_CONTENT VARCHAR "
            + ");";

    //Table info: Tree
    private static final String Table_TREE = "Tree";
    private static String[] TREE_FIELD_NAME = {
            "_TREE",
            "_DESCRIPTION",
            "_ARTICLENUMBER",
            "_TREEPHOTO"
    };
    private static String CREATE_TREE_TABLE = "CREATE table " + Table_TREE + "("
            + "_TREE VARCHAR PRIMARY KEY NOT NULL, "
            + "_DESCRIPTION VARCHAR, "
            + "_ARTICLENUMBER INTEGER, "
            + "_TREEPHOTO VARCHAR "
            + ");";

    //Table info: Config
    private static final String Table_CONFIG = "Config";
    private static String[] CONFIG_FIELD_NAME = {
            "_USERPHOTO",
            "_USERNAME",
            "_PASSWORD",
            "_THEME"
    };
    private static String CREATE_CONFIG_TABLE = "CREATE table " + Table_CONFIG + "("
            + "_USERPHOTO VARCHAR, "
            + "_USERNAME VARCHAR, "
            + "_PASSWORD VARCHAR, "
            + "_THEME VARCHAR "
            + ");";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context,String name) {
        this(context, name, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DIARY_TABLE);
        db.execSQL(CREATE_TREE_TABLE);
        db.execSQL(CREATE_CONFIG_TABLE);
        this.db = db;
        DefaultConfig();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table " + Table_TREE + ";");
        db.execSQL("DROP table " + Table_DIARY + ";");
        db.execSQL("DROP table " + Table_CONFIG + ";");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    private String ReplaceInvalidChar(String cont){
        cont = cont.replaceAll("'", "%01%");
        cont = cont.replaceAll("\"" , "%02%");
        return cont;
    }
    private String RecoverInvalidChar(String raw){
        raw = raw.replaceAll("%01%", "\'");
        raw =raw.replaceAll("%02%", "\"");
        raw = raw.replace("<p dir=\"ltr\">", "");
        raw =raw.replace("</p>", "");
        return raw;
    }
    /*********************************
     *         Diary Functions       *
     *********************************/
    public void SaveDiary(DiaryClass Diary) {
        Diary.SetContent(ReplaceInvalidChar(Diary.GetContent()));
        if(Diary.GetID() != -1){
            UpdateDiary(Diary);
            return;
        }
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(int i = 1; i<Diary.DiaryInfo().size(); i++)
            cv.put(DIARY_FIELD_NAME[i], Diary.DiaryInfo().get(i));
        db.insert(Table_DIARY, "", cv);
        this.UpdateTreeArticleNumber(Diary.GetTree());
    }
    public DiaryClass LoadDiaryById(int ID){
        db = this.getReadableDatabase();
        String Q = "Select * FROM " + Table_DIARY + " WHERE _ID = '" + String.valueOf(ID) + "';";
        Cursor cursor = db.rawQuery(Q, null);
        if(cursor.getCount() == 0) return null;
        DiaryClass result = new DiaryClass();
        cursor.moveToFirst();
        result.SetID(cursor.getInt(0));
        result.SetTree(cursor.getString(1));
        result.SetType(cursor.getString(2));
        result.SetDate(cursor.getString(3));
        result.SetTime(cursor.getString(4));
        result.SetPrivate(cursor.getString(5));
        result.SetMood(cursor.getString(6));
        result.SetWeather(cursor.getString(7));
        result.SetTitle(cursor.getString(8));
        result.SetContent(RecoverInvalidChar(cursor.getString(9)));
        cursor.close();
        return result;
    }
    public ArrayList<DiaryClass> LoadDiaryByTree(String TreeName){
        db = this.getReadableDatabase();
        String Q = "Select * FROM " + Table_DIARY + " WHERE _TREE = '" + TreeName + "';";
        Cursor cursor = db.rawQuery(Q, null);
        ArrayList<DiaryClass> result = new ArrayList<DiaryClass>();
        if(cursor.getCount() == 0) return null;
        else{
            cursor.moveToFirst();
            for(int i = 0; i<cursor.getCount(); i++){
                DiaryClass temp = new DiaryClass();
                temp.SetID(cursor.getInt(0));
                temp.SetTree(cursor.getString(1));
                temp.SetType(cursor.getString(2));
                temp.SetDate(cursor.getString(3));
                temp.SetTime(cursor.getString(4));
                temp.SetPrivate(cursor.getString(5));
                temp.SetMood(cursor.getString(6));
                temp.SetWeather(cursor.getString(7));
                temp.SetTitle(cursor.getString(8));
                temp.SetContent(RecoverInvalidChar(cursor.getString(9)));
                result.add(temp);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }
    public ArrayList<DiaryClass> SearchDairy(String TYPE, String Keyword) {
        int index = 0;
        if(TYPE.equals("type")) index = 2;
        if(TYPE.equals("title")) index = 8;
        if(TYPE.equals("content")) index = 9;

        if(index == 0) return null;

        db = this.getReadableDatabase();
        String Q = "SELECT * from " + Table_DIARY + ";";
        Cursor cursor = db.rawQuery(Q, null);
        ArrayList<DiaryClass> result = new ArrayList<DiaryClass>();
        if(cursor.getCount() == 0) return null;
        else{
            cursor.moveToFirst();
            for(int i = 0; i<cursor.getCount(); i++){
                if(TYPE != "content" && cursor.getString(index).indexOf(Keyword) == -1) {
                    cursor.moveToNext();
                    continue;
                }
                else if(TYPE == "content") {
                    if (Html.fromHtml(cursor.getString(index)).toString().indexOf(Keyword) == -1){
                        cursor.moveToNext();
                        continue;
                    }
                }
                DiaryClass temp = new DiaryClass();
                temp.SetID(cursor.getInt(0));
                temp.SetTree(cursor.getString(1));
                temp.SetType(cursor.getString(2));
                temp.SetDate(cursor.getString(3));
                temp.SetTime(cursor.getString(4));
                temp.SetPrivate(cursor.getString(5));
                temp.SetMood(cursor.getString(6));
                temp.SetWeather(cursor.getString(7));
                temp.SetTitle(cursor.getString(8));
                temp.SetContent(RecoverInvalidChar(cursor.getString(9)));
                result.add(temp);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result;
    }
    public void UpdateType(String Type, int id){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_DIARY + " set " + DIARY_FIELD_NAME[2] + " = '" + Type + "' WHERE _ID = " + String.valueOf(id) +";";
        db.execSQL(Q);
    }
    public void UpdateDate(String Date, int id){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_DIARY + " set " + DIARY_FIELD_NAME[3] + " = '" + Date + "' WHERE _ID = " + String.valueOf(id) +";";
        db.execSQL(Q);
    }
    public void UpdateTime(String Time, int id){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_DIARY + " set " + DIARY_FIELD_NAME[4] + " = '" + Time + "' WHERE _ID = " + String.valueOf(id) +";";
        db.execSQL(Q);
    }
    public void UpdatePrivate(String Private, int id){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_DIARY + " set " + DIARY_FIELD_NAME[5] + " = '" + Private + "' WHERE _ID = " + String.valueOf(id) +";";
        db.execSQL(Q);
    }
    public void UpdateMood(String Mood, int id){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_DIARY + " set " + DIARY_FIELD_NAME[6] + " = '" + Mood + "' WHERE _ID = " + String.valueOf(id) +";";
        db.execSQL(Q);
    }
    public void UpdateWeather(String Weather, int id){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_DIARY + " set " + DIARY_FIELD_NAME[7] + " = '" + Weather + "' WHERE _ID = " + String.valueOf(id) +";";
        db.execSQL(Q);
    }
    public void UpdateTitle(String Title, int id){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_DIARY + " set " + DIARY_FIELD_NAME[8] + " = '" + Title + "' WHERE _ID = " + String.valueOf(id) +";";
        db.execSQL(Q);
    }
    public void UpdateContent(String Content, int id){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_DIARY + " set " + DIARY_FIELD_NAME[9] + " = '" + Content + "' WHERE _ID = " + String.valueOf(id) +";";
        db.execSQL(Q);
    }
    private void UpdateDiary(DiaryClass Diary){
        int ID = Diary.GetID();
        UpdateType(Diary.GetType(), ID);
        UpdateDate(Diary.GetDate(), ID);
        UpdateTime(Diary.GetTime(), ID);
        UpdatePrivate(Diary.GetPrivate(), ID);
        UpdateMood(Diary.GetMood(), ID);
        UpdateWeather(Diary.GetWeather(), ID);
        UpdateTitle(Diary.GetTitle(), ID);
        UpdateContent(Diary.GetContent(), ID);
    }
    public void DeleteDiary(int id){
        db = this.getWritableDatabase();
        String Q = "DELETE from " + Table_DIARY + " WHERE _ID = " + String.valueOf(id) + ";";
        db.execSQL(Q);
        Cursor c = db.rawQuery("Select * from Tree", null);
        c.moveToFirst();
        if(c.getCount() == 0) return;
        for(int i = 0; i<c.getCount(); i++)
            UpdateTreeArticleNumber(c.getString(0));
    }
    public int GetDiaryNumber(){
        db = this.getReadableDatabase();
        String Q = "SELECT * FROM " + Table_DIARY + ";";
        return db.rawQuery(Q, null).getCount();
    }
    public int GetDiaryByMood(int Mood){
        db = this.getReadableDatabase();
        return db.rawQuery("SELECT * from " + Table_DIARY + " WHERE _MOOD = " + String.valueOf(Mood) + ";", null).getCount();
    }
    /*********************************
     *         Tree  Functions       *
     *********************************/
    public int SaveTree(TreeClass Tree){
        db = this.getReadableDatabase();
        String Q = "SELECT * FROM " + Table_TREE + " WHERE _TREE = '" + Tree + "';";
        Cursor cursor = db.rawQuery(Q, null);
        if(cursor.getCount() == 0) {
            cursor.close();
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            for (int i = 0; i < Tree.TreeInfo().size(); i++)
                cv.put(TREE_FIELD_NAME[i], Tree.TreeInfo().get(i));
            db.insert(Table_TREE, "", cv);
            return 1;
        }
        else{
            return -1; // Tree already exists.
        }
    }
    public void UpdateTreeArticleNumber(String Tree){
        db = this.getReadableDatabase();
        String Q = "SELECT * FROM " + Table_DIARY + " WHERE _TREE = '" + Tree + "';";
        Cursor cursor = db.rawQuery(Q, null);
        if(cursor.getCount() == 0) return;
        else{
            db = this.getWritableDatabase();
            Q = "UPDATE " + Table_TREE + " set " + TREE_FIELD_NAME[2] + " = " + cursor.getCount() + " WHERE " + TREE_FIELD_NAME[0] + "= \'" + Tree + "\';";
            db.execSQL(Q);
        }
        cursor.close();
    }
    public ArrayList<TreeClass> LoadTrees(){
        db = this.getReadableDatabase();
        String Q = "SELECT * from " + Table_TREE + ";";
        Cursor cursor = db.rawQuery(Q, null);
        if(cursor.getCount() == 0) return null;
        ArrayList<TreeClass> result = new ArrayList<TreeClass>();
        cursor.moveToFirst();
        for(int i = 0; i<cursor.getCount(); i++){
            TreeClass tmp = new TreeClass();
            tmp.SetTree(cursor.getString(0));
            tmp.SetDescription(cursor.getString(1));
            tmp.SetArticleNumber(cursor.getInt(2));
            tmp.SetTreePhoto(cursor.getInt(3));
            result.add(tmp);
            cursor.moveToNext();
        }
        cursor.close();
        return  result;
    }
    public int GetTreeNum(){
        db = this.getReadableDatabase();
        return db.rawQuery("SELECT * from " + Table_TREE + ";", null).getCount();
    }
    /*********************************
     *         Config Functions      *
     *********************************/
    public void DefaultConfig() {
        //db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ConfigClass a=new ConfigClass();
        cv.put(CONFIG_FIELD_NAME[0], a.GetUserPhoto());
        cv.put(CONFIG_FIELD_NAME[1], a.GetUserName());
        cv.put(CONFIG_FIELD_NAME[2], a.GetPassword());
        cv.put(CONFIG_FIELD_NAME[3], a.GetTheme());
        db.insert(Table_CONFIG, "", cv);
    }
    public void UpdateUserPhoto(String UserPhoto){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_CONFIG + " set " + CONFIG_FIELD_NAME[0] + " = '" + UserPhoto + "';";
        db.execSQL(Q);
    }
    public void UpdateUserName(String UserName){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_CONFIG + " set " + CONFIG_FIELD_NAME[1] + " = '" + UserName + "';";
        db.execSQL(Q);
    }
    public void UpdatePassword(String Password){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_CONFIG + " set " + CONFIG_FIELD_NAME[2] + " = '" + Password + "';";
        db.execSQL(Q);
    }
    public void UpdateTheme(String Theme){
        db = this.getWritableDatabase();
        String Q = "UPDATE " + Table_CONFIG + " set " + CONFIG_FIELD_NAME[3] + " = '" + Theme + "';";
        db.execSQL(Q);

    }
    public ConfigClass LoadConfig(){
        db = this.getReadableDatabase();
        String Q = "SELECT * from " + Table_CONFIG + ";";
        Cursor cursor = db.rawQuery(Q, null);
        cursor.moveToFirst();
        ConfigClass result = new ConfigClass();
        result.SetUserPhoto(cursor.getString(0));
        result.SetUserName(cursor.getString(1));
        result.SetPassword(cursor.getString(2));
        result.SetTheme(cursor.getString(3));
        return result;
    }
}
