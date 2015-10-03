/*
 * 文 件 名:  PersonCenterHouseAdapter.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-25
 
 */
package com.ymdq.thy.ui.personcenter;

import java.util.ArrayList;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseInfoDoc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <账号绑定房屋列表adapter>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PersonCenterHouseAdapter extends BaseAdapter
{
    /**
     * 房屋列表
     */
    private ArrayList<HouseInfoDoc> infoDocs;
    
    private Context context;
    
    private PersonCenterFragment centerFragment;
    
    private boolean showDelete;
    
    public void setInfoDocs(ArrayList<HouseInfoDoc> infoDocs)
    {
        if (infoDocs != null)
        {
            this.infoDocs = infoDocs;
        }
        else
        {
            this.infoDocs = new ArrayList<HouseInfoDoc>();
        }
        
    }
    
    public PersonCenterHouseAdapter(Context context, ArrayList<HouseInfoDoc> infoDocs,
        PersonCenterFragment centerFragment, boolean showDelete)
    {
        this.setInfoDocs(infoDocs);
        this.context = context;
        this.centerFragment = centerFragment;
    }
    
    public void updata(ArrayList<HouseInfoDoc> infoDocs, boolean showDelete)
    {
        this.setInfoDocs(infoDocs);
        this.showDelete = showDelete;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.person_center_house_item, null);
            holder.tvAddress = (TextView)convertView.findViewById(R.id.house_list_address_tv);
            holder.ivDelete = (ImageView)convertView.findViewById(R.id.house_list_delete_iv);
            holder.llItem = (LinearLayout)convertView.findViewById(R.id.house_list_item_ll);
            holder.tvState = (TextView)convertView.findViewById(R.id.house_list_state_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        final HouseInfoDoc info = infoDocs.get(position);
        holder.tvAddress.setText(info.getcName() + info.getbName() + info.getdName() + info.gethName());
        
        if (showDelete)
        {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.tvState.setVisibility(View.GONE);
        }
        else
        {
            holder.ivDelete.setVisibility(View.GONE);
            holder.tvState.setVisibility(View.VISIBLE);
        }
        
        if ("0".equals(info.getFlag()))
        {
            holder.tvState.setText("");
            holder.tvState.setTextColor(context.getResources().getColor(R.color.house_user_info_normal));
        }
        else
        {
            holder.tvState.setText("冻结");
            holder.tvState.setTextColor(context.getResources().getColor(R.color.person_center_no_house));
        }
        
        final int pos = position;
        holder.ivDelete.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                centerFragment.unBindingHouse(pos);
            }
        });
        
        holder.llItem.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if ("0".equals(info.getFlag()))
                {
                    Intent intent = new Intent(context, HouseUserInfoActivity.class);
                    intent.putExtra("house_user_info", info);
                    context.startActivity(intent);
                }
            }
        });
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private TextView tvAddress;
        
        private TextView tvState;
        
        private ImageView ivDelete;
        
        private LinearLayout llItem;
    }
    
}
