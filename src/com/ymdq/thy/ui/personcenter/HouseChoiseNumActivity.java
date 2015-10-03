/*
 * 文 件 名:  HouseChoiseBuildingActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-20
 
 */
package com.ymdq.thy.ui.personcenter;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseDList;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.BaseActivity;

/**
 * 房号选择
 * <功能详细描述>
 * 
 * @author  wt
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseChoiseNumActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 房屋信息
     */
    private ArrayList<HouseDList> dList;
    
    private ListView lvBuilding;
    
    private HouseNumAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_choise_building);
        initTitle();
        init();
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        String title=getIntent().getStringExtra("house_building_num_title");
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setText(title);
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                HouseChoiseNumActivity.this.finish();
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    private void init()
    {
        dList = (ArrayList<HouseDList>)getIntent().getSerializableExtra("house_building_num");
        lvBuilding = (ListView)findViewById(R.id.house_choise_building_lv);
        adapter = new HouseNumAdapter(this, dList);
        lvBuilding.setAdapter(adapter);
        
        lvBuilding.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
            {
                HouseDList houseDList = dList.get(position);
                Intent building = new Intent();
                building.putExtra("building_num", houseDList);
                HouseChoiseNumActivity.this.setResult(Constants.Register_get_num, building);
                HouseChoiseNumActivity.this.finish();
            }
        });
    }
    
}
