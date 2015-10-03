package com.ymdq.thy.bean.home;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <我的消息查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月19日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyCentralMeeageResponse extends BaseResponse
{
    private ArrayList<MyCentralMeeageDoc> doc;

    public ArrayList<MyCentralMeeageDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<MyCentralMeeageDoc> doc)
    {
        this.doc = doc;
    }
}
