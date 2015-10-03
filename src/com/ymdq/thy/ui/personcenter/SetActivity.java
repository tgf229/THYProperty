/*
 * 文 件 名:  SetActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-28
 
 */
package com.ymdq.thy.ui.personcenter;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.UpdateVersionResponse;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.DownApkUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.ToastUtil;

/**
 * <设置>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SetActivity extends BaseActivity implements OnClickListener
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 消息开关,更新,帮助,关于,退出
     */
    private LinearLayout llMessage, llUpdate, llHelp, llAbout, llExit;
    
    private ImageView ivPush;
    
    private View line;
    
    /**
     * 加载对话框
     */
    
    private NetLoadingDailog dailog;
    
    /**
     * 当前版本号
     */
    private String versionName;
    
    /**
     * 下载apk文件类
     */
    private DownApkUtil downApkUtil;
    
    /**
     * 下载apk后安装apk
     */
    private Handler handler = new Handler()
    {
        @Override
        public void dispatchMessage(Message msg)
        {
            super.dispatchMessage(msg);
            switch (msg.what)
            {
                case Constants.DOWNLOAD:
                    // 设置进度条位置  
                    downApkUtil.updateProgress();
                    break;
                case Constants.DOWNLOAD_FINISH:
                    // 安装文件  
                    downApkUtil.installApk();
                    break;
                case Constants.NO_SD:
                    ToastUtil.makeText(SetActivity.this, "请插入SD卡");
                    break;
                default:
                    break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
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
        tvTitle.setText("设置");
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                SetActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        llMessage = (LinearLayout)findViewById(R.id.set_message_ll);
        llUpdate = (LinearLayout)findViewById(R.id.set_update_ll);
        llHelp = (LinearLayout)findViewById(R.id.set_help_ll);
        llAbout = (LinearLayout)findViewById(R.id.set_about_ll);
        llExit = (LinearLayout)findViewById(R.id.set_exit_ll);
        line = findViewById(R.id.set_exit_ll_line);
        
        ivPush = (ImageView)findViewById(R.id.set_message_img_iv);
        
        llMessage.setOnClickListener(this);
        llUpdate.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        llAbout.setOnClickListener(this);
        llExit.setOnClickListener(this);
        dailog = new NetLoadingDailog(this);
        
        if (Global.isLogin())
        {
            llExit.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }
        else
        {
            llExit.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        
        boolean op_noti = JPushInterface.isPushStopped(SetActivity.this.getApplicationContext());
        if (Global.getPush())
        {
            ivPush.setImageResource(R.drawable.setting_swith_on);
            if (op_noti)
            {
                JPushInterface.resumePush(SetActivity.this.getApplicationContext());
            }
        }
        else
        {
            ivPush.setImageResource(R.drawable.setting_swith_off);
            if (!op_noti)
            {
                JPushInterface.stopPush(SetActivity.this.getApplicationContext());
            }
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.set_message_ll://消息开关
                if (Global.getPush())
                {
                    Global.savePush(false);
                    ivPush.setImageResource(R.drawable.setting_swith_off);
                    JPushInterface.stopPush(SetActivity.this.getApplicationContext());
                }
                else
                {
                    Global.savePush(true);
                    ivPush.setImageResource(R.drawable.setting_swith_on);
                    JPushInterface.resumePush(SetActivity.this.getApplicationContext());
                }
                break;
            
            case R.id.set_update_ll://检查更新
                
                updateVersion();
                
                break;
            
            case R.id.set_help_ll://帮助与反馈
                Intent intentHelp = new Intent(SetActivity.this, FeedbackActivity.class);
                startActivity(intentHelp);
                break;
            
            case R.id.set_about_ll://关于
                Intent intentAbout = new Intent(SetActivity.this, AboutUsActivity.class);
                startActivity(intentAbout);
                break;
            
            case R.id.set_exit_ll://退出
                Global.loginOut();
                //取消别名
                Global.setAliasApp(SetActivity.this, "");
                setResult(Constants.User_login_out);
                SetActivity.this.finish();
                
                Intent intentLogin = new Intent(SetActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 
     * <检查版本更新>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void updateVersion()
    {
        dailog.loading();
        versionName = GeneralUtils.getVersionName(this);
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("version", "" + versionName);
        param.put("type", "" + Constants.clientType);
        param.put("imei", GeneralUtils.getDeviceId(this));
        
        ConnectService.instance().connectServiceReturnResponse(SetActivity.this,
            param,
            SetActivity.this,
            UpdateVersionResponse.class,
            URLUtil.SET_UPDATE,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        
        if (ob instanceof UpdateVersionResponse)
        {
            final UpdateVersionResponse response = (UpdateVersionResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(response.getRetcode()))
            {
                if (response.getRetcode().equals(Constants.SUCESS_CODE))
                {
                    downApkUtil = new DownApkUtil(SetActivity.this, handler, response.getUrlAddress());
                    
                    String[] contentString = response.getContent().split(";");
                    String cancel = getResources().getString(R.string.set_update_cancel);
                    
                    if ("1".equals(response.getIsUpdate()))
                    {
                        cancel = getResources().getString(R.string.set_update_quit);
                    }
                    DialogUtil.showUpdateDialog(this,
                        getResources().getString(R.string.updateVersionTitel),
                        contentString,
                        getResources().getString(R.string.updateVersion),
                        cancel,
                        response.getIsUpdate(),
                        new DialogCallBack()
                        {
                            
                            @Override
                            public void dialogBack()
                            {
                                downApkUtil.downApk(response.getIsUpdate());
                                
                            }
                        });
                }
                else
                {
                    ToastUtil.makeText(this, response.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(SetActivity.this);
            }
        }
    }
    
}
