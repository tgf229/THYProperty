/*
 * 文 件 名:  SetNewPasswordActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-24
 
 */
package com.ymdq.thy.ui.personcenter;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.SetNewPasswordResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;

/**
 * <设置新密码>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SetNewPasswordActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    private EditText etPassword, etPassAgain;
    
    private Button btSumbit;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    private String phone, password;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_new_password);
        initTitle();
        init();
        addListener();
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setText("忘记密码");
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                SetNewPasswordActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        phone = getIntent().getStringExtra("old_username");
        etPassword = (EditText)findViewById(R.id.set_new_password_bt);
        etPassAgain = (EditText)findViewById(R.id.set_new_password_again_bt);
        btSumbit = (Button)findViewById(R.id.set_new_password_sumbit_bt);
        dailog = new NetLoadingDailog(this);
    }
    
    private void addListener()
    {
        btSumbit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                setNewPassword();
            }
        });
    }
    
    private void setNewPassword()
    {
        password = etPassword.getText().toString().trim();
        String passAgain = etPassAgain.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(password))
        {
            ToastUtil.makeText(this, "请输入密码");
            return;
        }
        
        if (!GeneralUtils.IsPassword(password))
        {
            ToastUtil.makeText(this, "密码为6至20位数字、字母、下划线组成!");
            return;
        }
        
        if (GeneralUtils.isNullOrZeroLenght(passAgain))
        {
            ToastUtil.makeText(this, "请再次输入密码");
            return;
        }
        if (!password.equals(passAgain))
        {
            ToastUtil.makeText(this, "您两次输入的密码不一致");
            return;
        }
        
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("username", phone);
        param.put("pwd", SecurityUtils.get32MD5Str(password));
        
        ConnectService.instance().connectServiceReturnResponse(SetNewPasswordActivity.this,
            param,
            SetNewPasswordActivity.this,
            SetNewPasswordResponse.class,
            URLUtil.USER_SET_NEW_PASSWORD,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        
        if (ob instanceof SetNewPasswordResponse)
        {
            SetNewPasswordResponse setNewPasswordResponse = (SetNewPasswordResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(setNewPasswordResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(setNewPasswordResponse.getRetcode()))
                {
                    ToastUtil.makeText(SetNewPasswordActivity.this, "密码重置成功");
                    SetNewPasswordActivity.this.setResult(Constants.Forget_password_set_new);
                    SetNewPasswordActivity.this.finish();
                }
                else
                {
                    ToastUtil.makeText(SetNewPasswordActivity.this, setNewPasswordResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(SetNewPasswordActivity.this);
            }
        }
    }
}
