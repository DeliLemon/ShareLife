package com.it.myapplication6;

import android.content.Context;

import com.it.database.User;
import com.it.database.UserDao;


public class LoginViewModel {
	final static int LOGIN_LOCK=0;
	final static int LOGIN_ACCOUNTERROR=1;
	final static int LOGIN_PASSWORDERROR=2;
	final static int LOGIN_ALLERROR=3;
	final static int LOGIN_SUCCEED=4;
	private User user=null;
	String LoginError=null;
	private int loginStatus=LOGIN_LOCK;
	
	
	public int getLoginStatus() {
		return loginStatus;
	}

	public LoginViewModel(User user) {
		super();
		this.user=user;
	}
	
	public LoginViewModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void LoginDataChanged(User user) {
		this.user=user;
		if(!isAccountVaild()) {
			loginStatus=LOGIN_ACCOUNTERROR;
		}
		if(!isPasswordVaild()) {
			loginStatus=LOGIN_PASSWORDERROR;
		}
		if(isAccountVaild()&&isPasswordVaild()) {
			loginStatus=LOGIN_SUCCEED;
		}
		if(!isAccountVaild()&&!isPasswordVaild()) {
			loginStatus=LOGIN_ALLERROR;
		}
	}
	public boolean isAccountVaild() {
		if(user.getAccount().length() <11)
			return false;
		return true;
	}
	public boolean isPasswordVaild() {
		if(user.getPassword().length()<6) {
			return false;
		}else if(user.getPassword().matches(".*[^A-Za-z0-9._]")) {
			return false;
		}
		return true;
	}
	public String getLoginError() {
		switch (loginStatus) {
			case LOGIN_LOCK:
				LoginError="无输入内容";
				break;
			case LOGIN_ACCOUNTERROR:
				LoginError="账号长度小于11";
				break;
			case LOGIN_PASSWORDERROR:
				LoginError="密码长度不低于6，格式：0-9、a-z、._";
				break;
			case LOGIN_ALLERROR:
				LoginError="账号长度小于12\n密码长度不低于6，格式：0-9、a-z、._";
				break;
			case LOGIN_SUCCEED:
				LoginError=null;
				break;
		}
		return LoginError;
	}
	public User verifyAccount(Context context){
		UserDao userDao =new UserDao(context);
		User user=userDao.queryUser(this.user);
		//User user=new User("201710214626",null,"西瓜大人","物联网172","学生");
		userDao.close();
		return user;
	}
	
}
