/*
 * 文 件 名:  ServiceItemResponse.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-12-25
 
 */
package com.ymdq.thy.bean.propertyservice;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-12-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ServiceItemResponse extends BaseResponse
{
    private ArrayList<ServiceItemDoc> doc;

    public ArrayList<ServiceItemDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<ServiceItemDoc> doc)
    {
        this.doc = doc;
    }
}
