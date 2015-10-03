package com.ymdq.thy.bean;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ChoiseMorePhotosParcelable implements Parcelable
{
    /**
     * 图片的父目录名称
     */
    private String fileName;
    
    /**
     * 图片列表
     */
    private ArrayList<String> fileImageList = new ArrayList<String>();
    
    @Override
    public int describeContents()
    {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(fileName);
        dest.writeList(fileImageList);
    }
    
    public static final Creator<ChoiseMorePhotosParcelable> CREATOR = new Creator<ChoiseMorePhotosParcelable>()
    {
        @Override
        public ChoiseMorePhotosParcelable createFromParcel(Parcel source)
        {
            ChoiseMorePhotosParcelable ftl = new ChoiseMorePhotosParcelable();
            ftl.fileName = source.readString();
            ftl.fileImageList = source.readArrayList(ChoiseMorePhotosParcelable.class.getClassLoader());
            return ftl;
        }

        @Override
        public ChoiseMorePhotosParcelable[] newArray(int size)
        {
            return null;
        }
        
    };

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public ArrayList<String> getFileImageList()
    {
        return fileImageList;
    }

    public void setFileImageList(ArrayList<String> fileImageList)
    {
        this.fileImageList = fileImageList;
    }
    
}
