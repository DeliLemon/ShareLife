package com.it.myapplication6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.it.HomeActivity;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
        fragmentManager=getSupportFragmentManager();
        LoginFragment loginFragment =new LoginFragment();
        loginFragment.setFragmentManager(fragmentManager);
        fragmentManager.beginTransaction().add(R.id.mainActivity, loginFragment).commit();
//        Intent intent =new Intent(this, HomeActivity.class);
//        startActivity(intent);
    }



}
