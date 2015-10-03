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
import java.util.List;

/**
 * 单元列表
 * <功能详细描述>
 * 
 * @author  wt
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseBList implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8781403120692871713L;
    
    /**
     * 单元ID
     */
    private String dId;
    
    /**
     * 单元名称
     */
    private String dName;
    
    private List<HouseDList> dList;
    
    public String getdId()
    {
        return dId;
    }
    
    public void setdId(String dId)
    {
        this.dId = dId;
    }
    
    public String getdName()
    {
        return dName;
    }
    
    public void setdName(String dName)
    {
        this.dName = dName;
    }
    
    public List<HouseDList> getdList()
    {
        return dList;
    }
    
    public void setdList(List<HouseDList> dList)
    {
        this.dList = dList;
    }
    
}
