/*
 * 文 件 名:  PraiseCommentDoc.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  tgf
 * 创建时间:  2015-11-12
 
 */
package com.ymdq.thy.bean.propertyservice;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tgf
 * @version  [版本号, 2015-11-12]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PraiseCommentDoc
{
    private String uId;
    private String nickName;
    private String tag;
    private String content;
    private String time;
    private String uImageUrl;
    public String getuId()
    {
        return uId;
    }
    public void setuId(String uId)
    {
        this.uId = uId;
    }
    public String getNickName()
    {
        return nickName;
    }
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }
    public String getTag()
    {
        return tag;
    }
    public void setTag(String tag)
    {
        this.tag = tag;
    }
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }
    public String getTime()
    {
        return time;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    public String getuImageUrl()
    {
        return uImageUrl;
    }
    public void setuImageUrl(String uImageUrl)
    {
        this.uImageUrl = uImageUrl;
    }
}
