package com.ymdq.thy.bean.home;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

public class AccountManageResponse extends BaseResponse
{
    private ArrayList<AccountManageDoc> doc;

    public ArrayList<AccountManageDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<AccountManageDoc> doc)
    {
        this.doc = doc;
    }
}
