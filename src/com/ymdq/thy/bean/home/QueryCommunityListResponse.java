package com.ymdq.thy.bean.home;

/**
 * 
 * <小区检索>
 * <功能详细描述>
 * 
 * @author  suqning
 * @version  [版本号, 2015年3月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class QueryCommunityListResponse
{
    /**
     * 小区ID
     */
    private String cId;
    
    /**
     * 小区名称
     */
    private String cName;
    
    /**
     * 小区所属城市名称
     */
    private String city;

    public String getcId()
    {
        return cId;
    }

    public void setcId(String cId)
    {
        this.cId = cId;
    }

    public String getcName()
    {
        return cName;
    }

    public void setcName(String cName)
    {
        this.cName = cName;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }
}
