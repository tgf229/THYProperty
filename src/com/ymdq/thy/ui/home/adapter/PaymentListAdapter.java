package com.ymdq.thy.ui.home.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.PaymentListDoc;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.util.GeneralUtils;

public class PaymentListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<PaymentListDoc> mList;
    
    public PaymentListAdapter(Context context,List<PaymentListDoc> list)
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
        PaymentListDoc entity = mList.get(position);
        HolderView mHolder;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.payment_list_fragment_item, null);
            mHolder.imgType = (ImageView)convertView.findViewById(R.id.img_type);
            mHolder.hName = (TextView)convertView.findViewById(R.id.home_name);
            mHolder.money = (TextView)convertView.findViewById(R.id.money);
            mHolder.time = (TextView)convertView.findViewById(R.id.time);
            mHolder.line_item = (ImageView)convertView.findViewById(R.id.line_item);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getType()))
        {
            //物业
            if("1".equals(entity.getType()))
            {
                mHolder.imgType.setImageResource(R.drawable.mybill_jiaofei_icon_wuye);
            }
            else if("2".equals(entity.getType()))
            {
                mHolder.imgType.setImageResource(R.drawable.mybill_jiaofei_icon_chewei);
            }
            else if("3".equals(entity.getType()))
            {
                mHolder.imgType.setImageResource(R.drawable.mybill_jiaofei_icon_fentan);
            }
            else
            {
                mHolder.imgType.setImageResource(R.drawable.community_default);
            }
        }
        else
        {
            mHolder.imgType.setImageResource(R.drawable.community_default);
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.gethName()))
        {
            mHolder.hName.setText(entity.gethName());
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getMoney()))
        {
            mHolder.money.setText(entity.getMoney());
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTime()))
        {
            mHolder.time.setText(GeneralUtils.splitToPay(entity.getTime()));
        }
        else
        {
            mHolder.time.setVisibility(View.INVISIBLE);
        }
        if(position == mList.size() - 1)
        {
            mHolder.line_item.setVisibility(View.GONE);
        }
        else
        {
            mHolder.line_item.setVisibility(View.VISIBLE);
        }
        
        return convertView;
    }
    
    class HolderView
    {
        ImageView imgType;
        
        TextView hName;
        
        TextView money;
        
        TextView time;
        
        ImageView line_item;
    }
    
}
