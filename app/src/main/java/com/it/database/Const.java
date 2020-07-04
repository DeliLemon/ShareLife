package com.it.database;

public class Const {
    public static final String DB_NAME = "LIFE.db"; //数据库名称
    public static final int DB_VERSION = 2;         //版本号

    public static final String USER_TABLE ="user";          //用户表
    public static final String MOMENT_TABLE="moment";       //生活瞬间表
    public static final String ATTENTION_TABLE="attention"; //关注表

    //用户表
    public static final String ACCOUNT = "account";         // 用户账号
    public static final String PASSWORD = "passwd";         //密码
    public static final String NAME = "name";               //用户名
    public static final String PHONE = "phone";             //手机
    public static final String PERSONALITY = "personality"; //个性签名

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE "+ USER_TABLE + "(" +
                ACCOUNT + " text primary key, "+
                PASSWORD + " text not null, "+
                NAME + " text not null, "+
                PHONE + " text not null, "+
                PERSONALITY + " TEXT"+
            ");";

    //生活表
    public static final String MOMENT_ID = "M_id";          //id自增类型
    public static final String IMAGE_SRC = "imageSrc";      //图片位置
    public static final String CONTENT = "content";         //内容
    public static final String LOCATION = "location";       //位置
    public static final String DATE = "M_date";             //日期
    public static final String OWNER = "ownerAccount";      //发布者账号

    public static final String CREATE_MOMENT_TABLE =
            "CREATE TABLE " + MOMENT_TABLE +"("+
                MOMENT_ID + " integer primary key autoincrement, "+
                IMAGE_SRC + " TEXT NOT NULL ,"+
                CONTENT + " TEXT NOT NULL ,"+
                LOCATION + " TEXT ,"+
                DATE + " TEXT NOT NULL ,"+
                OWNER + " TEXT NOT NULL "  +
            ");";

    //关注表

    public static final String C_ACCOUNT = "C_account"; //关注者账号字段

    public static final String CREATE_ATTENTION_TABLE =
            "CREATE TABLE attention(account TEXT NOT NULL ,C_account TEXT NOT NULL );";


}
