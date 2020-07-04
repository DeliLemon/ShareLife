package com.it.ui.attention;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.it.HomeActivity;
import com.it.database.AttentionDao;
import com.it.database.User;
import com.it.myapplication6.R;

import java.util.ArrayList;
import java.util.List;


public class AttentionFragment extends Fragment {

    private View attentionView;
    ListView relation;
    List<User> users;
    User user;
    NavController navController=null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        attentionView= inflater.inflate(R.layout.fragment_attention,container,false);

        initView();
        return attentionView;
    }

    private void initView() {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        relation= attentionView.findViewById(R.id.attention_listview);
        user= ((HomeActivity)getActivity()).getUser();
        AttentionDao attentionDao = new AttentionDao(getContext());

        users =attentionDao.queryAttentions(user.getAccount());

        List<String> lists= new ArrayList<>();

        for(User user :users){
            lists.add(user.getName());
        }
        relation.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,lists));
        relation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle info=new Bundle();
                info.putString("account",users.get(position).getAccount());
                info.putString("userName",users.get(position).getName());
                info.putString("personality",users.get(position).getPersonality());
                navController.navigate(R.id.nav_other_space,info);
            }
        });
    }
}