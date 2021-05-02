package com.example.NOTE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBService extends SQLiteOpenHelper {
    public static final String TABLE = "notes";
    public static final String ID = "_id";
    public static final String CONTENT = "content";
    public static final String DAY = "day";
    public static final String TIME = "time";
    public DBService(Context context) {
        super(context,"item.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE+"("+
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                CONTENT + " TEXT ,"+
                DAY + " DATE NOT NULL,"+
                TIME + " TIME NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}