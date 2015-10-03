package com.ymdq.thy.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * <一句话功能简述>判断网络连接状态工具类
 * <功能详细描述>
 * 
 * @author  wanghl
 * @version  [版本号, 2014年4月18日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HttpUtils
{
    /**
     * 
     * <网络是否可用>
     * <功能详细描述>
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNetworkConnected(Context context)
    {
        if (context != null)
        {
            ConnectivityManager mConnectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null)
            {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    
    public static boolean isGpsOpen(Context context)
    {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）  
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gps;
    }
    
    /**
     * 
     * <获取网络类型>
     * <获取当前的网络状态  -1：没有网络  1：WIFI网络2：wap网络3：net网络>
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getConnectedType(Context context)
    {
        if (context != null)
        {
            ConnectivityManager mConnectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable())
            {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
