/*
 * 文 件 名:  HouseUserInfoDoc.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-27
 
 */
package com.ymdq.thy.bean.personcenter;

import java.io.Serializable;

/**
 * <账号列表信息>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-27]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseUserInfoDoc implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3837470168211775597L;
    
    /**
     * 用户ID
     */
    private String uId;
    
    /**
     * 用户电话
     */
    private String phone;
    
    /**
     * 昵称
     */
    private String nickName;
    
    /**
     * 用户头像
     */
    private String image;
    
    /**
     * 用户级别
     */
    private String level;
    
    /**
     * 状态
     */
    private String status;

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
