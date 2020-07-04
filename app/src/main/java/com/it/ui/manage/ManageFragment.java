package com.it.ui.manage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.it.database.Moment;
import com.it.database.MomentDao;
import com.it.myapplication6.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManageFragment extends Fragment {

    private View galleryView=null;
    private LayoutInflater myInflater=null;
    private Bundle bundle;
    private Moment moment;
    ImageView imageView=null;
    TextView location=null;
    EditText content=null;
    FloatingActionButton fabShare=null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myInflater = inflater;
        galleryView = inflater.inflate(R.layout.item_cardview,container,false);
        initView();
        return galleryView;
    }

    private void initView() {
        bundle = getArguments();
        imageView=(ImageView)galleryView.findViewById(R.id.item_image);
        content=(EditText)galleryView.findViewById(R.id.item_content);
        location=(TextView)galleryView.findViewById(R.id.item_locating_tv);
        fabShare=(FloatingActionButton)galleryView.findViewById(R.id.fab_share);
        //content.setEnabled(false);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content.isEnabled()){
                    AlertDialog.Builder ask=new AlertDialog.Builder(getContext());
                    ask.setIcon(R.drawable.edit)
                            .setTitle("提示")
                            .setMessage("是否更改")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String timeStamp = new SimpleDateFormat("yyyy年MM月dd HH：mm").format(new Date());
                                    moment.setDate(timeStamp);
                                    moment.setContent(content.getText().toString());
                                    MomentDao momentDao = new MomentDao(getContext());
                                    momentDao.updateMoment(moment);
                                    momentDao.close();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    content.setText(moment.getContent());
                                }
                            }).create().show();
                    content.setEnabled(false);
                }else{
                    content.setEnabled(true);
                }

            }
        });
        if(bundle != null){
            moment=new Moment();
            fabShare.setVisibility(View.VISIBLE);
            moment.setId(Integer.parseInt(bundle.getString("M_id")));
            moment.setImage(bundle.getString("M_image"));
            moment.setContent(bundle.getString("M_content"));
            moment.setLocating(bundle.getString("M_loc"));
            moment.setOwnerAccount(bundle.getString("M_owner"));
            moment.setDate(bundle.getString("M_date"));
            File image=new File(moment.getImage());
            if(image.exists()){
                imageView.setImageURI(Uri.fromFile(image));
            }
            content.setText(moment.getContent());
            location.setText(moment.getLocating()+"\n"+moment.getDate());
        }
    }
}