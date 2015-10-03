package com.ymdq.thy.ui.home.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.AccountManageDoc;
import com.ymdq.thy.bean.home.HomeList;
import com.ymdq.thy.ui.home.AccountManagePaymentActivity;
import com.ymdq.thy.util.GeneralUtils;

public class AccountManageAdapter extends BaseAdapter
{
    private Context context;
    
    private List<AccountManageDoc> mList;
    
    private OnClickListener listener;
    
    private List<String> typeList = new ArrayList<String>();
    
    public AccountManageAdapter(Context context,List<AccountManageDoc> list,OnClickListener listener)
    {
        this.context = context;
        this.mList = list;
        this.listener = listener;
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
        final AccountManageDoc entity = mList.get(position);
        HolderView mHolder;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_account_manage_fragment_item, null);
            mHolder.hname = (TextView)convertView.findViewById(R.id.hname);
            mHolder.moreLayout = (LinearLayout)convertView.findViewById(R.id.more_layout);
            //            mHolder.lastLayout = (LinearLayout)convertView.findViewById(R.id.last_layout);
            //            mHolder.lastName = (TextView)convertView.findViewById(R.id.last_name);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.gethName()))
        {
            mHolder.hname.setText(entity.gethName());
        }
        
        mHolder.moreLayout.removeAllViews();
        typeList.clear();
        List<HomeList> homeList = entity.gethList();
        
        for(int i=0;i<homeList.size();i++)
        {
            final HomeList home = homeList.get(i);
            if(!typeList.contains(home.getType()))
            {
                typeList.add(home.getType());
                View view = LayoutInflater.from(context).inflate(R.layout.home_account_manage_fragment_item2, null);
                LinearLayout clickLayout = (LinearLayout)view.findViewById(R.id.click_layout);
                TextView moreName = (TextView)view.findViewById(R.id.more_name);
                moreName.setText(home.getTypeName());
                if(i == homeList.size())
                {
                    view.findViewById(R.id.line_item).setVisibility(View.GONE);;
                }
                mHolder.moreLayout.addView(view);
                clickLayout.setTag(home);
                clickLayout.setOnClickListener(new OnClickListener()
                {
                    
                    @Override
                    public void onClick(View v)
                    {
                        HomeList home = (HomeList)v.getTag();
                        Intent deltyIntent = new Intent(context,AccountManagePaymentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("doc", entity);
                        bundle.putString("type", home.getType());
                        bundle.putString("typeName", home.getTypeName());
                        deltyIntent.putExtras(bundle);
                        context.startActivity(deltyIntent);
                    }
                });
            }
        }
        
        return convertView;
    }
    
    class HolderView
    {
        TextView hname;
        LinearLayout moreLayout;
        
        //        LinearLayout lastLayout;
        //        TextView lastName;
    }
    
}
