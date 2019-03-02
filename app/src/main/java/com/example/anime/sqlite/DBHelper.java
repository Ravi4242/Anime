package com.example.anime.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {


    //Table Name
    static final String TABLE_NAME = "videos";

    //Table columns
    private static final String ID = "id";
    static final String VIDEO_TITLE = "video_title";
    static final String VIDEO_CURRENT_DURATION = "video_duration";
    static final String VIDEO_TOTAL_DURATION = "video_total_duration";
    private static final String VIDEO_TITLE_UNIQUE = "video_title_unique";

    //Database Name
    private static final String DB_NAME = "VideosDB";
    //Database Version
    private static final int DB_VERSION = 1;

    //Create table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VIDEO_TITLE + " TEXT NOT NULL, " + VIDEO_CURRENT_DURATION + " LONG, " +
            VIDEO_TOTAL_DURATION + " LONG, " +
            " CONSTRAINT " + VIDEO_TITLE_UNIQUE + " UNIQUE (" + VIDEO_TITLE + ")" + ")";

    DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
