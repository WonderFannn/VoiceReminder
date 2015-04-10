package com.sail.voicereminder.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    //数据库名字和版本号
    private static final String DB_NAME = "VoiveRecord.db";
    private static final int VERSION = 1;
    //数据库中的表
    public static final String RECORD_TABLE = "record";
    //数据库表中成员
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String TIME = "time";
    public static final String FILE = "file";
    public static final String CONTENT = "content";
    public static final String CLASSIFY = "classify";
    
    private static final String CREATE_TABLE = "create table " + RECORD_TABLE + " ( " + 
            _ID + " Integer primary key autoincrement," + 
            TITLE + " text," +
            TIME + " text," +
            FILE + " text," +
            CONTENT + " text," +
            CLASSIFY + " text" + ")";


    public MyDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //为了每次构造时不用传入dbName和版本号，自己得新定义一个构造方法
    public MyDBHelper(Context cxt){
        this(cxt, DB_NAME, null, VERSION);//调用上面的构造方法
    }

    //版本变更时
    public MyDBHelper(Context cxt,int version) {
        this(cxt,DB_NAME,null,version);
    }

    //当数据库创建的时候调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    //版本更新时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql  = "update student ....";//自己的Update操作
        db.execSQL(sql);
    }

}
