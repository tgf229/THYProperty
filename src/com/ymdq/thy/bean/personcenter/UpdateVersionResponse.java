/*
 * 文 件 名:  UpdateVersionResponse.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-28
 
 */
package com.ymdq.thy.bean.personcenter;

import com.ymdq.thy.bean.BaseResponse;

/**
 * <版本更新检查>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UpdateVersionResponse extends BaseResponse
{
    /**
     * 最新版本
     */
    private String version;
    
    /**
     * 更新内容
     */
    private String content;
    
    /**
     * 是否强制更新        0不强制       1强制
     */
    private String isUpdate;
    
    /**
     * 下载地址
     */
    private String urlAddress;
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getIsUpdate()
    {
        return isUpdate;
    }
    
    public void setIsUpdate(String isUpdate)
    {
        this.isUpdate = isUpdate;
    }
    
    public String getUrlAddress()
    {
        return urlAddress;
    }
    
    public void setUrlAddress(String urlAddress)
    {
        this.urlAddress = urlAddress;
    }
}
