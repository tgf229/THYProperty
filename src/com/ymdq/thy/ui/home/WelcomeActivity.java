/*
 * 文 件 名:  WelcomeActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-28
 
 */
package com.ymdq.thy.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mobstat.StatService;
import com.viewpagerindicator.CirclePageIndicator;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.LoginResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.home.adapter.GuidePagerAdapter;
import com.ymdq.thy.ui.personcenter.LoginActivity;
import com.ymdq.thy.util.GeneralUtils;

/**
 * <欢迎界面>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class WelcomeActivity extends BaseActivity
{
    private boolean fromJpush, fromJpushNoti;
    
    private String add_guide = Constants.GUIDE_VERSION_CODE + "";
    
    /**
     * 滑动的图片
     */
    private ViewPager banner_Pager;
    
    private GuidePagerAdapter circleImagePagerAdapter;
    
    private ArrayList<String> images;
    
    private CirclePageIndicator banner_indicator;
    
    private Handler handler = new Handler()
    {
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        //设置已开启APP
        Global.setAppOpen(true);
        //判断3栏引导页是否需要展示
        if (add_guide.equals(Global.getUserGuide()))
        {
            init();
        }
        else
        {
            setupView();
            Global.saveUserGuide(add_guide);
        }
    }
    
    /**
     * <渲染引导页>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void setupView()
    {
        images = new ArrayList<String>();
        images.add("guide_one");
        images.add("guide_two");
        images.add("guide_three");
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.circlepager_rl);
        rl.setVisibility(View.VISIBLE);
        banner_Pager = (ViewPager)findViewById(R.id.circlepager);
        circleImagePagerAdapter = new GuidePagerAdapter(this, images, WelcomeActivity.this);
        banner_Pager.setAdapter(circleImagePagerAdapter);
        //实例化CirclePageIndicator 并设置与ViewPager关联
        banner_indicator = (CirclePageIndicator)findViewById(R.id.circleindicator);
        banner_indicator.setViewPager(banner_Pager);
        
        getData();
    }
    
    private void init()
    {
        ImageView imageView = (ImageView)findViewById(R.id.loading_welcome_iv);
        imageView.setVisibility(View.VISIBLE);
        
        fromJpush = getIntent().getBooleanExtra("from_jpush", false);
        fromJpushNoti = getIntent().getBooleanExtra("from_jpush_noti", false);
        
        //Android延后处理事件的方法  :  postDelayed 和   Handler和TimerTask相结合 
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (fromJpush)
                {
                    //打开自定义的Activity
                    Intent i = new Intent(WelcomeActivity.this, MyCentralMeeageActivity.class);
                    i.putExtra("from_jpush", true);
                    i.putExtra("from_jpush_noti", fromJpushNoti);
                    WelcomeActivity.this.startActivity(i);
                }
                else
                {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    intent.putExtra("from_homepage", "from_homepage");
                    WelcomeActivity.this.startActivity(intent);
                    WelcomeActivity.this.finish();
                }
            }
        }, 2000);
        
        getData();
    }
    
    private void getData()
    {
        //        reqInit();
        initData();
    }
    
    /**
     * <判断是否可以自动登录 >
     * <若可以则自动登录 >
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        //自动登陆
        if (GeneralUtils.isNotNullOrZeroLenght(Global.getUserName())
            && GeneralUtils.isNotNullOrZeroLenght(Global.getPassword()))
        {
            Map<String, String> param = new HashMap<String, String>();
            try
            {
                param.put("username", Global.getUserName());
                param.put("pwd", Global.getPassword());
                param.put("type", Constants.clientType);
                param.put("version", GeneralUtils.getVersionName(WelcomeActivity.this));
                param.put("imei", GeneralUtils.getDeviceId(WelcomeActivity.this));
                param.put("cId", Global.getUCId());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            ConnectService.instance().connectServiceReturnResponse(WelcomeActivity.this,
                param,
                WelcomeActivity.this,
                LoginResponse.class,
                URLUtil.USER_LOGIN,
                Constants.ENCRYPT_NONE);
        }
        else
        {
            Global.setIsLogin(false);
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (ob instanceof LoginResponse)
        {
            LoginResponse loginResponse = (LoginResponse)ob;
            
            if (GeneralUtils.isNotNullOrZeroLenght(loginResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(loginResponse.getRetcode()))
                {
                    Global.saveData(loginResponse, Global.getUserName(), Global.getPassword(), true);
                }
            }
        }
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
        StatService.onPause(this);
        JPushInterface.onPause(this);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        StatService.onResume(this);
        JPushInterface.onResume(this);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
