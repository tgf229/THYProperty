package com.ymdq.thy.ui.community.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Member;
import com.ymdq.thy.constant.Constants;

/**
 * 
 * <社区成员适配器>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityMemberAdapter extends BaseAdapter
{
    private List<Member> data;
    
    private Context context;
    
    public CommunityMemberAdapter(List<Member> data, Context context)
    {
        super();
        this.data = data;
        this.context = context;
    }
    
    @Override
    public int getCount()
    {
        return data == null ? 0 : data.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return null;
    }
    
    @Override
    public long getItemId(int position)
    {
        return 0;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.community_member_item, null);
            viewHolder.headImage = (ImageView)convertView.findViewById(R.id.head_image);
            viewHolder.vip = (ImageView)convertView.findViewById(R.id.icon_vip);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.lineAll = (ImageView)convertView.findViewById(R.id.line_all);
            viewHolder.lineToRight = (ImageView)convertView.findViewById(R.id.line_to_right);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(data.get(position).getImage(),
            viewHolder.headImage,
            JRApplication.setAllDisplayImageOptions(context,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        if (Constants.LEVEL_VIP.equals(data.get(position).getUserLevel()))
        {
            viewHolder.vip.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.vip.setVisibility(View.GONE);
        }
        viewHolder.name.setText(data.get(position).getName());
        if (data.size() - 1 == position)
        {
            viewHolder.lineToRight.setVisibility(View.GONE);
            viewHolder.lineAll.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.lineToRight.setVisibility(View.VISIBLE);
            viewHolder.lineAll.setVisibility(View.GONE);
        }
        return convertView;
    }
    
    class ViewHolder
    {
        private ImageView headImage;
        
        private ImageView vip;
        
        private TextView name;
        
        private ImageView lineToRight;
        
        private ImageView lineAll;
    }
    
}
