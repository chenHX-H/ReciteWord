package com.example.reciteword.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WordDataOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final  String TABLE_NAME = "default_wordbook";

    public WordDataOpenHelper(Context context, String name){
        super(context, name, null, DATABASE_VERSION);
        System.out.println("构造函数触发:");
        System.out.println("当前版本号:"+DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("***********onCreate***********");
        try{
            db.execSQL("create table if not exists " + TABLE_NAME +
                    "(id integer primary key,word text,pronounce text,definition text,sentence_en text,sentence_zh text)");
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("***********更新检测**************");
        if(newVersion > oldVersion){
            System.out.println("***开始更新*******");
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        }
    }
}