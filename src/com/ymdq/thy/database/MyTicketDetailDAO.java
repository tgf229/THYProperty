package com.ymdq.thy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 轮播通告的赞&踩
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyTicketDetailDAO
{
    private DBHelper dbHelper;
    
    private SQLiteDatabase sqLite;
    
    private static MyTicketDetailDAO mDB;
    
    private MyTicketDetailDAO(Context context)
    {
        dbHelper = new DBHelper(context);
    }
    
    public static MyTicketDetailDAO getInstance(Context context)
    {
        if(mDB == null)
        {
            mDB = new MyTicketDetailDAO(context);
        }
        return mDB;
    }
    
    /**
     * 
     * <插入数据>
     * <功能详细描述>
     * @param aId  通告id
     * @param pORb 踩或赞
     * @see [类、类#方法、类#成员]
     */
    public void insertDB(String aId,String pORb)
    {
        sqLite = dbHelper.getWritableDatabase();
        sqLite.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("activity_id", aId);
        values.put("praise_or_blame", pORb);
        sqLite.insert(DBHelper.ACTIVITY_PRAISE_BLAME_TABLE, null, values);
        sqLite.setTransactionSuccessful();sqLite.endTransaction();
        sqLite.close();
    }
    
    /**
     * 
     * <根据通告id查询是否有这条信息>
     * <功能详细描述>
     * @param aID
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String queryDB(String aID)
    {
        sqLite = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        String haveData = null;
        sqLite.beginTransaction();
        cursor = sqLite.query(DBHelper.ACTIVITY_PRAISE_BLAME_TABLE, null, "activity_id=?", new String[]{aID}, null, null, null);
        if(cursor.moveToNext())
        {
            haveData = cursor.getString(cursor.getColumnIndex("praise_or_blame"));
        }
        sqLite.setTransactionSuccessful();
        sqLite.endTransaction();
        cursor.close();
        sqLite.close();
        return haveData;
    }
    
}
