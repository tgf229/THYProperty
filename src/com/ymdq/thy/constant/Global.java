package com.ymdq.thy.constant;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.bean.personcenter.LoginResponse;
import com.ymdq.thy.jni.JNIInterface;
import com.ymdq.thy.network.NetWork;
import com.ymdq.thy.sharepref.SharePref;
import com.ymdq.thy.util.CMLog;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;

/**
 * 
 * <全局静态缓存数据>
 * <功能详细描述>
 * 
 * @author  wt
 * @version  [版本号, 2014-11-4]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Global
{
    
    private static final String vip = "201412250933001234567890";
    
    public static String getUserId()
    {
        return SharePref.getString(SharePref.USER_ID, "");
    }
    
    public static void saveUserId(String userId)
    {
        SharePref.saveString(SharePref.USER_ID, userId);
    }
    
    public static void saveUserLevel(String isV)
    {
        SharePref.saveString(SharePref.USER_LEVEL, isV);
    }
    
    public static String getUserLevel()
    {
        return  SharePref.getString(SharePref.USER_LEVEL, "");
    }
    
    /**
     * 
     * <此用户是否为超级管理员>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isSuper()
    {
        if(isLogin() && getUserLevel().equals("2"))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 
     * <获取游客选择的小区，默认为1>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static String getTCId()
    {
        return SharePref.getString(SharePref.TOURIST_COMMUNITY_ID, "1");
    }
    
    /**
     * 保存游客选择的小区
     * <一句话功能简述>
     * <功能详细描述>
     * @param cid
     * @see [类、类#方法、类#成员]
     */
    public static void saveCId(String cid)
    {
        SharePref.saveString(SharePref.TOURIST_COMMUNITY_ID, cid);
    }
    
    /**
     * 
     * <获取登录的用户选择的小区>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getUCId()
    {
        return SharePref.getString(SharePref.USER_COMMUNITY_ID, "");
    }
    
    /**
     * 
     * <保存登录的用户选择的小区>
     * <功能详细描述>
     * @param ucId
     * @see [类、类#方法、类#成员]
     */
    public static void saveUCId(String ucId)
    {
        SharePref.saveString(SharePref.USER_COMMUNITY_ID, ucId);
    }
    
    /**
     * 
     * <获取游客或登录的用户当前的小区>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public static String getCId()
    {
        if(Global.isLogin())
        {
            return getUCId();
        }
        return getTCId();
    }
    
    /**
     * 引导页
     * <一句话功能简述>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getUserGuide()
    {
        return SharePref.getString(SharePref.APP_GUIDE, "");
    }
    
    public static void saveUserGuide(String guide)
    {
        SharePref.saveString(SharePref.APP_GUIDE, guide);
    }
    
    /**
     * 
     * <获取平台密钥>
     * <功能详细描述>
     * @param context
     * @return
     * @throws Exception 
     * @see [类、类#方法、类#成员]
     */
    public static String getBaseKey()
        throws Exception
    {
        String baseKey = SharePref.getString(SharePref.BASE_KEY, "");
        return SecurityUtils.decode2Str(baseKey, Global.getKey());
    }
    
    public static void saveBaseKey(String baseKey)
    {
        SharePref.saveString(SharePref.BASE_KEY, baseKey);
    }
    
    /**
     * 获取登录状态
     */
    public static boolean isLogin()
    {
        return SharePref.getBoolean(SharePref.LOGIN_TYPE, false);
    }
    
    public static void setIsLogin(boolean isLogin)
    {
        SharePref.saveBoolean(SharePref.LOGIN_TYPE, isLogin);
    }
    
    /**
     * app是否开启
     */
    public static boolean getAppOpen()
    {
        return SharePref.getBoolean(SharePref.APP_OPEN, false);
    }
    
    public static void setAppOpen(boolean isOpen)
    {
        SharePref.saveBoolean(SharePref.APP_OPEN, isOpen);
    }
    
    /**
     * 获取用户名
     */
    public static String getUserName()
    {
        return SharePref.getString(SharePref.LOGIN_USERNAME, "");
    }
    
    public static void saveUserName(String username)
    {
        SharePref.saveString(SharePref.LOGIN_USERNAME, username);
    }
    
    /**
     * 获取密码
     */
    public static String getPassword()
    {
        return SharePref.getString(SharePref.LOGIN_PASSWORD, "");
    }
    
    public static void savePassword(String password)
    {
        SharePref.saveString(SharePref.LOGIN_PASSWORD, password);
    }
    
    /**
     * 获取推送状态
     */
    public static boolean getPush()
    {
        return SharePref.getBoolean(SharePref.SET_MESSAGE, true);
    }
    
    public static void savePush(boolean push)
    {
        SharePref.saveBoolean(SharePref.SET_MESSAGE, push);
    }
    
    /**
     * 获取本地密钥
     * <一句话功能简述>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getKey()
    {
//        return JNIInterface.getAppName();
        return vip;
    }
    
    /**
     * 
     * <获取昵称>
     * <功能详细描述>
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getNickName()
    {
        return SharePref.getString(SharePref.USER_NICKNAME, "");
    }
    
    public static void saveNickName(String nickname)
    {
        SharePref.saveString(SharePref.USER_NICKNAME, nickname);
    }
    
    /**
     * 
     * <获取头像>
     * <功能详细描述>
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getImage()
    {
        return SharePref.getString(SharePref.USER_IMAGE, "");
    }
    
    public static void saveImage(String image)
    {
        SharePref.saveString(SharePref.USER_IMAGE, image);
    }
    
    /**
     * 
     * <获取姓名>
     * <功能详细描述>
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getName()
    {
        return SharePref.getString(SharePref.USER_NAME, "");
    }
    
    public static void saveName(String name)
    {
        SharePref.saveString(SharePref.USER_NAME, name);
    }
    
    /**
     * 
     * <获取性别>
     * <功能详细描述>
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getSex()
    {
        return SharePref.getString(SharePref.USER_SEX, "");
    }
    
    public static void saveSex(String sex)
    {
        SharePref.saveString(SharePref.USER_SEX, sex);
    }
    
    /**
     * 
     * <获取生日>
     * <功能详细描述>
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getBirth()
    {
        return SharePref.getString(SharePref.USER_BIRTH, "");
    }
    
    public static void saveBirth(String birth)
    {
        SharePref.saveString(SharePref.USER_BIRTH, birth);
    }
    
    /**
     * 
     * <保存登陆信息>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public static void saveData(LoginResponse loginResponse, String userName, String password, boolean isAuto)
    {
        Global.saveUserName(userName);
        if (isAuto)
        {
            Global.savePassword(password);
        }
        else
        {
            Global.savePassword(SecurityUtils.get32MD5Str(password));
        }
        Global.saveUserId(loginResponse.getuId());
        Global.saveBaseKey(loginResponse.getBaseKey());
        Global.setIsLogin(true);
        Global.saveNickName(loginResponse.getNickName());
        Global.saveImage(loginResponse.getImage());
        Global.saveName(loginResponse.getName());
        Global.saveSex(loginResponse.getSex());
        Global.saveBirth(loginResponse.getBirth());
        Global.saveUserLevel(loginResponse.getUserLevel());
        Intent intent = new Intent(Constants.LOGIN_SUCCESS_BROADCAST);
        if(GeneralUtils.isNotNullOrZeroLenght(loginResponse.getcId()))
        {
            Global.saveUCId(loginResponse.getcId());
            Global.saveCId(loginResponse.getcId());
            intent.putExtra("replace_cid", true);
        }
        JRApplication.jrApplication.sendBroadcast(intent);
    }
    
    /**
     * <退出登陆>
     * <功能详细描述>
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void loginOut()
    {
        //保存到配置文件
        Global.saveBaseKey("");
        Global.setIsLogin(false);
        Global.saveUserId("");
        Global.saveUserLevel("");
        Global.savePassword("");
        Global.saveNickName("");
        Global.saveImage("");
        Global.saveName("");
        Global.saveSex("");
        Global.saveBirth("");
        Global.saveUCId("");
        JRApplication.jrApplication.sendBroadcast(new Intent(Constants.LOGINOUT_SUCCESS_BROADCAST));
    }
    
    /**
     * <退出应用>
     * <功能详细描述>
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void logoutApplication()
    {
        try
        {
            //保存到配置文件
            Global.saveBaseKey("");
            Global.setIsLogin(false);
            Global.saveUserId("");
            Global.saveNickName("");
            Global.saveImage("");
            Global.saveName("");
            Global.saveSex("");
            Global.saveBirth("");
            Global.setAppOpen(false);
            for (Activity activity : JRApplication.activitys)
            {
                activity.finish();
                activity = null;
            }
            JRApplication.activitys.clear();
        }
        catch (Exception e)
        {
            CMLog.e("", "finish activity exception:" + e.getMessage());
        }
        finally
        {
            NetWork.shutdown();
            ImageLoader.getInstance().clearMemoryCache();
            System.exit(0);
        }
    }
    
    /**
     * 别名设置
     */
    public static void setAliasApp(Context context, String alias)
    {
        JPushInterface.setAlias(context, alias, new TagAliasCallback()
        {
            
            @Override
            public void gotResult(int result, String arg1, Set<String> arg2)
            {
                if (result == 0)
                {
                    CMLog.i("info", "别名设置成功");
                }
                else
                {
                    CMLog.i("info", "别名设置失败");
                }
            }
        });
    }
}
