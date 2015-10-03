package com.ymdq.thy.ui.personcenter;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseCodeResponse;
import com.ymdq.thy.bean.personcenter.LoginResponse;
import com.ymdq.thy.bean.personcenter.RegisterResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;

public class RegisterThreeActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 电话,验证码,昵称,密码,再次输入密码
     */
    private EditText etPhone, etCode, etNickName, etPassword, etPssAgain;
    
    private Button btCode, btSumbit;
    
    private String ownerPhone;
    
    private LinearLayout llCode;
    
    private ImageView ivDeletePhone;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 倒计时
     */
    private MyTime myTime;
    
    /**
     * 手机号
     */
    private String pNum;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 记录登陆成功还是失败
     */
    private boolean loginSuccess;
    
    /**
     * 房屋号
     */
    private String hId;
    
    private String register_cid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_three);
        initTitle();
        init();
        addListener();
        register_cid = getIntent().getStringExtra("register_cid");
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setText("注册");
        tvTitle.setTypeface(GeneralUtils.getFontFace(this));
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                RegisterThreeActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        ownerPhone = getIntent().getStringExtra("register_owner_phone");
        hId = getIntent().getStringExtra("register_owner_hid");
        etPhone = (EditText)findViewById(R.id.register_three_phone_et);
        etCode = (EditText)findViewById(R.id.register_three_code_et);
        etNickName = (EditText)findViewById(R.id.register_three_nickname_bt);
        etPassword = (EditText)findViewById(R.id.register_three_password_bt);
        etPssAgain = (EditText)findViewById(R.id.register_three_password_again_bt);
        llCode = (LinearLayout)findViewById(R.id.register_three_code_ll);
        if (GeneralUtils.isNotNullOrZeroLenght(ownerPhone))
        {
            etPhone.setText(ownerPhone);
            llCode.setVisibility(View.GONE);
        }
        
        btCode = (Button)findViewById(R.id.register_three_code_bt);
        btSumbit = (Button)findViewById(R.id.register_three_sumbit_bt);
        
        ivDeletePhone = (ImageView)findViewById(R.id.register_three_phone_delete_iv);
        
        dailog = new NetLoadingDailog(this);
    }
    
    private void addListener()
    {
        ivDeletePhone.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                etPhone.setText("");
            }
        });
        
        btSumbit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                sumbitRegister();
            }
        });
        
        btCode.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                String phoneNum = etPhone.getText().toString().trim();
                if (GeneralUtils.isNullOrZeroLenght(phoneNum))
                {
                    ToastUtil.makeText(RegisterThreeActivity.this, "请输入手机号码");
                    return;
                }
                
                if (!GeneralUtils.isTel(phoneNum))
                {
                    ToastUtil.makeText(RegisterThreeActivity.this, "请输入正确格式的手机号码!");
                }
                else
                {
                    getCode(phoneNum);
                }
            }
        });
        
        etPhone.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (GeneralUtils.isNotNullOrZeroLenght(ownerPhone))
                {
                    String ownPhone = etPhone.getText().toString().trim();
                    if (ownPhone.equals(ownerPhone))
                    {
                        llCode.setVisibility(View.GONE);
                    }
                    else
                    {
                        llCode.setVisibility(View.VISIBLE);
                    }
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
    }
    
    /**
     * 提交注册
     */
    private void sumbitRegister()
    {
        pNum = etPhone.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(pNum))
        {
            ToastUtil.makeText(RegisterThreeActivity.this, "请输入手机号码");
            return;
        }
        
        if (!GeneralUtils.isTel(pNum))
        {
            ToastUtil.makeText(RegisterThreeActivity.this, "请输入正确格式的手机号码");
            return;
        }
        
        String code = etCode.getText().toString().trim();
        if (llCode.getVisibility() == View.VISIBLE)
        {
            if (GeneralUtils.isNullOrZeroLenght(code))
            {
                ToastUtil.makeText(this, "请输入验证码");
                return;
            }
        }
        
        String nickName = etNickName.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(nickName))
        {
            ToastUtil.makeText(this, "请输入昵称");
            return;
        }
        
        password = etPassword.getText().toString().trim();
        String passAgain = etPssAgain.getText().toString().trim();
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
        param.put("phone", pNum);
        param.put("nickName", nickName);
        
        if (llCode.getVisibility() == View.VISIBLE)
        {
            param.put("msg", code);
        }
        param.put("pwd", SecurityUtils.get32MD5Str(password));
        param.put("version", GeneralUtils.getVersionName(RegisterThreeActivity.this));
        param.put("hId", hId);
        
        if (GeneralUtils.isNullOrZeroLenght(ownerPhone))
        {
            param.put("type", "2");
        }
        else
        {
            param.put("type", "1");
        }
        
        ConnectService.instance().connectServiceReturnResponse(RegisterThreeActivity.this,
            param,
            RegisterThreeActivity.this,
            RegisterResponse.class,
            URLUtil.USER_REGISTER,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <获取验证码>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void getCode(String phone)
    {
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("phone", phone);
        param.put("type", Constants.CODE_REGISTER);
        ConnectService.instance().connectServiceReturnResponse(RegisterThreeActivity.this,
            param,
            RegisterThreeActivity.this,
            HouseCodeResponse.class,
            URLUtil.HOUSE_CODE,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <自动登陆>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    /**
     * 登录
     */
    private void sumbitLoginData()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("username", pNum);
            param.put("pwd", SecurityUtils.get32MD5Str(password));
            param.put("type", Constants.clientType);
            param.put("version", GeneralUtils.getVersionName(RegisterThreeActivity.this));
            param.put("imei", GeneralUtils.getDeviceId(RegisterThreeActivity.this));
            param.put("cId", register_cid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(RegisterThreeActivity.this,
            param,
            RegisterThreeActivity.this,
            LoginResponse.class,
            URLUtil.USER_LOGIN,
            Constants.ENCRYPT_NONE);
        
    }
    
    @Override
    public void netBack(Object ob)
    {
        
        if (ob instanceof HouseCodeResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            
            HouseCodeResponse codeResponse = (HouseCodeResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(codeResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(codeResponse.getRetcode()))
                {
                    myTime = new MyTime(Constants.Countdown_start, Constants.Countdown_end);
                    myTime.start();
                    btCode.setClickable(false);
                    ToastUtil.makeText(RegisterThreeActivity.this, codeResponse.getRetinfo());
                }
                else
                {
                    ToastUtil.makeText(RegisterThreeActivity.this, codeResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(RegisterThreeActivity.this);
            }
        }
        
        if (ob instanceof RegisterResponse)
        {
            RegisterResponse registerResponse = (RegisterResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(registerResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(registerResponse.getRetcode()))
                {
                    Global.saveUCId(register_cid);
                    sumbitLoginData();
                }
                else
                {
                    if (dailog != null)
                    {
                        dailog.dismissDialog();
                    }
                    ToastUtil.makeText(RegisterThreeActivity.this, registerResponse.getRetinfo());
                }
            }
            else
            {
                if (dailog != null)
                {
                    dailog.dismissDialog();
                }
                ToastUtil.showError(RegisterThreeActivity.this);
            }
        }
        
        if (ob instanceof LoginResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            
            LoginResponse loginResponse = (LoginResponse)ob;
            
            if (GeneralUtils.isNotNullOrZeroLenght(loginResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(loginResponse.getRetcode()))
                {
                    loginSuccess = true;
                    Global.saveData(loginResponse, pNum, password, false);
                }
                else
                {
                    Global.saveUCId("");
                    ToastUtil.makeText(RegisterThreeActivity.this, loginResponse.getRetinfo());
                }
            }
            else
            {
                Global.saveUCId("");
                ToastUtil.showError(RegisterThreeActivity.this);
            }
            
            Intent data = new Intent();
            data.putExtra("login_successed", loginSuccess);
            setResult(Constants.Register_three_code, data);
            RegisterThreeActivity.this.finish();
        }
    }
    
    //倒计时
    private class MyTime extends CountDownTimer
    {
        
        public MyTime(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }
        
        @Override
        public void onFinish()
        {
            btCode.setText("获取验证码");
            btCode.setTextColor(getResources().getColor(R.color.white_color));
            btCode.setClickable(true);
            btCode.setBackgroundResource(R.drawable.register_two_code_sumbit_bg_selector);
        }
        
        @Override
        public void onTick(long millisUntilFinished)
        {
            btCode.setBackgroundResource(R.drawable.register_two_code_grey_bg);
            btCode.setTextColor(getResources().getColor(R.color.person_register_choise_title));
            btCode.setText("重新获取" + millisUntilFinished / 1000);
        }
        
    }
}
