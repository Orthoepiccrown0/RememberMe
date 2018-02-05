package com.armor.rememberme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Epiccrown on 05.02.2018.
 */

public class DataHelper extends SQLiteOpenHelper {

    public static final String nameDB = "AutoRememberMe";
    public static final int DB_VERSION = 1;

    public DataHelper(Context context) {
        super(context, nameDB, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "Create Table Login("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "Username TEXT,"+
                "Password TEXT);";
        db.execSQL(query);
        //

    }

    public static void insertUser(SQLiteDatabase db,String username, String password){
        ContentValues values = new ContentValues();
        values.put("Username",username);
        values.put("Password",password);
        db.insert("Login",null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
