/*
 * 文 件 名:  PraiseListResponse.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-11-11
 
 */
package com.ymdq.thy.bean.propertyservice;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-11-11]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PraiseListResponse extends BaseResponse
{
    private String praiseTimes;
    private String currentTime;
    private ArrayList<PraiseListDoc> doc;
    public String getPraiseTimes()
    {
        return praiseTimes;
    }
    public void setPraiseTimes(String praiseTimes)
    {
        this.praiseTimes = praiseTimes;
    }
    public String getCurrentTime()
    {
        return currentTime;
    }
    public void setCurrentTime(String currentTime)
    {
        this.currentTime = currentTime;
    }
    public ArrayList<PraiseListDoc> getDoc()
    {
        return doc;
    }
    public void setDoc(ArrayList<PraiseListDoc> doc)
    {
        this.doc = doc;
    }
}
