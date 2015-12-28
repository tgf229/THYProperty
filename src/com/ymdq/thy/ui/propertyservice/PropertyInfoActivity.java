/*
 * 文 件 名:  PropertyInfoActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-12-28
 
 */
package com.ymdq.thy.ui.propertyservice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.sharepref.SharePref;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.WebviewActivity;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.ToastUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-12-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PropertyInfoActivity extends BaseActivity implements OnClickListener
{
    private ImageView img;
    
    private TextView nameTxt, contentTxt, titleTxt, addressTxt, telTxt;
    
    private TextView title;
    
    private LinearLayout telLayout, titleLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_info);
        
        initView();
        initData();
    }
    
    private void initView()
    {
        LinearLayout back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setBackgroundResource(R.drawable.title_wuyegongsi);
        
        img = (ImageView)findViewById(R.id.img);
        nameTxt = (TextView)findViewById(R.id.name_txt);
        contentTxt = (TextView)findViewById(R.id.content_txt);
        titleTxt = (TextView)findViewById(R.id.title_txt);
        addressTxt = (TextView)findViewById(R.id.address_txt);
        telTxt = (TextView)findViewById(R.id.tel_txt);
        
        telLayout = (LinearLayout)findViewById(R.id.tel_layout);
        titleLayout = (LinearLayout)findViewById(R.id.title_layout);
        
        back.setOnClickListener(this);
        telLayout.setOnClickListener(this);
        titleLayout.setOnClickListener(this);
    }
    
    private void initData()
    {
        ImageLoader.getInstance().displayImage(SharePref.getString(SharePref.PROPERTY_LOGO, ""),
            img,
            JRApplication.setRoundDisplayImageOptions(this,
                "community_default",
                "community_default",
                "community_default"));
        nameTxt.setText(SharePref.getString(SharePref.PROPERTY_NAME, ""));
        contentTxt.setText(SharePref.getString(SharePref.PROPERTY_CONTENT, ""));
        titleTxt.setText(SharePref.getString(SharePref.PROPERTY_TITLE, ""));
        addressTxt.setText(SharePref.getString(SharePref.PROPERTY_ADDRESS, ""));
        telTxt.setText(SharePref.getString(SharePref.PROPERTY_TEL, ""));
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            case R.id.title_layout:
                Intent intent = new Intent(this, WebviewActivity.class);
                intent.putExtra("wev_view_url", SharePref.getString(SharePref.PROPERTY_TITLEURL, ""));
                startActivity(intent);
                break;
            case R.id.tel_layout:
                if (GeneralUtils.isNullOrZeroLenght(SharePref.getString(SharePref.PROPERTY_TEL, "")))
                {
                    ToastUtil.makeText(this, "暂无号码信息");
                    break;
                }
                DialogUtil.showTwoButtonDialog(this,
                    "您是否拨打物业号码：\n" + SharePref.getString(SharePref.PROPERTY_TEL, ""),
                    new DialogCallBack()
                    {
                        
                        @Override
                        public void dialogBack()
                        {
                            Intent callIntent =
                                new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                                    + SharePref.getString(SharePref.PROPERTY_TEL, "")));
                            startActivity(callIntent);
                        }
                    });
                break;
            default:
                break;
        }
    }
}
