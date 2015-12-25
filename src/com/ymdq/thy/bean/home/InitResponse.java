package com.ymdq.thy.bean.home;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <初始化>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月20日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class InitResponse extends BaseResponse
{
    /**
     * 0 进入应用 1 退出应用

     */
    private String flag;
    
    /**
     * 物业公司ID
     */
    private String id;
    
    /**
     * 物业公司名称
     */
    private String name;
    
    /**
     * 物业在小区内的位置
     */
    private String address;
    
    /**
     * 电话
     */
    private String tel;
    
    /**
     * 物业公司LOGO
     */
    private String logo;
    
    /**
     * 物业公司介绍
     */
    private String content;
    
    /**
     * 物业公司资质名称
     */
    private String title;
    
    /**
     * 物业公司资质地址
     */
    private String titleUrl;
    
    public String getLogo()
    {
        return logo;
    }
    
    public void setLogo(String logo)
    {
        this.logo = logo;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public String getTitleUrl()
    {
        return titleUrl;
    }
    
    public void setTitleUrl(String titleUrl)
    {
        this.titleUrl = titleUrl;
    }
    
    public String getFlag()
    {
        return flag;
    }
    
    public void setFlag(String flag)
    {
        this.flag = flag;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public String getTel()
    {
        return tel;
    }
    
    public void setTel(String tel)
    {
        this.tel = tel;
    }
}
