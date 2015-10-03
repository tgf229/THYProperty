package com.ymdq.thy.bean.home;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <账单查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月17日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyCentralCardResponse extends BaseResponse
{
    private ArrayList<MyCentralCardDoc> doc;

    public ArrayList<MyCentralCardDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<MyCentralCardDoc> doc)
    {
        this.doc = doc;
    }
}
