package com.ymdq.thy.ui.propertyservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.propertyservice.MyTicketDoc;
import com.ymdq.thy.bean.propertyservice.MyTicketResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.propertyservice.adapter.MyTicketAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 
 * <我的工单>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月25日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyTicketActivity extends BaseActivity implements OnClickListener,OnItemClickListener,
    OnHeaderRefreshListener
{
    /**
     * 上下拉刷新
     */
    private PullToRefreshView mPullToRefreshView;
    
    /**
     * 显示加载进度的view，当listview向下滚动的时候显示此view
     */
    private View loadingFooterView;   
    
    /**
     * 加载布局
     */
    private LinearLayout loadingMore;
    
    /**
     * 结束标志
     */
    private RelativeLayout endTips;
    
    /**
     * 是否有更多消息
     */
    private boolean anyMore = true;
    
    /**
     * 记录滚动列表的状态，是否已刷新
     */
    private boolean isRefreshing = false;
    
    private ListView mListView;
    
    private int page = 1;
    
    private int num = 10;
    
    private List<MyTicketDoc> mList;
    
    private MyTicketAdapter adapter;
    
    private LinearLayout loadingLayout;
    
    private LinearLayout clickrefreshLayout;
    
    private TextView clickTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_my_central_work_form);
        
        initView();
        initData();
        reqTicketList();
    }
    
    private void initView()
    {
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setBackgroundResource(R.drawable.title_wodegongdan);
        
        mListView = (ListView)findViewById(R.id.list_view);
        mList = new ArrayList<MyTicketDoc>();
        adapter = new MyTicketAdapter(this,mList);
        loadingFooterView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        endTips = (RelativeLayout)loadingFooterView.findViewById(R.id.end_tips);
        mListView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        
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
                    reqTicketList();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                
            }
        });

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }
    
    private void reqTicketList()
    {
        Map<String,String> param = new HashMap<String,String>();
        try
        {
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("page", SecurityUtils.encode2Str(page+""));
            param.put("num", SecurityUtils.encode2Str(num+""));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(this, param, this, 
            MyTicketResponse.class, URLUtil.MY_TICKET_LSIT, Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        MyTicketDoc ticket = (MyTicketDoc)adapter.getItem(position);
        
        Intent i = new Intent();
        i.setClass(this, MyTicketDetailActivity.class);
        i.putExtra("id", ticket.getId());
        startActivity(i);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //返回
            case R.id.title_back_layout:
                finish();
                break;
//            case R.id.click_refresh_layout:
//                loadingLayout.setVisibility(View.VISIBLE);
//                clickrefreshLayout.setVisibility(View.GONE);
//                reqTicketList();
//                break;
            default:
                break;
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        loadingLayout.setVisibility(View.GONE);
        loadingMore.setVisibility(View.GONE);
        mPullToRefreshView.onHeaderRefreshComplete();
        if(ob != null)
        {
            if(ob instanceof MyTicketResponse)
            {
                MyTicketResponse resp = (MyTicketResponse)ob;
                if(GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if(Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        List<MyTicketDoc> doc = resp.getDoc();
                        if(page == 1)
                        {
                            if(doc != null && doc.size() > 0)
                            {
                                mPullToRefreshView.setVisibility(View.VISIBLE);
                                clickrefreshLayout.setVisibility(View.GONE);
                                
                                isRefreshing = false;
                                if(doc.size() == num)
                                {
                                    anyMore = true;
                                    endTips.setVisibility(View.GONE);
                                }
                                else
                                {
                                    anyMore = false;
                                    endTips.setVisibility(View.VISIBLE);
                                }
                                mList.clear();
                                mList.addAll(doc);
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                mPullToRefreshView.setVisibility(View.GONE);
                                clickrefreshLayout.setVisibility(View.VISIBLE);
                                clickTextView.setText("暂无工单信息");
                            }
                        }
                        else
                        {

                            if(doc != null && doc.size() > 0)
                            {
                                mPullToRefreshView.setVisibility(View.VISIBLE);
                                clickrefreshLayout.setVisibility(View.GONE);
                                
                                isRefreshing = false;
                                if(doc.size() == num)
                                {
                                    anyMore = true;
                                    endTips.setVisibility(View.GONE);
                                }
                                else
                                {
                                    anyMore = false;
                                    endTips.setVisibility(View.VISIBLE);
                                }
                                
                                mList.addAll(doc);
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                endTips.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    else
                    {
                        mPullToRefreshView.setVisibility(View.GONE);
                        clickrefreshLayout.setVisibility(View.VISIBLE);
                        clickTextView.setText(resp.getRetinfo());
                    }
                    return;
                }

                mPullToRefreshView.setVisibility(View.GONE);
                clickrefreshLayout.setVisibility(View.VISIBLE);
                clickTextView.setText(Constants.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 1;
        reqTicketList();
    }
}
