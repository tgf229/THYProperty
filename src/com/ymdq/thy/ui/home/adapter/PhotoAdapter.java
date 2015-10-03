package com.ymdq.thy.ui.home.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Image;
import com.ymdq.thy.ui.ViewPagerActivity;
import com.ymdq.thy.util.DisplayUtil;

public class PhotoAdapter extends BaseAdapter
{ 
    private Context context;
    
    protected List<Image> photos;
    
    private ArrayList<String> photoUrls;
    
    private int width;
    
    public PhotoAdapter(Context context,List<Image> photos,int width)//, String channel)
    {
        this.context = context;
        this.photos = photos;
        photoUrls = new ArrayList<String>();
        this.width = width;
        if (photos != null)
        {
            for (Image image : photos)
            {
                photoUrls.add(image.getImageUrlL());
            }
        }
    }
    
    @Override
    public int getCount()
    {
        return photos == null ? 0 : photos.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return photos.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        PhotoViewHolder photoViewHolder;
        if (null == convertView)
        {
            photoViewHolder = new PhotoViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_fresh_news_gridview_item, null);
            photoViewHolder.pic = (ImageView)convertView.findViewById(R.id.group_dynamic_photo);
            LinearLayout.LayoutParams linearParams =
                (LinearLayout.LayoutParams)photoViewHolder.pic.getLayoutParams(); //取控件textView当前的布局参数  
            linearParams.width =
                (context.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(context, width)) / 3;// 控件的高强制设成20
            linearParams.height = linearParams.width;
            photoViewHolder.pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            convertView.setTag(photoViewHolder);
        }
        else
        {
            photoViewHolder = (PhotoViewHolder)convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(photos.get(position).getImageUrlS(),
            photoViewHolder.pic,
            JRApplication.setAllDisplayImageOptions(context, "community_default", "community_default",
                "community_default"));
        photoViewHolder.pic.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent intent = new Intent(context, ViewPagerActivity.class);
                intent.putExtra("currentItem", position);
                intent.putStringArrayListExtra("photoUrls", photoUrls);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    
    class PhotoViewHolder
    {
        private ImageView pic;
    }
    
}
