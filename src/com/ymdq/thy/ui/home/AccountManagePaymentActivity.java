package com.ymdq.thy.ui.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.AccountManageDoc;
import com.ymdq.thy.bean.home.HomeList;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.home.adapter.AccountManagePaymentAdapter;

/**
 * 
 * <账单详情>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年12月13日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AccountManagePaymentActivity extends BaseActivity implements OnClickListener
{
    private AccountManageDoc doc;
    
    private String type;
    
    private String typeName;
    
    private ListView mListView;
    
    private AccountManagePaymentAdapter adapter;
    
    private List<HomeList> mList;
    
    /**
     * 全部列表
     */
    private List<HomeList> mAllList;
    
    /**
     * 未缴账单的列表
     */
    private List<HomeList> mNoPayList;
    
    private LinearLayout top_ListView;
    
    private LinearLayout noDataLayout;
    
    private CheckBox mCheckBox;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_account_manage_pay);
        doc = (AccountManageDoc)getIntent().getSerializableExtra("doc");
        type = getIntent().getStringExtra("type");
        typeName = getIntent().getStringExtra("typeName");
        
        initView();
        initData();
//        if(mCheckBox.isChecked())
//        {
//            mList.clear();
//            mList.addAll(mNoPayList);
//            adapter.notifyDataSetChanged();
//        }
//        else
//        {
//            mList.clear();
//            mList.addAll(mAllList);
//            adapter.notifyDataSetChanged();
//        }
    }
    
    private void initView()
    {
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setBackgroundResource(R.drawable.title_feiyong);
//        titleBarName.setText(typeName);
        
        top_ListView = (LinearLayout)findViewById(R.id.top_layout);
        mListView = (ListView)findViewById(R.id.list_view);
        mNoPayList = new ArrayList<HomeList>();
        mAllList = new ArrayList<HomeList>();
        mList = new ArrayList<HomeList>();
        adapter = new AccountManagePaymentAdapter(this,mList);
        mListView.setAdapter(adapter);
        
        noDataLayout = (LinearLayout)findViewById(R.id.no_data);
        
        mCheckBox = (CheckBox)findViewById(R.id.no_pay_checkbox);
        mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    if(mNoPayList.size() > 0)
                    {
                        mListView.setVisibility(View.VISIBLE);
                        noDataLayout.setVisibility(View.GONE);
                        mList.clear();
                        mList.addAll(mNoPayList);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        mListView.setVisibility(View.GONE);
                        noDataLayout.setVisibility(View.VISIBLE);
                        noDataLayout.setBackgroundResource(R.color.property_repair_bg);
                    }
                }
                else
                {
                    mListView.setVisibility(View.VISIBLE);
                    noDataLayout.setVisibility(View.GONE);
                    mList.clear();
                    mList.addAll(mAllList);
                    adapter.notifyDataSetChanged();
                }
            }
            
        });
    }
    
    private void initData()
    {
        if(doc != null && doc.gethList().size() > 0)
        {
            List<HomeList> hList = doc.gethList();
            for(int i = 0; i < hList.size() ; i++)
            {
                HomeList home = hList.get(i);
                if(home.getType().equals(type))
                {
                    mAllList.add(home);
                    //未缴账单
                    if(!home.getStatus().equals(Constants.HAVE_TO_PAY))
                    {
                        mNoPayList.add(home);
                    }
                }
                else
                {
                    continue;
                }
            }
            if(mAllList.size() > 0)
            {
                top_ListView.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                noDataLayout.setVisibility(View.GONE);
                
                mList.clear();
                mList.addAll(mAllList);
                adapter.notifyDataSetChanged();
                return;
                
            }
        }
        top_ListView.setVisibility(View.GONE);
        findViewById(R.id.line).setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        noDataLayout.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
                
            default:
                break;
        }
    }
}
