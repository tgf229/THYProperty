package com.ymdq.thy.bean.home;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <便民信息查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月17日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyCentralListsResponse extends BaseResponse
{
    private ArrayList<MyCentralListsDoc> doc;

    public ArrayList<MyCentralListsDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<MyCentralListsDoc> doc)
    {
        this.doc = doc;
    }
}
