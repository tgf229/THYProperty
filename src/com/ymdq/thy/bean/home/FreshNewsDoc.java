package com.ymdq.thy.bean.home;

import java.util.ArrayList;

public class FreshNewsDoc
{
    /**
     * 用户ID
     */
    private String uId;
    
    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * imageUrl
     */
    private String imageUrl;
    
    /**
     * 话题发布时间距离当前时间
     */
    private String time;
    
    /**
     * 话题级别
     */
    private String level;
    
    /**
     * 话题所属的社区ID
     */
    private String id;
    
    /**
     * 话题所属的社区名称
     */
    private String name;
    
    /**
     * 话题ID
     */
    private String articleId;
    
    /**
     * 话题内容
     */
    private String content;
    
    /**
     * 是否已赞 未赞：0 已赞：1

     */
    private String flag;
    
    /**
     * 赞数量
     */
    private String praiseNum;
    
    /**
     * 分享数量
     */
    private String shareNum;
    
    /**
     * 评论数量
     */
    private String comment;
    
    /**
     * 话题类型 1 普通话题  2 投票话题
     */
    private String type;
    
    /**
     * 支持数量
     */
    private String yes;
    
    /**
     * 反对数量
     */
    private String no;
    
    /**
     * 是否投票
     */
    private String voteFlag;
    
    /**
     * 图片列表
     */
    private ArrayList<ImageList> imageList;
    
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

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getLevel()
    {
        return level;
    }

    public void setLevel(String level)
    {
        this.level = level;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

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

    public String getFlag()
    {
        return flag;
    }

    public void setFlag(String flag)
    {
        this.flag = flag;
    }

    public String getPraiseNum()
    {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum)
    {
        this.praiseNum = praiseNum;
    }

    public String getShareNum()
    {
        return shareNum;
    }

    public void setShareNum(String shareNum)
    {
        this.shareNum = shareNum;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getYes()
    {
        return yes;
    }

    public void setYes(String yes)
    {
        this.yes = yes;
    }

    public String getNo()
    {
        return no;
    }

    public void setNo(String no)
    {
        this.no = no;
    }

    public String getVoteFlag()
    {
        return voteFlag;
    }

    public void setVoteFlag(String voteFlag)
    {
        this.voteFlag = voteFlag;
    }

    public ArrayList<ImageList> getImageList()
    {
        return imageList;
    }

    public void setImageList(ArrayList<ImageList> imageList)
    {
        this.imageList = imageList;
    }
    
}
