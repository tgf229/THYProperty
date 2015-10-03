package com.ymdq.thy.ui.personcenter;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.ForgetPasswordResponse;
import com.ymdq.thy.bean.personcenter.HouseCodeResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.ToastUtil;

public class ForgetPasswordActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 用户名,验证码
     */
    private EditText etUserName, etCode;
    
    private Button btSumbit, btCode;
    
    private ImageView ivDeletePhone;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 倒计时
     */
    private MyTime myTime;
    
    private String pNum;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);
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
                ForgetPasswordActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        etUserName = (EditText)findViewById(R.id.forget_password_phone_et);
        etCode = (EditText)findViewById(R.id.forget_password_code_et);
        btSumbit = (Button)findViewById(R.id.forget_password_sumbit_bt);
        btCode = (Button)findViewById(R.id.forget_password_code_bt);
        ivDeletePhone = (ImageView)findViewById(R.id.forget_password_phone_delete_iv);
        dailog = new NetLoadingDailog(this);
    }
    
    private void addListener()
    {
        
        btSumbit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                sureUserName();
            }
        });
        
        ivDeletePhone.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                etUserName.setText("");
            }
        });
        
        btCode.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                String phoneNum = etUserName.getText().toString().trim();
                if (GeneralUtils.isNullOrZeroLenght(phoneNum))
                {
                    ToastUtil.makeText(ForgetPasswordActivity.this, "请输入手机号码");
                    return;
                }
                
                if (!GeneralUtils.isTel(phoneNum))
                {
                    ToastUtil.makeText(ForgetPasswordActivity.this, "请输入正确格式的手机号码!");
                }
                else
                {
                    getCode(phoneNum);
                }
                
            }
        });
    }
    
    /**
     * 验证用户名
     */
    private void sureUserName()
    {
        pNum = etUserName.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(pNum))
        {
            ToastUtil.makeText(ForgetPasswordActivity.this, "请输入手机号码");
            return;
        }
        
        if (!GeneralUtils.isTel(pNum))
        {
            ToastUtil.makeText(ForgetPasswordActivity.this, "请输入正确格式的手机号码");
            return;
        }
        
        String code = etCode.getText().toString().trim();
        
        if (GeneralUtils.isNullOrZeroLenght(code))
        {
            ToastUtil.makeText(this, "请输入验证码");
            return;
        }
        
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("username", pNum);
        param.put("msgCode", code);
        
        ConnectService.instance().connectServiceReturnResponse(ForgetPasswordActivity.this,
            param,
            ForgetPasswordActivity.this,
            ForgetPasswordResponse.class,
            URLUtil.USER_SURE_USERNAME,
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
        param.put("type", Constants.CODE_FORGET_PASSWORD);
        ConnectService.instance().connectServiceReturnResponse(ForgetPasswordActivity.this,
            param,
            ForgetPasswordActivity.this,
            HouseCodeResponse.class,
            URLUtil.HOUSE_CODE,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        
        if (ob instanceof HouseCodeResponse)
        {
            HouseCodeResponse codeResponse = (HouseCodeResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(codeResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(codeResponse.getRetcode()))
                {
                    myTime = new MyTime(Constants.Countdown_start, Constants.Countdown_end);
                    myTime.start();
                    btCode.setClickable(false);
                    ToastUtil.makeText(ForgetPasswordActivity.this, codeResponse.getRetinfo());
                }
                else
                {
                    ToastUtil.makeText(ForgetPasswordActivity.this, codeResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(ForgetPasswordActivity.this);
            }
        }
        if (ob instanceof ForgetPasswordResponse)
        {
            ForgetPasswordResponse forgetPasswordResponse = (ForgetPasswordResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(forgetPasswordResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(forgetPasswordResponse.getRetcode()))
                {
                    Intent intent = new Intent(ForgetPasswordActivity.this, SetNewPasswordActivity.class);
                    intent.putExtra("old_username", pNum);
                    ForgetPasswordActivity.this.startActivityForResult(intent, 5);
                }
                else
                {
                    ToastUtil.makeText(ForgetPasswordActivity.this, forgetPasswordResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(ForgetPasswordActivity.this);
            }
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Constants.Forget_password_set_new:
                ForgetPasswordActivity.this.finish();
                break;
            
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
