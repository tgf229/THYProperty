package com.ymdq.thy.bean.community;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <发表话题评论返回体>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-7-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GroupAddTopicCommentResponse extends BaseResponse
{
    /**
     * 评论id
     */
    private String commentId;
    
    public String getCommentId()
    {
        return commentId;
    }
    
    public void setCommentId(String commentId)
    {
        this.commentId = commentId;
    }
}
