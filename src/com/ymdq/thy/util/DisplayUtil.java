package com.ymdq.thy.util;

import android.content.Context;

/**
 * 
 * <dp、sp、px转换的工具类>
 * <功能详细描述>
 * 
 * @author  qiuqiaohua
 * @version  [版本号, Apr 19, 2014]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DisplayUtil {
     
    /**
     * 
     * <将px值转换为dip或dp值，保证尺寸大小不变>
     * <功能详细描述>
     * @param context
     * @param pxValue   DisplayMetrics类中属性density
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

   
    /**
     * 
     * <将dip或dp值转换为px值，保证尺寸大小不变>
     * <功能详细描述>
     * @param context
     * @param dipValue  DisplayMetrics类中属性density
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 
     * <将px值转换为sp值，保证文字大小不变>
     * <功能详细描述>
     * @param context
     * @param pxValue   DisplayMetrics类中属性scaledDensity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 
     * <将sp值转换为px值，保证文字大小不变>
     * <功能详细描述>
     * @param context
     * @param spValue   DisplayMetrics类中属性scaledDensity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}