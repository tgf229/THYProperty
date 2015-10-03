package com.ymdq.thy.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil
{
    
    /** 
     */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
    
    /** 
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
    
    /**
     * 
     * <获取屏幕像素x>
     * <功能详细描述>
     * @param activity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getXScreenpx(Activity activity)
    {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    
    /**
     * 
     * <获取屏幕像素y>
     * <功能详细描述>
     * @param activity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getYScreenpx(Activity activity)
    {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
