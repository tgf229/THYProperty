package com.ymdq.thy.ui;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.photoview.PhotoView;

/**
 * 
 * <图片左右滑动，放大缩小>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-7-3]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ViewPagerActivity extends BaseActivity
{
    /**
     * 图片url列表
     */
    private List<String> photoUrls;
    
    /**
     * 当然显示页数--从0开始
     */
    private int currentItem;
    
    /**
     * 左右滑动
     */
    private ViewPager mViewPager;
    
    /**
     * 当前页数
     */
    private TextView mCurrentPager;
    
    /**
     *总页数
     */
    private TextView mTotalPager;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_page_activity);
        
        photoUrls = getIntent().getStringArrayListExtra("photoUrls");
        currentItem = getIntent().getIntExtra("currentItem", 0);
        init();
        loadData();
    }
    
    /**
     * 
     * <一句话功能简述>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        mViewPager = (ViewPager)findViewById(R.id.viewpage_viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {
            
            @Override
            public void onPageSelected(int arg0)
            {
                
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                mCurrentPager.setText((mViewPager.getCurrentItem() + 1) + "");
            }
        });
        mCurrentPager = (TextView)findViewById(R.id.viewpage_page_current);
        mTotalPager = (TextView)findViewById(R.id.viewpage_page_total);
        
    }
    
    private void loadData()
    {
        mViewPager.setCurrentItem(currentItem, true);
        mCurrentPager.setText((currentItem + 1) + "");
        mTotalPager.setText("/" + photoUrls.size());
    }
    
    class SamplePagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return photoUrls.size();
        }
        
        @Override
        public View instantiateItem(ViewGroup container, int position)
        {
            View pageView = LayoutInflater.from(container.getContext()).inflate(R.layout.photo_view_item, null);
            PhotoView photoView = (PhotoView)pageView.findViewById(R.id.photo);
            ImageLoader.getInstance().displayImage(photoUrls.get(position),
                photoView,
                JRApplication.setAllDisplayImageOptions(container.getContext(),
                    "community_default_square",
                    "community_default_square",
                    "community_default_square"));
            container.addView(pageView, 0);
            return pageView;
        }
        
        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            ((ViewPager)container).removeView((View)object);
        }
        
        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
}
