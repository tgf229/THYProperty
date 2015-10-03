package com.ymdq.thy.bean.propertyservice;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <工单列表查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月25日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyTicketResponse extends BaseResponse
{
    private ArrayList<MyTicketDoc> doc;

    public ArrayList<MyTicketDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<MyTicketDoc> doc)
    {
        this.doc = doc;
    }
}
