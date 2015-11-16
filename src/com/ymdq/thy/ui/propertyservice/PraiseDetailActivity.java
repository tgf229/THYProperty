/*
 * 文 件 名:  PraiseDetailActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-11-12
 
 */
package com.ymdq.thy.ui.propertyservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.GroupTopicsCommentsResponse;
import com.ymdq.thy.bean.propertyservice.PraiseCommentDoc;
import com.ymdq.thy.bean.propertyservice.PraiseCommentResponse;
import com.ymdq.thy.bean.propertyservice.PraiseListDoc;
import com.ymdq.thy.bean.propertyservice.PraiseListResponse;
import com.ymdq.thy.bean.propertyservice.PraiseTagDoc;
import com.ymdq.thy.bean.propertyservice.PraiseTagResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.community.CommunityTopicDetailsActivity;
import com.ymdq.thy.ui.community.adapter.CommentAdapter;
import com.ymdq.thy.ui.community.service.CommunityService;
import com.ymdq.thy.ui.personcenter.RegisterTwoActivity;
import com.ymdq.thy.ui.propertyservice.adapter.PraiseCommentAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-11-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PraiseDetailActivity extends BaseActivity implements OnHeaderRefreshListener, OnClickListener
{
    private LinearLayout back, commitTipsLayout, loadingMore, eTopLayout;
    
    private RelativeLayout itemLinear;
    
    private TextView title, commitTipsNum, eName, eNum, eDepName, submitTxt, eTopTxt;
    
    private ImageView eImg;
    
    private PullToRefreshView mPullToRefreshView;
    
    private ListView listView;
    
    private View loadingFooterView;
    
    private View headView;
    
    /**
     * 是否有更多消息
     */
    private boolean anyMore = true;
    
    /**
     * 记录滚动列表的状态，是否已刷新
     */
    private boolean isRefreshing = false;
    
    /**
     * 当前页
     */
    private int page = 1;
    
   private ArrayList<PraiseTagDoc> tagList;
    public HashMap<String, String> tagMap;
    
    private List<PraiseCommentDoc> mList;
    
    private PraiseCommentAdapter commentAdapter;
    
    private NetLoadingDailog dailog;
    
    private PraiseListDoc detail = null;
    
    private String time = null;
    
    private String myVoteTimes = null;
    
    private String queryTime = null;
    
    private int num = 10;
    
    /**
     * 保存当前页数
     */
    private int currentPage = 1;
    
    private boolean isVoteSuccess = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_praise_details);
        //获取列表透传数据
        detail = (PraiseListDoc)getIntent().getSerializableExtra("detail");
        time = getIntent().getStringExtra("time");
        myVoteTimes = getIntent().getStringExtra("myVoteTimes");
        if (GeneralUtils.isNull(detail) || GeneralUtils.isNullOrZeroLenght(time))
        {
            ToastUtil.makeText(PraiseDetailActivity.this, "很抱歉，无法进入详情页面");
            this.finish();
            return;
        }
        
        initView();
        initData();
    }
    
    private void initView()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        title.setBackgroundResource(R.drawable.title_xiangqing);
        itemLinear = (RelativeLayout)findViewById(R.id.item_linear);
        submitTxt = (TextView)findViewById(R.id.submit_txt);
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        listView = (ListView)findViewById(R.id.list_view);
        headView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.property_praise_detail_head,
                null);
        loadingFooterView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        listView.addFooterView(loadingFooterView);
        listView.addHeaderView(headView);
        loadingMore.setVisibility(View.GONE);
        
        eImg = (ImageView)headView.findViewById(R.id.img);
        eName = (TextView)headView.findViewById(R.id.name);
        eNum = (TextView)headView.findViewById(R.id.num);
        eDepName = (TextView)headView.findViewById(R.id.dep_name);
        eTopLayout = (LinearLayout)headView.findViewById(R.id.top_layout);
        eTopTxt = (TextView)headView.findViewById(R.id.top_num_txt);
        
        commitTipsLayout = (LinearLayout)headView.findViewById(R.id.commit_tips_layout);
        commitTipsNum = (TextView)headView.findViewById(R.id.commit_tips_num);
        showHead();
        
        back.setOnClickListener(this);
        submitTxt.setOnClickListener(this);
    }
    
    private void showHead()
    {
        ImageLoader.getInstance().displayImage(detail.geteImageUrl(),
            eImg,
            JRApplication.setAllDisplayImageOptions(this,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        if (GeneralUtils.isNotNullOrZeroLenght(detail.geteName()))
        {
            eName.setText(detail.geteName());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(detail.geteNum()))
        {
            eNum.setText("工号-" + detail.geteNum());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(detail.geteDepName()))
        {
            eDepName.setText("部门-" + detail.geteDepName());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(detail.getTop()) && !"0".equals(detail.getTop()))
        {
            eTopLayout.setVisibility(View.VISIBLE);
            eTopTxt.setText(detail.getTop() + "次");
        }
        if (GeneralUtils.isNullOrZeroLenght(myVoteTimes) || "0".equals(myVoteTimes.trim()))
        {
            itemLinear.setVisibility(View.GONE);
        }
        if(GeneralUtils.isNotNullOrZeroLenght(detail.getIsPraise()) && "1".equals(detail.getIsPraise().trim()))
        {
            itemLinear.setVisibility(View.GONE);
        }
    }
    
    private void initData()
    {
        if (GeneralUtils.isNotNullOrZeroLenght(detail.getPraise()) && !"0".equals(detail.getPraise().trim()))
        {
            commitTipsLayout.setVisibility(View.VISIBLE);
            commitTipsNum.setText(detail.getPraise());
        }
        
        mList = new ArrayList<PraiseCommentDoc>();
        tagList = new ArrayList<PraiseTagDoc>();
        
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
                    reqCommentList();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
            }
        });
        commentAdapter = new PraiseCommentAdapter(mList, this);
        listView.setAdapter(commentAdapter);
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        reqTagList();
    }
    
    private void reqTagList()
    {
        Map<String, String> param = new HashMap<String, String>();
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            PraiseTagResponse.class,
            URLUtil.BUS_200701,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqCommentList()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("eId", detail.geteId());
            param.put("time", time);
            param.put("page", String.valueOf(page));
            param.put("num", num + "");
            if (GeneralUtils.isNotNullOrZeroLenght(queryTime) && page > 1)
            {
                param.put("queryTime", queryTime);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            PraiseCommentResponse.class,
            URLUtil.BUS_200801,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        super.netBack(ob);
        if (ob instanceof PraiseTagResponse)
        {
            PraiseTagResponse tagResponse = (PraiseTagResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(tagResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(tagResponse.getRetcode()))
                {
                    if (GeneralUtils.isNotNullOrZeroSize(tagResponse.getDoc()))
                    {
                        tagList.clear();
                        tagList.addAll(tagResponse.getDoc());
                        
                        tagMap = new HashMap<String, String>();
                        for (PraiseTagDoc tag : tagList)
                        {
                            tagMap.put(tag.gettId(), tag.gettName());
                        }
                        
                        reqCommentList();
                    }
                }
                else
                {
                    if (dailog != null)
                    {
                        dailog.dismissDialog();
                    }
                    ToastUtil.makeText(PraiseDetailActivity.this, tagResponse.getRetinfo());
                }
            }
            else
            {
                if (dailog != null)
                {
                    dailog.dismissDialog();
                }
                ToastUtil.showError(PraiseDetailActivity.this);
            }
        }
        if (ob instanceof PraiseCommentResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            loadingMore.setVisibility(View.GONE);
            mPullToRefreshView.onHeaderRefreshComplete();
            PraiseCommentResponse commentsResponse = (PraiseCommentResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(commentsResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(commentsResponse.getRetcode()))
                {
                    queryTime = commentsResponse.getQueryTime();
                    if (page == 1)
                    {
                        if (GeneralUtils.isNotNullOrZeroSize(commentsResponse.getDoc()))
                        {
                            mList.clear();
                            mList.addAll(commentsResponse.getDoc());
                            currentPage = page;
                            commentAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        isRefreshing = false;
                        //没有更多
                        if (commentsResponse.getDoc() == null || commentsResponse.getDoc().size() < 0)
                        {
                            page--;
                        }
                        else
                        {
                            mList.addAll(commentsResponse.getDoc());
                            currentPage = page;
                            commentAdapter.notifyDataSetChanged();
                        }
                    }
                    //是否有更多
                    if (commentsResponse.getDoc() == null || commentsResponse.getDoc().size() < num)
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
                        ToastUtil.makeText(PraiseDetailActivity.this, commentsResponse.getRetinfo());
                    }
                    else
                    {
                        page--;
                        isRefreshing = false;
                        ToastUtil.makeText(PraiseDetailActivity.this, commentsResponse.getRetinfo());
                    }
                }
            }
            else
            {
                if (page == 1)
                {
                    page = currentPage;
                    ToastUtil.showError(PraiseDetailActivity.this);
                }
                else
                {
                    page--;
                    isRefreshing = false;
                    ToastUtil.showError(PraiseDetailActivity.this);
                }
            }
        }
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 1;
        reqCommentList();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                if(isVoteSuccess)
                {
                    Intent data = new Intent();
                    data.putExtra("eId", detail.geteId());
                    setResult(Constants.Praise_vote_success, data);
                }
                this.finish();
                break;
            case R.id.submit_txt:
                Intent intent = new Intent(this, PraiseVoteActivity.class);
                intent.putExtra("detail", detail);
                intent.putExtra("tags", tagList);
                startActivityForResult(intent, Constants.Praise_vote_success);
                break;
            default:
                break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(isVoteSuccess)
            {
                Intent data = new Intent();
                data.putExtra("eId", detail.geteId());
                setResult(Constants.Praise_vote_success, data);
            }
            this.finish();
        }
        return false;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Praise_vote_success && resultCode == Constants.Praise_vote_success)
        {
            isVoteSuccess = true;
            page = 1;
            reqCommentList();
            itemLinear.setVisibility(View.GONE);
        }
    }
}
