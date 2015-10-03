/*
 * 文 件 名:  HouseUserInfoAdapter.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-27
 
 */
package com.ymdq.thy.ui.personcenter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseUserInfoDoc;
import com.ymdq.thy.constant.Global;

/**
 * <房屋绑定账号信息adapter>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-27]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseUserInfoAdapter extends BaseAdapter
{
    private ArrayList<HouseUserInfoDoc> infoDocs;
    
    private Context context;
    
    private ImageLoader imageLoader = ImageLoader.getInstance();
    
    private boolean ifEdit;
    
    private HouseUserInfoActivity activity;
    
    public void setInfoDocs(ArrayList<HouseUserInfoDoc> infoDocs)
    {
        if (infoDocs != null)
        {
            this.infoDocs = infoDocs;
        }
        else
        {
            this.infoDocs = new ArrayList<HouseUserInfoDoc>();
        }
    }
    
    public HouseUserInfoAdapter(ArrayList<HouseUserInfoDoc> infoDocs, Context context, boolean ifEdit,
        HouseUserInfoActivity activity)
    {
        this.setInfoDocs(infoDocs);
        this.context = context;
        this.ifEdit = ifEdit;
        this.activity = activity;
    }
    
    public void updata(ArrayList<HouseUserInfoDoc> infoDocs, boolean ifEdit)
    {
        this.setInfoDocs(infoDocs);
        this.ifEdit = ifEdit;
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount()
    {
        return infoDocs.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return infoDocs.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.house_user_info_item, null);
            holder.ivHeadPic = (ImageView)convertView.findViewById(R.id.house_user_info_item_head_pic_iv);
            holder.tvName = (TextView)convertView.findViewById(R.id.house_user_info_item_name_tv);
            holder.tvphone = (TextView)convertView.findViewById(R.id.house_user_info_item_pnone_tv);
            holder.tvState = (TextView)convertView.findViewById(R.id.house_user_info_item_state_tv);
            holder.tvIdentity = (TextView)convertView.findViewById(R.id.house_user_info_item_identity_tv);
            holder.tvEdit = (TextView)convertView.findViewById(R.id.house_user_info_item_edit_tv);
            holder.vLine = convertView.findViewById(R.id.house_user_info_item_line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        final HouseUserInfoDoc infoDoc = infoDocs.get(position);
        imageLoader.displayImage(infoDoc.getImage(),
            holder.ivHeadPic,
            JRApplication.setRoundDisplayImageOptions(context,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        
        holder.tvName.setText(infoDoc.getNickName());
        holder.tvphone.setText(infoDoc.getPhone());        
        
        if ("1".equals(infoDoc.getLevel()))
        {
            holder.tvIdentity.setText("业主");
            holder.tvIdentity.setBackgroundResource(R.drawable.house_user_info_red_icon);
            holder.tvState.setText("正常");
            holder.tvState.setTextColor(context.getResources().getColor(R.color.house_user_info_normal));
        }
        else
        {
            holder.tvIdentity.setText("住户");
            holder.tvIdentity.setBackgroundResource(R.drawable.house_user_info_blue_icon);
            
            if (ifEdit)
            {
                if (Global.getUserId().equals(infoDoc.getuId()))
                {
                    holder.tvEdit.setVisibility(View.GONE);
                    holder.tvState.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.tvEdit.setVisibility(View.VISIBLE);
                    holder.tvState.setVisibility(View.INVISIBLE);
                    
                    if ("0".equals(infoDoc.getStatus()))
                    {
                        holder.tvEdit.setText("恢复");
                        holder.tvEdit.setBackgroundResource(R.drawable.house_user_info_blue_bg_selector);
                        holder.tvIdentity.setBackgroundResource(R.drawable.house_user_info_grey_icon);
                    }
                    else
                    {
                        holder.tvEdit.setText("冻结");
                        holder.tvEdit.setBackgroundResource(R.drawable.house_user_info_red_bg_selector);
                        holder.tvIdentity.setBackgroundResource(R.drawable.house_user_info_blue_icon);
                    }
                    
                    holder.tvEdit.setOnClickListener(new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                            if ("0".equals(infoDoc.getStatus()))
                            {
                                activity.editHouse("1", infoDoc.getuId());
                            }
                            else
                            {
                                activity.editHouse("0", infoDoc.getuId());
                            }
                            
                        }
                    });
                }
                
                
            }
            else
            {
                holder.tvEdit.setVisibility(View.INVISIBLE);
                holder.tvState.setVisibility(View.VISIBLE);
                
                if ("0".equals(infoDoc.getStatus()))
                {
                    holder.tvState.setText("冻结");
                    holder.tvState.setTextColor(context.getResources().getColor(R.color.person_center_no_house));
                    holder.tvIdentity.setBackgroundResource(R.drawable.house_user_info_grey_icon);
                }
                else
                {
                    holder.tvState.setText("正常");
                    holder.tvState.setTextColor(context.getResources().getColor(R.color.house_user_info_normal));
                    holder.tvIdentity.setBackgroundResource(R.drawable.house_user_info_blue_icon);
                }
            }
            
        }
        
        if (position == infoDocs.size() - 1)
        {
            holder.vLine.setVisibility(View.GONE);
        }
        else
        {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private ImageView ivHeadPic;//头像
        
        private TextView tvName;//昵称
        
        private TextView tvphone;//电话
        
        private TextView tvState;//状态
        
        private TextView tvIdentity;//身份   
        
        private TextView tvEdit;//管理
        
        private View vLine;
        
    }
}
