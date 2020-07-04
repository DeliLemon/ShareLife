package com.it.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MomentDao {

    private SQLiteDatabase db=null;
    public MomentDao() {
    }
    // 关闭数据库
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }
    public MomentDao(Context context){
        db=DataBaseUnit.getSQLiteDatabase(context);

    }
    public List<Moment> queryMoments(String account){
        List<Moment> moments = new ArrayList<Moment>();
        Cursor cursor =db.query(Const.MOMENT_TABLE,null,Const.OWNER+"=? ",new String[]{account},
                null,null,Const.MOMENT_ID+" desc");
        while(cursor.moveToNext()){
            Moment moment =new Moment();
            moment.setId(cursor.getInt(cursor.getColumnIndex(Const.MOMENT_ID)));
            moment.setImage(cursor.getString(cursor.getColumnIndex(Const.IMAGE_SRC)));
            moment.setContent(cursor.getString(cursor.getColumnIndex(Const.CONTENT)));
            moment.setLocating(cursor.getString(cursor.getColumnIndex(Const.LOCATION)));
            moment.setDate(cursor.getString(cursor.getColumnIndex(Const.DATE)));
            moment.setOwnerAccount(account);
            moments.add(moment);
        }
        return moments;
    }
    public List<Moment> queryAllMoments(String account){
        List<Moment> moments = new ArrayList<Moment>();
        Cursor cursor =db.query(Const.MOMENT_TABLE,null,
                "ownerAccount =? or ownerAccount in ( select C_account from attention where account = ?)",
                new String[]{account,account},
                null,null,Const.MOMENT_ID+" desc");
        while(cursor.moveToNext()){
            Moment moment =new Moment();
            moment.setId(cursor.getInt(cursor.getColumnIndex(Const.MOMENT_ID)));
            moment.setImage(cursor.getString(cursor.getColumnIndex(Const.IMAGE_SRC)));
            moment.setContent(cursor.getString(cursor.getColumnIndex(Const.CONTENT)));
            moment.setLocating(cursor.getString(cursor.getColumnIndex(Const.LOCATION)));
            moment.setDate(cursor.getString(cursor.getColumnIndex(Const.DATE)));
            moment.setOwnerAccount(account);
            moments.add(moment);
        }
        return moments;
    }

    public void insertMoment(Moment moment){
        ContentValues values = new ContentValues();
        values.put(Const.IMAGE_SRC,moment.getImage());
        values.put(Const.CONTENT,moment.getContent());
        values.put(Const.LOCATION,moment.getLocating());
        values.put(Const.DATE,moment.getDate());
        values.put(Const.OWNER,moment.getOwnerAccount());
        long line =db.insert(Const.MOMENT_TABLE,null,values);
        if(line <= 0){
            Log.e("dao","moment_insertfail:"+line);
        }else{
            Log.i("dao","moment_insert: "+ line);
        }
    }

    //这个不应该存在
    public void updateMoment(Moment moment){

        ContentValues values = new ContentValues();
        values.put(Const.IMAGE_SRC,moment.getImage());
        values.put(Const.CONTENT,moment.getContent());
        values.put(Const.LOCATION,moment.getLocating());
        values.put(Const.DATE,moment.getDate());
        long line = db.update(Const.MOMENT_TABLE,values,
                Const.MOMENT_ID+ "=?",new String[]{""+moment.getId()});
        if(line <= 0){
            Log.e("moment_update","fail:"+line);
        }
    }

    public void deleteMoment(Integer id){
        long line = db.delete(Const.MOMENT_TABLE,Const.MOMENT_ID+ "=?",new String[]{""+id});
        if(line <= 0){
            Log.e("dao","moment_updatefail:"+line);
        }
    }
    public void deleteMoments(Integer[] ids){
        db.beginTransaction();
        try {
            for( int index =0 ;index <ids.length ;index++){
                db.delete(Const.MOMENT_TABLE,Const.MOMENT_ID+ "=?",new String[]{ids[index]+""});
            }
            db.setTransactionSuccessful();
        }catch (Exception i){
            Log.i("dao","deleteMomentsfail");
        }finally {
            db.endTransaction();
        }
    }
}
