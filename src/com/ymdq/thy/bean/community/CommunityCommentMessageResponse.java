package com.ymdq.thy.bean.community;

import java.util.List;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <新回复消息列表返回体>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-28]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityCommentMessageResponse extends BaseResponse
{
    private List<Message> doc;
    
    public List<Message> getDoc()
    {
        return doc;
    }
    
    public void setDoc(List<Message> doc)
    {
        this.doc = doc;
    }
}
