package com.ymdq.thy.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.MyCentralMeeageBean;
import com.ymdq.thy.bean.home.MyCentralMeeageDoc;
import com.ymdq.thy.bean.home.MyCentralMeeageResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.database.MycentralMessageDB;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.HomeFragmentActivity;
import com.ymdq.thy.ui.home.adapter.MyCentralMeeageAdapter;
import com.ymdq.thy.ui.personcenter.LoginActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;

/**
 * 
 * <我的消息>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyCentralMeeageActivity extends BaseActivity implements OnClickListener
{
    private ListView mListView;
    
    private List<MyCentralMeeageBean> mList;
    
    private MyCentralMeeageAdapter adapter;
    
    private LinearLayout loadingLayout;
    
    private LinearLayout clickrefreshLayout;
    
    private TextView clickTextView;
    
    private boolean fromJpush, fromJpushNoti;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_my_central_message);
        
        initView();
    }
    
    private void initView()
    {
        fromJpush = getIntent().getBooleanExtra("from_jpush", false);
        fromJpushNoti = getIntent().getBooleanExtra("from_jpush_noti", false);//判断是否来自消息推送
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setText(R.string.my_message);
        
        mListView = (ListView)findViewById(R.id.list_view);
        mList = new ArrayList<MyCentralMeeageBean>();
        adapter = new MyCentralMeeageAdapter(this, mList);
        mListView.setAdapter(adapter);
        mListView.setVisibility(View.GONE);
        
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        clickrefreshLayout = (LinearLayout)findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
        //        clickrefreshLayout.setOnClickListener(this);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        initData();
    }
    
    private void initData()
    {
        String messageInfo = getIntent().getStringExtra("messageInfo");
        if(messageInfo != null)
        {
            if(Constants.FAILED_CODE_END.equals(messageInfo))
            {
                UITips(Constants.ERROR_MESSAGE);
            }
            else if(Constants.NO_BIND_HOUSE.equals(messageInfo))
            {
                UITips("亲爱的用户，您未绑定任何房屋，无法操作");
            }
            else if(Constants.SUCESS_CODE_END.equals(messageInfo))
            {
                queryMessages();
            }
        }
        else
        {
            reqMyMessage();
        }
    }

    private void UITips(String content)
    {
        mListView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        clickrefreshLayout.setVisibility(View.VISIBLE);
        clickTextView.setText(content);
    }

    /**
     * 
     * <查询我的消息>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryMessages()
    {
        List<MyCentralMeeageBean> allList =
            MycentralMessageDB.getInstance(this).queryALlDb(Global.getUserId(), Global.getCId());
        if (allList != null && allList.size() > 0)
        {
            mListView.setVisibility(View.VISIBLE);
            mList.clear();
            mList.addAll(allList);
            adapter.notifyDataSetChanged();
            
            loadingLayout.setVisibility(View.GONE);
            clickrefreshLayout.setVisibility(View.GONE);
        }
        else
        {
            mListView.setVisibility(View.GONE);
            loadingLayout.setVisibility(View.GONE);
            clickrefreshLayout.setVisibility(View.VISIBLE);
            clickTextView.setText("暂无任何消息");
        }
    }
    
    /**
     * 
     * <我的消息查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqMyMessage()
    {
        if (Global.isLogin())
        {
            Map<String, String> param = new HashMap<String, String>();
            try
            {
                param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
                param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            ConnectService.instance().connectServiceReturnResponse(MyCentralMeeageActivity.this,
                param,
                this,
                MyCentralMeeageResponse.class,
                URLUtil.MY_MESSAGE,
                Constants.ENCRYPT_SIMPLE);
        }
        else
        {
            Intent i = new Intent(MyCentralMeeageActivity.this, LoginActivity.class);
            startActivityForResult(i, 0);
            finish();
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Constants.Person_center_login_code:
                reqMyMessage();
                break;
            
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                back();

                MyCentralMeeageActivity.this.finish();
                break;
            /**
             * 响应失败页面点击事件
             */
            case R.id.click_refresh_layout:
                loadingLayout.setVisibility(View.VISIBLE);
                clickrefreshLayout.setVisibility(View.GONE);
                initData();
            default:
                break;
        }
    }

    /**
     * 
     * <返回时，修改数据库，如果是从消息进入app，需要返回到首页>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void back()
    {
        MycentralMessageDB.getInstance(this).updateDbIsRead(Global.getUserId(), Global.getCId());
        if (fromJpush)
        {
            Intent intentTourist = new Intent(MyCentralMeeageActivity.this, HomeFragmentActivity.class);
            MyCentralMeeageActivity.this.startActivity(intentTourist);
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
      //我的消息查询
        if (ob instanceof MyCentralMeeageResponse)
        {
            MyCentralMeeageResponse messageResp = (MyCentralMeeageResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(messageResp.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(messageResp.getRetcode()))
                {
                    List<MyCentralMeeageDoc> docList = messageResp.getDoc();
                    if (docList != null && docList.size() > 0)
                    {
                        MycentralMessageDB.getInstance(this).insertDb(Global.getUserId(),
                            Global.getCId(),
                            docList);
                    }
                    queryMessages();
                }
                else
                {
                    UITips(messageResp.getRetinfo());
                }
            }
            else
            {
                UITips(Constants.ERROR_MESSAGE);
            }
        }
    }
    
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        back();
    }
}
