package com.ymdq.thy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.sharepref.SharePref;
import com.ymdq.thy.ui.home.MyCentralMeeageActivity;
import com.ymdq.thy.ui.home.WelcomeActivity;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JRReceiver extends BroadcastReceiver
{
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        
        SharedPreferences pref = context.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
        
        boolean openApp = pref.getBoolean(SharePref.APP_OPEN, false);
        
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
        {
            if (openApp)
            {
                //打开自定义的Activity
                Intent i = new Intent(context, MyCentralMeeageActivity.class);
                i.putExtras(bundle);
                i.putExtra("from_jpush_noti", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
            else
            {
                Intent i = new Intent(context, WelcomeActivity.class);
                i.putExtras(bundle);
                i.putExtra("from_jpush", true);
                i.putExtra("from_jpush_noti", true);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
            
        }
    }
}
