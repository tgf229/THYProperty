package com.ymdq.thy.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.utils.OauthHelper;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.community.Image;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.CommShareActivity;

/**
 * 
 * <分享工厂类>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2015-4-2]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ShareFactory implements OnClickListener, UICallBack
{
    private View view;
    
    private Context context;
    
    private Topic topic;
    
    private Dialog editDialog;
    
    private String share_url;
    
    private StringBuffer sbBuffer_weibo;
    
    private Bitmap bitmap;
    
    private UMImage img = null;
    
    private long oTime;
    
    private SinaShareContent sinaContent;
    
    private final UMSocialService mController = UMServiceFactory.getUMSocialService(Constants.DESCRIPTOR);
    
    // 微信好友，朋友圈，新浪微博，QQ好友，QQ空间, 置顶, 取消, 删除, 举报, 移动
    TextView winxin_friend, winxin_circle, xinlang, qq_friend, qq_circle;
    
    public ShareFactory(Dialog editDialog, View view, Context context, Topic topic)
    {
        super();
        this.view = view;
        this.context = context;
        this.topic = topic;
        this.editDialog = editDialog;
        
    }
    
    /**
     * 
     * <数据加载>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void loadData()
    {
        initView();
        initTopicContent();
        initData();
        initUMConfig();
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initView()
    {
        winxin_friend = (TextView)view.findViewById(R.id.winxin_friend);
        winxin_circle = (TextView)view.findViewById(R.id.winxin_circle);
        xinlang = (TextView)view.findViewById(R.id.xinlang);
        qq_friend = (TextView)view.findViewById(R.id.qq_friend);
        qq_circle = (TextView)view.findViewById(R.id.qq_circle);
        
        winxin_friend.setOnClickListener(this);
        winxin_circle.setOnClickListener(this);
        xinlang.setOnClickListener(this);
        qq_friend.setOnClickListener(this);
        qq_circle.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        share_url = URLUtil.SHARE_URL + "?flag=1&id=" + topic.getArticleId();
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
    
    private void addQQQZonePlatform()
    {
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity)context, Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
        qqSsoHandler.setTargetUrl("http://www.umeng.com");
        qqSsoHandler.addToSocialSDK();
        
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler =
            new QZoneSsoHandler((Activity)context, Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
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
        UMWXHandler wxHandler = new UMWXHandler(context, Constants.WEIXINID, Constants.WEIXINKEY);
        wxHandler.addToSocialSDK();
        
        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, Constants.WEIXINID, Constants.WEIXINKEY);
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
                weixinContent.setTitle(topic.getName());
                weixinContent.setShareContent(topic.getContent());
                weixinContent.setTargetUrl(share_url);
                if (bitmap != null)
                {
                    weixinContent.setShareMedia(new UMImage(context, bitmap));
                }
                else
                {
                    loadPic();
                    weixinContent.setShareMedia(img);
                }
                mController.getConfig().closeToast();
                mController.setShareMedia(weixinContent);
                mController.postShare(context, SHARE_MEDIA.WEIXIN, setPostListener());
                editDialog.dismiss();
                break;
            case R.id.winxin_circle:
                //添加朋友圈 
                CircleShareContent circleContent = new CircleShareContent();
                circleContent.setTitle(topic.getName());
                circleContent.setShareContent(topic.getContent());
                circleContent.setTargetUrl(share_url);
                if (bitmap != null)
                {
                    circleContent.setShareMedia(new UMImage(context, bitmap));
                }
                else
                {
                    loadPic();
                    circleContent.setShareMedia(img);
                }
                mController.getConfig().closeToast();
                mController.setShareMedia(circleContent);
                mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE, setPostListener());
                //                //设置为文本的模式
                //                circleContent.setShareImage(null);
                //                circleContent.setTitle(null);
                //                circleContent.setShareContent(null);
                //                mController.setShareMedia(null);
                editDialog.dismiss();
                break;
            case R.id.qq_friend:
                //添加QQ
                QQShareContent qqContent = new QQShareContent();
                qqContent.setTitle(topic.getName());
                qqContent.setShareContent(topic.getContent());
                if (bitmap != null)
                {
                    qqContent.setShareMedia(new UMImage(context, bitmap));
                }
                else
                {
                    loadPic();
                    qqContent.setShareMedia(img);
                }
                qqContent.setTargetUrl(share_url);
                mController.getConfig().closeToast();
                mController.setShareMedia(qqContent);
                mController.postShare(context, SHARE_MEDIA.QQ, setPostListener());
                mController.getConfig().closeToast();
                editDialog.dismiss();
                break;
            case R.id.qq_circle:
                //添加QQ空间
                QZoneShareContent qZoneContent = new QZoneShareContent();
                qZoneContent.setTitle(topic.getName());
                qZoneContent.setShareContent(topic.getContent());
                
                if (bitmap != null)
                {
                    qZoneContent.setShareMedia(new UMImage(context, bitmap));
                }
                else
                {
                    loadPic();
                    qZoneContent.setShareMedia(img);
                }
                qZoneContent.setTargetUrl(share_url);
                mController.getConfig().closeToast();
                mController.setShareMedia(qZoneContent);
                mController.postShare(context, SHARE_MEDIA.QZONE, setPostListener());
                mController.getConfig().closeToast();
                editDialog.dismiss();
                break;
            case R.id.xinlang:
//                ToastUtil.makeText(context, "暂未开通！");
                sinaContent = null;
                sinaContent = new SinaShareContent();
                sinaContent.setShareContent(sbBuffer_weibo.toString());
                if (bitmap != null)
                {
                    sinaContent.setShareMedia(new UMImage(context, bitmap));
                }
                else
                {
                    loadPic();
                    sinaContent.setShareMedia(img);
                }
                mController.setShareMedia(sinaContent);
                if (OauthHelper.isAuthenticated(context, SHARE_MEDIA.SINA))
                {
                    mController.postShare(context, SHARE_MEDIA.SINA, setPostListener());
                }
                else
                {
                    mController.doOauthVerify(context, SHARE_MEDIA.SINA, new UMAuthListener()
                    {
                        @Override
                        public void onError(SocializeException e, SHARE_MEDIA platform)
                        {
                        }
                        
                        @Override
                        public void onComplete(Bundle value, SHARE_MEDIA platform)
                        {
                            mController.postShare(context, SHARE_MEDIA.SINA, setPostListener());
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
                editDialog.dismiss();
                break;
            default:
                editDialog.dismiss();
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
                        topicNet();
                    }
                    oTime = currentTime;
                    
                }
//                if (platform.equals(SHARE_MEDIA.RENREN))
//                {
//                    editDialog.dismiss();
//                    
//                }
                if (platform.equals(SHARE_MEDIA.SINA))
                {
                    mController.unregisterListener(this);
                    if (eCode == 200)
                    {
                        ToastUtil.makeText(context, "发送成功");
                    }
                    else
                    {
                        ToastUtil.makeText(context, "发送失败");
                    }
                    
                    if (sinaContent != null)
                    {
                        sinaContent.setShareImage(null);
                        mController.setShareMedia(null);
                    }
                    editDialog.dismiss();
                }
//                else if (platform.equals(SHARE_MEDIA.TENCENT))
//                {
//                    mController.unregisterListener(this);
//                    editDialog.dismiss();
//                }
//                else if (platform.equals(SHARE_MEDIA.SMS))
//                {
//                    mController.unregisterListener(this);
//                    editDialog.dismiss();
//                }
                else if (platform.equals(SHARE_MEDIA.QZONE))
                {
                    mController.unregisterListener(this);
                    editDialog.dismiss();
                }
                else if (platform.equals(SHARE_MEDIA.QQ))
                {
                    mController.unregisterListener(this);
                    editDialog.dismiss();
                }
                else if (platform.equals(SHARE_MEDIA.WEIXIN))
                {
                    mController.unregisterListener(this);
                    if (eCode == 200)
                    {
                        ToastUtil.makeText(context, "发送成功");
                    }
                    else
                    {
                        ToastUtil.makeText(context, "发送失败");
                    }
                    editDialog.dismiss();
                }
                else if (platform.equals(SHARE_MEDIA.WEIXIN_CIRCLE))
                {
                    mController.unregisterListener(this);
                    if (eCode == 200)
                    {
                        ToastUtil.makeText(context, "发送成功");
                    }
                    else
                    {
                        ToastUtil.makeText(context, "发送失败");
                    }
                    editDialog.dismiss();
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
                editDialog.dismiss();
            }
        }
        
    }
    
    public void topicNet()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("id", topic.getArticleId());
        ConnectService.instance().connectServiceReturnResponse(context,
            param,
            this,
            BaseResponse.class,
            URLUtil.COMMUNITY_SHARE_TOPIC,
            Constants.ENCRYPT_NONE);
    }
    
    public void loadPic()
    {
        img = new UMImage(context, R.drawable.community_default);
    }
    
}
