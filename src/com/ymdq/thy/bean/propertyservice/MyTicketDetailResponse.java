package com.ymdq.thy.bean.propertyservice;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.home.ImageList;

/**
 * 
 * <工单详情查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月25日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyTicketDetailResponse extends BaseResponse
{
    /**
     * 提交工单的用户ID
     */
    private String uId;
    
    /**
     * 提交工单的用户昵称
     */
    private String uName;
    
    /**
     * 提交工单的用户头像
     */
    private String image;
    
    /**
     * 工单创建时间
     */
    private String time;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 类型
     */
    private String type;
    
    /**
     * 当前状态
     */
    private String status;
    
    private ArrayList<ImageList> imageList;
    
    /**
     * 处理流程列表
     */
    private ArrayList<MyTicketDetailPath> path;

    public String getuId()
    {
        return uId;
    }

    public void setuId(String uId)
    {
        this.uId = uId;
    }

    public String getuName()
    {
        return uName;
    }

    public void setuName(String uName)
    {
        this.uName = uName;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
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

    public ArrayList<ImageList> getImageList()
    {
        return imageList;
    }

    public void setImageList(ArrayList<ImageList> imageList)
    {
        this.imageList = imageList;
    }

    public ArrayList<MyTicketDetailPath> getPath()
    {
        return path;
    }

    public void setPath(ArrayList<MyTicketDetailPath> path)
    {
        this.path = path;
    }
}
