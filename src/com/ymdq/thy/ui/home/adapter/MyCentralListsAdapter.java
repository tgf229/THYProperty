package com.ymdq.thy.ui.home.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.MyCentralListsDoc;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.ToastUtil;

public class MyCentralListsAdapter extends BaseAdapter
{
    private Context context;
    
    private List<MyCentralListsDoc> mList ;
    
    public MyCentralListsAdapter(Context context,List<MyCentralListsDoc> list)
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
        final MyCentralListsDoc entity = mList.get(position);
        HolderView mHolder = null;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_my_central_lists_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.telNumber = (TextView)convertView.findViewById(R.id.tel);
            mHolder.address = (TextView)convertView.findViewById(R.id.address);
            mHolder.telLayout = (LinearLayout)convertView.findViewById(R.id.call_layout);
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getName()))
        {
            mHolder.name.setText(entity.getName());
        }
        else
        {
            mHolder.name.setText("");
        }
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTel()))
        {
            mHolder.telNumber.setText(entity.getTel());
            mHolder.telLayout.setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    DialogUtil.showTwoButtonDialog(context,"您是否拨打号码："+entity.getTel(),
                        new DialogCallBack(){

                        @Override
                        public void dialogBack()
                        {
                            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+entity.getTel()));
                            context.startActivity(intent);
                        }});
                }
            });
        }
        else
        {
            ToastUtil.makeText(context, "暂无号码信息");
        }
        
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getAddress()))
        {
            mHolder.address.setText(entity.getAddress());
        }
        else
        {
            mHolder.address.setText("");
        }
        
        
        return convertView;
    }
    
    class HolderView
    {
        TextView name;
        TextView telNumber;
        TextView address;
        LinearLayout telLayout;
    }
    
}
