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
import com.ymdq.thy.bean.personcenter.HouseBList;
import com.ymdq.thy.bean.personcenter.HouseDList;
import com.ymdq.thy.bean.personcenter.HouseDoc;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 楼栋选择
 * <功能详细描述>
 * 
 * @author  wt
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseChoiseBuildingActivity extends BaseActivity
{
    /**
     * 小区名称
     */
    private String communityName = "";
    
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 房屋信息
     */
    private ArrayList<HouseDoc> doc;
    
    /**
     * 楼栋信息
     */
    private HouseDoc houseDoc;
    
    private ArrayList<HouseBList> bList;
    
    private ListView lvBuilding;
    
    private HouseBuildingAdapter adapter;
    
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
        communityName = getIntent().getStringExtra("community_name");
        TextView commNameText = (TextView)findViewById(R.id.com_name);
        commNameText.setText(communityName);
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setText("小区住址");
        tvTitle.setTypeface(GeneralUtils.getFontFace(this));
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                HouseChoiseBuildingActivity.this.finish();
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    private void init()
    {
        doc = (ArrayList<HouseDoc>)getIntent().getSerializableExtra("house_building");
        lvBuilding = (ListView)findViewById(R.id.house_choise_building_lv);
        adapter = new HouseBuildingAdapter(this, doc);
        lvBuilding.setAdapter(adapter);
        
        lvBuilding.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
            {
                houseDoc = doc.get(position);
                bList = (ArrayList<HouseBList>)houseDoc.getbList();
                Intent intent = new Intent(HouseChoiseBuildingActivity.this, HouseChoiseUnitActivity.class);
                intent.putExtra("house_building_unit", bList);
                intent.putExtra("house_building_unit_title", houseDoc.getbName());
                HouseChoiseBuildingActivity.this.startActivityForResult(intent, 1);
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Constants.Register_get_unit:
                Intent building = new Intent();
                HouseDList houseDList=(HouseDList)data.getSerializableExtra("building_num");
                building.putExtra("building_num", houseDList);//房号
                building.putExtra("building_uint_no", data.getStringExtra("building_uint_no"));//单元号
                building.putExtra("building_uint_name", data.getStringExtra("building_uint_name"));//单元名
                building.putExtra("building_building_no", houseDoc.getbId());//楼栋号
                building.putExtra("building_building_name", houseDoc.getbName());//楼栋名
                HouseChoiseBuildingActivity.this.setResult(Constants.Register_get_building, building);
                HouseChoiseBuildingActivity.this.finish();
                break;
            
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
