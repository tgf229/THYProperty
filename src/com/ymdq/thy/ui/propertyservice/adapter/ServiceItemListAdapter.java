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
import com.ymdq.thy.bean.propertyservice.ServiceItemDoc;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.home.MainFragment;
import com.ymdq.thy.ui.propertyservice.PraiseDetailActivity;
import com.ymdq.thy.ui.propertyservice.PraiseListActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.ToastUtil;

public class ServiceItemListAdapter extends BaseAdapter
{
    private Context context;
    
    private List<ServiceItemDoc> mList;
    
    private UICallBack callBack;
    
    public ServiceItemListAdapter(Context context, List<ServiceItemDoc> list, UICallBack callBack)
    {
        this.context = context;
        this.mList = list;
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
        final ServiceItemDoc entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.property_service_list_item, null);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        ImageLoader.getInstance().displayImage(entity.getsImage(),
            mHolder.img,
            JRApplication.setAllDisplayImageOptions(context,
                "community_default",
                "community_default",
                "community_default"));
        mHolder.name.setText(entity.getsName());
        convertView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //                Intent intent = new Intent(context,PraiseDetailActivity.class);
                //                intent.putExtra("detail", entity);
                //                String time = GeneralUtils.isNullOrZeroLenght(context.choiseTime)? context.currentTime:context.choiseTime;
                //                intent.putExtra("time", time);
                //                intent.putExtra("myVoteTimes", context.myVoteTimes);
                //                context.startActivityForResult(intent, Constants.Praise_vote_success);
            }
        });
        return convertView;
    }
    
    class HolderView
    {
        ImageView img;
        
        TextView name;
    }
}
