package com.ymdq.thy.ui.personcenter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.LoginResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.HomeFragmentActivity;
import com.ymdq.thy.util.CMLog;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <登陆页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-4]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoginActivity extends BaseActivity implements OnClickListener
{
    
    /**
     * 登陆,注册,游客按钮
     */
    private Button btLogin, btRegister, btTourist;
    
    /**
     * 用户名,密码输入框
     */
    private EditText etUserName, etPassword;
    
    /**
     * 忘记密码
     */
    private TextView tvForgetPass;
    
    /**
     * 用户名,密码
     */
    private String userName, password;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    private boolean isAuto;
    
    /**
     * 判断登录界面是否来自首页
     */
    private String fromHomePage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        fromHomePage = getIntent().getStringExtra("from_homepage");
        
        btLogin = (Button)findViewById(R.id.person_login_bt);
        btRegister = (Button)findViewById(R.id.person_login_register_bt);
        btTourist = (Button)findViewById(R.id.person_login_tourist_bt);
        
        etUserName = (EditText)findViewById(R.id.person_login_username_et);
        etPassword = (EditText)findViewById(R.id.person_login_password_et);
        
        tvForgetPass = (TextView)findViewById(R.id.person_login_forget_password_tv);
        tvForgetPass.setText(Html.fromHtml("<u><b>忘记密码</b></u>" + "?"));
        
        /**
         * 添加按钮点击事件
         */
        btLogin.setOnClickListener(this);
        btRegister.setOnClickListener(this);
        btTourist.setOnClickListener(this);
        tvForgetPass.setOnClickListener(this);
        
        dailog = new NetLoadingDailog(this);
        
        etUserName.setText(Global.getUserName());
        etPassword.setText(Global.getPassword());
        if (GeneralUtils.isNotNullOrZeroLenght(Global.getUserName())
            && GeneralUtils.isNotNullOrZeroLenght(Global.getPassword()))
        {
            isAuto = true;
            if (GeneralUtils.isNotNullOrZeroLenght(fromHomePage))
            {
                Intent intentHome = new Intent(this, HomeFragmentActivity.class);
                startActivity(intentHome);
                LoginActivity.this.finish();
            }
        }
        else
        {
            isAuto = false;
        }
        etUserName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                userName = etUserName.getText().toString().trim();
                if (!userName.equals(Global.getUserName()))
                {
                    isAuto = false;
                    etPassword.setText("");
                }
                else
                {
                    isAuto = true;
                    etPassword.setText(Global.getPassword());
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                
            }
            
            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
        etPassword.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void afterTextChanged(Editable s)
            {
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                isAuto = false;
            }
            
        });
    }
    
    /**
     * 登录
     */
    private void sumbitLoginData()
    {
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        
        if (GeneralUtils.isNullOrZeroLenght(userName))
        {
            ToastUtil.makeText(this, "请输入手机号码");
            return;
        }
        
        if (GeneralUtils.isNullOrZeroLenght(password))
        {
            ToastUtil.makeText(this, "请输入密码");
            return;
        }
        
        if (!GeneralUtils.IsPassword(password) && !isAuto)
        {
            ToastUtil.makeText(this, "密码为6至20位数字、字母、下划线组成!");
            return;
        }
        
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("username", userName);
            if (isAuto)
            {
                param.put("pwd", password);
            }
            else
            {
                param.put("pwd", SecurityUtils.get32MD5Str(password));
            }
            param.put("type", Constants.clientType);
            param.put("version", GeneralUtils.getVersionName(LoginActivity.this));
            param.put("imei", GeneralUtils.getDeviceId(LoginActivity.this));
            param.put("cId", Global.getUCId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(LoginActivity.this,
            param,
            LoginActivity.this,
            LoginResponse.class,
            URLUtil.USER_LOGIN,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.person_login_bt:
                sumbitLoginData();
                break;
            
            case R.id.person_login_register_bt:
                Intent intentRegister = new Intent(LoginActivity.this, RegisterOneActivity.class);
                LoginActivity.this.startActivityForResult(intentRegister, 0);
                break;
            case R.id.person_login_tourist_bt:
                Constants.TOURIST = true;
                if (GeneralUtils.isNotNullOrZeroLenght(fromHomePage))
                {
                    Intent intentTourist = new Intent(LoginActivity.this, HomeFragmentActivity.class);
                    LoginActivity.this.startActivity(intentTourist);
                    LoginActivity.this.finish();
                }
                else
                {
                    LoginActivity.this.finish();
                }
                break;
            case R.id.person_login_forget_password_tv:
                Intent intentForget = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                LoginActivity.this.startActivityForResult(intentForget, 1);
                break;
            
            default:
                break;
        }
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Constants.Register_one_code:
                boolean loginSuccess = data.getBooleanExtra("login_successed", false);
                if (loginSuccess)
                {
                    if (GeneralUtils.isNotNullOrZeroLenght(fromHomePage))
                    {
                        Intent intentTourist = new Intent(LoginActivity.this, HomeFragmentActivity.class);
                        LoginActivity.this.startActivity(intentTourist);
                    }
                    else
                    {
                        setResult(Constants.Person_center_login_code);
                    }
                    LoginActivity.this.finish();
                }
                
                break;
            
            case Constants.Forget_password_code:
                etUserName.setText(Global.getUserName());
                etPassword.setText(Global.getPassword());
                break;
            
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void netBack(Object ob)
    {
        /**
         * 登陆
         */
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        
        if (ob instanceof LoginResponse)
        {
            LoginResponse loginResponse = (LoginResponse)ob;
            
            if (GeneralUtils.isNotNullOrZeroLenght(loginResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(loginResponse.getRetcode()))
                {
                    Constants.TOURIST = false;
                    Global.saveData(loginResponse, userName, password, isAuto);
                    
                    if (GeneralUtils.isNotNullOrZeroLenght(fromHomePage))
                    {
                        Intent intentTourist = new Intent(LoginActivity.this, HomeFragmentActivity.class);
                        LoginActivity.this.startActivity(intentTourist);
                    }
                    else
                    {
                        setResult(Constants.Person_center_login_code);
                    }
                    
                    //JPUSH 添加别名
                    Global.setAliasApp(LoginActivity.this, "U_" + loginResponse.getuId());
                    
                    LoginActivity.this.finish();
                }
                else
                {
                    ToastUtil.makeText(LoginActivity.this, loginResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(LoginActivity.this);
            }
            
        }
    }
    
}
