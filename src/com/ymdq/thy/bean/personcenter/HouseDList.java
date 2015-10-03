/*
 * 文 件 名:  HouseDoc.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-20
 
 */
package com.ymdq.thy.bean.personcenter;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  user
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseDList implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -1970278738552973649L;
    
    /**
     * 房屋ID
     */
    private String hId;
    
    /**
     * 房号
     */
    private String hName;
    
    /**
     * 业主身份证号
     */
    private String idCard;
    
    /**
     * 业主预留手机号
     */
    private String phone;
    
    public String gethId()
    {
        return hId;
    }
    
    public void sethId(String hId)
    {
        this.hId = hId;
    }
    
    public String gethName()
    {
        return hName;
    }
    
    public void sethName(String hName)
    {
        this.hName = hName;
    }
    
    public String getIdCard()
    {
        return idCard;
    }
    
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
}
