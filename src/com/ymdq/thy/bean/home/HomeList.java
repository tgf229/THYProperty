package com.ymdq.thy.bean.home;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeList implements Serializable
{
    private static final long serialVersionUID = 2522021558751397904L;
    
    private String name;
    
    private String type;
    
    private String typeName;
    
    private String totalMoney;
    
    private String money;
    
    private String status;
    
    private ArrayList<MoneyAllList> moneyList;
    
    public class MoneyAllList implements Serializable
    {
        private static final long serialVersionUID = 2522021558751397905L;
        
        private String money;
        
        private String time;

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

    public String getMoney()
    {
        return money;
    }

    public void setMoney(String money)
    {
        this.money = money;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public ArrayList<MoneyAllList> getMoneyList()
    {
        return moneyList;
    }

    public void setMoneyList(ArrayList<MoneyAllList> moneyList)
    {
        this.moneyList = moneyList;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
}
