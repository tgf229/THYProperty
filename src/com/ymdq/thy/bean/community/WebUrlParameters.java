package com.ymdq.thy.bean.community;

public class WebUrlParameters
{
    private String url;
    
    private int start;
    
    private int end;
    
    public WebUrlParameters(String url, int start, int end)
    {
        super();
        this.url = url;
        this.start = start;
        this.end = end;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public int getStart()
    {
        return start;
    }
    
    public void setStart(int start)
    {
        this.start = start;
    }
    
    public int getEnd()
    {
        return end;
    }
    
    public void setEnd(int end)
    {
        this.end = end;
    }
    
}
