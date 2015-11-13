/*
 * 文 件 名:  PraiseTagDoc.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-11-12
 
 */
package com.ymdq.thy.bean.propertyservice;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-11-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PraiseTagDoc implements Serializable
{
    private String tId;
    private String tName;
    private String tStatus;
    public String gettId()
    {
        return tId;
    }
    public void settId(String tId)
    {
        this.tId = tId;
    }
    public String gettName()
    {
        return tName;
    }
    public void settName(String tName)
    {
        this.tName = tName;
    }
    public String gettStatus()
    {
        return tStatus;
    }
    public void settStatus(String tStatus)
    {
        this.tStatus = tStatus;
    }
}
