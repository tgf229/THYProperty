package com.ymdq.thy.ui.community;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.GroupPersonInfoResponse;
import com.ymdq.thy.bean.personcenter.UpdatePersonInfoResponse;
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
 * <社区个人信息页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityPersonInfoEditActivity extends BaseActivity implements OnClickListener
{
    /**
     * 返回按钮
     */
    private LinearLayout back;
    
    /**
     * 标题
     */
    private TextView title;
    
    /**
     * 确认
     */
    private TextView confirm;
    
    /**
     * 确认布局
     */
    private LinearLayout confirmLayout;
    
    /**
     * 社区名称
     */
    private EditText name;
    
    /**
     * 社区公告
     */
    private EditText sign;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 昵称
     */
    private String personName;
    
    /**
     * 签名
     */
    private String personDesc;
    
    /**
     * 个人信息
     */
    private GroupPersonInfoResponse groupPersonInfoResponse;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_edit_person_info);
        
        init();
        initData();
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        confirm = (TextView)findViewById(R.id.title_btn_call);
        confirmLayout = (LinearLayout)findViewById(R.id.title_call_layout);
        name = (EditText)findViewById(R.id.name);
        sign = (EditText)findViewById(R.id.sign);
        
        /**
         * 添加按钮点击事件
         */
        back.setOnClickListener(this);
        confirmLayout.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        title.setText("个人设置");
        confirm.setText("确认");
        confirm.setTextSize(16);
        confirm.setTextColor(getResources().getColorStateList(R.color.bb474d));
        groupPersonInfoResponse = (GroupPersonInfoResponse)getIntent().getSerializableExtra("person");
        if (groupPersonInfoResponse != null)
        {
            name.setText(groupPersonInfoResponse.getNickName());
            sign.setText(groupPersonInfoResponse.getDesc());
            name.setSelection(name.getText().toString().length());
            sign.setSelection(sign.getText().toString().length());
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        /**
         * 响应返回按钮
         */
            case R.id.title_back_layout:
                finish();
                break;
            /**
             * 响应确认按钮
             */
            case R.id.title_call_layout:
                personName = name.getText().toString().trim();
                personDesc = sign.getText().toString().trim();
                if (GeneralUtils.isNullOrZeroLenght(personName))
                {
                    ToastUtil.makeText(this, "昵称不能为空,请输入昵称");
                }
                else
                {
                    editPersonInfo(personName, personDesc);
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <编辑社区>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void editPersonInfo(String name, String desc)
    {
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("nickName", SecurityUtils.encode2Str(name));
            param.put("desc", SecurityUtils.encode2Str(desc));
            param.put("flag", SecurityUtils.encode2Str(Constants.PERSON_INFO_EDIT_GROUP));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            UpdatePersonInfoResponse.class,
            URLUtil.USER_UPDATA_INFO,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        super.netBack(ob);
        if (null != dailog)
        {
            dailog.dismissDialog();
        }
        /**
         * 创建社区
         */
        if (ob instanceof UpdatePersonInfoResponse)
        {
            UpdatePersonInfoResponse res = (UpdatePersonInfoResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(res.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(res.getRetcode()))
                {
                    Intent intent = new Intent();
                    intent.putExtra("personName", personName);
                    intent.putExtra("personDesc", personDesc);
                    Global.saveNickName(personName);
                    ToastUtil.makeText(this, "个人信息修改成功！");
                    setResult(Constants.NUM1, intent);
                    finish();
                }
                else
                {
                    ToastUtil.makeText(this, res.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(this);
            }
        }
    }
}
