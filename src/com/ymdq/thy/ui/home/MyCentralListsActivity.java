package com.ymdq.thy.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.MyCentralListsDoc;
import com.ymdq.thy.bean.home.MyCentralListsResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.home.adapter.MyCentralListsAdapter;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <便民消息>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月26日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyCentralListsActivity extends BaseActivity implements OnClickListener, UICallBack
{
    private ListView mListView;
    
    private MyCentralListsAdapter adapter;
    
    private List<MyCentralListsDoc> mlist;
    
    private LinearLayout loadingLayout;
    
    private LinearLayout clickrefreshLayout;
    
    private TextView clickTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_my_central_lists);
        initView();
        reqServer();
    }
    
    private void initView()
    {
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setText(R.string.convenience_of_information);
        
        mListView = (ListView)findViewById(R.id.list_view);
        mlist = new ArrayList<MyCentralListsDoc>();
        adapter = new MyCentralListsAdapter(this, mlist);
        mListView.setAdapter(adapter);
        mListView.setVisibility(View.GONE);
        
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        clickrefreshLayout = (LinearLayout)findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
//        clickrefreshLayout.setOnClickListener(this);
    }
    
    private void reqServer()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("cId", Global.getCId());
        ConnectService.instance().connectServiceReturnResponse(MyCentralListsActivity.this,
            param,
            this,
            MyCentralListsResponse.class,
            URLUtil.MY_CENTRAL_LISTS,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            /**
             * 响应失败页面点击事件
             */
//            case R.id.click_refresh_layout:
//                loadingLayout.setVisibility(View.VISIBLE);
//                clickrefreshLayout.setVisibility(View.GONE);
//                reqServer();
//                break;
            default:
                break;
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        loadingLayout.setVisibility(View.GONE);
        if (ob != null)
        {
            if (ob instanceof MyCentralListsResponse)
            {
                MyCentralListsResponse resp = (MyCentralListsResponse)ob;
                if (GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if (Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        List<MyCentralListsDoc> doc = resp.getDoc();
                        if (doc != null && doc.size() > 0)
                        {
                            mListView.setVisibility(View.VISIBLE);
                            clickrefreshLayout.setVisibility(View.GONE);
                            
                            mlist.addAll(doc);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            mListView.setVisibility(View.GONE);
                            clickrefreshLayout.setVisibility(View.VISIBLE);
                            clickTextView.setText("暂无便民消息");
                        }
                        return;
                    }
                }
            }
        }
        
        mListView.setVisibility(View.GONE);
        clickrefreshLayout.setVisibility(View.VISIBLE);
        clickTextView.setText("获取信息失败");
    }
}
