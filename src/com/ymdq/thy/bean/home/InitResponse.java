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
