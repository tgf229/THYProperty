package com.ymdq.thy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * <数据库类>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-4]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "jiarun";
    
    /**
     * 数据库版本 1.1.0
     */
    private static final int DATABASE_VERSION = 2;
    
    /**
     * 用户会员卡表
     */
    public static final String USER_TABLE_NAME = "TABLE_CARD";
    
    /**
     * 我的消息查询
     */
    public static final String MY_MESSAGE_TABLE = "MY_MESSAGE";
    
    /**
     * 轮播通告赞&踩
     */
    public static final String ACTIVITY_PRAISE_BLAME_TABLE = "ACTIVITY_PRAISE_BLAME";
    
    /**
     * 回复消息
     */
    public static final String COMMUNITY_MESSAGE_TABLE = "TABLE_COMMUNITY_MESSAGE";
    
    /**
     * 会员卡表创建sql
     */
    private static final String CARD_TABLE_CREATE =
        "CREATE TABLE TABLE_CARD(USERID INTEGER PRIMARY KEY, STATUS TEXT, CARDNO TEXT, CARDNUM TEXT, CRMUSERID TEXT, CRMUSERNAME TEXT, CARDTYPE TEXT, LEVEL TEXT, STIME TEXT, ETIME TEXT, CREATESTORE TEXT)";
    
    /**
     * 首页 我的消息
     */
    private static final String MY_MESSAGE_CREATE =
        "create table MY_MESSAGE(_id integer primary key autoincrement,userId text,cId text,"
            + "m_name text,m_type text,messageId text,content text,time text,isRead text)";
    
    /**
     * 通告详情的赞&踩   0踩   1赞
     */
    private static final String ACTIVITY_PRAISE_BLAME_CREATE =
        "create table ACTIVITY_PRAISE_BLAME(_id integer primary key autoincrement,activity_id text,praise_or_blame text)";
    
    /**
     *  回复消息表创建sql
     */
    private static final String COMMUNITY_MESSAGE_TABLE_CREATE =
        "CREATE TABLE TABLE_COMMUNITY_MESSAGE(_ID INTEGER PRIMARY KEY AUTOINCREMENT, USERID TEXT, CID TEXT, ARTICLEID TEXT, TIME TEXT, CONTENT TEXT, IMAGEURL TEXT, REPLYUID TEXT, REPLYNICKNAME TEXT, REPLYCOMMENTID TEXT, REPLYCONTENT TEXT, REPLYHEADURL TEXT, BEREPLYUID TEXT, BEREPLYNICKNAME TEXT, USERLEVEL TEXT)";
    
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    //该函数在第一次创建数据库的时候执行，
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CARD_TABLE_CREATE);
        db.execSQL(MY_MESSAGE_CREATE);
        db.execSQL(ACTIVITY_PRAISE_BLAME_CREATE);
        db.execSQL(COMMUNITY_MESSAGE_TABLE_CREATE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if (oldVersion == 1)
        {
            oldVersion++;
            db.execSQL("alter table MY_MESSAGE add m_name text");
            db.execSQL("alter table MY_MESSAGE add m_type text");
        }
        if (oldVersion == 2)
        {
            db.beginTransaction();
            try
            {
                db.execSQL(COMMUNITY_MESSAGE_TABLE_CREATE);
                oldVersion++;
                db.setTransactionSuccessful();
            }
            catch (Exception e)
            {
            }
            finally
            {
                db.endTransaction();
            }
        }
    }
}
