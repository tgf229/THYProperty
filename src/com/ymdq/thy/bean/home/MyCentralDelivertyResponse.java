package com.ymdq.thy.bean.home;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <邮包列表查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyCentralDelivertyResponse extends BaseResponse
{
    private ArrayList<MyCentralDelivertyDoc> doc;

    public ArrayList<MyCentralDelivertyDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<MyCentralDelivertyDoc> doc)
    {
        this.doc = doc;
    }
}
