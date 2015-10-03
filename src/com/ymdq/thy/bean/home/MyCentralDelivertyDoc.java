package com.ymdq.thy.bean.home;

public class MyCentralDelivertyDoc
{
    /**
     * 邮包ID
     */
    private String id;
    
    /**
     * 房屋ID
     */
    private String hId;
    
    /**
     * 房屋名称
     */
    private String hName;
    
    /**
     * 物流公司名称
     */
    private String name;
    
    /**
     * 物流公司LOGO
     */
    private String logo;
    
    /**
     * 物流单号
     */
    private String num;
    
    /**
     * 送达时间
     */
    private String time;
    
    /**
     * 状态类型 1 带领取 2 已领取
     */
    private String type;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public String getNum()
    {
        return num;
    }

    public void setNum(String num)
    {
        this.num = num;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
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
