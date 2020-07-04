package com.it.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserDao {
    private SQLiteDatabase db = null;

    public UserDao (){

    }
    public UserDao(Context context){
        //获取数据库实例
        db=DataBaseUnit.getSQLiteDatabase(context);
    }
    //关闭数据库
    public void close(){
        if (db != null) {
            db.close();
            db = null;
        }
    }
    /*
     * 创建用户
     * */
    public void  insertUser(User user){

        ContentValues values = new ContentValues();
        values.put(Const.ACCOUNT,user.getAccount());
        values.put(Const.PASSWORD,user.getPassword());
        values.put(Const.NAME,user.getName());
        values.put(Const.PHONE,user.getPhone());
        values.put(Const.PERSONALITY,user.getPersonality());
        long line=db.insert(Const.USER_TABLE,null,values);
        if(line <= 0){
            Log.e("daofail","insertUserfail:"+line);
        }else{
            Log.i("dao","insertUser :"+ line);
        }
    }
    /*
     * 更新用户
     * */
    public void updateUser(User old_user,User new_user){

        ContentValues values = new ContentValues();
        values.put(Const.PASSWORD,new_user.getPassword());
        values.put(Const.NAME,new_user.getName());
        values.put(Const.PHONE,new_user.getPhone());
        values.put(Const.PERSONALITY,new_user.getPersonality());
        long line = db.update(Const.USER_TABLE,values,Const.ACCOUNT+"=? and "+Const.PASSWORD+"=?",
                new String[]{old_user.getAccount(),old_user.getPassword()});
        if(line <= 0){
            Log.e("daofail","updateUserfail:"+line);
        }else{
            Log.i("dao","updateUser: "+ line);
        }
    }
    /*
     * 删除用户
     * */
    public void deteleUser(String account){
        long line = db.delete(Const.USER_TABLE,Const.ACCOUNT+"=? ",new String[]{account});
        if(line <= 0){
            Log.e("daofail","deteleUserfail:"+line);
        }else{
            Log.i("dao","deteleUser: "+ line);
        }
    }
    /*
    * 查询用户
    * 通过账号、密码查询
    * */
    public User queryUser(User user){

        Cursor cursor=db.query(Const.USER_TABLE,null,Const.ACCOUNT+"=? and "+Const.PASSWORD+"=?",
                new String[]{user.getAccount(),user.getPassword()},null,null,null);
        if(cursor.moveToNext()){
            user.setName(cursor.getString(cursor.getColumnIndex(Const.NAME)));
            user.setPersonality(cursor.getString(cursor.getColumnIndex(Const.PERSONALITY)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(Const.PHONE)));
        }else{
            Log.e("daofail","queryUserfail"+user.getAccount()+"**"+user.getPassword());
            return null;
        }
        return user;
    }
    /*
     * @para String account
     * 查询用户
     *
     * */
    public User queryUser(String account){
        Cursor cursor=db.query(Const.USER_TABLE,null,Const.ACCOUNT+"=?",
                new String[]{account},null,null,null);
        User user=null;
        if(cursor.moveToNext()){
            user=new User();
            user.setAccount(account);
            user.setName(cursor.getString(cursor.getColumnIndex(Const.NAME)));
            user.setPersonality(cursor.getString(cursor.getColumnIndex(Const.PERSONALITY)));
        }else{
            Log.e("daofail","queryUserfail"+user.getAccount()+"**"+user.getPassword());
            return null;
        }
        return user;
    }
}
