/*
 * 文 件 名:  HouseInfoDoc.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-25
 
 */
package com.ymdq.thy.bean.personcenter;

import java.io.Serializable;

/**
 * <账号绑定房屋列表信息>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseInfoDoc implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6654178200101947595L;
    
    /**
     * 小区ID
     */
    private String cId;
    
    /**
     * 小区名称
     */
    private String cName;
    
    /**
     * 栋ID
     */
    private String bId;
    
    /**
     * 栋名称
     */
    private String bName;
    
    /**
     * 单元ID
     */
    private String dId;
    
    /**
     * 单元名称
     */
    private String dName;
    
    /**
     * 房屋ID
     */
    private String hId;
    
    /**
     * 房号
     */
    private String hName;
    
    /**
     * 账号状态
     */
    private String flag;
    
    public String getFlag()
    {
        return flag;
    }
    
    public void setFlag(String flag)
    {
        this.flag = flag;
    }
    
    public String getcId()
    {
        return cId;
    }
    
    public void setcId(String cId)
    {
        this.cId = cId;
    }
    
    public String getcName()
    {
        return cName;
    }
    
    public void setcName(String cName)
    {
        this.cName = cName;
    }
    
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
    
}
