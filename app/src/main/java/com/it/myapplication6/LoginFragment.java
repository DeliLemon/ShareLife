package com.it.myapplication6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.it.HomeActivity;
import com.it.database.User;

public class LoginFragment extends Fragment {

    View loginView=null;
    LayoutInflater myinflater=null;

    FragmentManager fragmentManager;
    private ProgressBar pb=null;
    private LoginViewModel loginViewModel=null;
    private EditText editAccount =null;
    private EditText editPassword=null;
    private Button loginbtn=null;
    private Button regiseter=null;
    private User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myinflater=inflater;
        loginView=inflater.inflate(R.layout.activity_login,container,false);
        init();
        if(!isFirst()){
            SharedPreferences sp = getActivity().getSharedPreferences("userLogin",
                    Context.MODE_PRIVATE);
            String account=sp.getString("account",null);
            String passwd=sp.getString("passwd",null);
            if(account!=null&&passwd!=null){
                editAccount.setText(account);
                editPassword.setText(passwd);
            }
        }
        return loginView;
    }
    /**
     * 判断是否第一次登录
     * */
    public boolean isFirst() {
        SharedPreferences sp = getActivity().getSharedPreferences("userLogin",
                Context.MODE_PRIVATE);
        boolean isFirst=sp.getBoolean("FirstLogin", true);
        return isFirst;
    }
    /**
     * 初始化视图控件
     *
     * */

    private void init(){
        user = new User();
        loginViewModel=new LoginViewModel();
        pb=(ProgressBar)loginView.findViewById(R.id.progressBar1_login);
        editAccount =(EditText)loginView.findViewById(R.id.editText1_loginAccount);
        editPassword=(EditText)loginView.findViewById(R.id.editText2_loginPassword);
        loginbtn=(Button)loginView.findViewById(R.id.btn_login);
        regiseter=(Button)loginView.findViewById(R.id.btn_register);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                user.setAccount(editAccount.getText().toString());
                user.setPassword(editPassword.getText().toString());
                loginViewModel.LoginDataChanged(user);

            }
        };

        editAccount.addTextChangedListener(afterTextChangedListener);
        editPassword.addTextChangedListener(afterTextChangedListener);

        editAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(loginViewModel.getLoginStatus()!=LoginViewModel.LOGIN_SUCCEED && !hasFocus){
                    showLoginFailed(loginViewModel.getLoginError());
                }
            }
        });
        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    showLoginFailed(loginViewModel.getLoginError());
                }
                return false;
            }
        });

        /*按钮点击事件注册
        * */
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查输入格式
                if(loginViewModel.getLoginStatus()==LoginViewModel.LOGIN_SUCCEED){
                    pb.setVisibility(View.VISIBLE);
                    //验证账号
                    User user=loginViewModel.verifyAccount(getContext());
                    try{
                        if(user==null) {
                            throw new NullPointerException();
                        } else{
                            SharedPreferences sp = getActivity().getSharedPreferences("userLogin",
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor =sp.edit();
                            editor.putBoolean("FirstLogin",false);
                            editor.putString("account",editAccount.getText().toString());
                            editor.putString("passwd",editPassword.getText().toString());
                            editor.commit();
                            Bundle info=new Bundle();
                            info.putString("account",user.getAccount());
                            info.putString("userName",user.getName());
                            info.putString("personality",user.getPersonality());
                            info.putString("phone",user.getPhone());
                            Intent intent=new Intent(getActivity(), HomeActivity.class);
                            intent.putExtras(info);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }catch (NullPointerException e){
                        showLoginFailed("登录失败，账号或密码错误");
                    }

                }else{
                    showLoginFailed(loginViewModel.getLoginError());
                }
            }
        });

        regiseter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                registerFragment.setFragmentManager(fragmentManager);
                fragmentManager.beginTransaction()
                                .replace(R.id.mainActivity,registerFragment)
                                .addToBackStack(null)
                                .commit();
            }
        });
    }
    /**
     * 显示信息
     * */
    private void showLoginFailed(String errorString) {
        Toast.makeText(getContext(),errorString, Toast.LENGTH_SHORT).show();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

}
