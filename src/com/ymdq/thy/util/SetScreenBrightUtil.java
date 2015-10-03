package com.ymdq.thy.util;

import android.content.Context;
import android.provider.Settings;
/**
 * 
 * <设置和获取屏幕亮度>
 * <功能详细描述>
 * 
 * @author  jiangfei
 * @version  [版本号, 2014-7-9]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SetScreenBrightUtil
{
    private Context context = null;
    
    public SetScreenBrightUtil(Context context)
    {
        this.context = context;
    }
    
    /** 
     * 获得当前屏幕亮度的模式     
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度 
     */
    public int getScreenMode()
    {
        int screenMode = 0;
        try
        {
            screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        }
        catch (Exception localException)
        {
            
        }
        return screenMode;
    }
    
    /** 
     * 获得当前屏幕亮度值  0--255 
     */
    public int getScreenBrightness()
    {
        int screenBrightness = 255;
        try
        {
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Exception localException)
        {
            
        }
        return screenBrightness;
    }
    
    /** 
     * 设置当前屏幕亮度的模式     
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度 
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度 
     */
    public void setScreenMode(int paramInt)
    {
        try
        {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
    }
    
    /** 
     * 设置当前屏幕亮度值  0--255 
     */
    public void saveScreenBrightness(int paramInt)
    {
        try
        {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
    }
    
    /** 
     * 保存当前的屏幕亮度值，并使之生效 
     */
    private void setScreenBrightness(int paramInt)
    {
        //        Window localWindow = getWindow();
        //        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        //        float f = paramInt / 255.0F;
        //        localLayoutParams.screenBrightness = f;
        //        localWindow.setAttributes(localLayoutParams);
    }
}
