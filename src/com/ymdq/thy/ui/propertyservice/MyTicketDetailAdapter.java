package com.ymdq.thy.ui.propertyservice;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Image;
import com.ymdq.thy.bean.home.ImageList;
import com.ymdq.thy.bean.propertyservice.MyTicketDetailPath;
import com.ymdq.thy.bean.propertyservice.MyTicketDetailResponse;
import com.ymdq.thy.ui.home.adapter.PhotoAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.view.MyGridView;

public class MyTicketDetailAdapter extends BaseAdapter
{
    private Context context;
    
    private List<MyTicketDetailPath> mList;
    
    private String selfOrServer;
    
    public MyTicketDetailAdapter( Context context,List<MyTicketDetailPath> list)
    {
        this.context = context;
        this.mList = list;
    }
    
    @Override
    public int getCount()
    {
        return mList == null ? 0: mList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return mList == null ? null: mList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    public void setSelfName(String selfOrServer)
    {
        this.selfOrServer = selfOrServer;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyTicketDetailPath entity = mList.get(position);
        HolderView mHolder;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.property_my_ticket_detail_listview_item, null);
            mHolder.serverImg = (ImageView)convertView.findViewById(R.id.server_img);
            mHolder.serverName = (TextView)convertView.findViewById(R.id.server_name);
            mHolder.serverTime = (TextView)convertView.findViewById(R.id.server_time);
            mHolder.serverDesc = (TextView)convertView.findViewById(R.id.server_desc);
            mHolder.serverContent = (TextView)convertView.findViewById(R.id.server_content);
            mHolder.serverStep = (Button)convertView.findViewById(R.id.step);
            mHolder.myGridView = (MyGridView)convertView.findViewById(R.id.photo_gridview);
            mHolder.selfOrServer = (TextView)convertView.findViewById(R.id.self_or_server);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        //mHolder.selfOrServer.setVisibility(View.GONE);
        
        ImageLoader.getInstance().displayImage(entity.getuImageUrl(),
            mHolder.serverImg,
            JRApplication.setRoundDisplayImageOptions(context,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getuName()))
        {
            mHolder.serverName.setText(entity.getuName());
        }
        else
        {
            mHolder.serverName.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getDesc()))
        {
            mHolder.serverDesc.setText(entity.getDesc());
        }
        else
        {
            mHolder.serverDesc.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getContent()))
        {
            mHolder.serverContent.setVisibility(View.VISIBLE);
            mHolder.serverContent.setText("描述："+entity.getContent());
        }
        else
        {
            mHolder.serverContent.setVisibility(View.GONE);
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTime()))
        {
            mHolder.serverTime.setText(entity.getTime());
        }
        else
        {
            mHolder.serverTime.setText("");
        }
        int step = mList.size()-position;
        mHolder.serverStep.setText("STEP"+step);
        
        mHolder.selfOrServer.setText(entity.getDepName());
//        if(selfOrServer != null && entity.getuName().equals(selfOrServer))
//        {
//            mHolder.selfOrServer.setVisibility(View.INVISIBLE);
//        }
//        else
//        {
//            mHolder.selfOrServer.setVisibility(View.VISIBLE);
//            mHolder.selfOrServer.setText("客服");
//        }
        
        List<Image> imgList = entity.getImageList();
        if(imgList != null && imgList.size() > 0)
        {
            mHolder.myGridView.setVisibility(View.VISIBLE);
            mHolder.myGridView.setAdapter(new PhotoAdapter(context, imgList, 85));
        }
        else
        {
            mHolder.myGridView.setVisibility(View.GONE);
        }
        
        return convertView;
    }
    
    class HolderView
    {
        ImageView serverImg;
        TextView serverName;
        TextView serverTime;
        TextView serverDesc;
        TextView serverContent;
        Button serverStep;
        MyGridView myGridView;
        TextView selfOrServer;
    }
}
