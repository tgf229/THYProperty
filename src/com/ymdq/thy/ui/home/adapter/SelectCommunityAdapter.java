package com.ymdq.thy.ui.home.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.QueryCommunityListResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.ui.home.MainFragment;
import com.ymdq.thy.ui.home.SelectCommunityActivity;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <选择小区>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2015年3月26日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SelectCommunityAdapter extends BaseAdapter
{
    private Context context;
    
    private ArrayList<QueryCommunityListResponse> mList;
    
    private boolean add_house;
    
    public SelectCommunityAdapter(Context context,ArrayList<QueryCommunityListResponse> mList,boolean add_house)
    {
        this.context = context;
        this.mList = mList;
        this.add_house = add_house;
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public Object getItem(int position)
    {
        return mList == null ? null :mList.get(position);
    }
    
    @Override
    public int getCount()
    {
        return mList == null ? 0 :mList.size();
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final QueryCommunityListResponse entity = mList.get(position);
        ViewHolder mHolder;
        if(convertView == null)
        {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.select_community_listview_item, null);
            mHolder.layout = (RelativeLayout)convertView.findViewById(R.id.select_community_layout);
            mHolder.city = (TextView)convertView.findViewById(R.id.city);
            mHolder.cName = (TextView)convertView.findViewById(R.id.c_name);
            mHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (ViewHolder)convertView.getTag();
        }
        if(position == mList.size()-1)
        {
            mHolder.line.setVisibility(View.GONE);
        }
        else
        {
            mHolder.line.setVisibility(View.VISIBLE);
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getCity()))
        {
            mHolder.city.setText(entity.getCity());
        }
        else
        {
            mHolder.city.setText("");
        }
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getcName()))
        {
            mHolder.cName.setText(entity.getcName());
        }
        else
        {
            mHolder.cName.setText("");
        }
        mHolder.layout.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
//                if(Global.isLogin())
//                {
//                    Global.saveUCId(entity.getcId());
//                }
               
                Intent i = new Intent(context,MainFragment.class);
                i.putExtra("c_city", entity.getCity());//小区所属的城市
                i.putExtra("c_name", entity.getcName());//小区名称
                //个人中心添加房屋
//                if(add_house)
//                {
                    i.putExtra("select_cid", entity.getcId());
//                }
//                else
//                {
//                    Global.saveUCId(entity.getcId());
//                }
                ((Activity)context).setResult(Constants.Select_community_cid, i);
                ((Activity)context).finish();
            }
        });
        return convertView;
    }
    
    class ViewHolder
    {
        RelativeLayout layout;
        TextView city;
        TextView cName;
        View line;
    }
}
