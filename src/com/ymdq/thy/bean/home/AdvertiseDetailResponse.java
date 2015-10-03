package com.ymdq.thy.bean.home;

import java.io.Serializable;
import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <轮播通告—详情查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AdvertiseDetailResponse extends BaseResponse implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    /**
     * 通告活动ID
     */
    private String id;
    
    /**
     * 通告活动名称
     */
    private String name;
    
    /**
     * 通告活动描述
     */
    private String desc;
    
    /**
     * 活动图片
     */
    private String imageUrl;
    
    /**
     * 被点赞次数
     */
    private String praiseNum;
    
    /**
     * 被踩次数
     */
    private String blameNum;
    
    /**
     * 被分享次数
     */
    private String shareNum;
    
    /**
     * 评论数据列表
     */
    private ArrayList<CommentList> comment; 
    
    public class CommentList implements Serializable
    {
        /**
         * 注释内容
         */
        private static final long serialVersionUID = 1L;

        /**
         * 用户昵称
         */
        private String name;
        
        /**
         * 用户头像
         */
        private String image;
        
        /**
         * 评论时间
         */
        private String time;
        
        /**
         * 评论内容
         */
        private String desc;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getTime()
        {
            return time;
        }

        public void setTime(String time)
        {
            this.time = time;
        }

        public String getDesc()
        {
            return desc;
        }

        public void setDesc(String desc)
        {
            this.desc = desc;
        }

        public String getImage()
        {
            return image;
        }

        public void setImage(String image)
        {
            this.image = image;
        }
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

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrlL(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getPraiseNum()
    {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum)
    {
        this.praiseNum = praiseNum;
    }

    public String getBlameNum()
    {
        return blameNum;
    }

    public void setBlameNum(String blameNum)
    {
        this.blameNum = blameNum;
    }

    public String getShareNum()
    {
        return shareNum;
    }

    public void setShareNum(String shareNum)
    {
        this.shareNum = shareNum;
    }

    public ArrayList<CommentList> getComment()
    {
        return comment;
    }

    public void setComment(ArrayList<CommentList> comment)
    {
        this.comment = comment;
    }
}
