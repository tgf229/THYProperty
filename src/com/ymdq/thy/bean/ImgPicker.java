package com.ymdq.thy.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 相册图片选择器
 * 
 * @author  huangchao
 * @version  [版本号, July 1, 2014]
 */
public class ImgPicker implements Parcelable
{
    /**
     * 缩略图地址
     */
    public String thumb;
    
    /**
     * 原图地址
     */
    public String path;
    
    /**
     * 是否已选中
     */
    public int picked;
    
    public ImgPicker(String thumb, String path, int picked)
    {
        super();
        this.thumb = thumb;
        this.path = path;
        this.picked = picked;
    }

    @Override
    public int describeContents()
    { 
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(thumb);
        dest.writeString(path);
        dest.writeInt(picked);
    }
    
    public static final Parcelable.Creator<ImgPicker> CREATOR = new Parcelable.Creator<ImgPicker>()
    {
        public ImgPicker createFromParcel(Parcel in)
        {
            ImgPicker img = new ImgPicker();
            img.thumb = in.readString();
            img.path = in.readString();
            img.picked = in.readInt();
            return img;
        }
        
        public ImgPicker[] newArray(int size)
        {
            return new ImgPicker[size];
        }
    };
    
    private ImgPicker()
    {
        super();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof ImgPicker)
        {
            return ((ImgPicker)o).path.equalsIgnoreCase(this.path);
        }
        else
        {
            return false;
        }
        
//        return super.equals(o);
    }
}
