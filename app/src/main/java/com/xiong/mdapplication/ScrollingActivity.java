package com.xiong.mdapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.xiong.mdapplication.adapter.PhotoAdapter;
import com.xiong.mdapplication.bean.ImageInfo;
import com.xiong.mdapplication.dao.ChoosePhotoDao;
import com.xiong.mdapplication.service.PhotoService;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity implements PhotoService.UploadListener {


    private GridView mGridView;
    private ChoosePhotoDao photoDao;
    private ArrayList<ImageInfo> mImageInfos = new ArrayList<>();
    private PhotoAdapter mAdapter;
    private PhotoService mService;
    private Button btn_select;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PhotoService.MyBinder binder = (PhotoService.MyBinder) service;

            //根据binder获取service
            mService = binder.getService();

            mService.upload(ScrollingActivity.this, mImageInfos);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Button mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        mGridView = (GridView) findViewById(R.id.photo_grid);
        btn_select = (Button) findViewById(R.id.btn_select);

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoDao == null) {
                    photoDao = new ChoosePhotoDao(ScrollingActivity.this, false);
                }
                photoDao.show();
            }
        });

        mFab = (Button) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageInfos.size()==0) return;
                Intent intent = new Intent(ScrollingActivity.this, PhotoService.class);
                intent.putParcelableArrayListExtra("images", mImageInfos);
                bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if (photoDao == null) {
                photoDao = new ChoosePhotoDao(ScrollingActivity.this, false);
            }
            photoDao.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpload(final int position, final int progress) {

        mGridView.post(new Runnable() {
            @Override
            public void run() {
                mFab.setEnabled(false);
                mImageInfos.get(position).progress = progress;
                mAdapter.notifyDataChanged(mImageInfos);
            }
        });
    }

    @Override
    public void onFinish(int position) {
        Snackbar.make(mFab,"第"+(position+1)+"张图片上传完成",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path = photoDao.onResult(requestCode, resultCode, data);


        if (path != null) {
            mImageInfos.add(new ImageInfo(path, "image" + (Math.random() * 1001 - 1), -1,
                    mImageInfos.size()));
        }


        if (mAdapter == null) {
           mAdapter = new PhotoAdapter(this, mImageInfos);
            mGridView.setAdapter(mAdapter);
        } else mAdapter.notifyDataChanged(mImageInfos);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

}
