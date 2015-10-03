package com.ymdq.thy.bean.home;

import java.util.ArrayList;

public class MyCentralCardDoc
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
     * 房屋下的费用列表
     */
    private ArrayList<MyCentralCardDocHList> hList;

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

    public ArrayList<MyCentralCardDocHList> gethList()
    {
        return hList;
    }

    public void sethList(ArrayList<MyCentralCardDocHList> hList)
    {
        this.hList = hList;
    }

    public class MyCentralCardDocHList
    {
        private String name;
        
        private String type;
        
        private String totalMoney;
        
        private String status;
        
        private String money;
        
        private String time;

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

        public String getTotalMoney()
        {
            return totalMoney;
        }

        public void setTotalMoney(String totalMoney)
        {
            this.totalMoney = totalMoney;
        }

        public String getStatus()
        {
            return status;
        }

        public void setStatus(String status)
        {
            this.status = status;
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


}
