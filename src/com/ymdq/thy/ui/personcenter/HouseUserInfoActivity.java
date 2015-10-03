/*
 * 文 件 名:  HouseUserInfoActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-25
 
 */
package com.ymdq.thy.ui.personcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseFrozenResponse;
import com.ymdq.thy.bean.personcenter.HouseInfoDoc;
import com.ymdq.thy.bean.personcenter.HouseUserInfoDoc;
import com.ymdq.thy.bean.personcenter.HouseUserInfoResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.community.CommunityPersonDetailActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.MyListView;

/**
 * <房屋绑定账号信息>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseUserInfoActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    private HouseInfoDoc info;
    
    private ArrayList<HouseUserInfoDoc> infoDocs;
    
    /**
     * 账号列表
     */
    private MyListView listView;
    
    private TextView tvAddress;
    
    private LinearLayout llEdit;
    
    private TextView tvEdit;
    
    private HouseUserInfoAdapter adapter;
    
    private boolean booEdit;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_user_info);
        initTitle();
        init();
        addListener();
        initData();
    }
    
    private void init()
    {
        info = (HouseInfoDoc)getIntent().getSerializableExtra("house_user_info");
        listView = (MyListView)findViewById(R.id.house_user_info_lv);
        tvAddress = (TextView)findViewById(R.id.house_user_info_adress_tv);
        llEdit = (LinearLayout)findViewById(R.id.house_user_info_edit_ll);
        tvEdit = (TextView)findViewById(R.id.house_user_info_edit_tv);
        adapter = new HouseUserInfoAdapter(infoDocs, this, booEdit, HouseUserInfoActivity.this);
        listView.setAdapter(adapter);
        GeneralUtils.setListViewHeightBasedOnChildrenExtend(listView);
        
        tvAddress.setText(info.getcName() + info.getbName() + info.getdName() + info.gethName());
        dailog = new NetLoadingDailog(this);
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setText("房屋信息");
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                HouseUserInfoActivity.this.finish();
            }
        });
    }
    
    private void initData()
    {
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("hId", SecurityUtils.encode2Str(info.gethId()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(HouseUserInfoActivity.this,
            param,
            HouseUserInfoActivity.this,
            HouseUserInfoResponse.class,
            URLUtil.HOUSE_USER_INFO,
            Constants.ENCRYPT_SIMPLE);
    }
    
    private void addListener()
    {
        llEdit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (booEdit)
                {
                    booEdit = false;
                    updateData(infoDocs, booEdit);
                    tvEdit.setText("编辑");
                }
                else
                {
                    tvEdit.setText("完成");
                    booEdit = true;
                    updateData(infoDocs, booEdit);
                }
            }
        });
    }
    
    /**
     * 更新账户信息
     */
    private void updateData(ArrayList<HouseUserInfoDoc> infoDocs, boolean ifEdit)
    {
        adapter.updata(infoDocs, ifEdit);
    }
    
    /**
     * 业主冻结&恢复房下账号
     */
    public void editHouse(String state, String optUId)
    {
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("hId", SecurityUtils.encode2Str(info.gethId()));
            param.put("optUId", SecurityUtils.encode2Str(optUId));
            param.put("type", SecurityUtils.encode2Str(state));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(HouseUserInfoActivity.this,
            param,
            HouseUserInfoActivity.this,
            HouseFrozenResponse.class,
            URLUtil.HOUSE_USER_FROZEN,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        
        if (ob instanceof HouseFrozenResponse)
        {
            HouseFrozenResponse frozenResponse = (HouseFrozenResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(frozenResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(frozenResponse.getRetcode()))
                {
                    initDataAgain();
                }
                else
                {
                    if (dailog != null)
                    {
                        dailog.dismissDialog();
                    }
                    ToastUtil.makeText(HouseUserInfoActivity.this, frozenResponse.getRetinfo());
                }
            }
            else
            {
                if (dailog != null)
                {
                    dailog.dismissDialog();
                }
                ToastUtil.showError(HouseUserInfoActivity.this);
            }
        }
        
        if (ob instanceof HouseUserInfoResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            HouseUserInfoResponse houseUserInfoResponse = (HouseUserInfoResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(houseUserInfoResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(houseUserInfoResponse.getRetcode()))
                {
                    infoDocs = (ArrayList<HouseUserInfoDoc>)houseUserInfoResponse.getDoc();
                    updateData(infoDocs, booEdit);
                    
                    for (int i = 0; i < infoDocs.size(); i++)
                    {
                        if (Global.getUserId().equals(infoDocs.get(i).getuId())
                            && "1".equals(infoDocs.get(i).getLevel()))
                        {
                            llEdit.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else
                {
                    ToastUtil.makeText(HouseUserInfoActivity.this, houseUserInfoResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(HouseUserInfoActivity.this);
            }
        }
    }
    
    private void initDataAgain()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("hId", SecurityUtils.encode2Str(info.gethId()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(HouseUserInfoActivity.this,
            param,
            HouseUserInfoActivity.this,
            HouseUserInfoResponse.class,
            URLUtil.HOUSE_USER_INFO,
            Constants.ENCRYPT_SIMPLE);
    }
}
