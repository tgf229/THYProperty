package com.ymdq.thy.bean.community;

/**
 * 
 * <官方社区最热话题列表>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class OcArticleList
{
    /**
     * 话题ID
     */
    private String id;
    
    /**
     * 话题内容
     */
    private String content;
    
    /**
     * 话题来自社区名称
     */
    private String from;
    
    /**
     * 话题图片
     */
    private String image;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getFrom()
    {
        return from;
    }
    
    public void setFrom(String from)
    {
        this.from = from;
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
