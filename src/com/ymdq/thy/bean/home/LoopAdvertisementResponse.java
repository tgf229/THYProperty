package com.ymdq.thy.bean.home;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <轮播通告—列表查询>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月17日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LoopAdvertisementResponse extends BaseResponse
{
    /**
     * 列表数据
     */
    private ArrayList<LoopAdvertisementDoc> doc;

    public ArrayList<LoopAdvertisementDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<LoopAdvertisementDoc> doc)
    {
        this.doc = doc;
    }
}
