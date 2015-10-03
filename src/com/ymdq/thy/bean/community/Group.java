package com.ymdq.thy.bean.community;

/**
 * 
 * <圈子返回体>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Group
{
    /**
     * 社区ID
     */
    private String id;
    
    /**
     * 社区名称
     */
    private String name;
    
    /**
     * 社区LOGO
     */
    private String icon;
    
    /**
     * 加入标志位
     * 当前用户是否已关注该社区
     * 未关注：0
     * 已关注：1
     * 若入参uId为空，则返回0
     */
    private String flag;
    
    /**
     * 社区公告
     */
    private String desc;
    
    /**
     * 用户关注数量
     */
    private String userCount;
    
    /**
     * 话题数量
     */
    private String articleCount;
    
    /**
     * 创建时间
     */
    private String time;
    
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
    
    public String getIcon()
    {
        return icon;
    }
    
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    
    public String getFlag()
    {
        return flag;
    }
    
    public void setFlag(String flag)
    {
        this.flag = flag;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
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
    
    public String getTime()
    {
        return time;
    }
    
    public void setTime(String time)
    {
        this.time = time;
    }
    
}
