package com.ymdq.thy.ui.home.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.AdvertiseDetailResponse.CommentList;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <通告详情>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AdvertiseDetailAdapter extends BaseAdapter
{
    private Context context;
    
    private List<CommentList> mList;
    
//    private DisplayImageOptions options;
    
    public AdvertiseDetailAdapter(Context context,List<CommentList> list)
    {
        this.context = context;
        this.mList = list;
//        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.home_banner)
//            .showImageForEmptyUri(R.drawable.home_banner)
//            .showImageOnFail(R.drawable.home_banner)
//            .cacheInMemory(false)
//            .cacheOnDisc(false)
//            .build();
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
        CommentList entity = mList.get(position);
        HolderView mHolder;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.community_topic_comment_item, null);
            mHolder.img = (ImageView)convertView.findViewById(R.id.head_image);
            mHolder.name = (TextView)convertView.findViewById(R.id.nick_name);
            mHolder.time = (TextView)convertView.findViewById(R.id.time);
            mHolder.content = (TextView)convertView.findViewById(R.id.content);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(entity.getImage(), mHolder.img, 
            JRApplication.setRoundDisplayImageOptions(context, "default_head_pic_round",
                "default_head_pic_round", "default_head_pic_round"));
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getName()))
        {
            mHolder.name.setText(entity.getName());
        }
        else
        {
            mHolder.name.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTime()))
        {
            mHolder.time.setText(GeneralUtils.splitToYear(entity.getTime()));
        }
        else
        {
            mHolder.time.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getDesc()))
        {
            mHolder.content.setText(entity.getDesc());
        }
        else
        {
            mHolder.content.setText("");
        }
        
        return convertView;
    }
    
    class HolderView
    {
        ImageView img;
        TextView name;
        TextView content;
        TextView time;
    }
    
}
