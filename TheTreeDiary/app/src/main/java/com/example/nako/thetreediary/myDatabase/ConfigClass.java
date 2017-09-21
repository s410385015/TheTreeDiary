package com.example.nako.thetreediary.myDatabase;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Nako on 2017/1/10.
 */

public class ConfigClass {
    private String PASSWORD = "admin";
    private String THEME = "1";
    private String USER_NAME = "User";
    private String USER_PHOTO = "PHOTO_BYTES";

    public String Bitmap2String(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte bytes[] = stream.toByteArray();
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        return base64;
    }
    public void SetPassword(String PASSWORD){this.PASSWORD = PASSWORD;}
    public void SetTheme(String THEME){this.THEME = THEME;}
    public void SetUserName(String USER_NAME){this.USER_NAME = USER_NAME;}
    public void SetUserPhoto(String USER_PHOTO){this.USER_PHOTO = USER_PHOTO;}

    public String GetPassword(){return this.PASSWORD;}
    public String GetTheme(){return this.THEME;}
    public String GetUserName(){return this.USER_NAME;}
    public String GetUserPhoto(){return this.USER_PHOTO;}

    public ArrayList<String> ConfigInfo(){
        ArrayList<String> info = new ArrayList<String>();
        info.add(this.USER_PHOTO);
        info.add(this.USER_NAME);
        info.add(this.PASSWORD);
        info.add(this.THEME);
        return info;
    }
}
