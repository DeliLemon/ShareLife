package com.it.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.it.database.User;
import com.it.database.UserDao;
import com.it.myapplication6.R;

public class SearchFragment extends Fragment {
    View searchView=null;

    EditText editText_search=null;
    Button button_search=null;
    ListView result;
    ArrayAdapter<String> arrayAdapter;
    NavController navController=null;
    User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        searchView = inflater.inflate(R.layout.search,container,false);
        init();
        return searchView;
    }

    private void init() {
        editText_search=searchView.findViewById(R.id.editText_search_host);
        button_search=searchView.findViewById(R.id.button_search);
        result=searchView.findViewById(R.id.search_host_listView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDao userDao =new UserDao(getContext());
                user = userDao.queryUser(editText_search.getText().toString().trim());
                if(user !=null){
                    arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,new String[]{user.getAccount()});
                    result.setAdapter(arrayAdapter);
                }
            }
        });
        result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle info=new Bundle();
                info.putString("account",user.getAccount());
                info.putString("userName",user.getName());
                info.putString("personality",user.getPersonality());
                navController.navigate(R.id.nav_other_space,info);
            }
        });
    }
}
