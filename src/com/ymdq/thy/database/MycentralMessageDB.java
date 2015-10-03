package com.ymdq.thy.database;

import java.util.ArrayList;
import java.util.List;

import com.ymdq.thy.bean.home.MyCentralMeeageBean;
import com.ymdq.thy.bean.home.MyCentralMeeageDoc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 
 * <首页我的消息表>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MycentralMessageDB
{
    private DBHelper dbHelper;
    
    private SQLiteDatabase sqLite;
    
    private static MycentralMessageDB mMessageDB;
    
    private MycentralMessageDB(Context context)
    {
        dbHelper = new DBHelper(context);
    }

    public static MycentralMessageDB getInstance(Context context)
    {
        if(mMessageDB == null)
        {
            mMessageDB = new MycentralMessageDB(context);
        }
        return mMessageDB;
    }
    
    /**
     * 
     * <向数据库中插入数据>
     * <功能详细描述>
     * @param userId
     * @param cId
     * @param bean
     * @see [类、类#方法、类#成员]
     */
    public void insertDb(String userId,String cId,List<MyCentralMeeageDoc> list)
    {
        sqLite = dbHelper.getWritableDatabase();
        sqLite.beginTransaction();
        for(int i=0;i<list.size();i++)
        {
            MyCentralMeeageDoc bean = list.get(i);
            Cursor cursor = sqLite.query(DBHelper.MY_MESSAGE_TABLE, null, "userId=? and cId=? and messageId=?", 
                new String[]{userId,cId,bean.getId()}, null, null, null);
            if(cursor.getCount() > 0)
            {
                cursor.close();
                continue;
            }
            
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("cId", cId);
            values.put("m_name", bean.getName());
            values.put("m_type", bean.getType());
            values.put("messageId", bean.getId());
            values.put("content", bean.getContent());
            values.put("time", bean.getTime());
            values.put("isRead", "0");
            sqLite.insert(DBHelper.MY_MESSAGE_TABLE, null, values);
            cursor.close();
        }
        sqLite.setTransactionSuccessful();
        sqLite.endTransaction();
        close(sqLite);
    }
    
    /**
     * 
     * <根据userId和cID查表>
     * <功能详细描述>
     * @param userId
     * @param cId
     * @see [类、类#方法、类#成员]
     */
    public List<MyCentralMeeageBean> queryALlDb(String userId,String cId)
    {
        sqLite = dbHelper.getWritableDatabase();
        sqLite.beginTransaction();
        Cursor cursor = null;
        cursor = sqLite.query(DBHelper.MY_MESSAGE_TABLE, null, "userId=? and cId=?", new String[]{userId,cId}, null, null, "time desc");
        List<MyCentralMeeageBean> list = new ArrayList<MyCentralMeeageBean>();
        while(cursor.moveToNext())
        {
            MyCentralMeeageBean bean = new MyCentralMeeageBean();
            bean.setName(cursor.getString(cursor.getColumnIndex("m_name")));
            bean.setType(cursor.getString(cursor.getColumnIndex("m_type")));
            bean.setContent(cursor.getString(cursor.getColumnIndex("content")));
            bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
            bean.setIsRead(cursor.getString(cursor.getColumnIndex("isRead")));
            list.add(bean);
        }
        sqLite.setTransactionSuccessful();
        sqLite.endTransaction();
        if(cursor != null)
        {
            cursor.close();
        }
        close(sqLite);
        return list;
    }
    
    /**
     * 
     * <未读消息查询>
     * <功能详细描述>
     * @param userId
     * @param cId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int queryNoRead(String userId,String cId)
    {
        sqLite = dbHelper.getWritableDatabase();
        int count = 0;
        sqLite.beginTransaction();
        Cursor cursor = null;
        cursor = sqLite.query(DBHelper.MY_MESSAGE_TABLE, null, "userId=? and cId=? and isRead=?", new String[]{userId,cId,"0"}, null, null, null);
        if(cursor != null)
        {
            count = cursor.getCount();
        }
        sqLite.setTransactionSuccessful();
        sqLite.endTransaction();
        if(cursor != null)
        {
            cursor.close();
        }
        close(sqLite);
        
        return count;
    }
    
    /**
     * 
     * <修改isRead字段>
     * <功能详细描述>
     * @param userId
     * @param cId
     * @see [类、类#方法、类#成员]
     */
    public void updateDbIsRead(String userId,String cId)
    {
        sqLite = dbHelper.getWritableDatabase();
        sqLite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("isRead", "1");
        sqLite.update(DBHelper.MY_MESSAGE_TABLE, values, "userId=? and cId=?", new String[]{userId,cId});
        sqLite.setTransactionSuccessful();
        sqLite.endTransaction();
        close(sqLite);
    }
    
    private void close(SQLiteDatabase sql)
    {
       if(sql != null && sql.isOpen())
       {
           sql.close();
       }
    }
}
