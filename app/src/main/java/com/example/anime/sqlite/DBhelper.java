package com.example.anime.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhelper extends SQLiteOpenHelper {


    //Table Name
    public static final String TABLE_NAME = "videos";

    //Table columns
    public static final String ID ="id";
    public static final String VIDEO_TITLE ="video_title";
    public static final String VIDEO_DURATION ="video_duration";

    //Database Name
    static final String DB_NAME = "VideosDB";
    //Database Version
    static final int DB_VERSION = 1;

    //Create table query
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VIDEO_TITLE + " TEXT NOT NULL, " + VIDEO_DURATION + " LONG);";

    public DBhelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
           db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            onCreate(db);
    }
}
