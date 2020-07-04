package com.it.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AttentionDao {

    private SQLiteDatabase db = null;

    public AttentionDao(Context context) {
        db=DataBaseUnit.getSQLiteDatabase(context);
    }

    public AttentionDao() {
    }

    public void close(){
        if(db !=null){
            db.close();
            db = null;
        }
    }
    public void insertAttention(Attention attention){

        ContentValues values =new ContentValues();
        values.put(Const.ACCOUNT,attention.getM_user());
        values.put(Const.C_ACCOUNT,attention.getA_user());
        long line =db.insert(Const.ATTENTION_TABLE,null,values);
        if(line <= 0){
            Log.e("daofail","insertAttentionfail:"+line);
        }else{
            Log.i("dao","insertAttention :"+ line);
        }
    }
    public void deleteAttention(Attention attention){
        long line = db.delete(Const.ATTENTION_TABLE,Const.ACCOUNT+"=? and "+Const.C_ACCOUNT+"=?",
                new String[]{attention.getM_user(),attention.getA_user()});
        if(line <= 0){
            Log.e("daofail","deleteAttentionfail:"+line);
        }else{
            Log.i("dao","deleteAttention :"+ line);
        }
    }
    public ArrayList<User> queryAttentions(String account){
        ArrayList<User> result =new ArrayList<User>();
        Cursor cursor= db.query(Const.USER_TABLE,null,
                "account in  ( select C_account from attention where account = ?)",
                 new String[]{account},
                null,null,null);
        while (cursor.moveToNext()){
            User user=new User();
            user.setAccount(cursor.getString(cursor.getColumnIndex(Const.ACCOUNT)));
            user.setName(cursor.getString(cursor.getColumnIndex(Const.NAME)));
            user.setPersonality(cursor.getString(cursor.getColumnIndex(Const.PERSONALITY)));
            result.add(user);
        }
        return result;
    }
    public boolean queryAttention(Attention attention){

        Cursor cursor= db.query(Const.ATTENTION_TABLE,null,
                Const.ACCOUNT+"= ? and "+Const.C_ACCOUNT+"=?"
                ,new String[]{attention.getM_user(),attention.getA_user()},
                null,null,null);
        return cursor.moveToNext();
    }
}
