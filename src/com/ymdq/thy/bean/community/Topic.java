package com.ymdq.thy.bean.community;

import java.io.Serializable;
import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <话题内容>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Topic extends BaseResponse implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID
     */
    private String uId;
    
    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * 用户头像地址
     */
    private String imageUrl;
    
    /**
     * 话题发布日期
     * YYYYMMDD
     */
    private String date;
    
    /**
     * 话题发布时间
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
     * 话题ID   v2.0
     */
    private String aId;
    
    /**
     * 话题内容
     */
    private String content;
    
    /**
     * 是否已赞
     * 未赞：0
     * 已赞：1
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
     * 评论数量
     */
    private String commentNum;
    
    /**
     * 话题类型
     *  1   普通话题
        2   投票(赞成or反对)话题
        3   投票(自定义)话题
        4   官方(美食)话题
        5   官方(生活常识)话题
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
     * 当前用户是否投票过此话题
     * 未投：0
     * 已投赞成：1
     * 已投反对：2
     */
    private String voteFlag;
    
    /**
     * 话题是否置顶
     * 0 否
     * 1 是
     */
    private String isTop;
    
    /**
     * 话题是否火
     * 0 否
     * 1 是
     */
    private String isHot;
    
    /**
     * 0普通用户
     * 1大V
     * 2超级管理员
     */
    private String userLevel;
    
    public String getaId()
    {
        return aId;
    }

    public void setaId(String aId)
    {
        this.aId = aId;
    }

    public String getCommentNum()
    {
        return commentNum;
    }

    public void setCommentNum(String commentNum)
    {
        this.commentNum = commentNum;
    }

    public String getIsHot()
    {
        return isHot;
    }

    public void setIsHot(String isHot)
    {
        this.isHot = isHot;
    }

    /**
     * 图片列表
     */
    private ArrayList<Image> imageList;
    
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
    
    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
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
    
    public ArrayList<Image> getImageList()
    {
        return imageList;
    }
    
    public void setImageList(ArrayList<Image> imageList)
    {
        this.imageList = imageList;
    }
    
    public String getIsTop()
    {
        return isTop;
    }
    
    public void setIsTop(String isTop)
    {
        this.isTop = isTop;
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
