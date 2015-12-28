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
import com.ymdq.thy.bean.propertyservice.ServiceItemDoc;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.home.MyCentralCardActivity;
import com.ymdq.thy.ui.home.MyCentralDelivertyActivity;
import com.ymdq.thy.ui.home.MyCentralListsActivity;
import com.ymdq.thy.ui.propertyservice.PraiseListActivity;
import com.ymdq.thy.ui.propertyservice.RepairActivity;
import com.ymdq.thy.util.GeneralUtils;

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
        
        if (GeneralUtils.isNullOrZeroLenght(entity.getsId()))
        {
        }
        else
        {
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
                    //报修
                    if (Constants.PROPERTY_JOB_REPAIR.equals(entity.getsId()))
                    {
                        Intent repairIntent = new Intent(context, RepairActivity.class);
                        repairIntent.putExtra("title_name", "报修");
                        repairIntent.putExtra("type", Constants.PROPERTY_REPAIR);
                        context.startActivity(repairIntent);
                    }
                    //投诉
                    else if (Constants.PROPERTY_JOB_COMPLAIN.equals(entity.getsId()))
                    {
                        Intent complaintIntent = new Intent(context, RepairActivity.class);
                        complaintIntent.putExtra("title_name", "投诉");
                        complaintIntent.putExtra("type", Constants.PROPERTY_COMPLAINT);
                        context.startActivity(complaintIntent);
                    }
                    //求助
                    else if (Constants.PROPERTY_JOB_HELP.equals(entity.getsId()))
                    {
                        Intent complaintIntent = new Intent(context, RepairActivity.class);
                        complaintIntent.putExtra("title_name", "求助");
                        complaintIntent.putExtra("type", Constants.PROPERTY_HELP);
                        context.startActivity(complaintIntent);
                    }
                    //建议
                    else if (Constants.PROPERTY_JOB_ADVISE.equals(entity.getsId()))
                    {
                        Intent suggestIntent = new Intent(context, RepairActivity.class);
                        suggestIntent.putExtra("title_name", "建议");
                        suggestIntent.putExtra("type", Constants.PROPERTY_SUGGEST);
                        context.startActivity(suggestIntent);
                    }
                    //表扬
                    else if (Constants.PROPERTY_STAFF_PRAISE.equals(entity.getsId()))
                    {
                        Intent praiseIntent = new Intent(context, PraiseListActivity.class);
                        context.startActivity(praiseIntent);
                    }
                    //快递
                    else if (Constants.PROPERTY_SERVICE_PARCEL.equals(entity.getsId()))
                    {
                        Intent delivertyIntent = new Intent(context, MyCentralDelivertyActivity.class);
                        context.startActivity(delivertyIntent);
                    }
                    //生活缴费
                    else if (Constants.PROPERTY_SERVICE_PAYMENT.equals(entity.getsId()))
                    {
                        Intent paymentIntent = new Intent(context, MyCentralCardActivity.class);
                        context.startActivity(paymentIntent);
                    }
                    //便民信息
                    else if (Constants.PROPERTY_SERVICE_CONVENIENT.equals(entity.getsId()))
                    {
                        Intent facilitateIntent = new Intent(context, MyCentralListsActivity.class);
                        context.startActivity(facilitateIntent);
                    }
                }
            });
        }
        
        return convertView;
    }
    
    class HolderView
    {
        ImageView img;
        
        TextView name;
    }
}
