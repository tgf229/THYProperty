/*
 * 文 件 名:  PraiseVoteActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-11-13
 
 */
package com.ymdq.thy.ui.propertyservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.personcenter.FeedbackResponse;
import com.ymdq.thy.bean.personcenter.HouseInfoResponse;
import com.ymdq.thy.bean.propertyservice.PraiseListDoc;
import com.ymdq.thy.bean.propertyservice.PraiseTagDoc;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.personcenter.FeedbackActivity;
import com.ymdq.thy.ui.propertyservice.adapter.PraiseTagAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.MyGridView;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PraiseVoteActivity extends BaseActivity implements OnClickListener
{
    private ImageView headImg;
    
    private TextView nameTxt, contentTipsTxt;
    
    private MyGridView tagGrid;
    
    private EditText contentTxt;
    
    private PraiseListDoc detail;
    
    private ArrayList<PraiseTagDoc> tags;
    
    private PraiseTagAdapter tagAdapter;
    private NetLoadingDailog dialog;
    public Map<String,String> tagIds = new HashMap<String,String>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_praise_vote);
        //获取详情透传数据
        detail = (PraiseListDoc)getIntent().getSerializableExtra("detail");
        tags = (ArrayList<PraiseTagDoc>)getIntent().getSerializableExtra("tags");
        if (GeneralUtils.isNull(detail) || GeneralUtils.isNullOrZeroSize(tags))
        {
            ToastUtil.makeText(PraiseVoteActivity.this, "很抱歉，无法进入该页面");
            this.finish();
            return;
        }
        
        
        initView();
        
    }
    
    private void initView()
    {
        headImg = (ImageView)findViewById(R.id.img);
        nameTxt = (TextView)findViewById(R.id.name);
        tagGrid = (MyGridView)findViewById(R.id.tag_grid);
        contentTxt = (EditText)findViewById(R.id.content);
        contentTipsTxt = (TextView)findViewById(R.id.content_length_tips);
        
        LinearLayout titleBarBack = (LinearLayout)findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setBackgroundResource(R.drawable.title_biaoyang);
        LinearLayout titleBarRight = (LinearLayout)findViewById(R.id.title_call_layout);
        titleBarRight.setOnClickListener(this);
        TextView titleBarTextV = (TextView)findViewById(R.id.title_btn_call);
        titleBarTextV.setBackgroundResource(R.drawable.title_red_tijiao);
        
        ImageLoader.getInstance().displayImage(detail.geteImageUrl(),
            headImg,
            JRApplication.setAllDisplayImageOptions(this,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        nameTxt.setText(detail.geteName());
        
        contentTxt.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                contentTipsTxt.setText(contentTxt.length() + "/200");
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
        
        
        for(int i=tags.size()-1; i>=0; i--)
        {
            if("0".equals(tags.get(i).gettStatus()))
            {
                tags.remove(i);
            }
        }
        
        tagAdapter = new PraiseTagAdapter(PraiseVoteActivity.this, tags, this);
        tagGrid.setAdapter(tagAdapter);
        dialog = new NetLoadingDailog(this);
    }
    
    private void reqSubmit()
    {
        String text = contentTxt.getText().toString().trim();
        
        if(tagIds.size()<1)
        {
            ToastUtil.makeText(this, "请给Ta选择至少一个标签再提交吧！");
            return;
        }
        
        StringBuffer sb = new StringBuffer();
        for(String ids: tagIds.keySet())
        {
            sb.append(ids+",");
        }
        
        String tags = sb.toString();
        
        dialog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("eId", SecurityUtils.encode2Str(detail.geteId()));
            param.put("tag", SecurityUtils.encode2Str(tags.substring(0, tags.length()-1)));
            if(GeneralUtils.isNotNullOrZeroLenght(text))
            {
                param.put("content", SecurityUtils.encode2Str(text));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            BaseResponse.class,
            URLUtil.BUS_200601,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dialog != null)
        {
            dialog.dismissDialog();
        }
        
        if (ob instanceof BaseResponse)
        {
            BaseResponse res = (BaseResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(res.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(res.getRetcode()))
                {
                    ToastUtil.makeText(PraiseVoteActivity.this, "表扬成功");
                    contentTxt.setText("");
                    setResult(Constants.Praise_vote_success);
                    PraiseVoteActivity.this.finish();
                }
                else
                {
                    ToastUtil.makeText(PraiseVoteActivity.this, res.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(PraiseVoteActivity.this);
            }
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                this.finish();
                break;
            case R.id.title_call_layout:
                reqSubmit();
                break;
            default:
                break;
        }
    }
}
