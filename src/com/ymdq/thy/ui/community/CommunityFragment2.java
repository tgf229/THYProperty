package com.ymdq.thy.ui.community;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.ui.HomeFragmentActivity;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <邻里主页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-13]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityFragment2 extends BaseFragment implements OnClickListener
{
    private View view;
    
    /**
     * 返回按钮
     */
    private RelativeLayout back;
    
    /**
     * 回复消息按钮
     */
    private Button messageBtn;
    
    /**
     * 用户
     */
    private TextView user;
    
    /**
     * 用户布局
     */
    private LinearLayout userLayout;
    
    /**
     * 目前的Fragment Tag
     */
    private String curFragmentTag;
    
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    
    /**
     * 社区动态
     */
    private TextView communityDynamic;
    
    /**
     * 社区广场
     */
    private TextView communitySquare;
    
    /**
     * 下划线
     */
    private ImageView cursor;
    
    /**
     * 下划线宽度
     */
    private int bitmapWeight = 70;
    
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
    
    private String[] tags;
    
    /**
     * 未读消息数量
     */
    private TextView community_message_number;
    
    /**
     * 登陆成功广播
     */
    private CommunityMessageBroard communityMessageBroard;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.community_fragment2, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        init();
        initData();
        registreBroadcast();
        initImageView();
        loadAnimation();
        setTabSelection(getString(R.string.community_square));
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        back = (RelativeLayout)view.findViewById(R.id.title_back_layout);
        messageBtn = (Button)view.findViewById(R.id.title_btn_back);
        user = (TextView)view.findViewById(R.id.title_btn_call);
        userLayout = (LinearLayout)view.findViewById(R.id.title_call_layout);
        communityDynamic = (TextView)view.findViewById(R.id.community_dynamic);
        communitySquare = (TextView)view.findViewById(R.id.community_square);
        cursor = (ImageView)view.findViewById(R.id.cursor);
        community_message_number = (TextView)view.findViewById(R.id.community_message_number);
        
        /**
         *  添加按钮点击事件
         */
        userLayout.setOnClickListener(this);
        communityDynamic.setOnClickListener(this);
        communitySquare.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        messageBtn.setBackgroundResource(R.drawable.community_message);
        user.setTextSize(0);
        user.setBackgroundResource(R.drawable.community_user);
        fragmentManager = getActivity().getSupportFragmentManager();
        bitmapWeight = DisplayUtil.dip2px(getActivity(), bitmapWeight);
        tags = new String[] {getString(R.string.community_dynamic), getString(R.string.community_square)};
        if (((HomeFragmentActivity)getActivity()).getMessageNumber() > 0)
        {
            community_message_number.setVisibility(View.VISIBLE);
            community_message_number.setText(((HomeFragmentActivity)getActivity()).getMessageNumber() + "");
        }
        else
        {
            community_message_number.setVisibility(View.GONE);
        }
    }
    
    /**
     * 
     * <话题分享广播注册>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void registreBroadcast()
    {
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(Constants.COMMUNITY_MESSAGE_NUMBER_BROADCAST);
        communityMessageBroard = new CommunityMessageBroard();
        getActivity().registerReceiver(communityMessageBroard, loginFilter);
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
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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
        int one =  bitmapWeight  + DisplayUtil.dip2px(getActivity(), 10);// 页卡1 -> 页卡2 偏移量
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
        if (TextUtils.equals(tag, getString(R.string.community_dynamic)))
        {
            // 当点击了消息tab时，改变控件的图片和文字颜色
            setSelection(R.id.community_dynamic);
            if (mFragment == null)
            {
                mFragment = new CommunityDynamicFragment();
            }
        }
        if (TextUtils.equals(tag, getString(R.string.community_square)))
        {
            setSelection(R.id.community_square);
            if (mFragment == null)
            {
                mFragment = new CommunitySquareFragment();
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
        detachFragment();
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
    private void detachFragment()
    {
        for (int i = 0; i < tags.length; i++)
        {
            Fragment fragment = fragmentManager.findFragmentByTag(tags[i]);
            if (fragment != null && !fragment.isDetached())
            {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.hide(fragment);
                ft.commit();
            }
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
            ft.add(R.id.community_content, fragment, tag);
        }
        ft.commit();
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
            case R.id.community_dynamic:
                if (GeneralUtils.isNotNullOrZeroLenght(curFragmentTag))
                {
                    cursor.clearAnimation();
                    cursor.startAnimation(leftToRightAnimation);
                }
                communityDynamic.setBackgroundResource(R.drawable.title_dongtai_press);
                communitySquare.setBackgroundResource(R.drawable.title_guangchang);
//                communityDynamic.setTextColor(getResources().getColor(R.color.community_dynamic));
//                communitySquare.setTextColor(getResources().getColor(R.color.black_color));
                break;
            case R.id.community_square:
                if (GeneralUtils.isNotNullOrZeroLenght(curFragmentTag))
                {
                    cursor.clearAnimation();
                    cursor.startAnimation(rightToleftAnimation);
                }
                communityDynamic.setBackgroundResource(R.drawable.title_dongtai);
                communitySquare.setBackgroundResource(R.drawable.title_guangchang_press);
//                communityDynamic.setTextColor(getResources().getColor(R.color.black_color));
//                communitySquare.setTextColor(getResources().getColor(R.color.community_dynamic));
                break;
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        /**
         * 响应社区动态按钮
         */
            case R.id.community_dynamic:
                if (Global.isLogin())
                {
                    setTabSelection(getString(R.string.community_dynamic));
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(getActivity());
                }
                break;
            /**
             * 响应社区广场按钮
             */
            case R.id.community_square:
                setTabSelection(getString(R.string.community_square));
                break;
            /**
             * 响应个人界面按钮
             */
            case R.id.title_call_layout:
                if (Global.isLogin())
                {
                    Intent intent = new Intent(getActivity(), CommunityPersonDetailActivity.class);
                    intent.putExtra("queryUId", Global.getUserId());
                    startActivity(intent);
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(getActivity());
                }
                break;
            /**
             * 响应评论消息
             */
            case R.id.title_back_layout:
                if (Global.isLogin())
                {
                    Intent intent = new Intent(getActivity(), CommunityCommentMessageActivity.class);
                    startActivity(intent);
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(getActivity());
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <登陆成功刷新>
     * <功能详细描述>
     * 
     * @author  cyf
     * @version  [版本号, 2014-12-5]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    class CommunityMessageBroard extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //登录成功
            if (Constants.COMMUNITY_MESSAGE_NUMBER_BROADCAST.equals(intent.getAction()))
            {
                String number = intent.getStringExtra("number");
                if (Integer.valueOf(number) > 0)
                {
                    community_message_number.setVisibility(View.VISIBLE);
                    community_message_number.setText(number);
                }
                else
                {
                    community_message_number.setVisibility(View.GONE);
                }
            }
        }
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(communityMessageBroard);
    }
}
