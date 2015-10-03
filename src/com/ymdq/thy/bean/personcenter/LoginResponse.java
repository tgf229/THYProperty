package com.ymdq.thy.bean.personcenter;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  user
 * @version  [版本号, 2014-11-19]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoginResponse extends BaseResponse
{
    /**
     * 平台密钥
     */
    private String baseKey;
    
    /**
     * 用户ID
     */
    private String uId;
    
    /**
     * 小区ID
     */
    private String cId;
    
    /**
     * 用户等级  0普通用户  1大V 2超级管理员
     */
    private String userLevel;
    
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
    
    public String getBaseKey()
    {
        return baseKey;
    }
    
    public void setBaseKey(String baseKey)
    {
        this.baseKey = baseKey;
    }
    
    public String getuId()
    {
        return uId;
    }
    
    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public String getcId()
    {
        return cId;
    }

    public void setcId(String cId)
    {
        this.cId = cId;
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
