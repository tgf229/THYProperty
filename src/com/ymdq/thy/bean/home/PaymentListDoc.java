package com.ymdq.thy.bean.home;

public class PaymentListDoc
{
    /**
     * 缴费记录ID
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
     * 费用名称
     */
    private String name;
    
    /**
     * 费用类型ID
     */
    private String type;
    
    /**
     * 缴费金额
     */
    private String money;
    
    /**
     * 缴费时间
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

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getMoney()
    {
        return money;
    }

    public void setMoney(String money)
    {
        this.money = money;
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
