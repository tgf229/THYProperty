package com.ymdq.thy.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ymdq.thy.bean.community.Message;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <物业消息模块DAO>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-4-16]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityMessageDAO
{
    /**
     * 主DB
     */
    private DBHelper dbHelper;
    
    /**
     * db对象
     */
    private SQLiteDatabase db;
    
    /**
     * <默认构造函数>
     * 
     * @param context context
     */
    public CommunityMessageDAO(Context context)
    {
        dbHelper = new DBHelper(context);
    }
    
    /**
     * <保存会员卡信息> <功能详细描述>
     * 
     * @param userBean userBean
     * @see [类、类#方法、类#成员]
     */
    public void saveMessage(List<Message> message)
    {
        if (GeneralUtils.isNullOrZeroSize(message))
            return;
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        for (int i = 0; i < message.size(); i++)
        {
            ContentValues values = new ContentValues();
            values.put("USERID", Global.getUserId());
            values.put("CID", Global.getCId());
            values.put("ARTICLEID", message.get(i).getArticleId());
            values.put("TIME", message.get(i).getTime());
            values.put("CONTENT", message.get(i).getContent());
            values.put("IMAGEURL", message.get(i).getImageUrl());
            values.put("REPLYUID", message.get(i).getReplyUId());
            values.put("REPLYNICKNAME", message.get(i).getReplyNickName());
            values.put("REPLYCOMMENTID", message.get(i).getReplyCommentId());
            values.put("REPLYCONTENT", message.get(i).getReplyContent());
            values.put("REPLYHEADURL", message.get(i).getReplyHeadUrl());
            values.put("BEREPLYUID", message.get(i).getBeReplyUId());
            values.put("BEREPLYNICKNAME", message.get(i).getBeReplyNickName());
            values.put("USERLEVEL", message.get(i).getUserLevel());
            db.insert(dbHelper.COMMUNITY_MESSAGE_TABLE, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    
    /**
     * 
     * <查询所有消息>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<Message> queryMessages(Integer offset, Integer maxResult)
    {
        List<Message> msgs = new ArrayList<Message>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor;
        //查询主表
        cursor =
            db.query(dbHelper.COMMUNITY_MESSAGE_TABLE,
                null,
                "USERID = ? and CID = ?",
                new String[] {Global.getUserId(), Global.getCId()},
                null,
                null,
                "TIME DESC",
                offset + "," + maxResult);
        while (cursor.moveToNext())
        {
            Message msg = new Message();
            msg.setArticleId(cursor.getString(cursor.getColumnIndex("ARTICLEID")));
            msg.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
            msg.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
            msg.setImageUrl(cursor.getString(cursor.getColumnIndex("IMAGEURL")));
            msg.setReplyUId(cursor.getString(cursor.getColumnIndex("REPLYUID")));
            msg.setReplyNickName(cursor.getString(cursor.getColumnIndex("REPLYNICKNAME")));
            msg.setReplyCommentId(cursor.getString(cursor.getColumnIndex("REPLYCOMMENTID")));
            msg.setReplyContent(cursor.getString(cursor.getColumnIndex("REPLYCONTENT")));
            msg.setReplyHeadUrl(cursor.getString(cursor.getColumnIndex("REPLYHEADURL")));
            msg.setBeReplyUId(cursor.getString(cursor.getColumnIndex("BEREPLYUID")));
            msg.setBeReplyNickName(cursor.getString(cursor.getColumnIndex("BEREPLYNICKNAME")));
            msg.setUserLevel(cursor.getString(cursor.getColumnIndex("USERLEVEL")));
            msgs.add(msg);
        }
        cursor.close();
        db.close();
        return msgs;
    }
}
