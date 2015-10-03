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
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseBList;
import com.ymdq.thy.util.DisplayUtil;

/**
 * 小区地址选择adapter
 * <功能详细描述>
 * 
 * @author  user
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseUnitAdapter extends BaseExpandableListAdapter
{
    private ArrayList<HouseBList> bList;
    
    private Context context;
    
    public void setbList(ArrayList<HouseBList> bList)
    {
        if (bList != null)
        {
            this.bList = bList;
        }
        else
        {
            this.bList = new ArrayList<HouseBList>();
        }
        
    }
    
    public HouseUnitAdapter(Context context, ArrayList<HouseBList> bList)
    {
        this.setbList(bList);
        this.context = context;
    }
    
    @Override
    public int getGroupCount()
    {
        return bList == null ? 0 : bList.size();
    }
    
    @Override
    public int getChildrenCount(int groupPosition)
    {
        return bList == null ? 0 : bList.get(groupPosition).getdList().size();
    }
    
    @Override
    public Object getGroup(int groupPosition)
    {
        return bList == null ? null : bList.get(groupPosition);
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return bList == null ? null : bList.get(groupPosition).getdList().get(childPosition);
    }
    
    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }
    
    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }
    
    @Override
    public boolean isEmpty()
    {
        return false;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        View v;
        if(convertView == null)
        {
            v = LayoutInflater.from(context).inflate(R.layout.house_choise_head, null);
            v.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context, 36)));
        }
        else
        {
            v = convertView;
        }
        TextView unitName = (TextView)v.findViewById(R.id.com_name);
        unitName.setText(bList.get(groupPosition).getdName());
        return v;
    }
    
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
        ViewGroup parent)
    {
        ChildViewHolder childHolder;
        if(convertView == null)
        {
            childHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.house_choise_building_item, null);
            childHolder.numName = (TextView)convertView.findViewById(R.id.house_building_name);
            childHolder.img = (ImageView)convertView.findViewById(R.id.house_building_img);
            childHolder.line = convertView.findViewById(R.id.house_building_line);
            convertView.setTag(childHolder);
        }
        else
        {
            childHolder = (ChildViewHolder)convertView.getTag();
        }
        childHolder.numName.setText(bList.get(groupPosition).getdList().get(childPosition).gethName());
        childHolder.img.setVisibility(View.GONE);
        if(groupPosition != (bList.size()-1) && childPosition == (bList.get(groupPosition).getdList().size()-1))
        {
            childHolder.line.setVisibility(View.GONE);
        }
        else
        {
            childHolder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
    
    public boolean hasStableIds() {
        return true;
    }
    
    class ChildViewHolder
    {
        TextView numName;
        ImageView img;
        View line;
    }
}
