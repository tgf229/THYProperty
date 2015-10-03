package com.ymdq.thy.bean.personcenter;

import java.util.ArrayList;

public class TestBean
{
    /**
     * 房号选择测试数据
     */
    public static ArrayList<HouseDoc> getHouseResponse()
    {
        ArrayList<HouseDoc> doc = new ArrayList<HouseDoc>();
        
        ArrayList<HouseBList> bList1 = new ArrayList<HouseBList>();
        ArrayList<HouseBList> bList2 = new ArrayList<HouseBList>();
        
        ArrayList<HouseDList> dList1 = new ArrayList<HouseDList>();
        ArrayList<HouseDList> dList2 = new ArrayList<HouseDList>();
        ArrayList<HouseDList> dList3 = new ArrayList<HouseDList>();
        
        for (int i = 0; i < 20; i++)
        {
            HouseDList houseDList = new HouseDList();
            houseDList.sethId("" + i);
            houseDList.sethName("A" + i);
            houseDList.setIdCard("3210***");
            houseDList.setPhone("15151828133");
            dList1.add(houseDList);
        }
        for (int i = 0; i < 20; i++)
        {
            HouseDList houseDList = new HouseDList();
            houseDList.sethId("" + i);
            houseDList.sethName("B" + i);
            houseDList.setIdCard("3210***");
            houseDList.setPhone("13921934010");
            dList2.add(houseDList);
        }
        for (int i = 0; i < 20; i++)
        {
            HouseDList houseDList = new HouseDList();
            houseDList.sethId("" + i);
            houseDList.sethName("C" + i);
            houseDList.setIdCard("3210***");
            houseDList.setPhone("15810275728");
            dList3.add(houseDList);
        }
        for (int j = 0; j < 2; j++)
        {
            HouseBList houseBList = new HouseBList();
            houseBList.setdId("" + j);
            houseBList.setdName(j + "单元");
            if (j == 0)
            {
                houseBList.setdList(dList1);
            }
            if (j == 1)
            {
                houseBList.setdList(dList2);
            }
            
            bList1.add(houseBList);
        }
        for (int K = 0; K < 2; K++)
        {
            HouseBList houseBList = new HouseBList();
            houseBList.setdId("" + (K + 2));
            houseBList.setdName((K + 2) + "单元");
            if (K == 0)
            {
                houseBList.setdList(dList2);
            }
            if (K == 1)
            {
                houseBList.setdList(dList3);
            }
            
            bList2.add(houseBList);
        }
        
        HouseDoc houseDoc = new HouseDoc();
        
        houseDoc.setbId("1");
        houseDoc.setbName("1栋");
        houseDoc.setbList(bList1);
        doc.add(houseDoc);
        
        houseDoc = new HouseDoc();
        houseDoc.setbId("2");
        houseDoc.setbName("2栋");
        houseDoc.setbList(bList2);
        doc.add(houseDoc);
        
        return doc;
    }
}
