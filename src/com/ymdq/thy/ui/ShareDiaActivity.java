package com.ymdq.thy.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.community.Image;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.bean.home.AdvertiseDetailResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.community.CommunityTopicDetailsActivity;
import com.ymdq.thy.ui.community.MoveTopicActivity;
import com.ymdq.thy.ui.home.AdvertiseDetailActivity;
import com.ymdq.thy.ui.home.MainFragment;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <分享公共类>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2015-4-1] v1.1.0
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ShareDiaActivity extends BaseActivity implements OnClickListener
{
    Animation animation, animation1, animation2;
    
    // 微信好友，朋友圈，新浪微博，QQ好友，QQ空间, 置顶, 取消, 删除, 举报, 移动
    TextView winxin_friend, winxin_circle, xinlang, qq_friend, qq_circle, topOrCancel, 
        cancel, delete, report, move;
    
    int height = 0;
    
    private final UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
    
    private SnsPostListener listener;
    
    private SinaShareContent sinaContent;
    
    private StringBuffer sbBuffer_weibo;
    
    private Bitmap bitmap;
    
    /**
     * 1 轮播通告 ;2 新鲜事分享/社区话题分享
     */
    int symbol;
    
    private AdvertiseDetailResponse advertiseDetailResponse;
    
    private Topic topic;
    
    private UMImage img = null;
    
    private Context mContext;
    
    private String share_url;
    
    private long oTime;
    
    private Intent intent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_edit_select_dialog);
        getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        getWindow().setWindowAnimations(R.style.dialog_style);
        intent = getIntent();
        if (null != intent)
        {
            symbol = intent.getIntExtra("symbol", 0);
            if (symbol == 1)
            {
                advertiseDetailResponse = (AdvertiseDetailResponse)intent.getSerializableExtra("advertiseDel");
                share_url = URLUtil.SHARE_URL + "?flag=0&id=" + advertiseDetailResponse.getId();
            }
            else if (symbol == 2)
            {
                topic = (Topic)intent.getSerializableExtra("topic");
                share_url = URLUtil.SHARE_URL + "?flag=1&id=" + topic.getArticleId();
            }
        }
        //轮播通告分享内容
        if (symbol == 1)
        {
            mContext = AdvertiseDetailActivity.mContext;
            initAdvertiseDetailContent();
        }
        //新鲜事分享内容
        else if (symbol == 2)
        {
            mContext = MainFragment.mContext;
            initTopicContent();
        }
        initUI();
        initUMConfig();
    }
    
    /**
     * 轮播通告内容
     */
    private void initAdvertiseDetailContent()
    {
        // 设置分享内容(新浪微博,腾讯微博,人人网,短信)
        sbBuffer_weibo = new StringBuffer();
        String content = "";
        if (advertiseDetailResponse.getDesc().length() > 90)
        {
            content = advertiseDetailResponse.getDesc().substring(0, 90) + "...";
        }
        else
        {
            content = advertiseDetailResponse.getDesc();
        }
        sbBuffer_weibo.append(advertiseDetailResponse.getName() + "\n" + content);
        sbBuffer_weibo.append("\n");
        sbBuffer_weibo.append(URLUtil.SHARE_URL + "?flag=0&id=" + advertiseDetailResponse.getId());
        
        loadNetImg(advertiseDetailResponse.getImageUrl());
    }
    
    /**
     * 新鲜事内容
     */
    private void initTopicContent()
    {
        // 设置分享内容(新浪微博,腾讯微博,人人网,短信)
        sbBuffer_weibo = new StringBuffer();
        String content = "";
        if (topic.getContent().length() > 90)
        {
            content = topic.getContent().substring(0, 90) + "...";
        }
        else
        {
            content = topic.getContent();
        }
        sbBuffer_weibo.append(topic.getName());
        sbBuffer_weibo.append("\n");
        sbBuffer_weibo.append(content);
        sbBuffer_weibo.append("\n");
        sbBuffer_weibo.append(URLUtil.SHARE_URL + "?flag=1&id=" + topic.getArticleId());
        List<Image> topicImgs = topic.getImageList();
        if (topicImgs != null && topicImgs.size() > 0)
        {
            loadNetImg(topicImgs.get(0).getImageUrlS());
        }
        
        mController.setShareContent(sbBuffer_weibo.toString());
    }
    
    private void initUI()
    {
        winxin_friend = (TextView)findViewById(R.id.winxin_friend);
        winxin_circle = (TextView)findViewById(R.id.winxin_circle);
        xinlang = (TextView)findViewById(R.id.xinlang);
        qq_friend = (TextView)findViewById(R.id.qq_friend);
        qq_circle = (TextView)findViewById(R.id.qq_circle);

        topOrCancel = (TextView)findViewById(R.id.top_or_cancel);
        cancel = (TextView)findViewById(R.id.cancel);
        delete = (TextView)findViewById(R.id.delete);
        report = (TextView)findViewById(R.id.report);
        move = (TextView)findViewById(R.id.move);
        if(intent.getBooleanExtra("hide_top", false))
        {
            topOrCancel.setVisibility(View.GONE);
        }
        else
        {
            topOrCancel.setVisibility(View.VISIBLE);
        }
        if(intent.getBooleanExtra("hide_delete", false))
        {
            delete.setVisibility(View.GONE);
        }
        else
        {
            delete.setVisibility(View.VISIBLE);
        }
        
        winxin_friend.setOnClickListener(this);
        winxin_circle.setOnClickListener(this);
        xinlang.setOnClickListener(this);
        qq_friend.setOnClickListener(this);
        qq_circle.setOnClickListener(this);
        topOrCancel.setOnClickListener(this);
        delete.setOnClickListener(this);
        report.setOnClickListener(this);
        move.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    
    private void initUMConfig()
    {
        //        // 添加新浪SSO授权
        //        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        //        // 添加腾讯微博SSO授权
        //        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        //        
        //        // 添加人人网SSO授权
        //        RenrenSsoHandler renrenSsoHandler =
        //            new RenrenSsoHandler(this, Constants.RENREN_APP_ID, Constants.RENREN_APP_KEY, Constants.RENREN_APP_SECRET);
        //        mController.getConfig().setSsoHandler(renrenSsoHandler);
        addQQQZonePlatform();
        addWXPlatform();
    }
    
    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform()
    {
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
        qqSsoHandler.setTargetUrl("http://www.umeng.com");
        qqSsoHandler.addToSocialSDK();
        
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
        qZoneSsoHandler.addToSocialSDK();
    }
    
    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform()
    {
        // 注意：在微信授权的时候，必须传递appSecret
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, Constants.WEIXINID, Constants.WEIXINKEY);
        wxHandler.addToSocialSDK();
        
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, Constants.WEIXINID, Constants.WEIXINKEY);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.winxin_friend:
                WeiXinShareContent weixinContent = new WeiXinShareContent();
                if (symbol == 1)
                {
                    weixinContent.setTitle(advertiseDetailResponse.getName());
                    weixinContent.setShareContent(advertiseDetailResponse.getDesc());
                    weixinContent.setTargetUrl(share_url);
                }
                else if (symbol == 2)
                {
                    weixinContent.setTitle(topic.getName());
                    weixinContent.setShareContent(topic.getContent());
                    weixinContent.setTargetUrl(share_url);
                }
                if (bitmap != null)
                {
                    weixinContent.setShareMedia(new UMImage(ShareDiaActivity.this, bitmap));
                }
                else
                {
                    loadPic();
                    weixinContent.setShareMedia(img);
                }
                mController.getConfig().closeToast();
                mController.setShareMedia(weixinContent);
                mController.postShare(this, SHARE_MEDIA.WEIXIN, setPostListener());
                finish();
                break;
            case R.id.winxin_circle:
                //添加朋友圈 
                CircleShareContent circleContent = new CircleShareContent();
                if (symbol == 1)
                {
                    circleContent.setShareContent(advertiseDetailResponse.getDesc());
                    circleContent.setTitle(advertiseDetailResponse.getName());
                }
                else if (symbol == 2)
                {
                    circleContent.setTitle(topic.getName());
                    circleContent.setShareContent(topic.getContent());
                }
                circleContent.setTargetUrl(share_url);
                if (bitmap != null)
                {
                    circleContent.setShareMedia(new UMImage(ShareDiaActivity.this, bitmap));
                }
                else
                {
                    loadPic();
                    circleContent.setShareMedia(img);
                }
                mController.getConfig().closeToast();
                mController.setShareMedia(circleContent);
                mController.postShare(ShareDiaActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE, setPostListener());
                //                //设置为文本的模式
                //                circleContent.setShareImage(null);
                //                circleContent.setTitle(null);
                //                circleContent.setShareContent(null);
                //                mController.setShareMedia(null);
                finish();
                break;
            case R.id.qq_friend:
                //添加QQ
                QQShareContent qqContent = new QQShareContent();
                if (symbol == 1)
                {
                    qqContent.setTitle(advertiseDetailResponse.getName());
                    qqContent.setShareContent(advertiseDetailResponse.getDesc());
                }
                else if (symbol == 2)
                {
                    qqContent.setTitle(topic.getName());
                    qqContent.setShareContent(topic.getContent());
                }
                if (bitmap != null)
                {
                    qqContent.setShareMedia(new UMImage(ShareDiaActivity.this, bitmap));
                }
                else
                {
                    loadPic();
                    qqContent.setShareMedia(img);
                }
                qqContent.setTargetUrl(share_url);
                mController.getConfig().closeToast();
                mController.setShareMedia(qqContent);
                mController.postShare(this, SHARE_MEDIA.QQ, setPostListener());
                mController.getConfig().closeToast();
                finish();
                break;
            case R.id.qq_circle:
                //添加QQ空间
                QZoneShareContent qZoneContent = new QZoneShareContent();
                if (symbol == 1)
                {
                    qZoneContent.setShareContent(advertiseDetailResponse.getName());
                    qZoneContent.setTitle(advertiseDetailResponse.getDesc());
                }
                else if (symbol == 2)
                {
                    qZoneContent.setTitle(topic.getName());
                    qZoneContent.setShareContent(topic.getContent());
                }
                
                if (bitmap != null)
                {
                    qZoneContent.setShareMedia(new UMImage(ShareDiaActivity.this, bitmap));
                }
                else
                {
                    loadPic();
                    qZoneContent.setShareMedia(img);
                }
                qZoneContent.setTargetUrl(share_url);
                mController.getConfig().closeToast();
                mController.setShareMedia(qZoneContent);
                mController.postShare(this, SHARE_MEDIA.QZONE, setPostListener());
                mController.getConfig().closeToast();
                finish();
                break;
            case R.id.xinlang:
                ToastUtil.makeText(ShareDiaActivity.this, "暂未开通！");
                //                sinaContent = null;
                //                sinaContent = new SinaShareContent();
                //                sinaContent.setShareContent(sbBuffer_weibo.toString());
                //                if (bitmap != null)
                //                {
                //                    sinaContent.setShareMedia(new UMImage(CommShareActivity.this, bitmap));
                //                }
                //                else
                //                {
                //                    loadPic();
                //                    sinaContent.setShareMedia(img);
                //                }
                //                mController.setShareMedia(sinaContent);
                //                if (OauthHelper.isAuthenticated(mContext, SHARE_MEDIA.SINA))
                //                {
                //                    mController.postShare(mContext, SHARE_MEDIA.SINA, setPostListener());
                //                }
                //                else
                //                {
                //                    mController.doOauthVerify(mContext, SHARE_MEDIA.SINA, new UMAuthListener()
                //                    {
                //                        @Override
                //                        public void onError(SocializeException e, SHARE_MEDIA platform)
                //                        {
                //                        }
                //                        
                //                        @Override
                //                        public void onComplete(Bundle value, SHARE_MEDIA platform)
                //                        {
                //                            mController.postShare(mContext, SHARE_MEDIA.SINA, setPostListener());
                //                        }
                //                        
                //                        @Override
                //                        public void onCancel(SHARE_MEDIA arg0)
                //                        {
                //                        }
                //                        
                //                        @Override
                //                        public void onStart(SHARE_MEDIA arg0)
                //                        {
                //                        }
                //                    });
                //                }
                //                mController.getConfig().closeToast();
                //                finish();
                break;
            //移动
            case R.id.move:
                
                if(Global.isLogin())
                {
                    if(Global.isSuper())
                    {
                        Intent i = new Intent(ShareDiaActivity.this, MoveTopicActivity.class);
                        i.putExtra("from_id", topic.getId());
                        i.putExtra("article_id", topic.getArticleId());
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(ShareDiaActivity.this, "您无权限移动话题", Toast.LENGTH_SHORT).show();
                    }
                    
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(ShareDiaActivity.this);
                }
                break;
            //置顶/取消置顶
            case R.id.top_or_cancel:
                
                break;
            //举报
            case R.id.report:
                
                break;
            //删除
            case R.id.delete:
                
                break;
            case R.id.cancel:
                finish();
                break;
            default:
                break;
        }
    }
    
    /**
     * 加载网络图片
     * @param uri
     */
    public Bitmap loadNetImg(String uri)
    {
        
        ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener()
        {
            @Override
            public void onLoadingStarted(String arg0, View arg1)
            {
            }
            
            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2)
            {
            }
            
            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2)
            {
                bitmap = arg2;
            }
            
            @Override
            public void onLoadingCancelled(String arg0, View arg1)
            {
            }
        });
        return bitmap;
    }
    
    public SnsPostListener setPostListener()
    {
        SnsPostListener listrner = new SnsPostListener()
        {
            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity arg2)
            {
                if (eCode == 200)
                {
                    
                    long currentTime = System.currentTimeMillis();
                    if ((currentTime - oTime) > 10000)
                    {
                        if (symbol == 1)
                        {
                            advertiseNet();
                        }
                        else if (symbol == 2)
                        {
                            topicNet();
                        }
                    }
                    oTime = currentTime;
                    
                }
                if (platform.equals(SHARE_MEDIA.RENREN))
                {
                    finish();
                    
                }
                else if (platform.equals(SHARE_MEDIA.SINA))
                {
                    mController.unregisterListener(this);
                    
                    if (sinaContent != null)
                    {
                        sinaContent.setShareImage(null);
                        mController.setShareMedia(null);
                    }
                    finish();
                }
                else if (platform.equals(SHARE_MEDIA.TENCENT))
                {
                    mController.unregisterListener(this);
                    finish();
                }
                else if (platform.equals(SHARE_MEDIA.SMS))
                {
                    mController.unregisterListener(this);
                    finish();
                }
                else if (platform.equals(SHARE_MEDIA.QZONE))
                {
                    mController.unregisterListener(this);
                    finish();
                }
                else if (platform.equals(SHARE_MEDIA.QQ))
                {
                    mController.unregisterListener(this);
                    finish();
                }
                else if (platform.equals(SHARE_MEDIA.WEIXIN))
                {
                    mController.unregisterListener(this);
                    if (eCode == 200)
                    {
                        ToastUtil.makeText(ShareDiaActivity.this, "发送成功");
                    }
                    else
                    {
                        ToastUtil.makeText(ShareDiaActivity.this, "发送失败");
                    }
                    finish();
                }
                else if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE))
                {
                    mController.unregisterListener(this);
                    if (eCode == 200)
                    {
                        ToastUtil.makeText(ShareDiaActivity.this, "发送成功");
                    }
                    else
                    {
                        ToastUtil.makeText(ShareDiaActivity.this, "发送失败");
                    }
                    finish();
                }
            }
            
            @Override
            public void onStart()
            {
            }
            
        };
        return listrner;
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (ob instanceof BaseResponse)
        {
            BaseResponse baseResponse = (BaseResponse)ob;
            if (Constants.SUCESS_CODE.equals(baseResponse.getRetcode()))
            {
                if (symbol == 1)
                {
                    Intent intent = new Intent();
                    intent.putExtra("id", advertiseDetailResponse.getId());
                    intent.setAction(Constants.ADVERTISE_SUCCESS_BROADCAST);
                    sendBroadcast(intent);
                    finish();
                }
                else if (symbol == 2)
                {
                    Intent intent = new Intent();
                    intent.putExtra("id", topic.getArticleId());
                    intent.setAction(Constants.TOPIC_SUCCESS_BROADCAST);
                    sendBroadcast(intent);
                    finish();
                }
            }
        }
        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
//            overridePendingTransition(R.anim.share_scale_out, R.anim.share_exit_cancle_anim);
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void advertiseNet()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", advertiseDetailResponse.getId());
        param.put("type", "2");
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            BaseResponse.class,
            URLUtil.ADVERT_PRAISE_BLAME_SHARE,
            Constants.ENCRYPT_NONE);
    }
    
    public void topicNet()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", topic.getArticleId());
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            BaseResponse.class,
            URLUtil.COMMUNITY_SHARE_TOPIC,
            Constants.ENCRYPT_NONE);
    }
    
    public void loadPic()
    {
        if (symbol == 1)
        {
            img = new UMImage(ShareDiaActivity.this, R.drawable.home_banner);
        }
        else if (symbol == 2)
        {
            img = new UMImage(ShareDiaActivity.this, R.drawable.community_default);
        }
        
    }
    
}

