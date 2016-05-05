package com.xiong.mdapplication;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.GridView;

import com.xiong.mdapplication.adapter.PhotoAdapter;
import com.xiong.mdapplication.bean.ImageInfo;
import com.xiong.mdapplication.dao.ChoosePhotoDao;
import com.xiong.mdapplication.service.PhotoService;

import java.util.ArrayList;

/**
 * Created by zxh on 2016/5/5.
 */
public class ChoosePhotoActivity extends FragmentActivity implements PhotoService.UploadListener {


  private Button mBtnUpload;
  private GridView mGvPhotoContent;

  private ChoosePhotoDao photoDao;
  private ArrayList<ImageInfo> mImageInfos = new ArrayList<>();

  private PhotoAdapter mAdapter;
  private PhotoService mService;

  //服务连接的
  private ServiceConnection mServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      PhotoService.MyBinder binder = (PhotoService.MyBinder) service;

      //根据binder获取service
      mService = binder.getService();

      mService.upload(ChoosePhotoActivity.this, mImageInfos);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
  };


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.content_scrolling);

    initViews();

    //


    String path = null;

    if (path != null) {
      mImageInfos.add(new ImageInfo(path, "image" + (Math.random() * 1001 - 1), -1,
              mImageInfos.size()));
    }

    if (mAdapter == null) {
      mAdapter = new PhotoAdapter(this, mImageInfos);
      mGvPhotoContent.setAdapter(mAdapter);
    } else mAdapter.notifyDataChanged(mImageInfos);

  }

  private void initViews() {
    mBtnUpload = (Button) findViewById(R.id.btn_upload);
    mGvPhotoContent = (GridView) findViewById(R.id.photo_grid);
  }

  @Override
  public void onUpload(int position, int progress) {

  }

  @Override
  public void onFinish(int position) {

  }


  public static void start(String path) {

  }

}
