package com.sail.voicereminder.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDBOperate {
    private MyDBHelper myDBHelper;

    public MyDBOperate(Context context) {
        myDBHelper = new MyDBHelper(context);
    }
    /**
     * 增，用insert向数据库中插入数据
     */
    public void add(VoiceRemindRecord p) {
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(MyDBHelper._ID, p.getId());
        values.put(MyDBHelper.TITLE, p.getTitle());
        values.put(MyDBHelper.TIME, p.getTime());
        values.put(MyDBHelper.FILE, p.getFile());
        values.put(MyDBHelper.CONTENT, p.getContent());
        values.put(MyDBHelper.CLASSIFY, p.getClassify());
        
        db.insert(MyDBHelper.RECORD_TABLE, null, values);
    }
    /**
     * 删，通过id删除数据
     */
    public void delete(int id) {
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        db.delete(MyDBHelper.RECORD_TABLE, MyDBHelper._ID + "=?", new String[]{String.valueOf(id)});
    }
    /**
     * 改，修改指定id的数据
     */
    public void updata(VoiceRemindRecord p) {
     SQLiteDatabase db = myDBHelper.getWritableDatabase();
     
     ContentValues values = new ContentValues();
     values.put(MyDBHelper._ID, p.getId());
     values.put(MyDBHelper.TITLE, p.getTitle());
     values.put(MyDBHelper.TIME, p.getTime());
     values.put(MyDBHelper.FILE, p.getFile());
     values.put(MyDBHelper.CONTENT, p.getContent());
     values.put(MyDBHelper.CLASSIFY, p.getClassify());

     db.update(MyDBHelper.RECORD_TABLE, values, MyDBHelper._ID + "=?", new String[]{String.valueOf(p.getId())});
    }
    /**
     * 查，查询表中所有的数据
     */
    public List<VoiceRemindRecord> findAll() {
        List<VoiceRemindRecord> records = null;
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(MyDBHelper.RECORD_TABLE, null, null, null, null, null, null);
        if(cursor != null){
            records = new ArrayList<VoiceRemindRecord>();
            while(cursor.moveToNext()){
                
                int _id = cursor.getInt(cursor.getColumnIndex(MyDBHelper._ID));
                String title = cursor.getString(cursor.getColumnIndex(MyDBHelper.TITLE));
                String time = cursor.getString(cursor.getColumnIndex(MyDBHelper.TIME));
                String file = cursor.getString(cursor.getColumnIndex(MyDBHelper.FILE));
                String content = cursor.getString(cursor.getColumnIndex(MyDBHelper.CONTENT));
                String classify = cursor.getString(cursor.getColumnIndex(MyDBHelper.CLASSIFY));
                
                VoiceRemindRecord record = new VoiceRemindRecord(_id, title, time, file, content, classify);
                records.add(record);
            }
        }
        return records;
    }
    public List<VoiceRemindRecord> findByKey(String condition ) {
        List<VoiceRemindRecord> records = null;
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        String sqlString = " like '%";
        for (int i = 0; i < condition.length(); i++) {
            sqlString += (condition.charAt(i) + "%"); 
        }
        sqlString += "'";
        // 查询的时候按字查询,例如查询"中国人",会按" *中*国*人* "查询
        Cursor cursor = db.query(MyDBHelper.RECORD_TABLE, null, MyDBHelper.CONTENT + sqlString+" or "+ MyDBHelper.TITLE + sqlString  , null, null, null, null);
        if(cursor != null){
            records = new ArrayList<VoiceRemindRecord>();
            while(cursor.moveToNext()){
                
                int _id = cursor.getInt(cursor.getColumnIndex(MyDBHelper._ID));
                String title = cursor.getString(cursor.getColumnIndex(MyDBHelper.TITLE));
                String time = cursor.getString(cursor.getColumnIndex(MyDBHelper.TIME));
                String file = cursor.getString(cursor.getColumnIndex(MyDBHelper.FILE));
                String content = cursor.getString(cursor.getColumnIndex(MyDBHelper.CONTENT));
                String classify = cursor.getString(cursor.getColumnIndex(MyDBHelper.CLASSIFY));
                
                VoiceRemindRecord record = new VoiceRemindRecord(_id, title, time, file, content, classify);
                records.add(record);
            }
        }
        return records;
    }
    public List<VoiceRemindRecord> findByClassify(String classifyString) {
        List<VoiceRemindRecord> records = null;
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        
        Cursor cursor = db.query(MyDBHelper.RECORD_TABLE, null, MyDBHelper.CLASSIFY + "=?" , new String[]{classifyString}, null, null, null);
        if(cursor != null){
            records = new ArrayList<VoiceRemindRecord>();
            while(cursor.moveToNext()){
                
                int _id = cursor.getInt(cursor.getColumnIndex(MyDBHelper._ID));
                String title = cursor.getString(cursor.getColumnIndex(MyDBHelper.TITLE));
                String time = cursor.getString(cursor.getColumnIndex(MyDBHelper.TIME));
                String file = cursor.getString(cursor.getColumnIndex(MyDBHelper.FILE));
                String content = cursor.getString(cursor.getColumnIndex(MyDBHelper.CONTENT));
                String classify = cursor.getString(cursor.getColumnIndex(MyDBHelper.CLASSIFY));
                
                VoiceRemindRecord record = new VoiceRemindRecord(_id, title, time, file, content, classify);
                records.add(record);
            }
        }
        return records;
    }
    /**
     * 查询指定id的数据
     */
    public VoiceRemindRecord findById(int id) {
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(MyDBHelper.RECORD_TABLE, null, MyDBHelper._ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        VoiceRemindRecord record = null;
        if(cursor != null && cursor.moveToFirst()){
            int _id = cursor.getInt(cursor.getColumnIndex(MyDBHelper._ID));
            String title = cursor.getString(cursor.getColumnIndex(MyDBHelper.TITLE));
            String time = cursor.getString(cursor.getColumnIndex(MyDBHelper.TIME));
            String file = cursor.getString(cursor.getColumnIndex(MyDBHelper.FILE));
            String content = cursor.getString(cursor.getColumnIndex(MyDBHelper.CONTENT));
            String classify = cursor.getString(cursor.getColumnIndex(MyDBHelper.CLASSIFY));

            record = new VoiceRemindRecord(_id, title, time, file, content, classify);
        }
        return record;
    }

}
