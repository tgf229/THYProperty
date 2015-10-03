package com.ymdq.thy.ui.community.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Group;
import com.ymdq.thy.ui.community.adapter.CommunityCircleAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TranslateTopicAdapter extends BaseAdapter
{
    private Context context;
    
    private ArrayList<Group> mList;
    
    private int checked_position = -1;
    
    public TranslateTopicAdapter(Context context,ArrayList<Group> mList)
    {
        this.context = context;
        this.mList = mList;
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
    
    public void setPositon(int position)
    {
        checked_position = position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Group entity = mList.get(position);
        ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.translate_topic_item, null);
            viewHolder.checkedImg = (CheckBox)convertView.findViewById(R.id.check_box);
            viewHolder.headImage = (ImageView)convertView.findViewById(R.id.head_image);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.userCount = (TextView)convertView.findViewById(R.id.user_count);
            viewHolder.articleCount = (TextView)convertView.findViewById(R.id.article_count);
            viewHolder.desc = (TextView)convertView.findViewById(R.id.desc);
            viewHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        
        ImageLoader.getInstance().displayImage(entity.getIcon(),
            viewHolder.headImage,
            JRApplication.setAllDisplayImageOptions(context,
                "community_default",
                "community_default",
                "community_default"));
        viewHolder.name.setText(entity.getName());
        viewHolder.userCount.setText(entity.getUserCount());
        viewHolder.articleCount.setText(entity.getArticleCount());
        viewHolder.desc.setText(entity.getDesc());
        
        if(checked_position == position)
        {
            viewHolder.checkedImg.setChecked(true);
        }
        else
        {
            viewHolder.checkedImg.setChecked(false);
        }
        
        if(position == mList.size()-1)
        {
            viewHolder.line.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.line.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    
    
    
    class ViewHolder
    {
        private CheckBox checkedImg;
        
        private ImageView headImage;
        
        private TextView name;
        
        private TextView userCount;
        
        private TextView articleCount;
        
        private TextView desc;
        
        private View line;
        
    }
}

