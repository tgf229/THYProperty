package com.ymdq.thy.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.community.Image;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.bean.home.AdvertiseDetailResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.community.CommunityTopicDetailsActivity;
import com.ymdq.thy.ui.home.AdvertiseDetailActivity;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <分享公共类>
 * <功能详细描述>
 * 
 * @author  yinshilin
 * @version  [版本号, 2014-12-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommShareActivity extends BaseActivity implements OnClickListener
{
    Animation animation, animation1, animation2;
    
    // 圈子，微信，朋友圈，腾讯微博，QQ，QQ空间，人人网，短信，微博
    TextView weixin_text, pyq_text, txwb_text, qq_text, qzone_text, renren_text, sms_text, weibo_text;
    
    //返回按钮
    ImageView cancle_btn;
    
    int height = 0;
    
    private final UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
    
    private SnsPostListener listener;
    
    private SinaShareContent sinaContent;
    
    private StringBuffer sbBuffer_weibo;
    
    private Bitmap bitmap;
    
    private Animation backgroundAnimation;
    
    private Animation btnAnimation;
    
    private ImageView back_img;
    
    int symbol;
    
    private AdvertiseDetailResponse advertiseDetailResponse;
    
    private Topic topic;
    
    private UMImage img = null;
    
    private Context mContext;
    
    private String share_url;
    
    private long oTime;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comm_share_activity);
        Intent intent = getIntent();
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
            mContext = CommunityTopicDetailsActivity.mContext;
            initTopicContent();
        }
        initUI();
        initUMConfig();
        // 布局加载完成
        sms_text.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
        {
            boolean b = true;
            
            @Override
            public boolean onPreDraw()
            {
                if (b)
                {
                    b = false;
                    setAnimation();
                    setBackgroudAnim();
                }
                return true;
            }
        });
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
        back_img = (ImageView)findViewById(R.id.back_img);
        cancle_btn = (ImageView)findViewById(R.id.cancle_btn);
        cancle_btn.setOnClickListener(this);
        weixin_text = (TextView)findViewById(R.id.weixin_text);
        pyq_text = (TextView)findViewById(R.id.pyq_text);
        txwb_text = (TextView)findViewById(R.id.txwb_text);
        qq_text = (TextView)findViewById(R.id.qq_text);
        qzone_text = (TextView)findViewById(R.id.qzone_text);
        renren_text = (TextView)findViewById(R.id.renren_text);
        sms_text = (TextView)findViewById(R.id.sms_text);
        weibo_text = (TextView)findViewById(R.id.weibo_text);
        weixin_text.setOnClickListener(this);
        pyq_text.setOnClickListener(this);
        txwb_text.setOnClickListener(this);
        qq_text.setOnClickListener(this);
        qzone_text.setOnClickListener(this);
        renren_text.setOnClickListener(this);
        sms_text.setOnClickListener(this);
        weibo_text.setOnClickListener(this);
    }
    
    /**
     * 设置进入分享页面时抖动动画
     */
    private void setAnimation()
    {
        height = getWindowManager().getDefaultDisplay().getHeight();
        weixin_text.setAnimation(getAnim(20));
        pyq_text.setAnimation(getAnim(40));
        txwb_text.setAnimation(getAnim(60));
        qq_text.setAnimation(getAnim(80));
        qzone_text.setAnimation(getAnim(100));
        renren_text.setAnimation(getAnim(120));
        sms_text.setAnimation(getAnim(140));
        weibo_text.setAnimation(getAnim(160));
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                weixin_text.setAnimation(shakeAnimation(1));
            }
        }, 520);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                pyq_text.setAnimation(shakeAnimation(1));
            }
        }, 540);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                txwb_text.setAnimation(shakeAnimation(1));
            }
        }, 560);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                qq_text.setAnimation(shakeAnimation(1));
            }
        }, 580);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                qzone_text.setAnimation(shakeAnimation(1));
            }
        }, 600);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                renren_text.setAnimation(shakeAnimation(1));
            }
        }, 620);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                sms_text.setAnimation(shakeAnimation(1));
            }
        }, 640);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                weibo_text.setAnimation(shakeAnimation(1));
            }
        }, 660);
    }
    
    public Animation shakeAnimation(int CycleTimes)
    {
        Animation translateAnimation = new TranslateAnimation(0, 0, 0, 3);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(200);
        return translateAnimation;
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
    
    private Animation getAnim(int startOffTime)
    {
        Animation animation = new TranslateAnimation(0, 0, height, 0);
        animation.setDuration(500);
        if (startOffTime != 0)
        {
            animation.setStartOffset(startOffTime);
        }
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.weixin_text:
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
                    weixinContent.setShareMedia(new UMImage(CommShareActivity.this, bitmap));
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
            case R.id.pyq_text:
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
                    circleContent.setShareMedia(new UMImage(CommShareActivity.this, bitmap));
                }
                else
                {
                    loadPic();
                    circleContent.setShareMedia(img);
                }
                mController.getConfig().closeToast();
                mController.setShareMedia(circleContent);
                mController.postShare(CommShareActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE, setPostListener());
                //                //设置为文本的模式
                //                circleContent.setShareImage(null);
                //                circleContent.setTitle(null);
                //                circleContent.setShareContent(null);
                //                mController.setShareMedia(null);
                finish();
                break;
            case R.id.txwb_text:
                TencentWbShareContent tencentContent = new TencentWbShareContent();
                tencentContent.setShareContent(sbBuffer_weibo.toString());
                if (bitmap != null)
                {
                    tencentContent.setShareMedia(new UMImage(CommShareActivity.this, bitmap));
                }
                else
                {
                    tencentContent.setShareMedia(new UMImage(CommShareActivity.this, R.drawable.community_default));
                }
                mController.setShareMedia(tencentContent);
                mController.getConfig().registerListener(setPostListener());
                mController.getConfig().registerListener(listener);
                
                if (OauthHelper.isAuthenticated(mContext, SHARE_MEDIA.TENCENT))
                {
                    mController.postShare(mContext, SHARE_MEDIA.TENCENT, null);
                }
                else
                {
                    mController.doOauthVerify(mContext, SHARE_MEDIA.TENCENT, new UMAuthListener()
                    {
                        @Override
                        public void onError(SocializeException e, SHARE_MEDIA platform)
                        {
                        }
                        
                        @Override
                        public void onComplete(Bundle value, SHARE_MEDIA platform)
                        {
                            mController.postShare(mContext, SHARE_MEDIA.TENCENT, null);
                        }
                        
                        @Override
                        public void onCancel(SHARE_MEDIA arg0)
                        {
                        }
                        
                        @Override
                        public void onStart(SHARE_MEDIA arg0)
                        {
                        }
                    });
                }
                mController.getConfig().closeToast();
                finish();
                break;
            case R.id.qq_text:
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
                    qqContent.setShareMedia(new UMImage(CommShareActivity.this, bitmap));
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
            case R.id.qzone_text:
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
                    qZoneContent.setShareMedia(new UMImage(CommShareActivity.this, bitmap));
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
            case R.id.renren_text:
                // 设置renren分享内容
                ToastUtil.makeText(CommShareActivity.this, "暂未开通！");
                //                RenrenShareContent renrenShareContent = new RenrenShareContent();
                //                UMImage image;
                //                if (bitmap != null)
                //                {
                //                    image = new UMImage(mContext, bitmap);
                //                }
                //                else
                //                {
                //                    if (symbol == 1)
                //                    {
                //                        image = new UMImage(mContext, R.drawable.home_banner);
                //                    }
                //                    else
                //                    {
                //                        image = new UMImage(mContext, R.drawable.community_default);
                //                    }
                //                }
                //                if (symbol == 1)
                //                {
                //                    renrenShareContent.setTitle(advertiseDetailResponse.getName());
                //                    renrenShareContent.setShareContent(sbBuffer_weibo.toString());
                //                    
                //                }
                //                else if (symbol == 2)
                //                {
                //                    renrenShareContent.setShareContent(sbBuffer_weibo.toString());
                //                }
                //                renrenShareContent.setShareImage(image);
                //                renrenShareContent.setAppWebSite(share_url);
                //                renrenShareContent.setTargetUrl(share_url);
                //                mController.setShareMedia(renrenShareContent);
                //                mController.getConfig().closeToast();
                //                if (OauthHelper.isAuthenticated(mContext, SHARE_MEDIA.RENREN))
                //                {
                //                    mController.postShare(mContext, SHARE_MEDIA.RENREN, setPostListener());
                //                }
                //                else
                //                {
                //                    mController.doOauthVerify(mContext, SHARE_MEDIA.RENREN, new UMAuthListener()
                //                    {
                //                        @Override
                //                        public void onError(SocializeException e, SHARE_MEDIA platform)
                //                        {
                //                        }
                //                        
                //                        @Override
                //                        public void onComplete(Bundle value, SHARE_MEDIA platform)
                //                        {
                //                            mController.postShare(mContext, SHARE_MEDIA.RENREN, setPostListener());
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
                //                finish();
                break;
            case R.id.sms_text:
                // 设置短信分享内容
                Uri smsToUri = Uri.parse("smsto://");
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);//绿色文字就是启动发送短信窗口
                intent.putExtra("sms_body", sbBuffer_weibo.toString());
                startActivity(intent);
                finish();
                break;
            
            case R.id.weibo_text:
                ToastUtil.makeText(CommShareActivity.this, "暂未开通！");
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
            
            case R.id.cancle_btn:
                finish();
                overridePendingTransition(R.anim.share_scale_out, R.anim.share_exit_cancle_anim);
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
                        ToastUtil.makeText(CommShareActivity.this, "发送成功");
                    }
                    else
                    {
                        ToastUtil.makeText(CommShareActivity.this, "发送失败");
                    }
                    finish();
                }
                else if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE))
                {
                    mController.unregisterListener(this);
                    if (eCode == 200)
                    {
                        ToastUtil.makeText(CommShareActivity.this, "发送成功");
                    }
                    else
                    {
                        ToastUtil.makeText(CommShareActivity.this, "发送失败");
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
    
    private void setBackgroudAnim()
    {
        backgroundAnimation = AnimationUtils.loadAnimation(CommShareActivity.this, R.anim.share_enter_alpha);
        back_img.startAnimation(backgroundAnimation);
        btnAnimation = AnimationUtils.loadAnimation(CommShareActivity.this, R.anim.share_enter_rotate);
        cancle_btn.startAnimation(btnAnimation);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.share_scale_out, R.anim.share_exit_cancle_anim);
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
            img = new UMImage(CommShareActivity.this, R.drawable.home_banner);
        }
        else if (symbol == 2)
        {
            img = new UMImage(CommShareActivity.this, R.drawable.community_default);
        }
        
    }
    
}
