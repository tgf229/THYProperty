package com.ymdq.thy.bean.community;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * <圈子成员列表>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Member implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private String uId;
    
    /**
     * 用户昵称
     */
    @SerializedName("nickName")
    private String name;
    
    /**
     * 用户头像
     */
    private String image;
    
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
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
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
