package com.ymdq.thy.ui.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 
 * <我的话题页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityMyTopicFragment extends BaseFragment implements UICallBack, OnHeaderRefreshListener
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
    private TextView errorMessage;
    
    /**
     * 动态适配器
     */
    private CommunityDynamicAdapter adapter;
    
    /**
     * 话题列表
     */
    private List<Topic> topics = new ArrayList<Topic>();
    
    /**
     * 查询id
     */
    public String queryUId;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.community_my_topic_list, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
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
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.storehome_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        loadingLayout = (LinearLayout)view.findViewById(R.id.loading_layout);
        loadingFailedLayout = (LinearLayout)view.findViewById(R.id.loading_failed);
        listView = (ListView)view.findViewById(R.id.list_view);
        loadingFooterView =
            ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading,
                null);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        errorMessage = (TextView)view.findViewById(R.id.loading_failed_txt);
        listView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        TextView view = new TextView(getActivity());
        view.setWidth(getActivity().getWindowManager().getDefaultDisplay().getWidth());
        view.setHeight(DisplayUtil.dip2px(getActivity(), 0));
        view.setBackgroundColor(getResources().getColor(R.color.community_bg));
        listView.addHeaderView(view);
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
                    queryJoinedDynamicTopics();
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
                Intent intent = new Intent(getActivity(), CommunityTopicDetailsActivity.class);
                intent.putExtra("id", topics.get(position - 1).getArticleId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        adapter = new CommunityDynamicAdapter(topics, getActivity(), CommunityMyTopicFragment.this);
        listView.setAdapter(adapter);
        adapter.setType(Constants.COMMUNITU_PERSON_CENTER);
        queryJoinedDynamicTopics();
    }
    
    /**
     * 
     * <社区列表查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryJoinedDynamicTopics()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("uId", Global.getUserId());
        }
        param.put("cId", Global.getCId());
        param.put("queryUId", queryUId);
        param.put("page", page + "");
        param.put("num", num + "");
        if (page > 1)
        {
            param.put("queryTime", queryTime);
        }
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            CommunityMyTopicFragment.this,
            JoinedGroupDynamicResponse.class,
            URLUtil.COMMUNITY_MY_TOPIC,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 1;
        queryJoinedDynamicTopics();
    }
    
    @Override
    public void netBack(Object ob)
    {
        /**
         * 查询圈子列表
         */
        if (ob instanceof JoinedGroupDynamicResponse)
        {
            loadingLayout.setVisibility(View.GONE);
            mPullToRefreshView.onHeaderRefreshComplete();
            loadingMore.setVisibility(View.GONE);
            JoinedGroupDynamicResponse dynamicResponse = (JoinedGroupDynamicResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(dynamicResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(dynamicResponse.getRetcode()))
                {
                    mPullToRefreshView.setVisibility(View.VISIBLE);
                    queryTime = dynamicResponse.getQueryTime();
                    if (page == 1)
                    {
                        if (GeneralUtils.isNotNullOrZeroSize(dynamicResponse.getDoc()))
                        {
                            topics.clear();
                            topics.addAll(dynamicResponse.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
                            mPullToRefreshView.setVisibility(View.GONE);
                            if (Global.getUserId().equals(queryUId))
                            {
                                errorMessage.setText("暂无我的话题");
                            }
                            else
                            {
                                errorMessage.setText("暂无他的话题");
                            }
                        }
                    }
                    else
                    {
                        isRefreshing = false;
                        //没有更多
                        if (dynamicResponse.getDoc() == null || dynamicResponse.getDoc().size() < 0)
                        {
                            page--;
                        }
                        else
                        {
                            topics.addAll(dynamicResponse.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                        }
                    }
                    //是否有更多
                    if (dynamicResponse.getDoc() == null || dynamicResponse.getDoc().size() < num)
                    {
                        anyMore = false;
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
                        if (topics == null || topics.size() == 0)
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
                            mPullToRefreshView.setVisibility(View.GONE);
                            errorMessage.setText(dynamicResponse.getRetinfo());
                        }
                        else
                        {
                            ToastUtil.makeText(getActivity(), dynamicResponse.getRetinfo());
                        }
                    }
                    else
                    {
                        page--;
                        isRefreshing = false;
                        ToastUtil.makeText(getActivity(), dynamicResponse.getRetinfo());
                    }
                }
            }
            else
            {
                if (page == 1)
                {
                    page = currentPage;
                    if (topics == null || topics.size() == 0)
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
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.NUM1 && resultCode == Constants.move_topic && data != null)
        {
            String fromId = data.getStringExtra("from_id");
            String articleId = data.getStringExtra("article_id");
            for (int i = 0; i < topics.size(); i++)
            {
                if (articleId.equals(topics.get(i).getArticleId()) && fromId.equals(topics.get(i).getId()))
                {
                    topics.remove(i);
                    i--;
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
