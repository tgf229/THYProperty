/*
 * 文 件 名:  AboutUsActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-12-2
 
 */
package com.ymdq.thy.ui.personcenter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;

/**
 * <关于我们>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-12-2]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AboutUsActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    private TextView tvVersion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        initTitle();
        init();
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
//        tvTitle.setText("关于");
        tvTitle.setBackgroundResource(R.drawable.title_guanyu);
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                AboutUsActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        tvVersion = (TextView)findViewById(R.id.about_us_version_tv);
        tvVersion.setText(GeneralUtils.getVersionName(AboutUsActivity.this));
    }
}
