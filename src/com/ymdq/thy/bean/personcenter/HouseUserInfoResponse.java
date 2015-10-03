/*
 * 文 件 名:  HouseUserInfoResponse.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-27
 
 */
package com.ymdq.thy.bean.personcenter;

import java.util.List;

import com.ymdq.thy.bean.BaseResponse;

/**
 * <房屋绑定账户信息>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-27]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HouseUserInfoResponse extends BaseResponse
{
    private List<HouseUserInfoDoc> doc;
    
    public List<HouseUserInfoDoc> getDoc()
    {
        return doc;
    }
    
    public void setDoc(List<HouseUserInfoDoc> doc)
    {
        this.doc = doc;
    }
}
