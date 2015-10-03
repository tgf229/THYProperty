package com.ymdq.thy.bean.community;

/**
 * 
 * <回复消息体>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2015-4-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Message
{
    
    /**
     * 话题ID
     */
    private String articleId;
    
    /**
     * 时间
     */
    private String time;
    
    /**
     * 话题内容
     */
    private String content;
    
    /**
     * 话题首张图片
     */
    private String imageUrl;
    
    /**
     * 回复人的userID
     */
    private String replyUId;
    
    /**
     * 回复人的用户昵称
     */
    private String replyNickName;
    
    /**
     * 回复人的评论ID
     */
    private String replyCommentId;
    
    /**
     * 回复人的评论内容
     */
    private String replyContent;
    
    /**
     * 回复人的头像
     */
    private String replyHeadUrl;
    
    /**
     * 被回复人的userID
     */
    private String beReplyUId;
    
    /**
     * 被回复人的昵称
     */
    private String beReplyNickName;
    
    /**
     * 0普通用户
     * 1大V
     * 2超级管理员
     */
    private String userLevel;
    
    public String getArticleId()
    {
        return articleId;
    }
    
    public void setArticleId(String articleId)
    {
        this.articleId = articleId;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getImageUrl()
    {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
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
    
    public String getReplyCommentId()
    {
        return replyCommentId;
    }
    
    public void setReplyCommentId(String replyCommentId)
    {
        this.replyCommentId = replyCommentId;
    }
    
    public String getReplyContent()
    {
        return replyContent;
    }
    
    public void setReplyContent(String replyContent)
    {
        this.replyContent = replyContent;
    }
    
    public String getBeReplyUId()
    {
        return beReplyUId;
    }
    
    public void setBeReplyUId(String beReplyUId)
    {
        this.beReplyUId = beReplyUId;
    }
    
    public String getBeReplyNickName()
    {
        return beReplyNickName;
    }
    
    public void setBeReplyNickName(String beReplyNickName)
    {
        this.beReplyNickName = beReplyNickName;
    }
    
    public String getReplyHeadUrl()
    {
        return replyHeadUrl;
    }
    
    public void setReplyHeadUrl(String replyHeadUrl)
    {
        this.replyHeadUrl = replyHeadUrl;
    }
    
    public String getTime()
    {
        return time;
    }
    
    public void setTime(String time)
    {
        this.time = time;
    }
    
    public String getUserLevel()
    {
        return userLevel;
    }
    
    public void setUserLevel(String userLevel)
    {
        this.userLevel = userLevel;
    }
    
}
