package com.it.ui.slideshow;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.it.HomeActivity;
import com.it.database.Moment;
import com.it.database.MomentDao;
import com.it.database.User;
import com.it.myapplication6.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class SlideshowFragment extends Fragment {

    private FragmentManager fragmentManager;
    private LayoutInflater myinflater;
    private FloatingActionButton addButton;
    private MomentDao momentDao=null;
    private View slideView=null;
    private List<Moment> moments=null;
    private ListView container=null;
    private ListViewAdapter listViewAdapter=null;
    private User user;
    private Bundle bundle=null;
    NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myinflater=inflater;
        slideView=inflater.inflate(R.layout.fragment_slideshow,container,false);
        initParameters();
        initView();
        return slideView;
    }

    private void initView(){
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        addButton=(FloatingActionButton) slideView.findViewById(R.id.fab);
        container=(ListView)slideView.findViewById(R.id.listView_container);
        container.setAdapter(listViewAdapter);
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

                navController.navigate(R.id.nav_manage,bundle);
                //Toast.makeText(getContext(),"onclick",Toast.LENGTH_SHORT).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_share);

            }
        });
    }
    private void initParameters(){
        user= ((HomeActivity)getActivity()).getUser();
        momentDao=new MomentDao(getContext());

//        Moment moment=new Moment();
//
//        moment.setContent("朝生夕逝 天地为家");
//        moment.setImage("/sdcard/Android/data/com.it.myapplication6/files/Pictures/夕阳.jpg");
//        String timeStamp = new SimpleDateFormat("yyyy年MM月dd HH：mm").format(new Date());
//        moment.setDate(timeStamp);
//        moment.setOwnerAccount("123");
//        //moment.setImage("/sdcard/Android/data/com.it.myapplication6/files/Pictures/夕阳.jpg");
//        moment.setLocating("海边");
//
//        momentDao.insertMoment(moment);

        if(user!=null){
            moments=momentDao.queryMoments(user.getAccount());
            momentDao.close();
            listViewAdapter=new ListViewAdapter();
        }


    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
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
