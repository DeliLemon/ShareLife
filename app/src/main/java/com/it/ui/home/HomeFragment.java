package com.it.ui.home;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.it.HomeActivity;
import com.it.database.Moment;
import com.it.database.MomentDao;
import com.it.database.User;
import com.it.myapplication6.R;

import java.io.File;
import java.util.List;

public class HomeFragment extends Fragment {

    View homeView=null;
    NavController navController;
    private FragmentManager fragmentManager;
    private LayoutInflater myinflater;
    private FloatingActionButton addButton;
    private MomentDao momentDao=null;
    private List<Moment> moments=null;
    private ListView container=null;
    private ListViewAdapter listViewAdapter=null;
    private User user;
    private Bundle bundle=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myinflater=inflater;
        homeView=inflater.inflate(R.layout.fragment_slideshow,container,false);

        initParameters();
        initView();

        return homeView;
    }

    private void initParameters(){
        user= ((HomeActivity)getActivity()).getUser();
        momentDao=new MomentDao(getContext());

        if(user!=null){
            moments=momentDao.queryAllMoments(user.getAccount());
            momentDao.close();
            listViewAdapter=new ListViewAdapter();
        }


    }

    private void initView(){
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        addButton=(FloatingActionButton) homeView.findViewById(R.id.fab);
        container=(ListView)homeView.findViewById(R.id.listView_container);
        container.setAdapter(listViewAdapter);
        //进入单项item查看具体内容
        container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Moment moment = moments.get(position);

                bundle.putString("M_id",moment.getId()+"");
                bundle.putString("M_image",moment.getImage());
                bundle.putString("M_content",moment.getContent());
                bundle.putString("M_loc",moment.getLocating());
                bundle.putString("M_date",moment.getDate());
                bundle.putString("M_owner",moment.getOwnerAccount());

                navController.navigate(R.id.nav_read,bundle);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_share);

            }
        });
    }
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
    //自定义适配器类
    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return moments.size();
        }

        @Override
        public Object getItem(int position) {
            return moments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return moments.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if(convertView==null){
                viewHolder=new ViewHolder();
                convertView=myinflater.inflate(R.layout.item_cardview,parent,false);
                EditText content=(EditText)convertView.findViewById(R.id.item_content);
                TextView location=(TextView)convertView.findViewById(R.id.item_locating_tv);
                ImageView imageView=(ImageView)convertView.findViewById(R.id.item_image);
                ImageView locating=(ImageView)convertView.findViewById(R.id.item_locating_btn);


                viewHolder.content=content;
                viewHolder.imageView=imageView;
                viewHolder.location=location;
                viewHolder.locating=locating;

                convertView.setTag(viewHolder);

            }else{
                viewHolder=(ViewHolder) convertView.getTag();
            }

            File image=new File(moments.get(position).getImage());
            if(image.exists()){
                viewHolder.imageView.setImageURI(Uri.fromFile(image));
            }
            viewHolder.content.setText(moments.get(position).getContent());
            viewHolder.location.setText(moments.get(position).getLocating()+"\n"+moments.get(position).getDate());

            return convertView;
        }
    }
    class  ViewHolder{
        ImageView imageView;
        EditText content;
        TextView location;
        ImageView locating;
    }
}