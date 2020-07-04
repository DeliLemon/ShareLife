package com.it.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.it.database.Moment;
import com.it.myapplication6.R;

import java.io.File;

public class ReadFragment extends Fragment {

    View readView=null;
    ImageView imageView=null;
    TextView location=null;
    EditText content=null;
    Bundle bundle=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        readView= inflater.inflate(R.layout.item_cardview_readable,container,false);
        initView();

        return readView;
    }

    private void initView() {
        bundle = getArguments();
        imageView=(ImageView)readView.findViewById(R.id.item_image_read);
        content=(EditText)readView.findViewById(R.id.item_content_read);
        location=(TextView)readView.findViewById(R.id.item_locating_tv_read);

        if(bundle != null){
            Moment moment=new Moment();
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
