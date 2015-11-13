package com.ymdq.thy.ui.propertyservice.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.propertyservice.PraiseTagDoc;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.ui.propertyservice.PraiseVoteActivity;

public class PraiseTagAdapter extends BaseAdapter
{
    private PraiseVoteActivity context;
    
    private List<PraiseTagDoc> mList;
    
    private UICallBack callBack;
    
    public PraiseTagAdapter(PraiseVoteActivity context, List<PraiseTagDoc> freshNewsList, UICallBack callBack)
    {
        this.context = context;
        this.mList = freshNewsList;
        this.callBack = callBack;
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
    
    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final PraiseTagDoc entity = mList.get(position);
        final HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.property_praise_tag_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.tag_name);
            mHolder.isChoise = (TextView)convertView.findViewById(R.id.tag_name_ischoise);
            
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        mHolder.name.setText(entity.gettName());
        
        convertView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if("1".equals(mHolder.isChoise.getText()))
                {
                    mHolder.name.setBackground(context.getResources().getDrawable(R.drawable.property_praise_tag_btn));
                    mHolder.name.setTextColor(context.getResources().getColor(R.color.home_title));
                    context.tagIds.remove(entity.gettId());
                    mHolder.isChoise.setText("0");
                }
                else
                {
                    mHolder.name.setBackground(context.getResources().getDrawable(R.drawable.property_praise_tag_btn_press));
                    mHolder.name.setTextColor(context.getResources().getColor(R.color.white_color));
                    context.tagIds.put(entity.gettId(), entity.gettId());
                    mHolder.isChoise.setText("1");
                }
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        TextView name;
        TextView isChoise;
    }
}
