package com.example.admin.selectpictest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "sss";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 12;

    @BindView(R.id.photo_rv)
    RecyclerView photo_rv;
    private PhotoRecyclerAdapter adapter;
    //图片相关
    private ArrayList<String> path = new ArrayList<>();
    private List<String> picPath;//上传图片路径

    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initGallery();

        picPath = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        photo_rv.setLayoutManager(gridLayoutManager);

        adapter = new PhotoRecyclerAdapter(this, path, new PhotoRecyclerAdapter.TakePhoto() {
            @Override
            public void takePhoto() {
                requestPermission();
            }

            @Override
            public void delPhoto(int position, String url) {
                path.remove(position);
                adapter.notifyDataSetChanged();

            }
        });
        photo_rv.setAdapter(adapter);

    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.i(TAG, "拒绝过了");
                // 提示用户如果想要正常使用，要手动去设置中授权。
                Toast.makeText(this, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                Log.i(TAG, "进行授权");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            Log.i(TAG, "不需要授权 ");
            // 进行正常操作
            GalleryPick.getInstance().setGalleryConfig( galleryConfig ).open( this );
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSIONS_REQUEST_READ_CONTACTS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "同意授权");
                // 进行正常操作。
                GalleryPick.getInstance().setGalleryConfig( galleryConfig ).open( this );

            } else {
                Log.i(TAG, "拒绝授权");
            }
        }
    }

    /**
     * 图片的初始化
     */
    private void initGallery() {

        Log.e( TAG, "initGallery: ----" );
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i( TAG, "onStart: 开启" );
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i( TAG, "onSuccess: 返回数据" );
                path.clear();
                for (String s : photoList) {
                    Log.i( TAG, s );
                    path.add( s );
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancel() {
                Log.i( TAG, "onCancel: 取消" );
            }

            @Override
            public void onFinish() {
                Log.i( TAG, "onFinish: 结束" );
            }

            @Override
            public void onError() {
                Log.i( TAG, "onError: 出错" );
            }
        };
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader( new HelpGlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack( iHandlerCallBack )     // 监听接口（必填）
                .provider( "com.hz.junxinbaoan.fileProvider" )   // provider(必填)
                .pathList( path )                         // 记录已选的图片
                .multiSelect( true )                      // 是否多选   默认：false
                .multiSelect( true, 9 )                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize( 9 )                             // 配置多选时 的多选数量。    默认：9
                .crop( false )                             // 快捷开启裁剪功能，仅当单选 或直接开启相机时有效
                .crop( false, 1, 1, 500, 500 )             // 配置裁剪功能的参数，   默认裁剪比例 1:1
                .isShowCamera( true )                     // 是否现实相机按钮  默认：false
                .filePath( "/" + getPackageName() + "/pic" )          // 图片存放路径
                .build();
    }
}
