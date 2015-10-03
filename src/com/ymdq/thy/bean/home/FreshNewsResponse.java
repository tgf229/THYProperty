package com.ymdq.thy.bean.home;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.community.Topic;

/**
 * 
 * <新鲜事儿查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月19日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FreshNewsResponse extends BaseResponse
{
    private ArrayList<Topic> doc;

    public ArrayList<Topic> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<Topic> doc)
    {
        this.doc = doc;
    }
}
