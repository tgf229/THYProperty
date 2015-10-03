package com.ymdq.thy.bean.home;

public class MyCentralMeeageDoc
{
    /**
     * 消息ID
     */
    private String id;
    
    /**
     * 消息名
     */
    private String name;
    
    /**
     * 消息类型 1通告 2营销推广 3服务信息 4其他 5快递
     */
    private String type;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 推送时间
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
