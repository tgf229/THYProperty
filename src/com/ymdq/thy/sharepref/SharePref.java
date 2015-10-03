package com.ymdq.thy.sharepref;

import android.content.Context;
import android.content.SharedPreferences;

import com.ymdq.thy.JRApplication;
import com.ymdq.thy.constant.Constants;

/**
 * 
 * <公共存储类>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-4]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SharePref
{
    /**
     * app是否开启
     */
    public static final String APP_OPEN = "app_open";
    
    /**
     * app是否开启引导页
     */
    public static final String APP_GUIDE = "app_guide";
    
    /**
     * 用户ID
     */
    public static final String USER_ID = "userId";
    
    /**
     * 用户等级
     */
    public static final String USER_LEVEL = "user_level";
    
    /**
     * 游客选择小区ID
     */
    public static final String TOURIST_COMMUNITY_ID = "cId";
    
    /**
     * 登录的用户选择的小区
     */
    public static final String USER_COMMUNITY_ID = "uCId";
    
    /**
     * 平台密钥
     */
    public static final String BASE_KEY = "baseKey";
    
    /**
     * 平台密钥
     */
    public static final String LOGIN_TYPE = "login_type";
    
    /**
     * 用户名
     */
    public static final String LOGIN_USERNAME = "login_username";
    
    /**
     * 密码
     */
    public static final String LOGIN_PASSWORD = "login_password";
    
    /**
     * 昵称
     */
    public static final String USER_NICKNAME = "user_nickname";
    
    /**
     * 头像
     */
    public static final String USER_IMAGE = "user_image";
    
    /**
     * 姓名
     */
    public static final String USER_NAME = "user_name";
    
    /**
     * 性别
     */
    public static final String USER_SEX = "user_sex";
    
    /**
     * 生日
     */
    public static final String USER_BIRTH = "user_birth";
    
    /**
     * 保存推送设置
     */
    public static final String SET_MESSAGE = "set_push";
    
    public static void saveBoolean(String key, boolean value)
    {
        getSharedPreferences().edit().putBoolean(key, value).commit();
    }
    
    public static boolean getBoolean(String key, boolean defvalue)
    {
        return getSharedPreferences().getBoolean(key, defvalue);
    }
    
    /**
     * Save a string value to the shared preference.
     * 
     * @param key
     *            to mark the store value.
     * @param value
     *            to saved value.
     */
    public static void saveString(String key, String value)
    {
        getSharedPreferences().edit().putString(key, value).commit();
    }
    
    /**
     * Get the specified value through the key value.
     * 
     * @param key
     *            to retrieve the value.
     * @return the string value returned.
     */
    public static String getString(String key, String def)
    {
        return getSharedPreferences().getString(key, def);
    }
    
    /**
     * Save a integer value to the shared preference.
     * 
     * @param key
     *            to mark the store value.
     * @param value
     *            to saved value.
     */
    public static void saveInt(String key, int value)
    {
        getSharedPreferences().edit().putInt(key, value).commit();
        
    }
    
    /**
     * Get the specified value through the key value.
     * 
     * @param key
     *            to retrieve the value.
     * @return the integer value returned.
     */
    public static int getInt(String key, int def)
    {
        return getSharedPreferences().getInt(key, def);
    }
    
    /**
     * Save a Long value to the shared preference.
     * 
     * @param key
     *            to mark the store value.
     * @param value
     *            to saved value.
     */
    public static void saveLong(String key, long value)
    {
        getSharedPreferences().edit().putLong(key, value).commit();
    }
    
    /**
     * Get the specified value through the key value.
     * 
     * @param key
     *            to retrieve the value.
     * @return the integer value returned.
     */
    public static long getLong(String key, long def)
    {
        return getSharedPreferences().getLong(key, def);
    }
    
    /**
     * Retrieve the package shared preferences object.
     * 
     * @param MyApp
     *            .appContext
     * @return
     */
    private static SharedPreferences getSharedPreferences()
    {
        return JRApplication.jrApplication.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);
    }
}
