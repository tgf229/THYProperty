package com.ymdq.thy.bean.home;

public class MyCentralMeeageBean extends MyCentralMeeageDoc
{
    /**
     * 此消息是否已读：0 未读  1 已读
     */
    private String isRead = "0";

    public String getIsRead()
    {
        return isRead;
    }

    public void setIsRead(String isRead)
    {
        this.isRead = isRead;
    }
}
