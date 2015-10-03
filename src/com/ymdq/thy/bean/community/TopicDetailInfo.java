package com.ymdq.thy.bean.community;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * <话题评论列表数据>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-7-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TopicDetailInfo
{
    /**
     *   评论ID
     */
    private String commentId;
    
    /**
     * 用户等级
     * 0普通用户
     * 1大V
     * 2超级管理员
     */
    private String userLevel;
    
    /**
     * 被回复的用户ID
     */
    private String replyUId;
    
    /**
     * 被回复的用户昵称
     */
    private String replyNickName;
    
    /**
     * 用户ID
     */
    @SerializedName("uId")
    private String userId;
    
    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * 评论内容
     */
    private String content;
    
    /**
     * 评论时间
     */
    private String time;
    
    /**
     * 用户头像地址
     */
    private String imageUrl;
    
    public String getUserId()
    {
        return userId;
    }
    
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    
    public String getNickName()
    {
        return nickName;
    }
    
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
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
    
    public String getImageUrl()
    {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }
    
    public String getCommentId()
    {
        return commentId;
    }
    
    public void setCommentId(String commentId)
    {
        this.commentId = commentId;
    }
    
    public String getUserLevel()
    {
        return userLevel;
    }
    
    public void setUserLevel(String userLevel)
    {
        this.userLevel = userLevel;
    }
    
    public String getReplyUId()
    {
        return replyUId;
    }
    
    public void setReplyUId(String replyUId)
    {
        this.replyUId = replyUId;
    }
    
    public String getReplyNickName()
    {
        return replyNickName;
    }
    
    public void setReplyNickName(String replyNickName)
    {
        this.replyNickName = replyNickName;
    }
    
}
