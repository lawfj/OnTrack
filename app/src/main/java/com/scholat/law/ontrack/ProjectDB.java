package com.scholat.law.ontrack;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProjectDB extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "notes";//表名
    public static final String CONTENT = "content";
    public static final String PATH = "path";//存储图片的路径
    public static final String VIDEO = "video";
    public static final String ID = "_id";
    public static final String TIME = "time";

    public ProjectDB(Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT
                + " TEXT NOT NULL," + PATH + " TEXT NOT NULL," + VIDEO
                + " TEXT NOT NULL," + TIME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新数据库
    }
}
