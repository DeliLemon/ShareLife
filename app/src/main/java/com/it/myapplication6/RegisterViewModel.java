package com.it.myapplication6;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.it.database.User;
import com.it.database.UserDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewModel {
    private String account="",passwd1="",passwd2="",name="",personality="";
    private Context context;
    private User user;
    Thread mthred;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what==1){
                isError=false;
            }else{
                isError=true;
            }
            Log.i("handler",""+isError);
        }
    };
    private boolean isError=true;
    public RegisterViewModel(){
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public void dataChange(String userAccount, String userPd1, String userPd2, String name, String personality) throws NullPointerException{
        this.account = userAccount;
        this.passwd1 = userPd1;
        this.passwd2 = userPd2;
        this.name = name;
        this.personality = personality;
    }

    /*保留的验证接口
    * */
    public boolean verifyInfo(Context c) {

        context=c;
        boolean verify =true;
        if(verify){
            createAccount();
        }
        return verify;
    }

    public void isVaild(){
        //账号格式
        isError=false;
        if(account.length()<11){
            isError=true;
            return;
        }
        if(passwd1.length()<6){
            isError=true;
            return;
        }else{
            if(passwd1.matches(".*[^A-Za-z0-9._]")){
                isError=true;
                return;
            }
        }
        if(!passwd1.equals(passwd2)){
            isError=true;
            return;
        }
    }
    private void createAccount(){
        new Thread(){
            @Override
            public void run() {
                UserDao userDao =new UserDao(context);
                user=new User(account,passwd1,name,personality,account);
                userDao.insertUser(user);
                userDao.close();
            }
        }.start();
    }
}
