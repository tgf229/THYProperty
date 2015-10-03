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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Group;
import com.ymdq.thy.bean.community.GroupListResponse;
import com.ymdq.thy.bean.community.TranslateTopicResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.community.adapter.TranslateTopicAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <移动话题>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2015年3月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MoveTopicActivity extends BaseActivity implements OnItemClickListener,OnClickListener
{
    private ListView mListView;
    
    private TextView trans_text;
    
    private View trans_view_top;
    
    private View trans_view_bottom;
    
    private TranslateTopicAdapter adapter;
    
    private String fromId;
    
    private String articleId;
    
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
    
    private LinearLayout loadingLayout;
    
    private LinearLayout addLayout;
    
    private TextView submit;
    
    private LinearLayout loadingFailedLayout;
    
    /**
     * 加载布局
     */
    private LinearLayout loadingMore;   
    
    /**
     * 显示加载进度的view，当listview向下滚动的时候显示此view
     */
    private View loadingFooterView;
    
    /**
     * 圈子列表
     */
    private ArrayList<Group> groups = new ArrayList<Group>();
    
    /**
     * 保存点击转移到的社区
     */
    private Group transEntity;
    
    private NetLoadingDailog loadingDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_topic);
        fromId = getIntent().getStringExtra("from_id");
        articleId = getIntent().getStringExtra("article_id");
        initView();
        queryCommunityList();
    }
    
    private void initView()
    {
        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("移动");
        LinearLayout back = (LinearLayout)findViewById(R.id.title_back_layout);
        loadingLayout = (LinearLayout)findViewById(R.id.loading);
        addLayout = (LinearLayout)findViewById(R.id.title_call_layout);
        submit = (TextView)findViewById(R.id.title_btn_call);
        submit.setText("确定");
        submit.setTextSize(15);
        submit.setTextColor(getResources().getColorStateList(R.color.selector_color_community_post));
        loadingFailedLayout = (LinearLayout)findViewById(R.id.loading_failed);
        mListView = (ListView)findViewById(R.id.list_view);
        loadingFooterView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        errorMessage = (TextView)findViewById(R.id.loading_failed_txt);
        mListView.addFooterView(loadingFooterView);
        loadingMore.setVisibility(View.GONE);
        adapter = new TranslateTopicAdapter(this,groups);
        mListView.setAdapter(adapter);
        View footView = new View(this);
        footView.setBackgroundResource(R.drawable.line_grey);
        footView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 1));
        mListView.addFooterView(footView);
        mListView.setOnItemClickListener(this);
        trans_text = (TextView)findViewById(R.id.trans_text);
        trans_view_top = findViewById(R.id.trans_view_top);
        trans_view_bottom = findViewById(R.id.trans_view_bottom);
        isVisible(View.GONE);
        
        initListScroll();
        back.setOnClickListener(this);
        addLayout.setOnClickListener(this);
        addLayout.setEnabled(false);
        loadingDialog = new NetLoadingDailog(this);
    }
    
    private void initListScroll()
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
                    queryCommunityList();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                // TODO Auto-generated method stub
                
            }
        });
    }
    
    /**
     * 
     * <社区列表查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryCommunityList()
    {
//        if (Global.isLogin())
//        {
            Map<String, String> param = new HashMap<String, String>();
            param.put("type", "5");
            param.put("uId", Global.getUserId());
            param.put("page", page + "");
            param.put("num", num + "");
            param.put("cId", Global.getCId());
            
            ConnectService.instance().connectServiceReturnResponse(MoveTopicActivity.this,
                param,
                MoveTopicActivity.this,
                GroupListResponse.class,
                URLUtil.COMMUNITY_LIST_QUERY,
                Constants.ENCRYPT_NONE);
//        }
    }
    
    private void reqTranslate()
    {
        loadingDialog.loading();
        HashMap<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("fromId", SecurityUtils.encode2Str(fromId));
            param.put("toId", SecurityUtils.encode2Str(transEntity.getId()));
            param.put("articleId", SecurityUtils.encode2Str(articleId));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(MoveTopicActivity.this, param, 
            this, TranslateTopicResponse.class, URLUtil.TRANSLATE_TOPIC, Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if(loadingDialog != null)
            loadingDialog.dismissDialog();
        if(ob instanceof GroupListResponse)
        {
            loadingLayout.setVisibility(View.GONE);
            loadingMore.setVisibility(View.GONE);
            GroupListResponse groupListResponse = (GroupListResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(groupListResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(groupListResponse.getRetcode()))
                {
                    loadingFailedLayout.setVisibility(View.GONE);
                    addLayout.setEnabled(true);
                    isVisible(View.VISIBLE);
                    if (page == 1)
                    {
                        if (GeneralUtils.isNotNullOrZeroSize(groupListResponse.getDoc()))
                        {
                            groups.clear();
                            List<Group> docList = groupListResponse.getDoc();
                            for(Group mGroup : docList)
                            {
                                if(!mGroup.getId().equals(fromId))
                                {
                                    groups.add(mGroup);
                                }
                            }
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            loadingFailedLayout.setVisibility(View.VISIBLE);
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
//                            groups.addAll(groupListResponse.getDoc());
                            List<Group> docList = groupListResponse.getDoc();
                            for(Group mGroup : docList)
                            {
                                if(!mGroup.getId().equals(fromId))
                                {
                                    groups.add(mGroup);
                                }
                            }
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
                            isVisible(View.GONE);
                            errorMessage.setText(groupListResponse.getRetinfo());
                        }
                        else
                        {
                            ToastUtil.makeText(MoveTopicActivity.this, groupListResponse.getRetinfo());
                        }
                    }
                    else
                    {
                        page--;
                        isRefreshing = false;
                        ToastUtil.makeText(MoveTopicActivity.this, groupListResponse.getRetinfo());
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
                        errorMessage.setText(Constants.ERROR_MESSAGE);
                    }
                    else
                    {
                        ToastUtil.showError(MoveTopicActivity.this);
                    }
                }
                else
                {
                    page--;
                    isRefreshing = false;
                    ToastUtil.showError(MoveTopicActivity.this);
                }
            }
            
        }
        else if(ob instanceof TranslateTopicResponse)
        {
            if(GeneralUtils.isNotNullOrZeroLenght(((TranslateTopicResponse)ob).getRetcode()))
            {
                if(Constants.SUCESS_CODE.equals(((TranslateTopicResponse)ob).getRetcode()))
                {
                    ToastUtil.makeText(MoveTopicActivity.this, "话题移动成功");
                    Intent intent = new Intent();
                    intent.putExtra("from_id", fromId);
                    intent.putExtra("article_id", articleId);
                    intent.putExtra("circleName", transEntity.getName());
                    setResult(Constants.move_topic,intent);
                    finish();
                }
                return;
            }
            Toast.makeText(MoveTopicActivity.this, Constants.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
        }
    }
    
    private void isVisible(int status)
    {
        trans_text.setVisibility(status);
        trans_view_top.setVisibility(status);
        trans_view_bottom.setVisibility(status);
        mListView.setVisibility(status);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        adapter.setPositon(position);
        adapter.notifyDataSetChanged();
        
        transEntity = (Group)adapter.getItem(position);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_call_layout:
                if(transEntity != null && GeneralUtils.isNotNullOrZeroLenght(transEntity.getId()))
                {
                    reqTranslate();
                    return;
                }
                Toast.makeText(MoveTopicActivity.this, "请选择要移动到的社区", Toast.LENGTH_SHORT).show();
                break;
            case R.id.title_back_layout:
                finish();
                break;
            default:
                break;
        }
    }
    
}
