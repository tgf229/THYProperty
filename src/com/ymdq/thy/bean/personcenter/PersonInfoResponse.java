/*
 * 文 件 名:  PersonInfoResponse.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-25
 
 */
package com.ymdq.thy.bean.personcenter;

import java.io.Serializable;

import com.ymdq.thy.bean.BaseResponse;

/**
 * <个人信息bean类>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PersonInfoResponse extends BaseResponse implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3853007204188619954L;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 头像地址
     */
    private String image;
    
    /**
     * 昵称
     */
    private String nickName;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 性别
     */
    private String sex;
    
    /**
     * 生日
     */
    private String birth;
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getImage()
    {
        return image;
    }
    
    public void setImage(String image)
    {
        this.image = image;
    }
    
    public String getNickName()
    {
        return nickName;
    }
    
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getSex()
    {
        return sex;
    }
    
    public void setSex(String sex)
    {
        this.sex = sex;
    }
    
    public String getBirth()
    {
        return birth;
    }
    
    public void setBirth(String birth)
    {
        this.birth = birth;
    }
}
