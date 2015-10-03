/*
 * 文 件 名:  HouseBuildingAdapter.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-20
 
 */
package com.ymdq.thy.ui.personcenter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseDList;

/**
 * 小区地址选择adapter
 * <功能详细描述>
 * 
 * @author  user
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseNumAdapter extends BaseAdapter
{
    private ArrayList<HouseDList> dList;
    
    private Context context;
    
    public void setdList(ArrayList<HouseDList> dList)
    {
        if (dList != null)
        {
            this.dList = dList;
        }
        else
        {
            this.dList = new ArrayList<HouseDList>();
        }
    }
    
    public HouseNumAdapter(Context context, ArrayList<HouseDList> dList)
    {
        this.setdList(dList);
        this.context = context;
    }
    
    public void updateData(ArrayList<HouseDList> dList)
    {
        this.setdList(dList);
        notifyDataSetChanged();
    }
    
    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return dList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return dList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.house_choise_building_item, null);
            holder.tvName = (TextView)convertView.findViewById(R.id.house_building_name);
            holder.vLine = convertView.findViewById(R.id.house_building_line);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        
        HouseDList houseDList = dList.get(position);
        holder.tvName.setText(houseDList.gethName());
        if (position < dList.size())
        {
            holder.vLine.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.vLine.setVisibility(View.INVISIBLE);
        }
        
        return convertView;
    }
    
    private class ViewHolder
    {
        private TextView tvName;
        
        private View vLine;
    }
    
}
