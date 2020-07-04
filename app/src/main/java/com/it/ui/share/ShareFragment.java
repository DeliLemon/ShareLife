package com.it.ui.share;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.it.HomeActivity;
import com.it.database.Moment;
import com.it.database.MomentDao;
import com.it.database.User;
import com.it.myapplication6.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


public class ShareFragment extends Fragment {

    private NavController navController;
    private LayoutInflater myinflater;
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView imageView=null;
    EditText content=null;
    TextView location_tv=null;
    ImageView locating=null;
    FloatingActionButton fabShare=null;
    private View shareLifeView=null;
    Moment moment=null;
    User user;
    String currentPhotoPath=null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myinflater=inflater;

        shareLifeView=inflater.inflate(R.layout.item_cardview,container,false);
        //container.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        initParameters();
        initView();
        return shareLifeView;
    }

    private void initView() {
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        imageView=(ImageView)shareLifeView.findViewById(R.id.item_image);
        content=(EditText)shareLifeView.findViewById(R.id.item_content);
        locating=(ImageView)shareLifeView.findViewById(R.id.item_locating_btn);
        location_tv=(TextView)shareLifeView.findViewById(R.id.item_locating_tv);

        content.setEnabled(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        locating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationClient.startLocation();
            }
        });

        fabShare=(FloatingActionButton)shareLifeView.findViewById(R.id.fab_share);
        fabShare.setVisibility(View.VISIBLE);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moment= new Moment();
                AlertDialog.Builder ask=new AlertDialog.Builder(getContext());
                ask.setIcon(R.drawable.edit)
                        .setTitle("提示")
                        .setMessage("是否发布")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String timeStamp = new SimpleDateFormat("yyyy年MM月dd HH：mm").format(new Date());
                                moment.setDate(timeStamp);
                                moment.setContent(content.getText().toString());
                                moment.setLocating(location_tv.getText().toString());
                                if(currentPhotoPath == null){
                                    moment.setImage("/sdcard/Android/data/com.it.myapplication6/files/Pictures/夕阳.jpg");
                                }else{
                                    moment.setImage(currentPhotoPath);
                                }
                                moment.setOwnerAccount(user.getAccount());
                                MomentDao momentDao = new MomentDao(getContext());
                                momentDao.insertMoment(moment);
                                momentDao.close();
                                navController.navigate(R.id.nav_slideshow);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navController.navigate(R.id.nav_slideshow);
                    }
                }).create().show();

            }
        });

        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(locationListener);

    }

    private void initParameters() {
        user= ((HomeActivity)getActivity()).getUser();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            imageView.setImageURI(contentUri);
            galleryAddPic();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.it.myapplication6.fileprovider",
                        photoFile);
                Log.i("path",photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    //String timeStamp = new SimpleDateFormat("yyyy年MM月dd HH：mm").format(new Date());
                    sb.append("地  址    : " + location.getCity()+location.getDistrict()+"\n");
                    //sb.append(timeStamp);
                } else {
                    //定位失败
                    sb.append("定位失败" );
                }

                //解析定位结果，
                String result = sb.toString();
                location_tv.setText(result);
            } else {
                location_tv.setText("定位失败");
            }
        }
    };
}