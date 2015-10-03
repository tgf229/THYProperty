package com.ymdq.thy.bean.community;

import java.io.Serializable;
import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <社区基本信息查询返回体>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GroupDetailInfoResponse extends BaseResponse implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 社区ID
     */
    private String id;
    
    /**
     * 社区名称
     */
    private String name;
    
    /**
     * 社区公告描述
     */
    private String desc;
    
    /**
     * 社区LOGO
     */
    private String icon;
    
    /**
     * 社区管理员ID
     */
    private String uId;
    
    /**
     * 社区管理员昵称
     */
    private String nickName;
    
    /**
     * 加入标志位
     */
    private String flag;
    
    /**
     * 用户关注数量
     */
    private String userCount;
    
    /**
     * 话题数量
     */
    private String articleCount;
    
    /**
     * 关注的人员列表
     * 如果入参uId为该社区的管理员，则返回该列表
     */
    private ArrayList<Member> member;
    
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
    
    public String getIcon()
    {
        return icon;
    }
    
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    
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
    
    public String getFlag()
    {
        return flag;
    }
    
    public void setFlag(String flag)
    {
        this.flag = flag;
    }
    
    public String getUserCount()
    {
        return userCount;
    }
    
    public void setUserCount(String userCount)
    {
        this.userCount = userCount;
    }
    
    public String getArticleCount()
    {
        return articleCount;
    }
    
    public void setArticleCount(String articleCount)
    {
        this.articleCount = articleCount;
    }
    
    public ArrayList<Member> getMember()
    {
        return member;
    }
    
    public void setMember(ArrayList<Member> member)
    {
        this.member = member;
    }
    
}
