package com.ymdq.thy.bean.community;

import java.util.List;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <圈子话题评论列表查询返回体>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-7-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GroupTopicsCommentsResponse extends BaseResponse
{
    /**
     * 查询时间点
     */
    private String queryTime;
    
    /**
     * 列表数据
     */
    private List<TopicDetailInfo> doc;
    
    public String getQueryTime()
    {
        return queryTime;
    }
    
    public void setQueryTime(String queryTime)
    {
        this.queryTime = queryTime;
    }
    
    public List<TopicDetailInfo> getDoc()
    {
        return doc;
    }
    
    public void setDoc(List<TopicDetailInfo> doc)
    {
        this.doc = doc;
    }
    
}
