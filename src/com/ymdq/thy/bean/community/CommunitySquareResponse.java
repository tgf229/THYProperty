package com.ymdq.thy.bean.community;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.ymdq.thy.bean.BaseResponse;

/**
 * 
 * <邻里-广场查询返回体>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunitySquareResponse extends BaseResponse
{
    /**
     * 官方推荐社区列表
     */
    private List<OcList> ocList;
    
    /**
     * 官方社区最热话题列表
     */
    private List<OcArticleList> ocArticleList;
    
    /**
     * 热门社区列表
     */
    private List<HcList> hcList;
    
    /**
     * 热门社区最热话题列表
     */
    private List<HcArticleList> hcArticleList;
    
    public List<OcList> getOcList()
    {
        return ocList;
    }
    
    public void setOcList(List<OcList> ocList)
    {
        this.ocList = ocList;
    }
    
    public List<OcArticleList> getOcArticleList()
    {
        return ocArticleList;
    }
    
    public void setOcArticleList(List<OcArticleList> ocArticleList)
    {
        this.ocArticleList = ocArticleList;
    }
    
    public List<HcList> getHcList()
    {
        return hcList;
    }
    
    public void setHcList(List<HcList> hcList)
    {
        this.hcList = hcList;
    }
    
    public List<HcArticleList> getHcArticleList()
    {
        return hcArticleList;
    }
    
    public void setHcArticleList(List<HcArticleList> hcArticleList)
    {
        this.hcArticleList = hcArticleList;
    }
    
}
