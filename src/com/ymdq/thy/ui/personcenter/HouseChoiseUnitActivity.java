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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseBList;
import com.ymdq.thy.bean.personcenter.HouseDList;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 单元选择
 * <功能详细描述>
 * 
 * @author  wt
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseChoiseUnitActivity extends BaseActivity implements OnScrollListener
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 房屋信息
     */
    private ArrayList<HouseBList> bList;
    /**
     * 单元
     */
    private HouseBList houseBList;
    
    private ArrayList<HouseDList> unit;
    
    private ExpandableListView lvBuilding;
    
    private HouseUnitAdapter adapter;
    
    private FrameLayout frameLay;
    
    private int frameLayHeight;
    
    private int frameLayId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_choise_unit);
        initTitle();
        init();
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        String title=getIntent().getStringExtra("house_building_unit_title");
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setText(title);
        tvTitle.setTypeface(GeneralUtils.getFontFace(this));
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                HouseChoiseUnitActivity.this.finish();
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    private void init()
    {
        bList = (ArrayList<HouseBList>)getIntent().getSerializableExtra("house_building_unit");
        lvBuilding = (ExpandableListView)findViewById(R.id.house_choise_building_lv);
        adapter = new HouseUnitAdapter(this, bList);
        lvBuilding.setAdapter(adapter);
        lvBuilding.setOnChildClickListener(new OnChildClickListener()
        {
            
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                Intent building = new Intent();
                HouseDList houseNum = ((HouseDList)adapter.getChild(groupPosition, childPosition));
                HouseBList bMess = (HouseBList)adapter.getGroup(groupPosition);
                String unitName = bMess.getdName();//单元名
                String unitId = bMess.getdId();//单元号
                building.putExtra("building_num", houseNum);//房号
                building.putExtra("building_uint_name", unitName);//单元名
                building.putExtra("building_uint_no", unitId);//单元号
                HouseChoiseUnitActivity.this.setResult(Constants.Register_get_unit, building);
                HouseChoiseUnitActivity.this.finish();
                return true;
            }
        });
//        lvBuilding.setOnItemClickListener(new OnItemClickListener()
//        {
//            
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
//            {
////                houseBList=bList.get(position);
////                unit = (ArrayList<HouseDList>)houseBList.getdList();
////                Intent intent = new Intent(HouseChoiseUnitActivity.this, HouseChoiseNumActivity.class);
////                intent.putExtra("house_building_num", unit);
////                intent.putExtra("house_building_num_title", houseBList.getdName());
////                HouseChoiseUnitActivity.this.startActivityForResult(intent, 2);
//            }
//        });
        expandAll();
        lvBuilding.setOnScrollListener(this);
        frameLay = (FrameLayout)findViewById(R.id.frame_layout);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.house_choise_head, frameLay, true);
    }
    
    private void expandAll()
    {
        if(adapter != null)
        {
            int count = adapter.getGroupCount();
            for(int i= 0;i<count;i++)
            {
                lvBuilding.expandGroup(i);
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        ExpandableListView expandView = (ExpandableListView)view;
        int nsp = expandView.pointToPosition(0, 0);
        if (nsp == AdapterView.INVALID_POSITION)// 如果第一个位置值无效
            return;
        long position = expandView.getExpandableListPosition(nsp);
        if(position == AdapterView.INVALID_POSITION)
        {
            return;
        }
        int childPosition = ExpandableListView.getPackedPositionChild(position);
        int groupPosition = ExpandableListView.getPackedPositionGroup(position);
        if(childPosition ==AdapterView.INVALID_POSITION)
        {
            // 第一行不是显示child,就是group,此时没必要显示指示器
            View groupView = expandView.getChildAt(nsp
                - expandView.getFirstVisiblePosition());// 第一行的view
            frameLayHeight = groupView.getHeight();// 获取group的高度
            frameLay.setVisibility(View.GONE);
        }
        else
        {
            frameLay.setVisibility(View.VISIBLE);
        }
        if(frameLayHeight == 0)
            return;
        
        if(groupPosition != frameLayId)
        {
            adapter.getGroupView(groupPosition, expandView.isGroupExpanded(groupPosition), frameLay.getChildAt(0), null);
            frameLayId = groupPosition;
        }
        if(frameLayId == -1)
        {
            return;
        }
        //往上推出效果
        int showHeight = frameLayHeight + 0;
        int nEndPos = expandView.pointToPosition(0, frameLayHeight);// 第二个item的位置
        if (nEndPos == AdapterView.INVALID_POSITION)//如果无效直接返回
            return;
        long pos2 = expandView.getExpandableListPosition(nEndPos);
        int groupPos2 = ExpandableListView.getPackedPositionGroup(pos2);//获取第二个group的id
        if (groupPos2 != frameLayId) {//如果不等于指示器当前的group
            View viewNext = expandView.getChildAt(nEndPos
                - expandView.getFirstVisiblePosition());
            showHeight = viewNext.getTop();
        }
        
        MarginLayoutParams layoutParams = (MarginLayoutParams) frameLay
            .getLayoutParams();
        layoutParams.topMargin = -(frameLayHeight - showHeight);
        frameLay.setLayoutParams(layoutParams);
    
    }
    
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        switch (resultCode)
//        {
//            case Constants.Register_get_num:
//                Intent building = new Intent();
//                HouseDList houseDList=(HouseDList)data.getSerializableExtra("building_num");
//                building.putExtra("building_num", houseDList);//房号
//                building.putExtra("building_uint_name", houseBList.getdName());//单元名
//                building.putExtra("building_uint_no", houseBList.getdId());//单元号
//                HouseChoiseUnitActivity.this.setResult(Constants.Register_get_unit, building);
//                HouseChoiseUnitActivity.this.finish();
//                break;
//            
//            default:
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
