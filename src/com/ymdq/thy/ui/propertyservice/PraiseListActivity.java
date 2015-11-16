/*
 * 文 件 名:  PraiseActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-11-11
 
 */
package com.ymdq.thy.ui.propertyservice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.propertyservice.PraiseListDoc;
import com.ymdq.thy.bean.propertyservice.PraiseListResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.propertyservice.adapter.PraiseListAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.view.GifView;
import com.ymdq.thy.view.MyGridView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-11-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressLint("NewApi")
public class PraiseListActivity extends BaseActivity implements OnClickListener
{
    private LinearLayout loadingLayout,clickrefreshLayout,titleName,back;
    
    private GifView gif1;
    
    private TextView clickTextView,voteTimes,yearTxt,monthTxt;
    
    private MyGridView praiseGrid;
    
    private ScrollView scrollView;
    
    private List<PraiseListDoc> mList;
    
    private PraiseListAdapter listAdapter;
    
    public String myVoteTimes;
    
    public String currentTime;
    public String choiseTime;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_praise_list);
        
        initView();
        initData();
    }
    
    private void initView()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        scrollView = (ScrollView)findViewById(R.id.scroll_view);
        titleName = (LinearLayout)findViewById(R.id.title_name);
        //加载中
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        gif1 = (GifView)loadingLayout.findViewById(R.id.gif1);
        // 设置背景gif图片资源  
        gif1.setMovieResource(R.raw.jiazai_gif);
        loadingLayout.setVisibility(View.VISIBLE);
        
        //加载失败
        clickrefreshLayout = (LinearLayout)findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
        
        voteTimes = (TextView)findViewById(R.id.vote_times_txt);
        praiseGrid = (MyGridView)findViewById(R.id.praise_grid);
        
        
        yearTxt = (TextView)findViewById(R.id.year_txt);
        monthTxt = (TextView)findViewById(R.id.month_txt);
        back.setOnClickListener(this);
    }
    
    private void initData()
    {
        mList = new ArrayList<PraiseListDoc>();
        listAdapter = new PraiseListAdapter(this, mList, this);
        praiseGrid.setAdapter(listAdapter);
        reqList();
    }
    
    private void reqList()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            if(GeneralUtils.isNotNullOrZeroLenght(choiseTime))
            {
                param.put("time", SecurityUtils.encode2Str(choiseTime));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            PraiseListResponse.class,
            URLUtil.BUS_200501,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        gif1.setPaused(true);
        loadingLayout.setVisibility(View.GONE);
        if (ob != null)
        {
            if (ob instanceof PraiseListResponse)
            {
                PraiseListResponse resp = (PraiseListResponse)ob;
                if (GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if (Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        myVoteTimes = resp.getPraiseTimes();
                        currentTime = resp.getCurrentTime();
                        if (GeneralUtils.isNullOrZeroLenght(myVoteTimes) || "0".equals(myVoteTimes.trim()))
                        {
                            voteTimes.setText("您本月已经没有投票机会了哦！");
                        }
                        else
                        {
                            voteTimes.setText(Html.fromHtml("您还有<font color=#d96e5d>" + myVoteTimes + "</font>次表扬机会哦！"));
                        }
                        if(GeneralUtils.isNotNullOrZeroLenght(currentTime) && currentTime.length() == 6)
                        {
                            yearTxt.setText(GeneralUtils.isNullOrZeroLenght(choiseTime)?currentTime.substring(0, 4):choiseTime.substring(0, 4));
                            monthTxt.setText(GeneralUtils.isNullOrZeroLenght(choiseTime)?formatMonth(currentTime.substring(4, 6)):formatMonth(choiseTime.substring(4, 6)));
                            titleName.setOnClickListener(this);
                        }
                        List<PraiseListDoc> doc = resp.getDoc();
                        if (GeneralUtils.isNotNullOrZeroSize(doc))
                        {
                            mList.clear();
                            mList.addAll(doc);
                            clickrefreshLayout.setVisibility(View.GONE);
                            listAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            clickrefreshLayout.setVisibility(View.VISIBLE);
                            clickTextView.setText("暂无投票信息");
                        }
                    }
                    else
                    {
                        clickrefreshLayout.setVisibility(View.VISIBLE);
                        clickTextView.setText(resp.getRetinfo());
                    }
                }
                else
                {
                    clickrefreshLayout.setVisibility(View.VISIBLE);
                    clickTextView.setText("暂无投票信息");
                }
            }
        }
    }
    
    @Override
    protected void onResume()
    {
        scrollView.smoothScrollTo(0, 0);
        super.onResume();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                this.finish();
                break;
            case R.id.title_name:
                showCalendar();
                break;
            
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Praise_vote_success && resultCode == Constants.Praise_vote_success)
        {
            String eId = data.getStringExtra("eId");
            for(PraiseListDoc doc: mList)
            {
                if(eId.equals(doc.geteId()))
                {
                    doc.setIsPraise("1");
                }
            }
            myVoteTimes = String.valueOf(Integer.parseInt(myVoteTimes) - 1);
            voteTimes.setText(Html.fromHtml("您还有<font color=#d96e5d>" + myVoteTimes + "</font>次表扬机会哦！"));
        }
    }
    
    private void showCalendar()
    {
        final DatePicker datePicker = new DatePicker(PraiseListActivity.this);
        datePicker.setCalendarViewShown(false);

        //通过反射机制，访问private的mDaySpinner成员，并隐藏它
        try {
            Field daySpinner =datePicker.getClass().getDeclaredField("mDaySpinner");
            daySpinner.setAccessible(true);
            ((View)daySpinner.get(datePicker)).setVisibility(View.GONE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Calendar minCalendar = Calendar.getInstance();
        minCalendar.set(Calendar.HOUR_OF_DAY,0);
        minCalendar.set(Calendar.MINUTE,0);
        minCalendar.set(Calendar.SECOND,0);
        minCalendar.set(2014, 8, 1);
        datePicker.setMinDate(minCalendar.getTimeInMillis());
        
        Calendar maxCalendar = Calendar.getInstance();
//        maxCalendar.add(Calendar.YEAR,1);
        maxCalendar.set(Integer.parseInt(currentTime.substring(0, 4)), Integer.parseInt(formatMonth(currentTime.substring(4, 6)))-1, 1);
        datePicker.setMaxDate(maxCalendar.getTimeInMillis());
        
        
        Calendar curCalendar = Calendar.getInstance();
        datePicker.init(curCalendar.get(Calendar.YEAR),
        curCalendar.get(Calendar.MONTH),
        curCalendar.get(Calendar.DAY_OF_MONTH),null);

        AlertDialog.Builder builder = new AlertDialog.Builder(PraiseListActivity.this);
        builder.setView(datePicker);
        builder.setTitle("请选择日期");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                int y = datePicker.getYear();
                int m = datePicker.getMonth();
                yearTxt.setText(String.valueOf(y));
                monthTxt.setText(String.valueOf(m+1));
                String yearData = String.valueOf(y);
                String monthData = String.valueOf(m+1);
                if(monthData.length()<2)
                {
                    monthData = "0"+monthData;
                }
                choiseTime = yearData+monthData;
                mList.clear();
                reqList();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    
    private String formatMonth(String str)
    {
        String s = "";
        if(GeneralUtils.isNotNullOrZeroLenght(str) && str.length() == 2 && str.startsWith("0"))
        {
            s = str.substring(1, 2);
            return s;
        }
        return str;
    }

}
