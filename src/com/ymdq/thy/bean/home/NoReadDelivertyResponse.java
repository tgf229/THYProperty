package com.ymdq.thy.bean.home;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <用户提醒（用于查询新消息等数量）>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月24日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NoReadDelivertyResponse extends BaseResponse
{
    /**
     * 未收取的邮包数量
     */
    private String newPost;
    
    /**
     * 未读的话题回复数量    
     */
    private String newReply;
    
    public String getNewPost()
    {
        return newPost;
    }
    
    public void setNewPost(String newPost)
    {
        this.newPost = newPost;
    }
    
    public String getNewReply()
    {
        return newReply;
    }
    
    public void setNewReply(String newReply)
    {
        this.newReply = newReply;
    }
    
}
