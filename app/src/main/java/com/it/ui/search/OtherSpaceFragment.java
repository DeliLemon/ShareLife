package com.it.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.it.HomeActivity;
import com.it.database.Attention;
import com.it.database.AttentionDao;
import com.it.database.User;
import com.it.myapplication6.R;

import java.util.ArrayList;

public class OtherSpaceFragment extends Fragment {

    View osView=null;
    NavController navController=null;
    TextView userName_tv=null,personality_tv=null;
    Button space_btn=null,attention_btn=null;

    User A_user=null,user=null;
    Bundle bundle=null;
    Attention attention =null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        osView=inflater.inflate(R.layout.fragment_space,container,false);

        initParameters();
        initView();

        return osView;
    }

    private void initParameters() {
        bundle=getArguments();
        if(bundle!=null){
            A_user = new User();
            A_user.setAccount(bundle.getString("account"));
            A_user.setName(bundle.getString("userName"));
            A_user.setPersonality(bundle.getString("personality"));
        }
        user= ((HomeActivity)getActivity()).getUser();
        attention =new Attention();
        attention.setM_user(user.getAccount());
        attention.setA_user(A_user.getAccount());
    }

    private void initView() {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        userName_tv=osView.findViewById(R.id.space_name_tv);
        personality_tv=osView.findViewById(R.id.space_personality_tv);
        attention_btn=osView.findViewById(R.id.space_attention_btn);

        userName_tv.setText(A_user.getName());
        personality_tv.setText(A_user.getPersonality());



        AttentionDao attentionDao =new AttentionDao(getContext());
        if(attentionDao.queryAttention(attention)){
            attention_btn.setText("取消关注");
        }else{
            attention_btn.setText("关注");
            Log.i("test",attention.getM_user()+"**"+attention.getA_user());
        }
        attentionDao.close();
        attention_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttentionDao attentionDao =new AttentionDao(getContext());

                if(attention_btn.getText().toString().equals("关注")){
                    attentionDao.insertAttention(attention);
                    attention_btn.setText("取消关注");
                }else{
                    attentionDao.deleteAttention(attention);
                    attention_btn.setText("关注");
                }
                attentionDao.close();
            }
        });
    }
}
