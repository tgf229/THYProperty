package com.ymdq.thy.ui;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.ymdq.thy.JRApplication;
import com.ymdq.thy.callback.UICallBack;

/**
 * 
 * <基础类> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-3-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BaseActivity extends Activity implements UICallBack
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //动态设置屏幕方向 强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        
        JRApplication.jrApplication.addActivity(this);
    }
    
    
    
    @Override
    public void netBack(Object ob)
    {
        
    }
    
    @Override
    protected void onResume()
    {
        JRApplication.currentActivity = this.getClass().getName();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
    
    @Override
    protected void onDestroy()
    {
        JRApplication.jrApplication.deleteActivity(this);
        super.onDestroy();
    }
    
}
