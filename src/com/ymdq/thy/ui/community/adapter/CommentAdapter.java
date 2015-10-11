package com.ymdq.thy.ui.community.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.TopicDetailInfo;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.community.CommunityPersonDetailActivity;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <评论适配器>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-7-8]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommentAdapter extends BaseAdapter
{
    
    private List<TopicDetailInfo> data;
    
    private Context context;
    
    public CommentAdapter(List<TopicDetailInfo> data, Context context)
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
    public Object getItem(int arg0)
    {
        return null;
    }
    
    @Override
    public long getItemId(int arg0)
    {
        return 0;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.community_topic_comment_item, null);
            viewHolder.headImage = (ImageView)convertView.findViewById(R.id.head_image);
            viewHolder.vip = (ImageView)convertView.findViewById(R.id.icon_vip);
            viewHolder.nickName = (TextView)convertView.findViewById(R.id.nick_name);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
//            viewHolder.lineAll = (ImageView)convertView.findViewById(R.id.line_all);
            viewHolder.lineToRight = (ImageView)convertView.findViewById(R.id.line_to_right);
            viewHolder.content = (TextView)convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if (GeneralUtils.isNotNullOrZeroLenght(data.get(position).getImageUrl()))
        {
            if (data.get(position).getImageUrl().length() > 4
                && !"http".equals(data.get(position).getImageUrl().substring(0, 4)))
            {
                ImageLoader.getInstance().displayImage("file://" + data.get(position).getImageUrl(),
                    viewHolder.headImage,
                    JRApplication.setRoundDisplayImageOptions(context,
                        "default_head_pic_round",
                        "default_head_pic_round",
                        "default_head_pic_round"));
            }
            else
            {
                ImageLoader.getInstance().displayImage(data.get(position).getImageUrl(),
                    viewHolder.headImage,
                    JRApplication.setRoundDisplayImageOptions(context,
                        "default_head_pic_round",
                        "default_head_pic_round",
                        "default_head_pic_round"));
            }
        }
        else
        {
            viewHolder.headImage.setImageResource(R.drawable.default_head_pic_round);
        }
        if (Constants.LEVEL_VIP.equals(data.get(position).getUserLevel()))
        {
            viewHolder.vip.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.vip.setVisibility(View.GONE);
        }
        viewHolder.nickName.setText(data.get(position).getNickName());
        viewHolder.time.setText(GeneralUtils.splitMinuteToComment(data.get(position).getTime()));
        if (GeneralUtils.isNotNullOrZeroLenght(data.get(position).getReplyNickName()))
        {
            viewHolder.content.setText(Html.fromHtml("<font color=#666666>回复</font>" + "<font color=#4a5f8b>"
                + data.get(position).getReplyNickName() + "</font>" + "<font color=#666666>:</font>"
                + "<font color=#666666>" + data.get(position).getContent().replaceAll("\n", "<br>") + "</font>"));
        }
        else
        {
            viewHolder.content.setText(data.get(position).getContent());
        }
        if (data.size() - 1 == position)
        {
            viewHolder.lineToRight.setVisibility(View.GONE);
//            viewHolder.lineAll.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.lineToRight.setVisibility(View.VISIBLE);
//            viewHolder.lineAll.setVisibility(View.GONE);
        }
        viewHolder.headImage.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, CommunityPersonDetailActivity.class);
                intent.putExtra("queryUId", data.get(position).getUserId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    
    class ViewHolder
    {
        private ImageView headImage;
        
        private TextView nickName;
        
        private TextView time;
        
        private TextView content;
        
        private ImageView lineToRight;
        
//        private ImageView lineAll;
        
        private ImageView vip;
    }
}
