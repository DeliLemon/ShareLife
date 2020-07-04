package com.it.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
/**
 * Helper类，用于建立、更新和打开数据库
 */
public  class DataBaseUnit extends SQLiteOpenHelper {

    public DataBaseUnit(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Const.CREATE_USER_TABLE);
        db.execSQL(Const.CREATE_MOMENT_TABLE);
        db.execSQL(Const.CREATE_ATTENTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Const.USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Const.MOMENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Const.ATTENTION_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Const.USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Const.MOMENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Const.ATTENTION_TABLE);
        onCreate(db);
    }
    /*  param : Context context
        创建一个DataBaseUnit实例
        创建或打开一个数据库SQLiteDatabase实例，其中DataBaseUnit.getWritableDatabase()
        得到的数据库具有读写的权限，而DataBaseUnit.getReadableDatabase()得到的数据库则具有只读的权限。
    * */
    public static SQLiteDatabase getSQLiteDatabase(Context context){
        DataBaseUnit dataBaseUnit = new DataBaseUnit(context,Const.DB_NAME,null,Const.DB_VERSION);
        SQLiteDatabase db;
        try {
            db = dataBaseUnit.getWritableDatabase();
        } catch (SQLiteException e) {
            db = dataBaseUnit.getReadableDatabase();
        }
        return db;
    }
}
