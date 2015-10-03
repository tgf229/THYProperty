package com.ymdq.thy.bean.community;

import java.io.Serializable;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <用户—用户基本信息查询>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GroupPersonInfoResponse extends BaseResponse implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    private String uId;
    
    private String nickName;
    
    private String imageUrl;
    
    private String desc;
    
    /**
     * 0普通用户
     * 1大V
     * 2超级管理员
     */
    private String userLevel;
    
    public String getuId()
    {
        return uId;
    }
    
    public void setuId(String uId)
    {
        this.uId = uId;
    }
    
    public String getNickName()
    {
        return nickName;
    }
    
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }
    
    public String getImageUrl()
    {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public String getUserLevel()
    {
        return userLevel;
    }
    
    public void setUserLevel(String userLevel)
    {
        this.userLevel = userLevel;
    }
    
}
