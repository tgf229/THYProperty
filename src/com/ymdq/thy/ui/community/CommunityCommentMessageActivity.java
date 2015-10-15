package com.ymdq.thy.ui.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.CommunityCommentMessageResponse;
import com.ymdq.thy.bean.community.Message;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.database.CommunityMessageDAO;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.community.adapter.CommunityCommentMessageAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 
 * <评论消息列表页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityCommentMessageActivity extends BaseActivity implements UICallBack, OnClickListener,
    OnHeaderRefreshListener
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
     * 上下拉刷新
     */
    private PullToRefreshView mPullToRefreshView;
    
    /**
     * listview
     */
    private ListView listView;
    
    /**
     * 显示加载进度的view，当listview向下滚动的时候显示此view
     */
    private View loadingFooterView;
    
    /**
     * 数据加载框
     */
    private LinearLayout loadingLayout;
    
    /**
     * 加载布局
     */
    private LinearLayout loadingMore;
    
    /**
     * 加载失败显示界面
     */
    private LinearLayout loadingFailedLayout;
    
    /**
     * 每页展示条数
     */
    private int num = 10;
    
    /**
     * 当前页
     */
    private int page = 0;
    
    /**
     * 是否有更多消息
     */
    private boolean anyMore = true;
    
    /**
     * 记录滚动列表的状态，是否已刷新
     */
    private boolean isRefreshing = false;
    
    /**
     * 请求失败展示信息
     */
    private TextView errorMessage;
    
    private CommunityCommentMessageAdapter adapter;
    
    /**
     * 消息列表
     */
    private List<Message> messages = new ArrayList<Message>();
    
    /**
     * 中间处理结果
     */
    private List<Message> middle = new ArrayList<Message>();
    
    /**
     * 数据库实体
     */
    private CommunityMessageDAO messageDao;
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(android.os.Message msg)
        {
            super.handleMessage(msg);
            if (middle.size() > 0)
            {
                anyMore = true;
                messages.addAll(middle);
                adapter.notifyDataSetChanged();
            }
            else
            {
                anyMore = false;
                page--;
            }
            loadingMore.setVisibility(View.GONE);
            isRefreshing = false;
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_circle_list);
        
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
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.storehome_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingFailedLayout = (LinearLayout)findViewById(R.id.loading_failed);
        listView = (ListView)findViewById(R.id.list_view);
        loadingFooterView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        errorMessage = (TextView)findViewById(R.id.loading_failed_txt);
        listView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        
        /**
         * 添加按钮点击事件
         */
        back.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
//        title.setText("评论消息");
        title.setBackgroundResource(R.drawable.title_pinglunxiaoxi);
        messageDao = new CommunityMessageDAO(this);
        listView.setOnScrollListener(new OnScrollListener()
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
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            middle.clear();
                            middle = messageDao.queryMessages(num * page, num);
                            handler.sendMessage(handler.obtainMessage());
                        }
                    }).start();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(CommunityCommentMessageActivity.this, CommunityTopicDetailsActivity.class);
                intent.putExtra("id", messages.get(position).getArticleId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        adapter = new CommunityCommentMessageAdapter(messages, CommunityCommentMessageActivity.this);
        listView.setAdapter(adapter);
        queryMessageList();
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 0;
        queryMessageList();
    }
    
    /**
     * 
     * <社区列表查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryMessageList()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            if (Global.isLogin())
            {
                param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            }
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(CommunityCommentMessageActivity.this,
            param,
            CommunityCommentMessageActivity.this,
            CommunityCommentMessageResponse.class,
            URLUtil.COMMUNITY_COMMENT_MESSAGE,
            Constants.ENCRYPT_SIMPLE);
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
            default:
                break;
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        super.netBack(ob);
        /**
         * 查询圈子列表
         */
        if (ob instanceof CommunityCommentMessageResponse)
        {
            loadingLayout.setVisibility(View.GONE);
            mPullToRefreshView.onHeaderRefreshComplete();
            CommunityCommentMessageResponse messageResponse = (CommunityCommentMessageResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(messageResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(messageResponse.getRetcode()))
                {
                    if (GeneralUtils.isNotNullOrZeroSize(messageResponse.getDoc()))
                    {
                        messageDao.saveMessage(messageResponse.getDoc());
                    }
                }
            }
            addData();
        }
    }
    
    /**
     * 
     * <重新刷新界面>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void addData()
    {
        messages.clear();
        messages.addAll(messageDao.queryMessages(num * page, num));
        if (messages == null || messages.size() < num)
        {
            anyMore = false;
        }
        else
        {
            anyMore = true;
        }
        adapter.notifyDataSetChanged();
        if (GeneralUtils.isNullOrZeroSize(messages))
        {
            loadingFailedLayout.setVisibility(View.VISIBLE);
            mPullToRefreshView.setVisibility(View.GONE);
            errorMessage.setText("暂无评论消息");
        }
        else
        {
            mPullToRefreshView.setVisibility(View.VISIBLE);
        }
        Intent intent = new Intent(Constants.COMMUNITY_MESSAGE_NUMBER_BROADCAST);
        intent.putExtra("number", "0");
        sendBroadcast(intent);
    }
}
