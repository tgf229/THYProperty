package com.ymdq.thy.bean.community;

import java.util.List;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <邻里—社区列表查询返回体>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GroupListResponse extends BaseResponse
{
    /**
     * 列表数据
     */
    private List<Group> doc;
    
    public List<Group> getDoc()
    {
        return doc;
    }
    
    public void setDoc(List<Group> doc)
    {
        this.doc = doc;
    }
}
