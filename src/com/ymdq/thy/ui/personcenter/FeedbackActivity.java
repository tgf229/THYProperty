package com.ymdq.thy.ui.personcenter;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.FeedbackResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <意见反馈界面>
 * <功能详细描述>
 * 
 * @author  wanghl
 * @version  [版本号, 2014年4月25日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FeedbackActivity extends BaseActivity
{
    /**
     * 加载框
     */
    private NetLoadingDailog dialog;
    
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    private EditText etPhone, etText;
    
    private Button btSumbit;
    
    private TextView tvWordLenth;
    
    private String phone, text;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
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
        tvTitle.setText("意见反馈");
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                FeedbackActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        etPhone = (EditText)findViewById(R.id.feedback_phonenum_et);
        etText = (EditText)findViewById(R.id.feedback_phonenum_text_et);
        btSumbit = (Button)findViewById(R.id.feedback_sumbit_bt);
        tvWordLenth = (TextView)findViewById(R.id.feedback_text_length_tv);
        dialog = new NetLoadingDailog(this);
    }
    
    private void addListener()
    {
        btSumbit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                sumbitData();
            }
        });
        
        etText.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                text = etText.getText().toString().trim();
                tvWordLenth.setText(text.length() + "/100");
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
    
    private void sumbitData()
    {
        phone = etPhone.getText().toString().trim();
        text = etText.getText().toString().trim();
        
        if (GeneralUtils.isNotNullOrZeroLenght(phone) && phone.length() > 20)
        {
            ToastUtil.makeText(this, "联系方式最多20位!");
            return;
        }
        if (GeneralUtils.isNullOrZeroLenght(text))
        {
            ToastUtil.makeText(this, "反馈内容不能为空，请重新输入!");
            return;
        }
        
        dialog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("type", Constants.clientType);
            param.put("version", GeneralUtils.getVersionName(FeedbackActivity.this));
            param.put("model", android.os.Build.MODEL);
            param.put("imei", GeneralUtils.getDeviceId(FeedbackActivity.this));
            if (GeneralUtils.isNotNullOrZeroLenght(phone))
            {
                param.put("contact", phone);
            }
            param.put("remark", text);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(FeedbackActivity.this,
            param,
            FeedbackActivity.this,
            FeedbackResponse.class,
            URLUtil.FEEDBACK,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dialog != null)
        {
            dialog.dismissDialog();
        }
        
        if (ob instanceof FeedbackResponse)
        {
            FeedbackResponse feedbackResponse = (FeedbackResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(feedbackResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(feedbackResponse.getRetcode()))
                {
                    ToastUtil.makeText(FeedbackActivity.this, "意见反馈提交成功");
                    FeedbackActivity.this.finish();
                    etPhone.setText("");
                    etText.setText("");
                }
                else
                {
                    ToastUtil.makeText(FeedbackActivity.this, feedbackResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(FeedbackActivity.this);
            }
        }
    }
}
