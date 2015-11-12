package com.ymdq.thy.ui.propertyservice.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
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
import com.ymdq.thy.bean.propertyservice.PraiseListDoc;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.propertyservice.PraiseDetailActivity;
import com.ymdq.thy.util.GeneralUtils;

public class PraiseListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<PraiseListDoc> mList;
    
    private UICallBack callBack;
    
    public PraiseListAdapter(Context context, List<PraiseListDoc> freshNewsList, UICallBack callBack)
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
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final PraiseListDoc entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.property_praise_list_item, null);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.depName = (TextView)convertView.findViewById(R.id.dep_name);
            mHolder.num = (TextView)convertView.findViewById(R.id.num);
            mHolder.praise = (TextView)convertView.findViewById(R.id.praise);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        ImageLoader.getInstance().displayImage(entity.geteImageUrl(),
            mHolder.img,
            JRApplication.setAllDisplayImageOptions(context,
                "community_default",
                "community_default",
                "community_default"));   
        mHolder.name.setText(entity.geteName());
        mHolder.depName.setText(entity.geteDepName());
        mHolder.num.setText(entity.geteNum());
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getPraise()))
        {
            mHolder.praise.setText(entity.getPraise()+"人");
        }
        else
        {
            mHolder.praise.setText("0人");
        }
        convertView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context,PraiseDetailActivity.class);
                intent.putExtra("detail", entity);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        ImageView img;
        
        TextView name;
        
        TextView depName;
        
        TextView num;
        
        TextView praise;
        
    }
}
