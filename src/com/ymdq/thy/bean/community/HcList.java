package com.ymdq.thy.bean.community;

/**
 * 
 * <热门社区列表>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HcList
{
    /**
     * 社区ID
     */
    private String id;
    
    /**
     * 社区名称
     */
    private String name;
    
    /**
     * 社区图标
     */
    private String icon;
    
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
    
    public String getIcon()
    {
        return icon;
    }
    
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    
}
