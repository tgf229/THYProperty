package com.ymdq.thy.ui.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Group;
import com.ymdq.thy.bean.community.GroupListResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.ui.community.adapter.CommunityCircleAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.GifView;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 
 * <社区动态页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityMyAddedFragment extends BaseFragment implements UICallBack, OnHeaderRefreshListener
{
    private View view;
    
    /**
     * 标题
     */
    private RelativeLayout title;
    
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
     * 列表查询类型
     */
    private String groupType;
    
    private CommunityCircleAdapter adapter;
    
    /**
     * 圈子列表
     */
    private List<Group> groups = new ArrayList<Group>();
    
    /**
     * 查询id
     */
    public String queryUId;
    
    private GifView gif1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.community_circle_list, null);
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
        title = (RelativeLayout)view.findViewById(R.id.title);
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.storehome_main_pull_refresh_view);
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
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        errorMessage = (TextView)view.findViewById(R.id.loading_failed_txt);
        listView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        title.setVisibility(view.GONE);
        groupType = Constants.MY_JOINED_COMMUNITY;
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
                    queryCommunityList();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                
            }
        });
        adapter = new CommunityCircleAdapter(groups, getActivity(), groupType);
        listView.setAdapter(adapter);
        queryCommunityList();
    }
    
    /**
     * 
     * <分类显示>
     * <功能详细描述>
     * @param view
     * @param groupType
     * @see [类、类#方法、类#成员]
     */
    private void loadTitle(TextView view, String groupType, String exist)
    {
        if (Constants.OFFICIAL_RECOMMENDATION.equals(groupType))
        {
            view.setText(exist + "官方推荐");
        }
        else if (Constants.POPULAR_COMMUNITY.equals(groupType))
        {
            view.setText(exist + "热门圈子");
        }
        else if (Constants.MY_JOINED_COMMUNITY.equals(groupType))
        {
            if (Global.getUserId().equals(queryUId))
            {
                view.setText(exist + "我加入的圈子");
            }
            else
            {
                view.setText(exist + "他加入的圈子");
            }
        }
        else
        {
            if (Global.getUserId().equals(queryUId))
            {
                view.setText(exist + "我管理的圈子");
            }
            else
            {
                view.setText(exist + "他管理的圈子");
            }
        }
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 1;
        queryCommunityList();
    }
    
    /**
     * 
     * <社区列表查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryCommunityList()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("type", groupType);
        param.put("uId", queryUId);
        param.put("page", page + "");
        param.put("num", num + "");
        param.put("cId", Global.getCId());
        
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            CommunityMyAddedFragment.this,
            GroupListResponse.class,
            URLUtil.COMMUNITY_LIST_QUERY,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <刷新界面>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void refresh()
    {
        mPullToRefreshView.headerRefreshing();
    }
    
    @Override
    public void netBack(Object ob)
    {
        super.netBack(ob);
        /**
         * 查询圈子列表
         */
        if (ob instanceof GroupListResponse)
        {
            gif1.setPaused(true);
            loadingLayout.setVisibility(View.GONE);
            mPullToRefreshView.onHeaderRefreshComplete();
            loadingMore.setVisibility(View.GONE);
            GroupListResponse groupListResponse = (GroupListResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(groupListResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(groupListResponse.getRetcode()))
                {
                    mPullToRefreshView.setVisibility(View.VISIBLE);
                    if (page == 1)
                    {
                        if (GeneralUtils.isNotNullOrZeroSize(groupListResponse.getDoc()))
                        {
                            groups.clear();
                            groups.addAll(groupListResponse.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
                            mPullToRefreshView.setVisibility(View.GONE);
                            loadTitle(errorMessage, groupType, "暂无");
                        }
                    }
                    else
                    {
                        isRefreshing = false;
                        //没有更多
                        if (groupListResponse.getDoc() == null || groupListResponse.getDoc().size() < 0)
                        {
                            page--;
                        }
                        else
                        {
                            groups.addAll(groupListResponse.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                        }
                    }
                    //是否有更多
                    if (groupListResponse.getDoc() == null || groupListResponse.getDoc().size() < num)
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
                        if (groups == null || groups.size() == 0)
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
                            mPullToRefreshView.setVisibility(View.GONE);
                            errorMessage.setText(groupListResponse.getRetinfo());
                        }
                        else
                        {
                            ToastUtil.makeText(getActivity(), groupListResponse.getRetinfo());
                        }
                    }
                    else
                    {
                        page--;
                        isRefreshing = false;
                        ToastUtil.makeText(getActivity(), groupListResponse.getRetinfo());
                    }
                }
            }
            else
            {
                if (page == 1)
                {
                    page = currentPage;
                    if (groups == null || groups.size() == 0)
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
}
