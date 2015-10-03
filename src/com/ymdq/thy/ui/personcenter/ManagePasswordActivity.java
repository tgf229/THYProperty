package com.ymdq.thy.ui.personcenter;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.ManagePasswordResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <修改密码>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ManagePasswordActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    private EditText etPassword, etPassAgain, etOldPassword;
    
    private Button btSumbit;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    private String oldPassword, newPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_password);
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
        tvTitle.setText("修改密码");
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                ManagePasswordActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        etOldPassword = (EditText)findViewById(R.id.manage_old_password_bt);
        etPassword = (EditText)findViewById(R.id.manage_new_password_bt);
        etPassAgain = (EditText)findViewById(R.id.manage_new_password_again_bt);
        btSumbit = (Button)findViewById(R.id.manage_password_sumbit_bt);
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
        oldPassword = etOldPassword.getText().toString().trim();
        newPassword = etPassword.getText().toString().trim();
        String passAgain = etPassAgain.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(oldPassword))
        {
            ToastUtil.makeText(this, "请输入旧密码");
            return;
        }
        if (GeneralUtils.isNullOrZeroLenght(newPassword))
        {
            ToastUtil.makeText(this, "请输入新密码");
            return;
        }
        
        if (!GeneralUtils.IsPassword(newPassword))
        {
            ToastUtil.makeText(this, "密码为6至20位数字、字母、下划线组成!");
            return;
        }
        
        if (GeneralUtils.isNullOrZeroLenght(passAgain))
        {
            ToastUtil.makeText(this, "请再次输入新密码");
            return;
        }
        if (!newPassword.equals(passAgain))
        {
            ToastUtil.makeText(this, "您两次输入的密码不一致");
            return;
        }
        
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("oldPwd", SecurityUtils.encode2Str(SecurityUtils.get32MD5Str(oldPassword)));
            param.put("pwd", SecurityUtils.encode2Str(SecurityUtils.get32MD5Str(newPassword)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(ManagePasswordActivity.this,
            param,
            ManagePasswordActivity.this,
            ManagePasswordResponse.class,
            URLUtil.USER_MANAGE_PASSWORD,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        
        if (ob instanceof ManagePasswordResponse)
        {
            ManagePasswordResponse managePasswordResponse = (ManagePasswordResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(managePasswordResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(managePasswordResponse.getRetcode()))
                {
                    ToastUtil.makeText(ManagePasswordActivity.this, "修改密码成功");
                    
                    Global.loginOut();
                    //取消别名
                    Global.setAliasApp(ManagePasswordActivity.this, "");
                    setResult(Constants.User_login_out);
                    ManagePasswordActivity.this.finish();
                    Intent intent = new Intent(ManagePasswordActivity.this, LoginActivity.class);
                    ManagePasswordActivity.this.startActivity(intent);
                }
                else
                {
                    ToastUtil.makeText(ManagePasswordActivity.this, managePasswordResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(ManagePasswordActivity.this);
            }
        }
    }
}
