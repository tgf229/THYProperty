package com.ymdq.thy.ui.community.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Message;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.util.GeneralUtils;

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
public class CommunityCommentMessageAdapter extends BaseAdapter
{
    private List<Message> data;
    
    private Context context;
    
    public CommunityCommentMessageAdapter(List<Message> data, Context context)
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
            convertView = LayoutInflater.from(this.context).inflate(R.layout.community_comment_message_item, null);
            viewHolder.headImage = (ImageView)convertView.findViewById(R.id.head_image);
            viewHolder.vip = (ImageView)convertView.findViewById(R.id.icon_vip);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.comment = (TextView)convertView.findViewById(R.id.comment);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.contentImage = (ImageView)convertView.findViewById(R.id.content_image);
            viewHolder.content = (TextView)convertView.findViewById(R.id.content);
            viewHolder.lineAll = (ImageView)convertView.findViewById(R.id.line_all);
            viewHolder.lineToRight = (ImageView)convertView.findViewById(R.id.line_to_right);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(data.get(position).getReplyHeadUrl(),
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
        viewHolder.name.setText(data.get(position).getReplyNickName());
        
        if (GeneralUtils.isNotNullOrZeroLenght(data.get(position).getBeReplyNickName()))
        {
            if (Global.getUserId().equals(data.get(position).getBeReplyUId()))
            {
                viewHolder.comment.setText(Html.fromHtml("<font color=#000000>回复</font>"
                    + "<font color=#4a5f8b>我</font>" + "<font color=#666666>:</font>" + "<font color=#666666>"
                    + data.get(position).getReplyContent().replaceAll("\n", "<br>") + "</font>"));
            }
            else
            {
                viewHolder.comment.setText(Html.fromHtml("<font color=#000000>回复</font>" + "<font color=#4a5f8b>"
                    + data.get(position).getBeReplyNickName() + "</font>" + "<font color=#666666>:</font>"
                    + "<font color=#666666>" + data.get(position).getReplyContent().replaceAll("\n", "<br>") + "</font>"));
            }
        }
        else
        {
            viewHolder.comment.setText(data.get(position).getReplyContent());
        }
        viewHolder.time.setText(GeneralUtils.timeStamp(data.get(position).getTime()));
        if (GeneralUtils.isNotNullOrZeroLenght(data.get(position).getImageUrl()))
        {
            viewHolder.contentImage.setVisibility(View.VISIBLE);
            viewHolder.content.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(data.get(position).getImageUrl(),
                viewHolder.contentImage,
                JRApplication.setAllDisplayImageOptions(context,
                    "community_default",
                    "community_default",
                    "community_default"));
        }
        else
        {
            viewHolder.contentImage.setVisibility(View.GONE);
            viewHolder.content.setVisibility(View.VISIBLE);
            viewHolder.content.setText(data.get(position).getContent());
        }
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
        
        private TextView name;
        
        private TextView comment;
        
        private TextView time;
        
        private ImageView contentImage;
        
        private TextView content;
        
        private ImageView lineToRight;
        
        private ImageView lineAll;
        
        private ImageView vip;
    }
}
