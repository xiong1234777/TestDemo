package com.xiong.mdapplication.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.xiong.mdapplication.R;
import com.xiong.mdapplication.bean.ImageInfo;
import com.xiong.mdapplication.engine.ImageLoader;

import java.util.List;

/**
 * Created by zxh on 2016/3/16.
 * 通用选择完图片列表的适配器
 */
public class PhotoAdapter extends CommonAdapter<ImageInfo> {
    public PhotoAdapter(Context context, List<ImageInfo> data) {
        super(context, data, R.layout.adapter_choose_photo);
    }

    @Override
    public void convert(ViewHolder helper, final ImageInfo item) {

        ImageLoader.getInstance().loadImage(item.path, (ImageView) helper.getView(R.id.iv_choose));
        helper.setOnClickListener(R.id.delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(item);
                notifyDataChanged(mDatas);
            }
        });
        String progress = null;
        if (item.progress==-1) {
            progress = "";
        } else if (item.progress == -2) {
            progress = "点击重试";
        } else {
            progress = item.progress+"";
            helper.setViewGone(R.id.delete);
        }
        helper.setText(R.id.tv_progress, progress);
    }
}
