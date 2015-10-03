package com.ymdq.thy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DefineWHImageView extends ImageView
{
    public DefineWHImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width=(getResources().getDisplayMetrics().widthPixels - 26)/3;
        
        int height=width;
        setMeasuredDimension(width, height);
    }
}
