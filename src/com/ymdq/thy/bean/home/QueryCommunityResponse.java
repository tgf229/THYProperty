package com.ymdq.thy.bean.home;

import java.util.List;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <小区检索>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2015年3月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class QueryCommunityResponse extends BaseResponse
{
    private List<QueryCommunityListResponse> doc;

    public List<QueryCommunityListResponse> getDoc()
    {
        return doc;
    }

    public void setDoc(List<QueryCommunityListResponse> doc)
    {
        this.doc = doc;
    }
}
