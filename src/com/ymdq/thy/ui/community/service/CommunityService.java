package com.ymdq.thy.ui.community.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.ymdq.thy.bean.community.GroupDeleteResponse;
import com.ymdq.thy.bean.community.GroupPersonInfoResponse;
import com.ymdq.thy.bean.community.GroupPraiseResponse;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.bean.community.TopicUpOrDownResponse;
import com.ymdq.thy.bean.community.TopipDeleteResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.util.SecurityUtils;

/**
 * 
 * <邻里网络层>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityService
{
    private static class CommunityServiceHolder
    {
        private static CommunityService CommunityServiceSingleton = new CommunityService();
        
    }
    
    public static CommunityService instance()
    {
        return CommunityServiceHolder.CommunityServiceSingleton;
    }
    
    /**
     * 
     * <点赞/取消赞>
     * <功能详细描述>
     * @param position
     * @see [类、类#方法、类#成员]
     */
    public void addOrCancelPraise(String id, String type, Context context, UICallBack callback)
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(id));
            param.put("type", SecurityUtils.encode2Str(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(context,
            param,
            callback,
            GroupPraiseResponse.class,
            URLUtil.COMMUNITY_ADD_PRAISE,
            Constants.ENCRYPT_SIMPLE);
    }
    
    /**
     * 
     * <话题—话题投票>
     * <功能详细描述>
     * @param position
     * @see [类、类#方法、类#成员]
     */
    public void agreeOrDisagree(String id, String type, Context context, UICallBack callback)
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(id));
            param.put("type", SecurityUtils.encode2Str(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(context,
            param,
            callback,
            GroupPraiseResponse.class,
            URLUtil.COMMUNITY_TOPIC_ARGEE,
            Constants.ENCRYPT_SIMPLE);
    }
    
    /**
     * 
     * <话题—话题详情查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void topicDetails(String id, Context context, UICallBack callback)
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
            param.put("uId", Global.getUserId());
        param.put("id", id);
        ConnectService.instance().connectServiceReturnResponse(context,
            param,
            callback,
            Topic.class,
            URLUtil.COMMUNITY_TOPIC_DETAILS,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <话题置顶&取消置顶>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void uPOrDownTopic(String id, String type, String GroupId, Context context, UICallBack callback)
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(GroupId));
            param.put("corpId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(id));
            param.put("type", SecurityUtils.encode2Str(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(context,
            param,
            callback,
            TopicUpOrDownResponse.class,
            URLUtil.COMMUNITY_UPORDELETE_TOPIC,
            Constants.ENCRYPT_SIMPLE);
    }
    
    /**
     * 
     * <话题删除>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void deleteTopic(String id, String type, String GroupId, Context context, UICallBack callback)
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(GroupId));
            param.put("corpId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(id));
            param.put("type", SecurityUtils.encode2Str(type));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(context,
            param,
            callback,
            TopipDeleteResponse.class,
            URLUtil.COMMUNITY_UPORDELETE_TOPIC,
            Constants.ENCRYPT_SIMPLE);
    }
    
    /**
     * 
     * <用户基本信息查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void queryPersonInfo(String queryUId, Context context, UICallBack callback)
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("uId", Global.getUserId());
        }
        param.put("queryUId", queryUId);
        param.put("cId", Global.getCId());
        ConnectService.instance().connectServiceReturnResponse(context,
            param,
            callback,
            GroupPersonInfoResponse.class,
            URLUtil.COMMUNITY_QUERY_PERSONINFO,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <用户基本信息查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void deleteGroup(String id, Context context, UICallBack callback)
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(context,
            param,
            callback,
            GroupDeleteResponse.class,
            URLUtil.COMMUNITY_DELETE_GROUP,
            Constants.ENCRYPT_SIMPLE);
    }
}
