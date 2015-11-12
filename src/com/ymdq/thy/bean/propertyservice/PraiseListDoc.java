/*
 * 文 件 名:  PraiseListDoc.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-11-11
 
 */
package com.ymdq.thy.bean.propertyservice;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-11-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PraiseListDoc implements Serializable
{
    private String eId;
    private String eName;
    private String eNum;
    private String eImageUrl;
    private String eDepName;
    private String praise;
    private String top;
    private String isPraise;
    public String geteId()
    {
        return eId;
    }
    public void seteId(String eId)
    {
        this.eId = eId;
    }
    public String geteName()
    {
        return eName;
    }
    public void seteName(String eName)
    {
        this.eName = eName;
    }
    public String geteNum()
    {
        return eNum;
    }
    public void seteNum(String eNum)
    {
        this.eNum = eNum;
    }
    public String geteImageUrl()
    {
        return eImageUrl;
    }
    public void seteImageUrl(String eImageUrl)
    {
        this.eImageUrl = eImageUrl;
    }
    public String geteDepName()
    {
        return eDepName;
    }
    public void seteDepName(String eDepName)
    {
        this.eDepName = eDepName;
    }
    public String getPraise()
    {
        return praise;
    }
    public void setPraise(String praise)
    {
        this.praise = praise;
    }
    public String getTop()
    {
        return top;
    }
    public void setTop(String top)
    {
        this.top = top;
    }
    public String getIsPraise()
    {
        return isPraise;
    }
    public void setIsPraise(String isPraise)
    {
        this.isPraise = isPraise;
    }
}
