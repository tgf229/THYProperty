package com.ymdq.thy.bean.propertyservice;

import java.util.ArrayList;

import com.ymdq.thy.bean.community.Image;

public class MyTicketDetailPath
{
    /**
     * 处理人
     */
    private String uName;
    
    /**
     * 处理人头像
     */
    private String uImageUrl;
    
    /**
     * 部门名称
     */
    private String depName;
    
    /**
     * 工单流程描述
     */
    private String desc;
    
    /**
     * 处理描述
     */
    private String content;
    
    /**
     * 处理时间距离当前时间
     */
    private String time;
    
    /**
     * 图片列表
     */
    private ArrayList<Image> imageList;
    
    public String getuName()
    {
        return uName;
    }
    
    public void setuName(String uName)
    {
        this.uName = uName;
    }
    
    public String getDepName()
    {
        return depName;
    }
    
    public void setDepName(String depName)
    {
        this.depName = depName;
    }
    
    public String getuImageUrl()
    {
        return uImageUrl;
    }
    
    public void setuImageUrl(String uImageUrl)
    {
        this.uImageUrl = uImageUrl;
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
    
    public ArrayList<Image> getImageList()
    {
        return imageList;
    }
    
    public void setImageList(ArrayList<Image> imageList)
    {
        this.imageList = imageList;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
}
