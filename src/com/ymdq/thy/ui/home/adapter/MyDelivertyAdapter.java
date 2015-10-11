package com.ymdq.thy.ui.home.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.MyCentralDelivertyDoc;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.util.GeneralUtils;

public class MyDelivertyAdapter extends BaseAdapter
{
    private Context context;
    
    private List<MyCentralDelivertyDoc> mList;
    
    private DisplayImageOptions option;
    
    public MyDelivertyAdapter(Context context,List<MyCentralDelivertyDoc> list)
    {
        this.context = context;
        this.mList = list;
        option = new DisplayImageOptions.Builder().cacheInMemory(true)
            .showImageOnFail(R.drawable.community_default)
            .showImageOnLoading(R.drawable.community_default)
            .showImageForEmptyUri(R.drawable.community_default)
            .cacheOnDisc(true)
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(0))
            .build();
    }
    
    @Override
    public int getCount()
    {
        return mList == null ? 0 : mList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return mList == null ? null : mList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyCentralDelivertyDoc entity = mList.get(position);
        HolderView mHolder;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_my_central_deliverty_item, null);
            mHolder.logo = (ImageView)convertView.findViewById(R.id.logo);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.time = (TextView)convertView.findViewById(R.id.time);
            mHolder.number = (TextView)convertView.findViewById(R.id.number);
            mHolder.status = (Button)convertView.findViewById(R.id.status);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        ImageLoader.getInstance().displayImage(entity.getLogo(), mHolder.logo, option);
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.gethName()))
        {
            mHolder.name.setText(entity.gethName());
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTime()))
        {
            mHolder.time.setText(GeneralUtils.splitToSecond(entity.getTime()));
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getNum()))
        {
            mHolder.number.setText(entity.getNum());
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getType()))
        {
            //待领取
            if(Constants.NO_TAKE_DELIVERTY.equals(entity.getType()))
            {
                mHolder.status.setBackgroundResource(R.drawable.service_icon_processing);
                mHolder.status.setText("待领取");
            }
            //已收取
            else if(Constants.HAS_TAKE_DELIVERTY.equals(entity.getType()))
            {
                mHolder.status.setBackgroundResource(R.drawable.service_icon_complete);
                mHolder.status.setText("已领取");
            }
        }
        
        return convertView;
    }
    
    class HolderView
    {
        ImageView logo;
        TextView name;
        TextView time;
        TextView number;
        Button status;
    }
    
}
