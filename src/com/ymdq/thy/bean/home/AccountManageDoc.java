package com.ymdq.thy.bean.home;

import java.io.Serializable;
import java.util.ArrayList;

public class AccountManageDoc implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 2522021558751397903L;
    
    private String hId;
    
    private String hName;
    
    private ArrayList<HomeList> hList;

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

    public ArrayList<HomeList> gethList()
    {
        return hList;
    }

    public void sethList(ArrayList<HomeList> hList)
    {
        this.hList = hList;
    }
    
}
