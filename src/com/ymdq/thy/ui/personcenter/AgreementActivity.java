package com.ymdq.thy.ui.personcenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;

@SuppressLint("JavascriptInterface")
public class AgreementActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    private WebView web;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    private String mainUrl = URLUtil.UN_OPEN_URL;
    
    private Handler mHandler = new Handler();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agreement);
        initTitle();
        init();
        webview();
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setBackgroundResource(R.drawable.title_yinsi);
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                AgreementActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        String url = getIntent().getStringExtra("wev_view_url");
        
        if (GeneralUtils.isNotNullOrZeroLenght(url))
        {
            mainUrl = url;
        }
        
        web = (WebView)findViewById(R.id.webview_helper_web);
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private void webview()
    {
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAllowFileAccess(true);
        //        web.getSettings().setPluginsEnabled(true);
        web.getSettings().setSupportZoom(true);
        web.getSettings().setAppCacheEnabled(true);
        web.getSettings().setBuiltInZoomControls(true);
        // web.setInitialScale(25);
        loadurl(web, mainUrl);
        
        web.setWebChromeClient(new CustomWebChromeClient());
        web.addJavascriptInterface(new JavaScriptInterface(), "jsExtend");
        web.setWebViewClient(new WebViewClient()
        {
            
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                loadurl(view, url.trim());// 载入网页
                return true;
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                dailog.dismissDialog();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
            
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                dailog.dismissDialog();
            }
            
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                dailog.dismissDialog();
            }
        });
        
    }
    
    private void loadurl(final WebView view, final String url)
    {
        view.loadUrl(url);// 载入网页
    }
    
    @Override
    protected void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
        web.clearView();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (web.canGoBack())
            {
                web.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * Provides a hook for calling "alert" from javascript
     */
    final class CustomWebChromeClient extends WebChromeClient
    {
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result)
        {
            // Log.d(TAG, message);
            new AlertDialog.Builder(AgreementActivity.this).setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        result.confirm();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
            return true;
            // result.confirm();
            // return true;
        }
        
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result)
        {
            new AlertDialog.Builder(AgreementActivity.this).setTitle(R.string.app_name)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        result.confirm();
                        // finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        result.cancel();
                    }
                })
                .create()
                .show();
            return true;
        };
    }
    
    final class JavaScriptInterface
    {
        JavaScriptInterface()
        {
        }
        
        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        public void clickOnAndroid()
        {
            mHandler.post(new Runnable()
            {
                public void run()
                {
                    web.loadUrl("javascript:wave()");
                }
            });
        }
        
        public void exit()
        {
            finish();
        }
        
        public String getConfirmMsg()
        {
            return "确定要退出吗?";
        }
    }
    
}
