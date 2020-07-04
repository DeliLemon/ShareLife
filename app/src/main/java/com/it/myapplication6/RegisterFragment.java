package com.it.myapplication6;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class RegisterFragment extends Fragment {

    View registerView =null;
    FragmentManager fragmentManager=null;
    private EditText account,passwd1,passwd2,name,personality;
    private RegisterViewModel registerViewModel=null;
    private Button register_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        registerView=inflater.inflate(R.layout.activity_register,container,false);
        init();
        return registerView;
    }

    /**
     * 初始化控件
     * */
    private void init(){
        account=(EditText)registerView.findViewById(R.id.editText5_register_account);
        passwd1=(EditText)registerView.findViewById(R.id.editText6_register_passwd1);
        passwd2=(EditText)registerView.findViewById(R.id.editText7_register_passwd2);
        name=(EditText)registerView.findViewById(R.id.editText8_register_name);
        personality=(EditText)registerView.findViewById(R.id.editText9_register_class);
        register_btn=(Button)registerView.findViewById(R.id.button6_register_refg);
        registerViewModel=new RegisterViewModel();

        account.addTextChangedListener(new TextWatcherAdapter());
        passwd1.addTextChangedListener(new TextWatcherAdapter());
        passwd2.addTextChangedListener(new TextWatcherAdapter());
        name.addTextChangedListener(new TextWatcherAdapter());
        personality.addTextChangedListener(new TextWatcherAdapter());

        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(((EditText) v).getText().toString().length()<11){
                        showRegisterMess("账号长度小于11");
                        registerViewModel.setError(true);
                    }
                }
            }
        });
        passwd1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String value=((EditText) v).getText().toString();
                    if(value.length()<6){
                        showRegisterMess("密码长度小于6");
                        registerViewModel.setError(true);
                    }else if(value.matches(".*[^A-Za-z0-9._]")){
                        registerViewModel.setError(true);
                        showRegisterMess("密码格式：0-9、a-z、._");
                    }
                }
            }
        });
        passwd2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String value1=passwd1.getText().toString();
                String value2=passwd2.getText().toString();
                if(!hasFocus){
                    if(!value1.equals(value2)){
                        registerViewModel.setError(true);
                        showRegisterMess("两次输入密码不一致");
                    }
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewModel.isVaild();
                if(!registerViewModel.isError()){
                    //验证匹配
                    if(registerViewModel.verifyInfo(getContext())){
                        showRegisterMess("注册成功");
                        fragmentManager.popBackStack();
                    }else{
                        showRegisterMess("查无匹配信息");
                    }
                }else{
                    showRegisterMess("请输入正确格式信息");
                }
            }
        });

    }
    class TextWatcherAdapter implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String userAccount=account.getText().toString();
            String userPd1=passwd1.getText().toString();
            String userPd2=passwd2.getText().toString();
            String userName=name.getText().toString();
            String userPersonality=personality.getText().toString();
            registerViewModel.dataChange(userAccount,userPd1,userPd2,userName,userPersonality);
        }
    }
    /**
     * 显示信息
     * */
    private void showRegisterMess(String errorString) {
        Toast.makeText(getContext(),errorString, Toast.LENGTH_SHORT).show();
    }
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

}
