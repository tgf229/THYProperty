package com.ymdq.thy.ui.home.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.MyCentralMeeageBean;
import com.ymdq.thy.util.GeneralUtils;

public class MyCentralMeeageAdapter extends BaseAdapter
{
    private Context context;
    
    private List<MyCentralMeeageBean> mList;
    
    public MyCentralMeeageAdapter(Context context,List<MyCentralMeeageBean> list)
    {
        this.context = context;
        this.mList = list;
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
        MyCentralMeeageBean entity = mList.get(position);
        HolderView mHolder;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_my_central_message_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img);
            mHolder.content = (TextView)convertView.findViewById(R.id.content);
            mHolder.time = (TextView)convertView.findViewById(R.id.time);
            mHolder.dividerLine = convertView.findViewById(R.id.divider_line);
            mHolder.lastView = convertView.findViewById(R.id.last_line);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        //消息类型
        if(entity.getType() != null)
        {
            if(entity.getType().equals("1"))
            {
                mHolder.img.setImageResource(R.drawable.message_icon_tongzhi);
                mHolder.name.setText("通告");
            }
            else if(entity.getType().equals("3"))
            {
                mHolder.img.setImageResource(R.drawable.message_icon_tongzhi);
                mHolder.name.setText("服务信息");
            }
            else if(entity.getType().equals("2"))
            {
                mHolder.img.setImageResource(R.drawable.message_icon_tuiguang);
                mHolder.name.setText("营销推广");
            }
            else if(entity.getType().equals("4"))
            {
                mHolder.img.setImageResource(R.drawable.message_icon_tuiguang);
                mHolder.name.setText("其他");
            }
            else if(entity.getType().equals("5"))
            {
                mHolder.img.setImageResource(R.drawable.message_icon_kuaidi);
                mHolder.name.setText("快递");
            } 
            else
            {
                mHolder.img.setImageResource(R.drawable.community_default);
                mHolder.name.setText("其他");
            }
        }
        else
        {
            mHolder.img.setImageResource(R.drawable.community_default);
            mHolder.name.setText("其他");
        }
        //未读
        if(entity.getIsRead().equals("0"))
        {
            mHolder.name.setTextColor(context.getResources().getColor(R.color.black_color));
            mHolder.name.setTypeface(Typeface.DEFAULT_BOLD);
            mHolder.time.setTextColor(context.getResources().getColor(R.color.text_color));
            mHolder.content.setTextColor(context.getResources().getColor(R.color.person_register_one_agree));
        }
        else
        {
            mHolder.name.setTextColor(context.getResources().getColor(R.color.text_color));
            mHolder.name.setTypeface(Typeface.DEFAULT);
            mHolder.time.setTextColor(context.getResources().getColor(R.color.person_input_hint));
            mHolder.content.setTextColor(context.getResources().getColor(R.color.text_color));
        }
//        if(GeneralUtils.isNotNullOrZeroLenght(entity.getName()))
//        {
//            mHolder.name.setText(entity.getName());
//        }
//        else
//        {
//            mHolder.name.setText("");
//        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getContent()))
        {
            mHolder.content.setText(entity.getContent());
        }
        else
        {
            mHolder.content.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTime()))
        {
            mHolder.time.setText(GeneralUtils.splitToSecond(entity.getTime()));
        }
        else
        {
            mHolder.time.setText("");
        }
        if(position == mList.size()-1)
        {
            mHolder.lastView.setVisibility(View.VISIBLE);
            mHolder.dividerLine.setVisibility(View.GONE);
        }
        else
        {
            mHolder.lastView.setVisibility(View.GONE);
            mHolder.dividerLine.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    
    class HolderView
    {
        ImageView img;
        TextView name;
        TextView content;
        TextView time;
        View dividerLine;
        View lastView;
    }
    
}
