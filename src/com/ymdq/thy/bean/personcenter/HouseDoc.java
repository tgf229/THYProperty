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
 * 楼栋列表
 * <功能详细描述>
 * 
 * @author  wt
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseDoc implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3864894373555716703L;
    
    /**
     * 栋ID
     */
    private String bId;
    
    /**
     * 栋名称
     */
    private String bName;
    
    private List<HouseBList> bList;
    
    public String getbId()
    {
        return bId;
    }
    
    public void setbId(String bId)
    {
        this.bId = bId;
    }
    
    public String getbName()
    {
        return bName;
    }
    
    public void setbName(String bName)
    {
        this.bName = bName;
    }
    
    public List<HouseBList> getbList()
    {
        return bList;
    }
    
    public void setbList(List<HouseBList> bList)
    {
        this.bList = bList;
    }
}
