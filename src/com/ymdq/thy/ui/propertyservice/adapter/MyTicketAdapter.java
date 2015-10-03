package com.ymdq.thy.ui.propertyservice.adapter;

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
import com.ymdq.thy.bean.propertyservice.MyTicketDoc;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.util.GeneralUtils;

public class MyTicketAdapter extends BaseAdapter
{
    private Context context;
    
    private List<MyTicketDoc> mList;
    
    public MyTicketAdapter(Context context,List<MyTicketDoc> list)
    {
        this.context = context;
        this.mList = list;
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
        MyTicketDoc entity = mList.get(position);
        HolderView mHolder;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.property_my_ticket_item, null);
            mHolder.typeImg = (ImageView)convertView.findViewById(R.id.type_img);
            mHolder.number = (TextView)convertView.findViewById(R.id.number);
            mHolder.statusBtn = (Button)convertView.findViewById(R.id.status_btn);
            mHolder.content = (TextView)convertView.findViewById(R.id.content);
            mHolder.time = (TextView)convertView.findViewById(R.id.time);
            mHolder.serverImg = (ImageView)convertView.findViewById(R.id.server_img);
            mHolder.serverName = (TextView)convertView.findViewById(R.id.server_name);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getType()))
        {
            //报修
            if(Constants.PROPERTY_REPAIR.equals(entity.getType()))
            {
                mHolder.typeImg.setImageResource(R.drawable.service_icon_round_repair);
            }
            //投诉
            else if(Constants.PROPERTY_COMPLAINT.equals(entity.getType()))
            {
                mHolder.typeImg.setImageResource(R.drawable.service_icon_round_complaint);
            }
            //表扬
//            else if(Constants.PROPERTY_PRAISE.equals(entity.getType()))
//            {
//                mHolder.typeImg.setImageResource(R.drawable.service_icon_round_thank);
//            }
            //求助
            else if(Constants.PROPERTY_HELP.equals(entity.getType()))
            {
                mHolder.typeImg.setImageResource(R.drawable.service_icon_round_help);
            }
            //建议
            else if(Constants.PROPERTY_SUGGEST.equals(entity.getType()))
            {
                mHolder.typeImg.setImageResource(R.drawable.service_icon_round_suggest);
            }
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getId()))
        {
            mHolder.number.setText(entity.getId());
        }
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getStatus()))
        {
            if(Constants.NO_TAKE_DELIVERTY.equals(entity.getStatus()))
            {
                mHolder.statusBtn.setBackgroundResource(R.drawable.service_icon_untreated);
                mHolder.statusBtn.setText("待处理");
            }
            else if(Constants.HAS_TAKE_DELIVERTY.equals(entity.getStatus()))
            {
                mHolder.statusBtn.setBackgroundResource(R.drawable.service_icon_processing);
                mHolder.statusBtn.setText("处理中");
            }
            else if(Constants.HAVE_TO_PAY.equals(entity.getStatus()))
            {
                mHolder.statusBtn.setBackgroundResource(R.drawable.service_icon_yichuli);
                mHolder.statusBtn.setText("已处理");
            }
            else if(Constants.OVER.equals(entity.getStatus()))
            {
                mHolder.statusBtn.setBackgroundResource(R.drawable.service_icon_complete);
                mHolder.statusBtn.setText("已完成");
            }
        }
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getContent()))
        {
            mHolder.content.setText("工单描述："+entity.getContent());
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTime()))
        {
            mHolder.time.setText(GeneralUtils.splitTodate(entity.getTime()));
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getNickName()))
        {
            mHolder.serverName.setText(entity.getNickName());
        }
        ImageLoader.getInstance().displayImage(entity.getImage(),
            mHolder.serverImg,
            JRApplication.setRoundDisplayImageOptions(context,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        
        
        return convertView;
    }
    
    class HolderView
    {
        ImageView typeImg;
        //工单号
        TextView number;
        //处理的状态
        Button statusBtn;
        //工单的描述内容
        TextView content;
        //时间
        TextView time;
        //客服头像
        ImageView serverImg;
        //客服的名字
        TextView serverName;
        
    }
    
}
