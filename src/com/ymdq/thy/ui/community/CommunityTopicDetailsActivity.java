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
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView.BufferType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.GroupAddTopicCommentResponse;
import com.ymdq.thy.bean.community.GroupTopicsCommentsResponse;
import com.ymdq.thy.bean.community.Image;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.bean.community.TopicDetailInfo;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.ViewPagerActivity;
import com.ymdq.thy.ui.community.adapter.CommentAdapter;
import com.ymdq.thy.ui.community.service.CommunityService;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 
 * <社区话题页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityTopicDetailsActivity extends BaseActivity implements UICallBack, OnClickListener,
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
     * 加载布局
     */
    private LinearLayout loadingMore,commitTipsLayout;
    
    /**
     * listview头
     */
    private View headView;
    
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
     * 头像
     */
    private ImageView headImage;
    
    /**
     * 名称
     */
    private TextView name;
    
    /**
     * 时间
     */
    private TextView time;
    
    /**
     * 来自哪个圈子
     */
    private TextView come;
    
    /**
     * 内容
     */
    private TextView content;
    
    /**
     * 图片
     */
    private LinearLayout photo;
    
    /**
     * 是否支持赞成反对
     */
    private LinearLayout vote;
    
    /**
     * 赞成
     */
    private LinearLayout agree;
    
    /**
     * 反对
     */
    private LinearLayout disagree;
    
    /**
     * 赞成数量
     */
    private TextView yesNum;
    
    /**
     * 反对数量
     */
    private TextView noNum,commitTipsNum;
    
    /**
     * 赞成图片
     */
    private ImageView yes;
    
    /**
     * 返回图片
     */
    private ImageView no;
    
    /**
     * 发送块
     */
    private LinearLayout sendLinear;
    
    /**
     * 点赞,评论,其他
     */
    private LinearLayout itemLinear;
    
    /**
     * 点赞布局
     */
    private LinearLayout likeLayout;
    
    /**
     * 评价布局
     */
    private LinearLayout chatLayout,commentAdd;
    
    /**
     * 分享布局
     */
    private LinearLayout moreLayout;
    
    /**
     * 喜欢数量
     */
    private TextView like;
    
    /**
     * 评论数量
     */
    private TextView chat;
    
    /**
     * 点赞图标
     */
    private ImageView praise;
    
    /**
     * 大V
     */
    private ImageView vip;
    
    /**
     * 话题内容
     */
    private Topic topic;
    
    /**
     * 评论列表
     */
    private List<TopicDetailInfo> topics = new ArrayList<TopicDetailInfo>();
    
    /**
     * 评论列表适配器
     */
    private CommentAdapter adapter;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 发表评论对象
     */
    private TopicDetailInfo topicDetailInfo;
    
    /**
     * 输入发表内容
     */
    private EditText inputContent;
    
    /**
     * 发送按钮
     */
    private Button send;
    
    /**
     * 是否是第一次请求圈子基本信息
     */
    private boolean isFirstInfo = true;
    
    /**
     * 话题id
     */
    private String id;
    
    /**
     * 类型
     */
    private int type;
    
    /**
     *    图片放大动画
     */
    private Animation toLargeAnimation;
    
    /**
     * 登陆成功广播
     */
    private LoginSuccessBroard loginBroardcast;
    
    /**
     * 分享时候需要这个静态变量
     */
    public static Context mContext;
    
    /**
     * 被回复的用户ID
     */
    private String replyUId;
    
    /**
     * 被回复的评论的ID
     */
    private String commentId;
    
    /**
     * 被回复的用户昵称
     */
    private String replyNickName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_dynamic_topic_details);
        mContext = this;
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
        mPullToRefreshView = (PullToRefreshView)findViewById(R.id.storehome_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        listView = (ListView)findViewById(R.id.list_view);
        headView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.community_topic_detail,
                null);
        loadingFooterView =
            ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading, null);
        loadingMore = (LinearLayout)loadingFooterView.findViewById(R.id.loading_more);
        listView.addFooterView(loadingFooterView);
        listView.addHeaderView(headView);
        loadingMore.setVisibility(View.GONE);
        sendLinear = (LinearLayout)findViewById(R.id.send_linear);
        inputContent = (EditText)findViewById(R.id.input_content);
        send = (Button)findViewById(R.id.send);
        itemLinear = (LinearLayout)findViewById(R.id.item_linear);
        likeLayout = (LinearLayout)findViewById(R.id.community_like_layout);
        chatLayout = (LinearLayout)findViewById(R.id.community_chat_layout);
        commentAdd = (LinearLayout)findViewById(R.id.comment_add);
        moreLayout = (LinearLayout)findViewById(R.id.community_more_layout);
        like = (TextView)findViewById(R.id.community_like);
        chat = (TextView)findViewById(R.id.community_chat);
        praise = (ImageView)findViewById(R.id.praise);
        
        headImage = (ImageView)headView.findViewById(R.id.head);
        vip = (ImageView)headView.findViewById(R.id.icon_vip);
        name = (TextView)headView.findViewById(R.id.name);
        time = (TextView)headView.findViewById(R.id.time);
        come = (TextView)headView.findViewById(R.id.come_from);
        content = (TextView)headView.findViewById(R.id.content);
        photo = (LinearLayout)headView.findViewById(R.id.photo);
        vote = (LinearLayout)headView.findViewById(R.id.yes_or_no);
        agree = (LinearLayout)headView.findViewById(R.id.agree);
        disagree = (LinearLayout)headView.findViewById(R.id.disagree);
        yesNum = (TextView)headView.findViewById(R.id.yes_num);
        noNum = (TextView)headView.findViewById(R.id.no_num);
        yes = (ImageView)headView.findViewById(R.id.yes);
        no = (ImageView)headView.findViewById(R.id.no);
        commitTipsLayout = (LinearLayout)headView.findViewById(R.id.commit_tips_layout);
        commitTipsNum = (TextView)headView.findViewById(R.id.commit_tips_num);
        
        /**
         * 添加按钮点击事件
         */
        back.setOnClickListener(this);
        headImage.setOnClickListener(this);
        agree.setOnClickListener(this);
        disagree.setOnClickListener(this);
        likeLayout.setOnClickListener(this);
        chatLayout.setOnClickListener(this);
        commentAdd.setOnClickListener(this);
        moreLayout.setOnClickListener(this);
        send.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 0);
        if (Constants.NUM1 == type)
        {
            listView.removeHeaderView(headView);
            title.setText("评论");
        }
        else
        {
            title.setText("详情");
        }
        listView.setOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                if (sendLinear.getVisibility() == View.VISIBLE)
                {
                    hideSendLinear();
                }
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && anyMore && !isRefreshing
                    && view.getLastVisiblePosition() == view.getCount() - 1)
                {
                    loadingMore.setVisibility(View.VISIBLE);
                    isRefreshing = true;
                    page++;
                    queryCommunityCommentList();
                }
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
            }
        });
        adapter = new CommentAdapter(topics, this);
        listView.setAdapter(adapter);
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        CommunityService.instance().topicDetails(id, this, CommunityTopicDetailsActivity.this);
        
        headView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (sendLinear.getVisibility() == View.VISIBLE)
                {
                    hideSendLinear();
                }
            }
        });
        headView.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (sendLinear.getVisibility() == View.VISIBLE)
                {
                    hideSendLinear();
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int currentPosition = 0;
                if (Constants.NUM1 != type)
                {
                    currentPosition = position - 1;
                }
                else
                {
                    currentPosition = position;
                }
                if (sendLinear.getVisibility() == View.VISIBLE)
                {
                    hideSendLinear();
                }
                else
                {
                    //显示键盘
                    sendLinear.setVisibility(View.VISIBLE);
                    itemLinear.setVisibility(View.GONE);
                    inputContent.requestFocus();
                    showSoftInput();
                    inputContent.setHint("回复" + topics.get(currentPosition).getNickName());
                    replyUId = topics.get(currentPosition).getUserId();
                    commentId = topics.get(currentPosition).getCommentId();
                    replyNickName = topics.get(currentPosition).getNickName();
                }
            }
        });
    }
    
    /**
     * 
     * <隐藏发送布局>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void hideSendLinear()
    {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
        {
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
        sendLinear.setVisibility(View.GONE);
        itemLinear.setVisibility(View.VISIBLE);
        inputContent.setText("");
        inputContent.setHint(getString(R.string.comment_hint));
        replyUId = "";
        commentId = "";
        replyNickName = "";
    }
    
    /**
     * 隐藏软键盘
     * 
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showSoftInput()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        if (imm.isActive())
        {
            //如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }
    
    /**
     * 
     * <刷新界面>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void refreshView()
    {
        loadConstantView();
        loadChangeView();
    }
    
    /**
     * 
     * <加载内容不变的view>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void loadConstantView()
    {
        ImageLoader.getInstance().displayImage(topic.getImageUrl(),
            headImage,
            JRApplication.setRoundDisplayImageOptions(CommunityTopicDetailsActivity.this,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        if (Constants.LEVEL_VIP.equals(topic.getUserLevel()))
        {
            vip.setVisibility(View.VISIBLE);
        }
        else
        {
            vip.setVisibility(View.GONE);
        }
        name.setText(topic.getNickName());
        time.setText(topic.getTime());
        come.setText(topic.getName());
        //判断是否有内容
        if (GeneralUtils.isNotNullOrZeroLenght(topic.getComment()))
        {
            content.setVisibility(View.VISIBLE);
            content.setText(topic.getContent());
            content.setText(GeneralUtils.clickSpan(content,
                this,
                topic.getContent(),
                GeneralUtils.getWebUrl(topic.getContent())),
                BufferType.SPANNABLE);
        }
        else
        {
            content.setVisibility(View.GONE);
        }
        photo.removeAllViews();
        //判断是否有图片
        if (GeneralUtils.isNotNullOrZeroSize(topic.getImageList()))
        {
            photo.setVisibility(View.VISIBLE);
            LinearLayout photoLineOne = null;
            LinearLayout photoLineTwo = null;
            final ArrayList<String> photoUrls = new ArrayList<String>();
            for (Image image : topic.getImageList())
            {
                photoUrls.add(image.getImageUrlL());
            }
            for (int i = 0; i < topic.getImageList().size(); i++)
            {
                final int currentItem = i;
                if (i == 0)
                {
                    photoLineOne = new LinearLayout(CommunityTopicDetailsActivity.this);
                    photoLineOne.setPadding(0, 0, 0, DisplayUtil.dip2px(CommunityTopicDetailsActivity.this, 7));
                    photo.addView(photoLineOne);
                }
                if (i == 3)
                {
                    photoLineTwo = new LinearLayout(CommunityTopicDetailsActivity.this);
                    photo.addView(photoLineTwo);
                }
                ImageView pic = new ImageView(CommunityTopicDetailsActivity.this);
                pic.setImageResource(R.drawable.community_default);
                pic.setScaleType(ScaleType.CENTER_CROP);
                if (i < 3)
                {
                    photoLineOne.addView(pic);
                }
                else
                {
                    photoLineTwo.addView(pic);
                }
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)pic.getLayoutParams(); //取控件textView当前的布局参数  
                linearParams.width =
                    (CommunityTopicDetailsActivity.this.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(CommunityTopicDetailsActivity.this,
                        44)) / 3;
                linearParams.height = linearParams.width;
                if (i != 2 || i != 5)
                    linearParams.setMargins(0, 0, DisplayUtil.dip2px(CommunityTopicDetailsActivity.this, 7), 0);
                pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                ImageLoader.getInstance().displayImage(topic.getImageList().get(i).getImageUrlS(),
                    pic,
                    JRApplication.setAllDisplayImageOptions(CommunityTopicDetailsActivity.this,
                        "community_default",
                        "community_default",
                        "community_default"));
                pic.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        //普通话题
                        Intent intent = new Intent(CommunityTopicDetailsActivity.this, ViewPagerActivity.class);
                        intent.putExtra("currentItem", currentItem);
                        intent.putStringArrayListExtra("photoUrls", photoUrls);
                        startActivity(intent);
                    }
                });
            }
        }
        else
        {
            photo.setVisibility(View.GONE);
        }
    }
    
    /**
     * 
     * <加载内容变化的view>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void loadChangeView()
    {
        //未赞
        if (Constants.CANCEL_PRAISE.equals(topic.getFlag()))
        {
            praise.setImageResource(R.drawable.community_topic_detail_praise);
        }
        else
        {
            praise.setImageResource(R.drawable.community_topic_detail_praise_press);
        }
        
        //是否有投票界面
        if (Constants.TOPIC_TYPE_VOTE.equals(topic.getType()))
        {
            vote.setVisibility(View.VISIBLE);
            yesNum.setText(topic.getYes());
            noNum.setText(topic.getNo());
            if (Constants.NO_VOTE.equals(topic.getVoteFlag()))
            {
                yes.setBackgroundResource(R.drawable.community_agree);
                no.setBackgroundResource(R.drawable.community_disagree);
            }
            else
            {
                if (Constants.AGREE.equals(topic.getVoteFlag()))
                {
                    yes.setBackgroundResource(R.drawable.community_agree_clicked);
                    no.setBackgroundResource(R.drawable.community_disagree);
                }
                else
                {
                    yes.setBackgroundResource(R.drawable.community_agree);
                    no.setBackgroundResource(R.drawable.community_disagree_clicked);
                }
            }
        }
        else
        {
            vote.setVisibility(View.GONE);
        }
        like.setText(topic.getPraiseNum());
        chat.setText(topic.getComment());
        //响应头像按钮点击事件
        headImage.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CommunityTopicDetailsActivity.this, CommunityPersonDetailActivity.class);
                intent.putExtra("queryUId", topic.getuId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //响应反对按钮事件
        disagree.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Global.isLogin())
                {
                    if (Constants.NO_VOTE.equals(topic.getVoteFlag()))
                    {
                        topic.setNo((Integer.parseInt(topic.getNo()) + 1) + "");
                        topic.setVoteFlag(Constants.DISAGREE);
                        loadChangeView();
                        CommunityService.instance().agreeOrDisagree(topic.getArticleId(),
                            topic.getVoteFlag(),
                            CommunityTopicDetailsActivity.this,
                            CommunityTopicDetailsActivity.this);
                    }
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(CommunityTopicDetailsActivity.this);
                }
                
            }
        });
        //响应赞成按钮点击
        agree.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Global.isLogin())
                {
                    if (Constants.NO_VOTE.equals(topic.getVoteFlag()))
                    {
                        topic.setYes((Integer.parseInt(topic.getYes()) + 1) + "");
                        topic.setVoteFlag(Constants.AGREE);
                        loadChangeView();
                        CommunityService.instance().agreeOrDisagree(topic.getArticleId(),
                            topic.getVoteFlag(),
                            CommunityTopicDetailsActivity.this,
                            CommunityTopicDetailsActivity.this);
                    }
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(CommunityTopicDetailsActivity.this);
                }
            }
        });
        //响应点赞按钮
        likeLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Global.isLogin())
                {
                    if (Constants.CANCEL_PRAISE.equals(topic.getFlag()))
                    {
                        topic.setFlag(Constants.PRAISE);
                        topic.setPraiseNum((Integer.parseInt(topic.getPraiseNum()) + 1) + "");
                    }
                    else
                    {
                        topic.setFlag(Constants.CANCEL_PRAISE);
                        topic.setPraiseNum((Integer.parseInt(topic.getPraiseNum()) - 1) + "");
                    }
                    loadChangeView();
                    showAnimation((ImageView)((LinearLayout)v).getChildAt(0));
                    CommunityService.instance().addOrCancelPraise(topic.getArticleId(),
                        topic.getFlag(),
                        CommunityTopicDetailsActivity.this,
                        CommunityTopicDetailsActivity.this);
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(CommunityTopicDetailsActivity.this);
                }
            }
        });
        //响应评论按钮点击事件
        chatLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //显示键盘
                sendLinear.setVisibility(View.VISIBLE);
                itemLinear.setVisibility(View.GONE);
                inputContent.requestFocus();
                showSoftInput();
            }
        });
        commentAdd.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //显示键盘
                sendLinear.setVisibility(View.VISIBLE);
                itemLinear.setVisibility(View.GONE);
                inputContent.requestFocus();
                showSoftInput();
            }
        });
    }
    
    /**
     * 
     * <点赞动画效果>
     * <功能详细描述>
     * @param imageView
     * @param position
     * @see [类、类#方法、类#成员]
     */
    private void showAnimation(final ImageView imageView)
    {
        toLargeAnimation = AnimationUtils.loadAnimation(CommunityTopicDetailsActivity.this, R.anim.group_to_large);
        toLargeAnimation.setAnimationListener(new AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation arg0)
            {
                
            }
            
            @Override
            public void onAnimationRepeat(Animation arg0)
            {
                
            }
            
            @Override
            public void onAnimationEnd(Animation arg0)
            {
                if (Constants.CANCEL_PRAISE.equals(topic.getFlag()))
                {
                    imageView.setImageResource(R.drawable.community_topic_detail_praise);
                    imageView.clearAnimation();
                }
                else
                {
                    imageView.setImageResource(R.drawable.community_topic_detail_praise_press);
                    imageView.clearAnimation();
                }
            }
        });
        imageView.startAnimation(toLargeAnimation);
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        page = 1;
        CommunityService.instance().topicDetails(id, this, CommunityTopicDetailsActivity.this);
    }
    
    /**
     * 
     * <社区列表查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryCommunityCommentList()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);
        param.put("page", page + "");
        param.put("num", num + "");
        if (page > 1)
        {
            param.put("queryTime", queryTime + "");
        }
        ConnectService.instance().connectServiceReturnResponse(CommunityTopicDetailsActivity.this,
            param,
            CommunityTopicDetailsActivity.this,
            GroupTopicsCommentsResponse.class,
            URLUtil.COMMUNITY_COMMENT_LIST,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <发表评论>
     * <功能详细描述>
     * @param content
     * @see [类、类#方法、类#成员]
     */
    private void sendComment(String content)
    {
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("id", SecurityUtils.encode2Str(topic.getArticleId()));
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("content", SecurityUtils.encode2Str(content));
            if (GeneralUtils.isNotNullOrZeroLenght(replyUId))
                param.put("replyUId", SecurityUtils.encode2Str(replyUId));
            if (GeneralUtils.isNotNullOrZeroLenght(commentId))
                param.put("commentId", SecurityUtils.encode2Str(commentId));
            topicDetailInfo = new TopicDetailInfo();
            topicDetailInfo.setContent(content);
            topicDetailInfo.setUserLevel(Global.getUserLevel());
            topicDetailInfo.setImageUrl(Global.getImage());
            topicDetailInfo.setNickName(Global.getNickName());
            topicDetailInfo.setUserId(Global.getUserId());
            topicDetailInfo.setTime(GeneralUtils.getRightNowDateString());
            topicDetailInfo.setReplyUId(replyUId);
            topicDetailInfo.setReplyNickName(replyNickName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            CommunityTopicDetailsActivity.this,
            GroupAddTopicCommentResponse.class,
            URLUtil.COMMUNITY_ADD_COMMENT,
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
            case R.id.send:
                //判断是否登陆
                if (Global.isLogin())
                {
                    if (GeneralUtils.isNullOrZeroLenght(inputContent.getText().toString().trim()))
                    {
                        ToastUtil.makeText(CommunityTopicDetailsActivity.this, "评论内容为空，请输入评论内容");
                    }
                    else
                    {
                        sendComment(inputContent.getText().toString().trim());
                    }
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(CommunityTopicDetailsActivity.this);
                }
                break;
            
            //点击更多
            case R.id.community_more_layout:
                
                DialogUtil.communityEditDiaLog(CommunityTopicDetailsActivity.this,
                    "",
                    "",
                    topic,
                    null,
                    null,
                    new DialogCallBack()
                    {
                        @Override
                        public void dialogBack()
                        {
                            if (Global.isLogin())
                            {
                                if (Global.isSuper())
                                {
                                    Intent i = new Intent(CommunityTopicDetailsActivity.this, MoveTopicActivity.class);
                                    i.putExtra("from_id", topic.getId());
                                    i.putExtra("article_id", id);
                                    CommunityTopicDetailsActivity.this.startActivityForResult(i, Constants.NUM1);
                                }
                                else
                                {
                                    Toast.makeText(CommunityTopicDetailsActivity.this, "您无权限移动话题", Toast.LENGTH_SHORT)
                                        .show();
                                }
                            }
                            else
                            {
                                DialogUtil.TwoButtonDialogGTLogin(CommunityTopicDetailsActivity.this);
                            }
                        }
                    }, new DialogCallBack()
                    {
                        @Override
                        public void dialogBack()
                        {
                            if (Global.isLogin())
                            {
                                Intent intent = new Intent(CommunityTopicDetailsActivity.this, ReportActivity.class);
                                intent.putExtra("article_id", id);
                                CommunityTopicDetailsActivity.this.startActivity(intent);
                            }
                            else
                            {
                                DialogUtil.TwoButtonDialogGTLogin(CommunityTopicDetailsActivity.this);
                            }
                        }
                    });
                
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.NUM0)
        {
            //登陆成功返回刷新
            if (resultCode == Constants.Person_center_login_code)
            {
                refresh();
            }
        }
        if (requestCode == Constants.NUM1 && resultCode == Constants.move_topic && data != null)
        {
            come.setText(data.getStringExtra("circleName"));
        }
    }
    
    /**
     * 
     * <刷新界面>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void refresh()
    {
        CommunityService.instance().topicDetails(id, this, CommunityTopicDetailsActivity.this);
    }
    
    @Override
    public void netBack(Object ob)
    {
        super.netBack(ob);
        /**
         * 话题详情
         */
        if (ob instanceof Topic)
        {
            topic = (Topic)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(topic.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(topic.getRetcode()))
                {
                    isFirstInfo = false;
                    queryCommunityCommentList();
                    refreshView();
                }
                else
                {
                    if (isFirstInfo)
                    {
                        if (dailog != null)
                        {
                            dailog.dismissDialog();
                        }
                        DialogUtil.showBtnFinishActivityDialog(CommunityTopicDetailsActivity.this, "获取话题详情失败");
                    }
                    else
                    {
                        queryCommunityCommentList();
                    }
                }
            }
            else
            {
                if (isFirstInfo)
                {
                    if (dailog != null)
                    {
                        dailog.dismissDialog();
                    }
                    DialogUtil.showBtnFinishActivityDialog(CommunityTopicDetailsActivity.this, "获取话题详情失败");
                }
                else
                {
                    queryCommunityCommentList();
                }
            }
        }
        /**
         * 查询评论列表
         */
        else if (ob instanceof GroupTopicsCommentsResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            mPullToRefreshView.onHeaderRefreshComplete();
            loadingMore.setVisibility(View.GONE);
            GroupTopicsCommentsResponse topicsCommentsResponse = (GroupTopicsCommentsResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(topicsCommentsResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(topicsCommentsResponse.getRetcode()))
                {
                    queryTime = topicsCommentsResponse.getQueryTime();
                    if (page == 1)
                    {
                        if (GeneralUtils.isNotNullOrZeroSize(topicsCommentsResponse.getDoc()))
                        {
                            topics.clear();
                            topics.addAll(topicsCommentsResponse.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                            
                            commitTipsLayout.setVisibility(View.VISIBLE);
                            commitTipsNum.setText(topic.getComment());
                        }
                        else
                        {
                            commitTipsLayout.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        isRefreshing = false;
                        //没有更多
                        if (topicsCommentsResponse.getDoc() == null || topicsCommentsResponse.getDoc().size() < 0)
                        {
                            page--;
                        }
                        else
                        {
                            topics.addAll(topicsCommentsResponse.getDoc());
                            currentPage = page;
                            adapter.notifyDataSetChanged();
                        }
                    }
                    //是否有更多
                    if (topicsCommentsResponse.getDoc() == null || topicsCommentsResponse.getDoc().size() < num)
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
                        ToastUtil.makeText(CommunityTopicDetailsActivity.this, topicsCommentsResponse.getRetinfo());
                    }
                    else
                    {
                        page--;
                        isRefreshing = false;
                        ToastUtil.makeText(CommunityTopicDetailsActivity.this, topicsCommentsResponse.getRetinfo());
                    }
                }
            }
            else
            {
                if (page == 1)
                {
                    page = currentPage;
                    ToastUtil.showError(CommunityTopicDetailsActivity.this);
                }
                else
                {
                    page--;
                    isRefreshing = false;
                    ToastUtil.showError(CommunityTopicDetailsActivity.this);
                }
            }
        }
        /**
         * 发表评论
         */
        else if (ob instanceof GroupAddTopicCommentResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            GroupAddTopicCommentResponse addTopicCommentResponse = (GroupAddTopicCommentResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(addTopicCommentResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(addTopicCommentResponse.getRetcode()))
                {
                    showSoftInput();
                    inputContent.setText("");
                    inputContent.setHint(getString(R.string.comment_hint));
                    replyUId = "";
                    replyNickName = "";
                    commentId = "";
                    topicDetailInfo.setCommentId(addTopicCommentResponse.getCommentId());
                    topics.add(0, topicDetailInfo);
                    adapter.notifyDataSetChanged();
                    chat.setText((Integer.parseInt(chat.getText().toString()) + 1) + "");
                    ToastUtil.makeText(CommunityTopicDetailsActivity.this, "发表成功");
                }
                else
                {
                    ToastUtil.makeText(CommunityTopicDetailsActivity.this, addTopicCommentResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(CommunityTopicDetailsActivity.this);
            }
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
                refresh();
            }
        }
    }
    
}
