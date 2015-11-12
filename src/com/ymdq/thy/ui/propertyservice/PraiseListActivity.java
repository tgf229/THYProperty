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
    private LinearLayout loadingLayout,clickrefreshLayout,titleName;
    
    private GifView gif1;
    
    private TextView clickTextView,voteTimes;
    
    private MyGridView praiseGrid;
    
    private ScrollView scrollView;
    
    private List<PraiseListDoc> mList;
    
    private PraiseListAdapter listAdapter;
    
    private String myVoteTimes;
    
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
        
        titleName.setOnClickListener(this);
    }
    
    private void initData()
    {
        mList = new ArrayList<PraiseListDoc>();
        
        //测试
        gif1.setPaused(true);
        loadingLayout.setVisibility(View.GONE);
        ArrayList<PraiseListDoc> list = new ArrayList<PraiseListDoc>();
        PraiseListDoc bean = new PraiseListDoc();
        bean.seteId("1");
        bean.seteName("涂高峰");
        bean.seteNum("052402");
        bean.seteImageUrl("http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2d97560012dfa9ecfd7b5e1357e0db35/962bd40735fae6cda4cd9f7d0cb30f2442a70fb4.jpg");
        bean.seteDepName("保卫部");
        bean.setPraise("2011");
        bean.setTop("");
        bean.setIsPraise("0");
        
        PraiseListDoc bean2 = new PraiseListDoc();
        bean2.seteId("1");
        bean2.seteName("涂高峰");
        bean2.seteNum("052402");
        bean2.seteImageUrl("http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2d97560012dfa9ecfd7b5e1357e0db35/962bd40735fae6cda4cd9f7d0cb30f2442a70fb4.jpg");
        bean2.seteDepName("保卫部");
        bean2.setPraise("2011");
        bean2.setTop("");
        bean2.setIsPraise("0");
        
        PraiseListDoc bean3 = new PraiseListDoc();
        bean3.seteId("1");
        bean3.seteName("涂高峰");
        bean3.seteNum("052402");
        bean3.seteImageUrl("http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2d97560012dfa9ecfd7b5e1357e0db35/962bd40735fae6cda4cd9f7d0cb30f2442a70fb4.jpg");
        bean3.seteDepName("保卫部");
        bean3.setPraise("2011");
        bean3.setTop("");
        bean3.setIsPraise("0");
        
        PraiseListDoc bean4 = new PraiseListDoc();
        bean4.seteId("1");
        bean4.seteName("涂高峰");
        bean4.seteNum("052402");
        bean4.seteImageUrl("http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2d97560012dfa9ecfd7b5e1357e0db35/962bd40735fae6cda4cd9f7d0cb30f2442a70fb4.jpg");
        bean4.seteDepName("保卫部");
        bean4.setPraise("2011");
        bean4.setTop("");
        bean4.setIsPraise("0");
        
        list.add(bean);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        listAdapter = new PraiseListAdapter(this, list, this);
        praiseGrid.setAdapter(listAdapter);
    }
    
    private void reqList()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
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
                        if (GeneralUtils.isNullOrZeroLenght(myVoteTimes) || "0".equals(myVoteTimes.trim()))
                        {
                            voteTimes.setText("您已经没有投票机会了哦！");
                        }
                        else
                        {
                            voteTimes.setText(Html.fromHtml("您还有<font color=#d96e5d>" + myVoteTimes + "</font>次表扬机会哦！"));
                        }
                        List<PraiseListDoc> doc = resp.getDoc();
                        if (GeneralUtils.isNotNullOrZeroSize(doc))
                        {
                            mList.clear();
                            mList.addAll(doc);
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
                    clickTextView.setText(Constants.ERROR_MESSAGE);
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
            case R.id.title_name:
                showCalendar();
                break;
            
            default:
                break;
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
        minCalendar.set(2015, 8, 1);
        datePicker.setMinDate(minCalendar.getTimeInMillis());
        
        Calendar maxCalendar = Calendar.getInstance();
//        maxCalendar.add(Calendar.YEAR,1);
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
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
