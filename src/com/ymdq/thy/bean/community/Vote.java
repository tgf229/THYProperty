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
public class Vote implements Serializable
{
    private String voteId;
    
    private String voteName;
    
    private String voteNum;
    
    private String myVote;
    
    public String getVoteId()
    {
        return voteId;
    }
    
    public void setVoteId(String voteId)
    {
        this.voteId = voteId;
    }
    
    public String getVoteName()
    {
        return voteName;
    }
    
    public void setVoteName(String voteName)
    {
        this.voteName = voteName;
    }
    
    public String getVoteNum()
    {
        return voteNum;
    }
    
    public void setVoteNum(String voteNum)
    {
        this.voteNum = voteNum;
    }
    
    public String getMyVote()
    {
        return myVote;
    }
    
    public void setMyVote(String myVote)
    {
        this.myVote = myVote;
    }
}
