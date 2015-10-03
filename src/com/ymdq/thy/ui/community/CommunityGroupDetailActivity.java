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
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.GroupAddOrQuitResponse;
import com.ymdq.thy.bean.community.GroupDetailInfoResponse;
import com.ymdq.thy.bean.community.JoinedGroupDynamicResponse;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.bean.community.TopipDeleteResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.community.adapter.CommunityDynamicAdapter;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 
 * <圈子详情界面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-7-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityGroupDetailActivity extends BaseActivity implements OnHeaderRefreshListener, OnClickListener,
    UICallBack
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
     * 发表话题
     */
    private TextView postTopic;
    
    /**
     * 注册点击
     */
    private LinearLayout postTopicLayout;
    
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
     * 加载布局
     */
    private LinearLayout loadingMore;
    
    /**
     * listview头
     */
    private View headView;
    
    /**
     * 头像
     */
    private ImageView head;
    
    /**
     * 名称
     */
    private TextView name;
    
    /**
     * 关注数量
     */
    private TextView userCount;
    
    /**
     * 发帖数量
     */
    private TextView articleCount;
    
    /**
     * 关注
     */
    private Button add;
    
    /**
     * 查看成员
     */
    private Button viewMember;
    
    /**
     * 编辑
     */
    private LinearLayout edit;
    
    /**
     * 公告
     */
    private TextView notice;
    
    /**
     * 动态适配器
     */
    private CommunityDynamicAdapter adapter;
    
    /**
     * 话题列表
     */
    private List<Topic> topics = new ArrayList<Topic>();
    
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
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 圈子Id
     */
    private String circleId;
    
    /**
     * 圈子名称
     */
    private String circleName;
    
    /**
     * 是否是第一次请求圈子基本信息
     */
    private boolean isFirstInfo = true;
    
    /**
     * 请求失败展示信息
     */
    private TextView errorMessage;
    
    /**
     * 结束标志
     */
    private RelativeLayout endTips;
    
    /**
     * 加载失败显示界面
     */
    private LinearLayout loadingFailedLayout;
    
    /**
     * 加入/退出
     */
    private String type;
    
    /**
     * 圈子基本信息
     */
    private GroupDetailInfoResponse groupDetailInfoResponse;
    
    /**
     * 登陆成功广播
     */
    private LoginSuccessBroard loginBroardcast;
    
    /**
     * 是否在当前界面登录
     */
    private boolean isLogin = false;
    
    /**
     * 是否关注或取消关注成功
     */
    private boolean isAddedOrCancel = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_group_detail);
        
        init();
        initData();
        registreBroadcast();
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
        postTopic = (TextView)findViewById(R.id.title_btn_call);
        postTopicLayout = (LinearLayout)findViewById(R.id.title_call_layout);
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.storehome_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        listView = (ListView)findViewById(R.id.list_view);
        headView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.community_group_detail_head_view,
                null);
        loadingFooterView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        endTips = (RelativeLayout)loadingFooterView.findViewById(R.id.end_tips);
        head = (ImageView)headView.findViewById(R.id.head_image);
        name = (TextView)headView.findViewById(R.id.name);
        userCount = (TextView)headView.findViewById(R.id.user_count);
        articleCount = (TextView)headView.findViewById(R.id.article_count);
        add = (Button)headView.findViewById(R.id.add);
        loadingFailedLayout = (LinearLayout)headView.findViewById(R.id.no_data);
        errorMessage = (TextView)headView.findViewById(R.id.loading_failed_txt);
        viewMember = (Button)headView.findViewById(R.id.view_member);
        notice = (TextView)headView.findViewById(R.id.notice);
        edit = (LinearLayout)headView.findViewById(R.id.edit);
        listView.addHeaderView(headView);
        listView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        
        /**
         * 添加按钮点击事件
         */
        add.setOnClickListener(this);
        viewMember.setOnClickListener(this);
        back.setOnClickListener(this);
        postTopicLayout.setOnClickListener(this);
        edit.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        circleId = getIntent().getStringExtra("circleId");
        circleName = getIntent().getStringExtra("circleName");
        title.setText(circleName);
        postTopic.setText("发话题");
        postTopic.setTextSize(15);
        postTopic.setTextColor(getResources().getColorStateList(R.color.selector_color_community_post));
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
                    queryGroupDetailDyanmic();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                
            }
        });
        adapter =
            new CommunityDynamicAdapter(topics, CommunityGroupDetailActivity.this, CommunityGroupDetailActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(CommunityGroupDetailActivity.this, CommunityTopicDetailsActivity.class);
                intent.putExtra("id", topics.get(position - 1).getArticleId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        queryGroupDetail(true);
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
        this.registerReceiver(loginBroardcast, loginFilter);
    }
    
    /**
     * 
     * <查询圈子基本信息>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryGroupDetail(boolean isFirst)
    {
        if (isFirst)
        {
            dailog = new NetLoadingDailog(this);
            dailog.loading();
        }
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
            param.put("uId", Global.getUserId());
        param.put("id", circleId);
        ConnectService.instance().connectServiceReturnResponse(CommunityGroupDetailActivity.this,
            param,
            CommunityGroupDetailActivity.this,
            GroupDetailInfoResponse.class,
            URLUtil.COMMUNITY_DETAILINFO_QUERY,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <圈子主页动态查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryGroupDetailDyanmic()
    {
        endTips.setVisibility(View.GONE);
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
            param.put("uId", Global.getUserId());
        param.put("id", circleId);
        param.put("page", page + "");
        param.put("num", num + "");
        if (page > 1)
        {
            param.put("queryTime", queryTime + "");
        }
        ConnectService.instance().connectServiceReturnResponse(CommunityGroupDetailActivity.this,
            param,
            CommunityGroupDetailActivity.this,
            JoinedGroupDynamicResponse.class,
            URLUtil.COMMUNITY_TOPIC_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <关注&&取消关注请求>
     * <功能详细描述>
     * @param type
     * @see [类、类#方法、类#成员]
     */
    private void addOrQuitGroup(String type)
    {
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("id", SecurityUtils.encode2Str(circleId));
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("type", SecurityUtils.encode2Str(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(CommunityGroupDetailActivity.this,
            param,
            CommunityGroupDetailActivity.this,
            GroupAddOrQuitResponse.class,
            URLUtil.COMMUNITY_ADD_QUIT,
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
                if (isAddedOrCancel)
                {
                    setResult(Constants.NUM3);
                }
                finish();
                break;
            /**
             * 响应发帖按钮
             */
            case R.id.title_call_layout:
                if (Global.isLogin())
                {
                    if (Constants.ADDED_GROUP.equals(type))
                    {
                        Intent intent = new Intent(CommunityGroupDetailActivity.this, CommunityPostTopicActivity.class);
                        intent.putExtra("circleId", circleId);
                        startActivityForResult(intent, Constants.NUM0);
                    }
                    else
                    {
                        ToastUtil.makeText(CommunityGroupDetailActivity.this, "您还未关注此圈子");
                    }
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(CommunityGroupDetailActivity.this);
                }
                break;
            /**
             * 响应关注按钮
             */
            case R.id.add:
                if (Global.isLogin())
                {
                    if (Constants.ADDED_GROUP.equals(type))
                    {
                        addOrQuitGroup(Constants.QUITED_GROUP);
                    }
                    else
                    {
                        addOrQuitGroup(Constants.ADDED_GROUP);
                    }
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(CommunityGroupDetailActivity.this);
                }
                break;
            /**
             * 响应查看成员按钮
             */
            case R.id.view_member:
                Intent intent = new Intent(CommunityGroupDetailActivity.this, CommunityMemberActivity.class);
                intent.putExtra("members", groupDetailInfoResponse.getMember());
                startActivity(intent);
                break;
            /**
             * 响应编辑按钮
             */
            case R.id.edit:
                Intent editIntent = new Intent(CommunityGroupDetailActivity.this, CommunityCreateOrEditActivity.class);
                editIntent.putExtra("groupDetailInfoResponse", groupDetailInfoResponse);
                editIntent.putExtra("type", Constants.EDIT_COMMUNITY);
                startActivityForResult(editIntent, Constants.NUM0);
                break;
            default:
                break;
        }
    }
    
    /**
     * {@inheritDoc}
     * 下拉刷新
     */
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 1;
        queryGroupDetail(false);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //发帖成功,编辑圈子刷新界面
        if (requestCode == Constants.NUM0)
        {
            if (resultCode == Constants.NUM2 || resultCode == Constants.NUM1)
            {
                refresh();
            }
        }
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
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * 
     * <刷新界面>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void refresh()
    {
        mPullToRefreshView.headerRefreshing();
    }
    
    @Override
    public void netBack(Object ob)
    {
        /**
         * 圈子基本信息查询 
         */
        if (ob instanceof GroupDetailInfoResponse)
        {
            groupDetailInfoResponse = (GroupDetailInfoResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(groupDetailInfoResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(groupDetailInfoResponse.getRetcode()))
                {
                    isFirstInfo = false;
                    showGroupDetail(groupDetailInfoResponse);
                    if (!isLogin)
                        queryGroupDetailDyanmic();
                    else
                    {
                        isLogin = false;
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    if (dailog != null)
                        dailog.dismissDialog();
                    mPullToRefreshView.onHeaderRefreshComplete();
                    if (isFirstInfo)
                    {
                        DialogUtil.showBtnFinishActivityDialog(CommunityGroupDetailActivity.this, "获取圈子基本信息失败");
                    }
                }
            }
            else
            {
                if (dailog != null)
                    dailog.dismissDialog();
                mPullToRefreshView.onHeaderRefreshComplete();
                if (isFirstInfo)
                {
                    DialogUtil.showBtnFinishActivityDialog(CommunityGroupDetailActivity.this, "获取圈子基本信息失败");
                }
            }
        }
        /**
         * 圈子主页动态查询
         */
        else if (ob instanceof JoinedGroupDynamicResponse)
        {
            if (dailog != null)
                dailog.dismissDialog();
            loadingMore.setVisibility(View.GONE);
            mPullToRefreshView.onHeaderRefreshComplete();
            JoinedGroupDynamicResponse groupHomeResponse = (JoinedGroupDynamicResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(groupHomeResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(groupHomeResponse.getRetcode()))
                {
                    queryTime = groupHomeResponse.getQueryTime();
                    if (page == 1)
                    {
                        if (GeneralUtils.isNotNullOrZeroSize(groupHomeResponse.getDoc()))
                        {
                            topics.clear();
                            topics.addAll(groupHomeResponse.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                            loadingFailedLayout.setVisibility(View.GONE);
                        }
                        else
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
                            errorMessage.setText("圈子里没有任何话题");
                        }
                    }
                    else
                    {
                        isRefreshing = false;
                        //没有更多
                        if (groupHomeResponse.getDoc() == null || groupHomeResponse.getDoc().size() == 0)
                        {
                            page--;
                        }
                        else
                        {
                            topics.addAll(groupHomeResponse.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                        }
                    } //是否有更多
                    if (groupHomeResponse.getDoc() == null || groupHomeResponse.getDoc().size() < num)
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
                        if (topics == null || topics.size() == 0)
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
                            errorMessage.setText(groupHomeResponse.getRetinfo());
                        }
                        else
                        {
                            ToastUtil.makeText(CommunityGroupDetailActivity.this, groupHomeResponse.getRetinfo());
                        }
                    }
                    else
                    {
                        page--;
                        isRefreshing = false;
                        ToastUtil.makeText(CommunityGroupDetailActivity.this, groupHomeResponse.getRetinfo());
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
                        errorMessage.setText(Constants.ERROR_MESSAGE);
                    }
                    else
                    {
                        ToastUtil.showError(CommunityGroupDetailActivity.this);
                    }
                }
                else
                {
                    page--;
                    isRefreshing = false;
                    ToastUtil.showError(CommunityGroupDetailActivity.this);
                }
            }
        }
        /**
        * 加入/退出圈子
        */
        else if (ob instanceof GroupAddOrQuitResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            GroupAddOrQuitResponse groupAddOrQuitResponse = (GroupAddOrQuitResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(groupAddOrQuitResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(groupAddOrQuitResponse.getRetcode()))
                {
                    isAddedOrCancel = !isAddedOrCancel;
                    if (Constants.ADDED_GROUP.equals(type))
                    {
                        type = Constants.QUITED_GROUP;
                        ToastUtil.makeText(CommunityGroupDetailActivity.this, "取消关注圈子成功");
                    }
                    else
                    {
                        type = Constants.ADDED_GROUP;
                        ToastUtil.makeText(CommunityGroupDetailActivity.this, "关注圈子成功");
                    }
                    showAddOrQuitButton(type);
                }
                else
                {
                    ToastUtil.makeText(CommunityGroupDetailActivity.this, groupAddOrQuitResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(CommunityGroupDetailActivity.this);
            }
        }
        /**
         * 删除
         */
        else if (ob instanceof TopipDeleteResponse)
        {
            if (adapter.getDailog() != null)
            {
                adapter.getDailog().dismissDialog();
            }
            TopipDeleteResponse topipDeleteResponse = (TopipDeleteResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(topipDeleteResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(topipDeleteResponse.getRetcode()))
                {
                    ToastUtil.makeText(CommunityGroupDetailActivity.this, "话题删除成功");
                    topics.remove(adapter.deletePosition);
                    adapter.notifyDataSetChanged();
                    articleCount.setText((Integer.parseInt(articleCount.getText().toString()) - 1) + "");
                }
                else
                {
                    ToastUtil.makeText(CommunityGroupDetailActivity.this, topipDeleteResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(CommunityGroupDetailActivity.this);
            }
        }
    }
    
    /**
     * 
     * <加入退出按钮>
     * <功能详细描述>
     * @param flag
     * @see [类、类#方法、类#成员]
     */
    private void showAddOrQuitButton(String flag)
    {
        if (Constants.ADDED_GROUP.equals(flag))
        {
            add.setText("取消关注");
            userCount.setText((Integer.parseInt(userCount.getText().toString()) + 1) + "");
        }
        else
        {
            add.setText("关注");
            userCount.setText((Integer.parseInt(userCount.getText().toString()) - 1) + "");
        }
    }
    
    /**
     * 
     * <展示头部信息>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showGroupDetail(GroupDetailInfoResponse groupDetailInfoResponse)
    {
        ImageLoader.getInstance().displayImage(groupDetailInfoResponse.getIcon(),
            head,
            JRApplication.setAllDisplayImageOptions(CommunityGroupDetailActivity.this,
                "community_default",
                "community_default",
                "community_default"));
        name.setText(groupDetailInfoResponse.getNickName());
        userCount.setText(groupDetailInfoResponse.getUserCount());
        articleCount.setText(groupDetailInfoResponse.getArticleCount());
        notice.setText(Html.fromHtml("<font color=#000000>公告: </font>" + "<font color=#60656b>"
            + (groupDetailInfoResponse.getDesc() == null ? "" : groupDetailInfoResponse.getDesc()) + "</font>"));
        type = groupDetailInfoResponse.getFlag();
        adapter.setType(Constants.COMMUNITY_DETAIL);
        adapter.setUserId(groupDetailInfoResponse.getuId());
        if (Global.isLogin())
        {
            //此圈子是本用户创建
            if (Global.getUserId().equals(groupDetailInfoResponse.getuId()))
            {
                adapter.setType(Constants.COMMUNITY_DETAIL_OWNER);
                add.setVisibility(View.GONE);
                viewMember.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }
            //不是本用户创建,但是加入了
            else if (Constants.ADDED_GROUP.equals(groupDetailInfoResponse.getFlag()))
            {
                add.setText("取消关注");
                add.setVisibility(View.VISIBLE);
                viewMember.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
            //不是本用户创建,未加入
            else
            {
                add.setText("关注");
                add.setVisibility(View.VISIBLE);
                viewMember.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
            
        }
        else
        {
            //未登录
            add.setVisibility(View.VISIBLE);
            viewMember.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            add.setText("关注");
        }
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(loginBroardcast);
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
            if (Constants.LOGIN_SUCCESS_BROADCAST.equals(intent.getAction()))
            {
                isLogin = true;
                queryGroupDetail(false);
            }
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (isAddedOrCancel)
            {
                setResult(Constants.NUM3);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
