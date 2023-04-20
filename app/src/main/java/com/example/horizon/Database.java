package com.example.horizon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import  android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "create table users (first_name text, last_name text, phone text, email text, password text)";
        sqLiteDatabase.execSQL (qry1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void register(String first_name, String last_name, String phone, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put("first_name", first_name);
        cv.put("last_name", last_name);
        cv.put("phone", phone);
        cv.put("email", email);
        cv.put("password",password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users", null, cv);
        db.close();

    }
}
