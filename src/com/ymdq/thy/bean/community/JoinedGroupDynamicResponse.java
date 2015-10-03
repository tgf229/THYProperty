package com.ymdq.thy.bean.community;

import java.util.List;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <邻里—我关注的社区动态列表查询>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-7-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class JoinedGroupDynamicResponse extends BaseResponse
{
    
    /**
     * 查询时间点
     */
    private String queryTime;
    
    /**
     * 话题列表数据
     */
    private List<Topic> doc;

    public String getQueryTime()
    {
        return queryTime;
    }

    public void setQueryTime(String queryTime)
    {
        this.queryTime = queryTime;
    }

    public List<Topic> getDoc()
    {
        return doc;
    }

    public void setDoc(List<Topic> doc)
    {
        this.doc = doc;
    }
    
}
