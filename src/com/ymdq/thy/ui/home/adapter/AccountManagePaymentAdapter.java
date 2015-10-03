package com.ymdq.thy.ui.home.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.HomeList;
import com.ymdq.thy.bean.home.HomeList.MoneyAllList;
import com.ymdq.thy.util.GeneralUtils;

public class AccountManagePaymentAdapter extends BaseAdapter
{
    private Context context;
    
    private List<HomeList> mList;
    
    public AccountManagePaymentAdapter(Context context ,List<HomeList> list)
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
        HomeList entity = mList.get(position);
        HolderView mHolder;
        if(convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.home_account_manage_pay_item, null);
            mHolder.name = (TextView)convertView.findViewById(R.id.name);
            mHolder.totalMoney = (TextView)convertView.findViewById(R.id.total_money);
            mHolder.money = (TextView)convertView.findViewById(R.id.money);
            mHolder.moneyList = (LinearLayout)convertView.findViewById(R.id.money_list);
            mHolder.goToPay = (Button)convertView.findViewById(R.id.go_to_payment);
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
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getTotalMoney()))
        {
            String total = String.format(context.getResources().getString(R.string.home_total_money), entity.getTotalMoney());
            mHolder.totalMoney.setText(total);
        }
        mHolder.goToPay.setVisibility(View.GONE);
        if(GeneralUtils.isNotNullOrZeroLenght(entity.getMoney()))
        {
            //没缴完
            if(Double.valueOf(entity.getTotalMoney()) > Double.valueOf(entity.getMoney()))
            {
                mHolder.money.setTextColor(context.getResources().getColor(R.color.payment_ff6256));
//                mHolder.goToPay.setVisibility(View.VISIBLE);
            }
            else
            {
                mHolder.money.setTextColor(context.getResources().getColor(R.color.payment_9ddb54));
//                mHolder.goToPay.setVisibility(View.GONE);
            }
            String money = String.format(context.getResources().getString(R.string.home_total_money), entity.getMoney());
            mHolder.money.setText(money);
        }
        else{
            String money = String.format(context.getResources().getString(R.string.home_total_money), "0");
            mHolder.money.setText(money);
        }
        
        mHolder.moneyList.removeAllViews();
        List<MoneyAllList> moneylist = entity.getMoneyList();
        if(moneylist != null && moneylist.size() > 0)
        {
            for(int i=0 ; i<moneylist.size() ;i++)
            {
                View view = LayoutInflater.from(context).inflate(R.layout.home_account_manage_pay_money_list, null);
                TextView time = (TextView)view.findViewById(R.id.time);
                TextView payedMoney = (TextView)view.findViewById(R.id.payed_money);
                ImageView line = (ImageView)view.findViewById(R.id.last_line);
                mHolder.moneyList.addView(view);
                MoneyAllList money_list = moneylist.get(i);
                time.setText(GeneralUtils.splitToPay(money_list.getTime()));
                payedMoney.setText(money_list.getMoney());
                
                if(i == moneylist.size() -1)
                {
                    line.setVisibility(View.GONE);
                }
            }
        }
        
        return convertView;
    }
    
    class HolderView
    {
        //费用名
        TextView name;
        //应缴费用
        TextView totalMoney;
        //已缴费用
        TextView money;
        //已缴费用列表
        LinearLayout moneyList;
        //去缴费
        Button goToPay;
    }
    
}
