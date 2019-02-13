package com.example.anime.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
    DBhelper dBhelper;
    Context context;
    SQLiteDatabase sqLiteDatabase;

    public DBManager(Context context) {
        this.context = context;
    }

    public DBManager opens() throws SQLException{
        dBhelper = new DBhelper(context);
        sqLiteDatabase = dBhelper.getWritableDatabase();
        return this;
    }
    public void close(){
        dBhelper.close();
    }

    public void insert(String title, long videoTime){
        ContentValues values = new ContentValues();
        values.put(DBhelper.VIDEO_TITLE,title);
        values.put(DBhelper.VIDEO_DURATION, videoTime);
        sqLiteDatabase.insert(DBhelper.TABLE_NAME,null,values);
        close();
    }

    public int update(String video_title, long videoTime){
        ContentValues values = new ContentValues();
        values.put(DBhelper.VIDEO_DURATION, videoTime);
        return sqLiteDatabase.update(DBhelper.TABLE_NAME,values,DBhelper.VIDEO_TITLE +  "=" + video_title, null);
    }


    private long videotime;
    public long fetch(String video_title){
        String selectQuery = "SELECT * FROM " + DBhelper.TABLE_NAME + " WHERE " + DBhelper.VIDEO_TITLE + "='" + video_title+"'";
        SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
              videotime = cursor.getLong(cursor.getColumnIndexOrThrow(DBhelper.VIDEO_DURATION));
            }while (cursor.moveToNext());
        }
        dBhelper.close();
        return videotime;
    }


}
