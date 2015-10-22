package com.ymdq.thy.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.home.AdvertiseDetailResponse;
import com.ymdq.thy.bean.home.AdvertiseDetailResponse.CommentList;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.database.MyTicketDetailDAO;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.CommShareActivity;
import com.ymdq.thy.ui.home.adapter.AdvertiseDetailAdapter;
import com.ymdq.thy.ui.personcenter.LoginActivity;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.GifView;
import com.ymdq.thy.view.MyListView;
import com.ymdq.thy.view.MyScrollView;

/**
 * 
 * <通告详情>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AdvertiseDetailActivity extends BaseActivity implements OnClickListener, UICallBack
{
    private String id;
    
    private LinearLayout loadingLayout;
    
    private LinearLayout clickrefreshLayout;
    
    private TextView clickTextView;
    
    private LinearLayout praise_blame_layout;
    
    private ImageView adImg;
    
    private TextView adName;
    
    private TextView adContent;
    
    private ListView mListView;
    
    private TextView praiseNum;
    
//    private TextView blameNum,shareNum;
    
    private TextView commentNum;
    
    private TextView commitTipsNum;
    
    private ImageView praiseImg;
    
//    private ImageView blameImg;
    
    private LinearLayout praiseLayout;
    
    private LinearLayout commentAdd;
    
    private LinearLayout commentLayout;
    
    private LinearLayout shareLayout,commitTipsLayout;
    
    private List<CommentList> mList;
    
    private AdvertiseDetailAdapter adapter;
    
    private LinearLayout go_to_evaluate_layout;
    
    private EditText evaluateContent;
    
    private Animation toLargeAnimation;
    
    private AdvertiseDetailResponse detailREsp;
    
    private TextView titleBarName;
    
//    private AdvertiseBroard broardcast;
    
    /**
     * 存放是赞还是踩操作
     */
    private String praiseOrBlame;
    
    private NetLoadingDailog loadingDialog;
    
    /**
     * 存放评论的内容
     */
    private String tempEvaluate = "";
    
    private Button sendBtn;
    /**
    * 分享时候需要这个静态
    */
    public static Context mContext;
    
    private GifView gif1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_advertise_detail);
        id = getIntent().getStringExtra("id");
        mContext=this;
        initView();
        loadingDialog = new NetLoadingDailog(this);
        reqAdvertiseDetail();
//        registreBroadcast();
    }
    
    private void initView()
    {
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setText("详情");
        
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        gif1 = (GifView)loadingLayout.findViewById(R.id.gif1);  
        // 设置背景gif图片资源  
        gif1.setMovieResource(R.raw.jiazai_gif);
        loadingLayout.setVisibility(View.VISIBLE);
        
        clickrefreshLayout = (LinearLayout)findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
//        clickrefreshLayout.setOnClickListener(this);
        
        praise_blame_layout = (LinearLayout)findViewById(R.id.praise_blame_layout);
        adImg = (ImageView)findViewById(R.id.img);
        adName = (TextView)findViewById(R.id.ad_name);
        adContent = (TextView)findViewById(R.id.ad_content);
        mListView = (ListView)findViewById(R.id.ad_listview);
        praiseNum = (TextView)findViewById(R.id.praise_num);
//        blameNum = (TextView)findViewById(R.id.blame_num);
//        blameImg = (ImageView)findViewById(R.id.blame_img);
//        shareNum = (TextView)findViewById(R.id.share_num);
//        blameLayout = (LinearLayout)findViewById(R.id.blame_layout);
        commentAdd = (LinearLayout)findViewById(R.id.comment_add);
        commentNum = (TextView)findViewById(R.id.comment_num);
        praiseImg = (ImageView)findViewById(R.id.praise_img);
        praiseLayout = (LinearLayout)findViewById(R.id.praise_layout);
        commentLayout = (LinearLayout)findViewById(R.id.comment_layout);
        shareLayout = (LinearLayout)findViewById(R.id.share_layout);
        
        commitTipsLayout = (LinearLayout)findViewById(R.id.commit_tips_layout);
        commitTipsNum = (TextView)findViewById(R.id.commit_tips_num);
        
        commentAdd.setOnClickListener(this);
        commentLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = width * 200 / 640;
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
        adImg.setLayoutParams(param);
        
        MyListView mListView = (MyListView)findViewById(R.id.ad_listview);
        mList = new ArrayList<CommentList>();
        adapter = new AdvertiseDetailAdapter(this, mList);
        mListView.setAdapter(adapter);
        mListView.setFocusable(false);
        
        go_to_evaluate_layout = (LinearLayout)findViewById(R.id.go_to_evaluate_layout);
        evaluateContent = (EditText)findViewById(R.id.input_content);
        sendBtn = (Button)findViewById(R.id.send);
        sendBtn.setOnClickListener(this);
        
        MyScrollView scrollView = (MyScrollView)findViewById(R.id.scroll_view);
        scrollView.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (go_to_evaluate_layout.getVisibility() == View.VISIBLE)
                {
                    hideSoftInput();
                    go_to_evaluate_layout.setVisibility(View.GONE);
                    praise_blame_layout.setVisibility(View.VISIBLE);
                }
                
                return false;
            }
        });
    }
    
    /**
     * 
     * <请求详情接口>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqAdvertiseDetail()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            AdvertiseDetailResponse.class,
            URLUtil.ADVERT_DETAIL,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
//            case R.id.click_refresh_layout:
//                loadingLayout.setVisibility(View.VISIBLE);
//                clickrefreshLayout.setVisibility(View.GONE);
//                reqAdvertiseDetail();
//                break;
            //赞
            case R.id.praise_layout:
                praiseLayout.setEnabled(false);
//                blameLayout.setEnabled(false);
                showAnimation(praiseImg, Constants.PRAISE);
                praiseOrBlame = Constants.PRAISE;
                reqPraiseOrBlame(Constants.PRAISE);
                break;
            //踩
//            case R.id.blame_layout:
//                praiseLayout.setEnabled(false);
//                blameLayout.setEnabled(false);
//                showAnimation(blameImg, Constants.CANCEL_PRAISE);
//                praiseOrBlame = Constants.CANCEL_PRAISE;
//                reqPraiseOrBlame(Constants.CANCEL_PRAISE);
//                break;
            //评论
            case R.id.comment_layout:
                praise_blame_layout.setVisibility(View.GONE);
                go_to_evaluate_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.comment_add:
                praise_blame_layout.setVisibility(View.GONE);
                go_to_evaluate_layout.setVisibility(View.VISIBLE);
                break;
            //分享
            case R.id.share_layout:
                praiseOrBlame = Constants.SHARE;
                Intent comm_int = new Intent(AdvertiseDetailActivity.this, CommShareActivity.class);
                comm_int.putExtra("symbol", 1);
                comm_int.putExtra("advertiseDel", detailREsp);
                startActivity(comm_int);
                break;
            //发送评论
            case R.id.send:
                praiseOrBlame = Constants.COMMENT;
                if (Global.isLogin())
                {
                    if (GeneralUtils.isNotNullOrZeroLenght(evaluateContent.getText().toString().trim()))
                    {
                        loadingDialog.loading();
                        tempEvaluate = evaluateContent.getText().toString();
                        reqEvaluate();
                        evaluateContent.setText("");
                    }
                    else
                    {
                        ToastUtil.makeText(AdvertiseDetailActivity.this, "请填写评论内容");
                    }
                }
                else
                {
                    DialogUtil.showTwoButtonDialog(AdvertiseDetailActivity.this,"很抱歉，游客无法操作此功能，请进行注册或登录", 
                        new DialogCallBack()
                    {
                        
                        @Override
                        public void dialogBack()
                        {
                            Intent i = new Intent(AdvertiseDetailActivity.this, LoginActivity.class);
                            startActivityForResult(i, 0);
                        }
                    });
                    
//                    Toast.makeText(AdvertiseDetailActivity.this, "很抱歉，游客无法操作此功能，请进行注册或登录", Toast.LENGTH_SHORT).show();
                }
                
            default:
                break;
        }
    }
    
    /**
     * 
     * <轮播通告—赞&踩&分享>
     * <功能详细描述>
     * @param type
     * @see [类、类#方法、类#成员]
     */
    private void reqPraiseOrBlame(String type)
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", id);
        param.put("type", type);
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            BaseResponse.class,
            URLUtil.ADVERT_PRAISE_BLAME_SHARE,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <通告评论>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqEvaluate()
    {
        sendBtn.setEnabled(false);
        
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(id));
            param.put("content", SecurityUtils.encode2Str(evaluateContent.getText().toString().trim()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            BaseResponse.class,
            URLUtil.ADVERT_EVALUATE,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        gif1.setPaused(true);
        loadingLayout.setVisibility(View.GONE);
        if (ob != null)
        {
            if (ob instanceof AdvertiseDetailResponse)
            {
                loadingDialog.dismissDialog();
                detailREsp = (AdvertiseDetailResponse)ob;
                if (GeneralUtils.isNotNullOrZeroLenght(detailREsp.getRetcode()))
                {
                    if (Constants.SUCESS_CODE.equals(detailREsp.getRetcode()))
                    {
                        DisplayImageOptions options =
                            new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.home_banner)
                                .showImageForEmptyUri(R.drawable.home_banner)
                                .showImageOnFail(R.drawable.home_banner)
                                .cacheInMemory(false)
                                .cacheOnDisc(false)
                                .build();
                        ImageLoader.getInstance().displayImage(detailREsp.getImageUrl(), adImg, options);
                        
                        if (GeneralUtils.isNotNullOrZeroLenght(detailREsp.getName()))
                        {
                            adName.setText(detailREsp.getName());
                        }
                        if (GeneralUtils.isNotNullOrZeroLenght(detailREsp.getDesc()))
                        {
                            adContent.setText(detailREsp.getDesc());
                        }
                        if (GeneralUtils.isNotNullOrZeroLenght(detailREsp.getPraiseNum()))
                        {
                            praiseNum.setText(detailREsp.getPraiseNum());
                        }
//                        if (GeneralUtils.isNotNullOrZeroLenght(detailREsp.getBlameNum()))
//                        {
//                            blameNum.setText(detailREsp.getBlameNum());
//                        }
//                        if (GeneralUtils.isNotNullOrZeroLenght(detailREsp.getShareNum()))
//                        {
//                            shareNum.setText(detailREsp.getShareNum());
//                        }
                        List<CommentList> list = detailREsp.getComment();
                        if (list != null && list.size() > 0)
                        {
                            commentNum.setText(list.size() + "");
                            mList.clear();
                            mList.addAll(list);
                            commitTipsLayout.setVisibility(View.VISIBLE);
                            commitTipsNum.setText(String.valueOf(list.size()));
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            commitTipsLayout.setVisibility(View.GONE);
                            commentNum.setText("0");
                        }
                        String pORb = MyTicketDetailDAO.getInstance(this).queryDB(id);
                        //说明数据库中有此数据，否则赞&踩不可点击
                        if (GeneralUtils.isNotNullOrZeroLenght(pORb))
                        {
                            //踩
//                            if (Constants.CANCEL_PRAISE.equals(pORb))
//                            {
//                                blameImg.setImageResource(R.drawable.icon_caired_pressed);
//                            }
                            //赞
                            if (Constants.PRAISE.equals(pORb))
                            {
                                praiseImg.setImageResource(R.drawable.btn_zan_press);
                            }
                        }
                        else
                        {
                            praiseLayout.setOnClickListener(this);
//                            blameLayout.setOnClickListener(this);
                        }
                    }
                    else
                    {
                        clickrefreshLayout.setVisibility(View.VISIBLE);
                        gif1.setPaused(true);
                        loadingLayout.setVisibility(View.GONE);
                        clickTextView.setText(detailREsp.getRetinfo());
                    }
                    return;
                }
                clickrefreshLayout.setVisibility(View.VISIBLE);
                gif1.setPaused(true);
                loadingLayout.setVisibility(View.GONE);
                clickTextView.setText(Constants.ERROR_MESSAGE);
            }
            else if (ob instanceof BaseResponse)
            {
                BaseResponse resp = (BaseResponse)ob;
                if (GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if (Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        //踩
//                        if (praiseOrBlame.equals(Constants.CANCEL_PRAISE))
//                        {
                            //存数据库
//                            MyTicketDetailDAO.getInstance(this).insertDB(id, praiseOrBlame);
//                            int b_num = Integer.valueOf(blameNum.getText().toString());
//                            ++b_num;
//                            blameNum.setText(b_num + "");
//                        }
                        //赞
                        if (praiseOrBlame.equals(Constants.PRAISE))
                        {
                            //存数据库
                            MyTicketDetailDAO.getInstance(this).insertDB(id, praiseOrBlame);
                            int p_num = Integer.valueOf(praiseNum.getText().toString());
                            ++p_num;
                            praiseNum.setText(p_num + "");
                        }
                        //分享
                        else if (praiseOrBlame.equals(Constants.SHARE))
                        {
                            
                        }
                        //评论
                        else if (praiseOrBlame.equals(Constants.COMMENT))
                        {
                            reqAdvertiseDetail();
                            sendBtn.setEnabled(true);
                            ToastUtil.makeText(AdvertiseDetailActivity.this, "发送成功");
                        }
                    }
                    else
                    {
                        //评论
                        if (praiseOrBlame.equals(Constants.COMMENT))
                        {
                            loadingDialog.dismissDialog();
                            ToastUtil.makeText(AdvertiseDetailActivity.this, resp.getRetinfo());
                        }
                    }
                    return;
                }
                if(praiseOrBlame.equals(Constants.COMMENT))
                {
                    evaluateContent.setText(tempEvaluate);
                    sendBtn.setEnabled(true);
                    ToastUtil.showError(this);
                }
                loadingDialog.dismissDialog();
            }
        }
    }
    
    /**
     * 隐藏软键盘
     * 
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void hideSoftInput()
    {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
        {
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * 
     * <点赞动画效果>
     * <功能详细描述>
     * @param imageView
     * @param position
     * @see [类、类#方法、类#成员]
     */
    private void showAnimation(final ImageView imageView, final String ok)
    {
        toLargeAnimation = AnimationUtils.loadAnimation(this, R.anim.group_to_large);
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
                //踩
                if (Constants.CANCEL_PRAISE.equals(ok))
                {
                    imageView.setImageResource(R.drawable.icon_caired_pressed);
                    imageView.clearAnimation();
                }
                //赞
                else if (Constants.PRAISE.equals(ok))
                {
                    imageView.setImageResource(R.drawable.btn_zan_press);
                    imageView.clearAnimation();
                }
            }
        });
        imageView.startAnimation(toLargeAnimation);
    }
    
    /**
     * 
     * <公告广播注册>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
//    private void registreBroadcast()
//    {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Constants.ADVERTISE_SUCCESS_BROADCAST);
//        broardcast = new AdvertiseBroard();
//        this.registerReceiver(broardcast, filter);
//    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
//        this.unregisterReceiver(broardcast);
    }
    
//    class AdvertiseBroard extends BroadcastReceiver
//    {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            if (Constants.ADVERTISE_SUCCESS_BROADCAST.equals(intent.getAction()))
//            {
//                shareNum.setText(String.valueOf((Integer.parseInt(shareNum.getText().toString()) + 1)));
//            }
//        }
//    }
}
