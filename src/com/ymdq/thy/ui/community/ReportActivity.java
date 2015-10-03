package com.ymdq.thy.ui.community;

import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.ReportResponse;
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
 * <社区话题举报>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2015年3月25日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ReportActivity extends BaseActivity
{
    private String articleId = "";
    
    private LinearLayout back;
    
    private RadioGroup leftRadioGroup;
    
    private RadioGroup rightRadioGroup;
    
    private Button submit;
    
    private int checked_id = -1;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        intiView();
        initListener();
    }
    
    private void intiView()
    {
        articleId = getIntent().getStringExtra("article_id");
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("举报");
        leftRadioGroup = (RadioGroup)findViewById(R.id.left_radio_group);
        rightRadioGroup = (RadioGroup)findViewById(R.id.right_radio_group);
        submit = (Button)findViewById(R.id.report_submit);
    }
    
    private void initListener()
    {
        back.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        leftRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checked_id != checkedId)
                {
                    resetCheckedId(checkedId);
                    checked_id = checkedId;
                }
                
            }
        });
        rightRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checked_id != checkedId)
                {
                    resetCheckedId(checkedId);
                    checked_id = checkedId;
                }
            }
        });
        submit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                checkReport();
            }
        });
    }
    
    private void resetCheckedId(int new_checked_id)
    {
        if (checked_id < 0 || checked_id == new_checked_id)
        {
            RadioButton btn0 = (RadioButton)findViewById(new_checked_id);
            btn0.setChecked(true);
            return;
        }
        RadioButton oldId = (RadioButton)findViewById(checked_id);
        oldId.setChecked(false);
        RadioButton newId = (RadioButton)findViewById(new_checked_id);
        newId.setChecked(true);
        
    }
    
    private void checkReport()
    {
        if (checked_id < 0)
        {
            Toast.makeText(ReportActivity.this, "请选择举报原因", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (checked_id)
        {
            case R.id.report_btn_0:
                reqReport("0");
                break;
            case R.id.report_btn_1:
                reqReport("1");
                break;
            case R.id.report_btn_2:
                reqReport("2");
                break;
            case R.id.report_btn_3:
                reqReport("3");
                break;
            case R.id.report_btn_4:
                reqReport("4");
                break;
            case R.id.report_btn_5:
                reqReport("5");
                break;
            case R.id.report_btn_6:
                reqReport("6");
                break;
            default:
                break;
        }
    }
    
    private void reqReport(String type)
    {
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        HashMap<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("userId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("articleId", SecurityUtils.encode2Str(articleId));
            param.put("type", SecurityUtils.encode2Str(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(ReportActivity.this,
            param,
            this,
            ReportResponse.class,
            URLUtil.REPORT,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dailog != null)
            dailog.dismissDialog();
        if (ob instanceof ReportResponse)
        {
            ReportResponse reportResponse = (ReportResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(reportResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(reportResponse.getRetcode()))
                {
                    ToastUtil.makeText(ReportActivity.this, "举报成功");
                    ReportActivity.this.finish();
                }
                else
                {
                    ToastUtil.makeText(ReportActivity.this, reportResponse.getRetinfo());
                }
            }
            else
            {
                Toast.makeText(ReportActivity.this, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
