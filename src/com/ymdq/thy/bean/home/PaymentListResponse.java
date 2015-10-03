package com.ymdq.thy.bean.home;

import java.util.ArrayList;

import com.ymdq.thy.bean.BaseResponse;

public class PaymentListResponse extends BaseResponse
{
    private ArrayList<PaymentListDoc> doc;

    public ArrayList<PaymentListDoc> getDoc()
    {
        return doc;
    }

    public void setDoc(ArrayList<PaymentListDoc> doc)
    {
        this.doc = doc;
    }
}
