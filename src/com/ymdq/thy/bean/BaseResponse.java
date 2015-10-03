package com.ymdq.thy.bean;

/**
 * <网络请求返回体> 
 * <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-3-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BaseResponse
{
    /**
     * 返回状态码
     */
    protected String retcode;
    
    /**
     * 返回信息描述
     */
    protected String retinfo;
    
    public String getRetcode()
    {
        return retcode;
    }
    
    public void setRetcode(String retcode)
    {
        this.retcode = retcode;
    }
    
    public String getRetinfo()
    {
        return retinfo;
    }
    
    public void setRetinfo(String retinfo)
    {
        this.retinfo = retinfo;
    }
}
