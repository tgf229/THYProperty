package com.ymdq.thy.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <我的账单>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年11月20日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyCentralCardActivity extends FragmentActivity implements OnClickListener
{
    private TextView accountManage;
    
    private TextView paymentList;
    
    private ImageView cursor;
    
    private FragmentManager fragmentManager;
    
    /**
     * 下划线宽度
     */
    private int bitmapWeight = 100;    
    
    /**
     * 动画图片偏移量
     */
    private int offset;
    
    /**
     * 从左往右动画
     */
    private Animation leftToRightAnimation;
    
    /**
     * 从右往左动画
     */
    private Animation rightToleftAnimation;  
    
    /**
     * 目前的Fragment Tag
     */
    private String curFragmentTag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_my_central_card);
        
        JRApplication.jrApplication.addActivity(this);
        
        initView();
        initImageView();
        loadAnimation();
        setTabSelection(getString(R.string.account_manage_text));
    }
    
    @Override
    protected void onResume()
    {
        JRApplication.currentActivity = this.getClass().getName();
        super.onResume();
    }
    
    private void initView()
    {
        RelativeLayout titleBarBack = (RelativeLayout)findViewById(R.id.title_back_layout);
        titleBarBack.setOnClickListener(this);
        
        accountManage = (TextView)findViewById(R.id.account_manage);
        paymentList = (TextView)findViewById(R.id.payment_list);
        accountManage.setOnClickListener(this);
        paymentList.setOnClickListener(this);
        cursor = (ImageView)findViewById(R.id.cursor);

        fragmentManager = getSupportFragmentManager();
        bitmapWeight = DisplayUtil.dip2px(this, bitmapWeight);
    }
    
    /**
     * 
     * <初始化动画>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initImageView()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bitmapWeight);// 计算偏移量
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams)cursor.getLayoutParams(); //取控件textView当前的布局参数  
        linearParams.setMargins(offset, 0, 0, 0);
        cursor.setLayoutParams(linearParams);
    }
    
    /**
     * 
     * <初始化动画>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void loadAnimation()
    {
        int one = bitmapWeight  + DisplayUtil.dip2px(this, 10);;// 页卡1 -> 页卡2 偏移量
        leftToRightAnimation = new TranslateAnimation(0, one, 0, 0);
        leftToRightAnimation.setFillAfter(true);// True:图片停在动画结束位置
        leftToRightAnimation.setDuration(300);
        rightToleftAnimation = new TranslateAnimation(one, 0, 0, 0);
        rightToleftAnimation.setFillAfter(true);// True:图片停在动画结束位置
        rightToleftAnimation.setDuration(300);
    }
    
    /**
     * 
     * <设置选中的Tab>
     * <功能详细描述>
     * @param tag tab的名称
     * @see [类、类#方法、类#成员]
     */
    public void setTabSelection(String tag)
    {
        if (TextUtils.equals(tag, curFragmentTag))
        {
            return;
        }
        // 开启一个Fragment事务
        Fragment mFragment = fragmentManager.findFragmentByTag(tag);
        if (TextUtils.equals(tag, getString(R.string.account_manage_text)))
        {
            // 当点击了消息tab时，改变控件的图片和文字颜色
            setSelection(R.id.account_manage);
            if (mFragment == null)
            {
                mFragment = new AccountManageFragment();
            }
        }
        if (TextUtils.equals(tag, getString(R.string.payment_list_text)))
        {
            setSelection(R.id.payment_list);
            if (mFragment == null)
            {
                mFragment = new PaymentListFragment();
            }
        }
        switchFragment(mFragment, tag);
    }
    
    /**
     * 
     * <根据传入的tag切换fragment>
     * <功能详细描述>
     * @param tag
     * @see [类、类#方法、类#成员]
     */
    private void switchFragment(Fragment fragment, String tag)
    {
        detachFragment(curFragmentTag);
        attachFragment(fragment, tag);
        curFragmentTag = tag;
    }
    
    /**
     * 
     * <隐藏当前Fragment>
     * <功能详细描述>
     * @param f
     * @see [类、类#方法、类#成员]
     */
    private void detachFragment(String tag)
    {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null && !fragment.isDetached())
        {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.hide(fragment);
            ft.commit();
        }
    }
    
    /**
     * 
     * <加入Fragment>
     * <功能详细描述>
     * @param layout
     * @param f
     * @param tag
     * @see [类、类#方法、类#成员]
     */
    private void attachFragment(Fragment fragment, String tag)
    {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (fragment.isHidden())
        {
            ft.show(fragment);
        }
        else if (fragment.isDetached())
        {
            ft.attach(fragment);
        }
        else if (!fragment.isAdded())
        {
            ft.add(R.id.account_fragment, fragment, tag);
        }
        ft.commit();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            //账号管理
            case R.id.account_manage:
                setTabSelection(getString(R.string.account_manage_text));
                break;
            //缴费清单
            case R.id.payment_list:
                setTabSelection(getString(R.string.payment_list_text));
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <设置控件的选择状态>
     * <功能详细描述>
     * @param id   //传入父视图的id
     * @see [类、类#方法、类#成员]
     */
    private void setSelection(int id)
    {
        switch (id)
        {
            //账号管理
            case R.id.account_manage:
                if (GeneralUtils.isNotNullOrZeroLenght(curFragmentTag))
                {
                    cursor.clearAnimation();
                    cursor.startAnimation(rightToleftAnimation);
                }
                accountManage.setTextColor(getResources().getColor(R.color.register_title_new));
                paymentList.setTextColor(getResources().getColor(R.color.black_color));
                break;
            //缴费清单
            case R.id.payment_list:
                if (GeneralUtils.isNotNullOrZeroLenght(curFragmentTag))
                {
                    cursor.clearAnimation();
                    cursor.startAnimation(leftToRightAnimation);
                }
                accountManage.setTextColor(getResources().getColor(R.color.black_color));
                paymentList.setTextColor(getResources().getColor(R.color.register_title_new));
                break;
        }
    }
    
    @Override
    protected void onDestroy()
    {
        JRApplication.jrApplication.deleteActivity(this);
        super.onDestroy();
        
    }
}
