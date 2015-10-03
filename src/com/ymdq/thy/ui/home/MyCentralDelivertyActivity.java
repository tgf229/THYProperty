package com.ymdq.thy.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.MyCentralDelivertyDoc;
import com.ymdq.thy.bean.home.MyCentralDelivertyResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.home.adapter.MyDelivertyAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;

/**
 * 
 * <快递信息>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月26日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyCentralDelivertyActivity extends BaseActivity implements OnClickListener ,UICallBack
{
    /**
     * 显示加载进度的view，当listview向下滚动的时候显示此view
     */
    private View loadingFooterView;   
    
    /**
     * 加载布局
     */
    private LinearLayout loadingMore;
    
    /**
     * 是否有更多消息
     */
    private boolean anyMore = true;
    
    /**
     * 记录滚动列表的状态，是否已刷新
     */
    private boolean isRefreshing = false;
    
    private ListView mListView;
    
    private List<MyCentralDelivertyDoc> mList;
    
    private MyDelivertyAdapter adapter;
    
    /**
     * 当前的页面数
     */
    private int page = 1;
    
    /**
     * 每页显示的条数
     */
    private int num = 10;
    
    /**
     * 加载页面
     */
    private LinearLayout loadingLayout;
    
    /**
     * 请求失败、或没有数据时的显示页面
     */
    private LinearLayout clickrefreshLayout;
    
    /**
     * 请求失败、或没有数据时的显示文字
     */
    private TextView clickTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_my_central_lists);
        
        initView();
        initData();
        reqServer();
    }

    private void initView()
    {
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setText(R.string.delivery_message);
        
        mListView = (ListView)findViewById(R.id.list_view);
        mList = new ArrayList<MyCentralDelivertyDoc>();
        adapter = new MyDelivertyAdapter(this,mList);
        mListView.setVisibility(View.GONE);
        loadingFooterView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        mListView.addFooterView(loadingFooterView);
        
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        clickrefreshLayout = (LinearLayout)findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
//        clickrefreshLayout.setOnClickListener(this);
    }
    
    private void initData()
    {
        mListView.setOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && anyMore && !isRefreshing
                    && view.getLastVisiblePosition() == view.getCount() - 1)
                {
                    loadingMore.setVisibility(View.VISIBLE);
                    isRefreshing = true;
                    page++;
                    reqServer();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                
            }
        });

        mListView.setAdapter(adapter);
    }
    
    /**
     * 
     * <请求邮包列表接口>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqServer()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("type", SecurityUtils.encode2Str(Constants.DELIVERTY_ALL));
            param.put("page", SecurityUtils.encode2Str(page+""));
            param.put("num", SecurityUtils.encode2Str(num+""));
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(this, param, this, 
            MyCentralDelivertyResponse.class, 
            URLUtil.MY_DELIVERTY, 
            Constants.ENCRYPT_SIMPLE);
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
            default:
                break;
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        loadingLayout.setVisibility(View.GONE);
        loadingMore.setVisibility(View.GONE);
        if(ob != null)
        {
            if(ob instanceof MyCentralDelivertyResponse)
            {
                MyCentralDelivertyResponse resp = (MyCentralDelivertyResponse)ob;
                if(GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if(Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        List<MyCentralDelivertyDoc> list = resp.getDoc();
                        if(page == 1)
                        {
                            if(list != null && list.size() > 0)
                            {
                                mListView.setVisibility(View.VISIBLE);
                                clickrefreshLayout.setVisibility(View.GONE);
                                
                                isRefreshing = false;
                                if(list.size() == num)
                                {
                                    anyMore = true;
                                }
                                else
                                {
                                    anyMore = false;
                                }
                                
                                mList.addAll(list);
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                mListView.setVisibility(View.GONE);
                                clickrefreshLayout.setVisibility(View.VISIBLE);
                                clickTextView.setText("暂无快递信息");
                            }
                        }
                        else
                        {
                            if(list != null && list.size() > 0)
                            {
                                mListView.setVisibility(View.VISIBLE);
                                clickrefreshLayout.setVisibility(View.GONE);
                                
                                isRefreshing = false;
                                if(list.size() == num)
                                {
                                    anyMore = true;
                                }
                                else
                                {
                                    anyMore = false;
                                }
                                
                                mList.addAll(list);
                                adapter.notifyDataSetChanged();
                            } 
                        }
                    }
                    else
                    {
                        mListView.setVisibility(View.GONE);
                        clickrefreshLayout.setVisibility(View.VISIBLE);
                        clickTextView.setText(resp.getRetinfo());
                    }
                }
                else
                {
                    mListView.setVisibility(View.GONE);
                    clickrefreshLayout.setVisibility(View.VISIBLE);
                    clickTextView.setText(Constants.ERROR_MESSAGE);
                }
            }
        }
    }
}
