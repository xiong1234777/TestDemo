package com.xiong.mdapplication.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Xiong on 2016/5/4.
 */
public class ImageInfo implements Parcelable {
    public String path;
    public String name;
    public int progress;

    public ImageInfo(String path, String name, int progress, int position) {
        this.path = path;
        this.name = name;
        this.progress = progress;
        this.position = position;
    }

    public int position;


    public ImageInfo() {}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeInt(this.progress);
        dest.writeInt(this.position);
    }

    protected ImageInfo(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.progress = in.readInt();
        this.position = in.readInt();
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel source) {return new ImageInfo(source);}

        @Override
        public ImageInfo[] newArray(int size) {return new ImageInfo[size];}
    };
}
