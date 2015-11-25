package com.ymdq.thy.ui.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.JoinedGroupDynamicResponse;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.ui.community.adapter.CommunityDynamicAdapter;
import com.ymdq.thy.ui.community.adapter.CommunityListAdapter;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.GifView;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * <社区主页>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-11-24]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityFragment extends BaseFragment implements UICallBack, OnClickListener,
    OnHeaderRefreshListener
{
    
    private View view;
    
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
    private LinearLayout loadingLayout,back,loadingMore,loadingFailedLayout,rightLayout;
    /**
     * 结束标志
     */
    private RelativeLayout endTips;
    
    /**
     * 每页展示条数
     */
    private int num = 10;
    
    /**
     * 当前页
     */
    private int page = 1;
    
    /**
     * 保存当前页数
     */
    private int currentPage = 1;
    
    /**
     * 查询时间点
     */
    private String queryTime;
    
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
    private TextView errorMessage,title,rightTxt;
    
    /**
     * 动态适配器
     */
    private CommunityListAdapter adapter;
    
    /**
     * 话题列表
     */
    private List<Topic> mList = new ArrayList<Topic>();
    
    /**
     * 登陆成功广播
     */
    private LoginSuccessBroard loginBroardcast;
    
    private GifView gif1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.community_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        registreBroadcast();
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initView()
    {
        back = (LinearLayout)view.findViewById(R.id.title_back_layout);
        back.setVisibility(View.GONE);
        title = (TextView)view.findViewById(R.id.title_name);
        rightTxt = (TextView)view.findViewById(R.id.title_btn_call);
        rightLayout = (LinearLayout)view.findViewById(R.id.title_call_layout);
        
        title.setBackgroundResource(R.drawable.title_linli_big);
        rightTxt.setBackgroundResource(R.drawable.title_red_fahuati);
        
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        loadingLayout = (LinearLayout)view.findViewById(R.id.loading_layout);
        gif1 = (GifView)loadingLayout.findViewById(R.id.gif1);  
        // 设置背景gif图片资源  
        gif1.setMovieResource(R.raw.jiazai_gif);
        loadingFailedLayout = (LinearLayout)view.findViewById(R.id.loading_failed);
        listView = (ListView)view.findViewById(R.id.list_view);
        loadingFooterView =
            ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading,
                null);
        endTips = (RelativeLayout)loadingFooterView.findViewById(R.id.end_tips);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        errorMessage = (TextView)loadingFailedLayout.findViewById(R.id.loading_failed_txt);
        listView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        TextView view = new TextView(getActivity());
        view.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
        view.setHeight(DisplayUtil.dip2px(getActivity(), 0));
        view.setBackgroundColor(getResources().getColor(R.color.community_bg));
        listView.addHeaderView(view);
        
        /**
         * 添加按钮点击事件
         */
        loadingFailedLayout.setOnClickListener(this);
        rightLayout.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
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
                    reqList();
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
//                Intent intent = new Intent(getActivity(), CommunityTopicDetailsActivity.class);
//                intent.putExtra("id", topics.get(position - 1).getArticleId());
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });
        adapter = new CommunityListAdapter(mList, getActivity(), CommunityFragment.this);
        listView.setAdapter(adapter);
        reqList();
    }
    
    /**
     * 
     * <社区列表查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqList()
    {
        endTips.setVisibility(View.GONE);
        Map<String, String> param = new HashMap<String, String>();
        param.put("cId", Global.getCId());
        param.put("uId", Global.getUserId());
        param.put("page", page + "");
        param.put("num", num + "");
        if (page > 1)
        {
            param.put("queryTime", queryTime);
        }
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            CommunityFragment.this,
            JoinedGroupDynamicResponse.class,
            URLUtil.BUS_302301,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 1;
        reqList();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_call_layout:
                Intent intent = new Intent(getActivity(),CommunityPostTopicActivity.class);
                startActivityForResult(intent, Constants.NUM2);
                break;
        /**
         * 响应失败页面点击事件
         */
            case R.id.loading_failed:
                loadingFailedLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.VISIBLE);
                reqList();
                break;
            default:
                break;
        }
        
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (ob instanceof JoinedGroupDynamicResponse)
        {
            gif1.setPaused(true);
            loadingLayout.setVisibility(View.GONE);
            mPullToRefreshView.onHeaderRefreshComplete();
            loadingMore.setVisibility(View.GONE);
            JoinedGroupDynamicResponse response = (JoinedGroupDynamicResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(response.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(response.getRetcode()))
                {
                    mPullToRefreshView.setVisibility(View.VISIBLE);
                    queryTime = response.getQueryTime();
                    if (page == 1)
                    {
                        if (GeneralUtils.isNotNullOrZeroSize(response.getDoc()))
                        {
                            mList.clear();
                            mList.addAll(response.getDoc());
                            currentPage = page;
                            adapter = new CommunityListAdapter(mList, getActivity(), CommunityFragment.this);
                            listView.setAdapter(adapter);
                        }
                        else
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
                            mPullToRefreshView.setVisibility(View.GONE);
                            errorMessage.setText("暂未查询到信息");
                        }
                    }
                    else
                    {
                        isRefreshing = false;
                        //没有更多
                        if (response.getDoc() == null || response.getDoc().size() < 0)
                        {
                            page--;
                        }
                        else
                        {
                            mList.addAll(response.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                        }
                    }
                    //是否有更多
                    if (response.getDoc() == null || response.getDoc().size() < num)
                    {
                        anyMore = false;
                        endTips.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        anyMore = true;
                    }
                }
                else
                {
                    if (page == 1)
                    {
                        //请求失败后，page重置
                        page = currentPage;
                        //内容为空,才显示哭脸,有数据的情况下,加载失败,不覆盖原先的内容
                        if (GeneralUtils.isNullOrZeroSize(mList))
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
                            mPullToRefreshView.setVisibility(View.GONE);
                            errorMessage.setText(response.getRetinfo());
                        }
                        else
                        {
                            ToastUtil.makeText(getActivity(), response.getRetinfo());
                        }
                    }
                    else
                    {
                        page--;
                        isRefreshing = false;
                        ToastUtil.makeText(getActivity(), response.getRetinfo());
                    }
                }
            }
            else
            {
                if (page == 1)
                {
                    page = currentPage;
                    if (GeneralUtils.isNullOrZeroSize(mList))
                    {
                        loadingFailedLayout.setVisibility(View.VISIBLE);
                        mPullToRefreshView.setVisibility(View.GONE);
                        errorMessage.setText(Constants.ERROR_MESSAGE);
                    }
                    else
                    {
                        ToastUtil.showError(getActivity());
                    }
                }
                else
                {
                    page--;
                    isRefreshing = false;
                    ToastUtil.showError(getActivity());
                }
            }
        }
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(loginBroardcast);
    }
    
    /**
     * 
     * <话题分享广播注册>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void registreBroadcast()
    {
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(Constants.LOGIN_SUCCESS_BROADCAST);
        loginBroardcast = new LoginSuccessBroard();
        IntentFilter cellFilter = new IntentFilter();
        cellFilter.addAction(Constants.SELECT_NEW_COMMUNITY);
        getActivity().registerReceiver(loginBroardcast, loginFilter);
        getActivity().registerReceiver(loginBroardcast, cellFilter);
    }
    
    /**
     * 
     * <登陆成功刷新>
     * <功能详细描述>
     * 
     * @author  cyf
     * @version  [版本号, 2014-12-5]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    class LoginSuccessBroard extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //登录成功
            if (Constants.SELECT_NEW_COMMUNITY.equals(intent.getAction()))
            {
                mList.clear();
                adapter.notifyDataSetChanged();
                mPullToRefreshView.setVisibility(View.GONE);
                loadingFailedLayout.setVisibility(View.GONE);
                page = 1;
                currentPage = 1;
                loadingLayout.setVisibility(View.VISIBLE);
                reqList();
            }
        }
    }
    
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == Constants.NUM1 && resultCode == Constants.move_topic && data != null)
//        {
//            String fromId = data.getStringExtra("from_id");
//            String articleId = data.getStringExtra("article_id");
//            for (int i = 0; i < topics.size(); i++)
//            {
//                if (articleId.equals(topics.get(i).getArticleId()) && fromId.equals(topics.get(i).getId()))
//                {
//                    topics.remove(i);
//                    i--;
//                }
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }
}
