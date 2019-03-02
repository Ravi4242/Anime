package com.example.anime.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DBHelper dBhelper;
    private SQLiteDatabase sqLiteDatabase;

    public DBManager(Context context) {
        dBhelper = new DBHelper(context);
    }

    private void opens() throws SQLException{
        sqLiteDatabase = dBhelper.getWritableDatabase();
    }
    private void close(){
        dBhelper.close();
    }

    public void insert(String title, long videoCurrentDuration, long videoTotalDuration){
        opens();
        ContentValues values = new ContentValues();
        values.put(DBHelper.VIDEO_TITLE,title);
        values.put(DBHelper.VIDEO_CURRENT_DURATION, videoCurrentDuration);
        values.put(DBHelper.VIDEO_TOTAL_DURATION, videoTotalDuration);
        sqLiteDatabase.insert(DBHelper.TABLE_NAME,null,values);
        close();
    }

    public void update(String video_title, long videoCurrentDuration){
        opens();
        ContentValues values = new ContentValues();
        values.put(DBHelper.VIDEO_CURRENT_DURATION, videoCurrentDuration);
        sqLiteDatabase.update(DBHelper.TABLE_NAME,values, DBHelper.VIDEO_TITLE +  "=?", new String[]{video_title});
        close();
    }


    private long videoCurrentDuration;
    public long fetchVideoCurrentDuration(String video_title){
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.VIDEO_TITLE + "='" + video_title+"'";
        SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
              videoCurrentDuration = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.VIDEO_CURRENT_DURATION));
            }while (cursor.moveToNext());
        }
        cursor.close();
        dBhelper.close();
        return videoCurrentDuration;
    }

    private long videoTotalDuration;
    public long fetchVideoTotalDuration(String video_title){
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.VIDEO_TITLE + "='" + video_title+"'";
        SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                videoTotalDuration = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.VIDEO_TOTAL_DURATION));
            }while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return videoTotalDuration;
    }

    public List<String> fetchAllTitles(){
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_NAME  ;
        SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        List<String> titles = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
               titles.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.VIDEO_TITLE)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return titles;
    }

}
