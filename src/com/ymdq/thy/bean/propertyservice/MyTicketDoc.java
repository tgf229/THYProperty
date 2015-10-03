package com.ymdq.thy.bean.propertyservice;

public class MyTicketDoc
{
    /**
     * 房屋ID
     */
    private String hId;
    
    /**
     * 房屋名称
     */
    private String hName;
    
    /**
     * 工单ID
     */
    private String id;
    
    /**
     * 工单创建时间
     */
    private String time;
    
    /**
     * 提交工单的用户ID
     */
    private String uId;
    
    /**
     * 提交工单的用户昵称
     */
    private String nickName;
    
    /**
     * 提交工单的用户头像
     */
    private String image;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 类型 1 报修 2 投诉 3 表扬

     */
    private String type;
    
    /**
     * 当前状态 1 待处理 2 处理中 3 已处理 4 已完成

     */
    private String status;

    public String gethId()
    {
        return hId;
    }

    public void sethId(String hId)
    {
        this.hId = hId;
    }

    public String gethName()
    {
        return hName;
    }

    public void sethName(String hName)
    {
        this.hName = hName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
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

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
