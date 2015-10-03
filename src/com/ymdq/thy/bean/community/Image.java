package com.ymdq.thy.bean.community;

import java.io.Serializable;

/**
 * 
 * <图片列表>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-7-1]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Image implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    /**
     * 缩略图地址
     */
    private String imageUrlS;
    
    /**
     * 大图地址
     */
    private String imageUrlL;
    
    public String getImageUrlS()
    {
        return imageUrlS;
    }
    
    public void setImageUrlS(String imageUrlS)
    {
        this.imageUrlS = imageUrlS;
    }
    
    public String getImageUrlL()
    {
        return imageUrlL;
    }
    
    public void setImageUrlL(String imageUrlL)
    {
        this.imageUrlL = imageUrlL;
    }
    
}
