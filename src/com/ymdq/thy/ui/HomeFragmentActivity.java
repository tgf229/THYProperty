package com.ymdq.thy.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.UploadCrashFileResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.community.CommunityFragment;
import com.ymdq.thy.ui.home.MainFragment;
import com.ymdq.thy.ui.personcenter.PersonCenterFragment;
import com.ymdq.thy.ui.propertyservice.PropertyServiceFragment;
import com.ymdq.thy.util.FileSystemManager;
import com.ymdq.thy.util.FileUtil;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <基础FragmentActivity>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HomeFragmentActivity extends FragmentActivity implements UICallBack, OnClickListener
{
    /**
     * Tab标签
     */
    private RelativeLayout home_menu_home, home_menu_property_service, home_menu_community, home_menu_person_center;
    
    /**
     * 标签的图片
     */
    private ImageView home_menu_home_img, home_menu_property_service_img, home_menu_community_img,
        home_menu_person_center_img;
    
    /**
     * 标签文本
     */
    private ImageView home_menu_home_txt, home_menu_property_service_txt, home_menu_community_txt,
        home_menu_person_center_txt;
    
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    
    /**
     * 目前的Fragment Tag
     */
    private String curFragmentTag;
    
    /**
     * 上次退出的时间
     */
    private long downTime;
    
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
    protected void onCreate(Bundle arg0)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(arg0);
        setContentView(R.layout.activity_home_fragment);
        JRApplication.jrApplication.addActivity(this);
        init();
        registreBroadcast();
        initData();
        setTabSelection(getString(R.string.home_menu_home));
        uploadCrashFile();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        //        super.onSaveInstanceState(outState);
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        home_menu_home = (RelativeLayout)findViewById(R.id.home_menu_home);
        home_menu_property_service = (RelativeLayout)findViewById(R.id.home_menu_property_service);
        home_menu_community = (RelativeLayout)findViewById(R.id.home_menu_community);
        home_menu_person_center = (RelativeLayout)findViewById(R.id.home_menu_person_center);
        
        home_menu_home_img = (ImageView)findViewById(R.id.home_menu_home_img);
        home_menu_property_service_img = (ImageView)findViewById(R.id.home_menu_property_service_img);
        home_menu_community_img = (ImageView)findViewById(R.id.home_menu_community_img);
        home_menu_person_center_img = (ImageView)findViewById(R.id.home_menu_person_center_img);
        
        home_menu_home_txt = (ImageView)findViewById(R.id.home_menu_home_txt);
        home_menu_property_service_txt = (ImageView)findViewById(R.id.home_menu_property_service_txt);
        home_menu_community_txt = (ImageView)findViewById(R.id.home_menu_community_txt);
        home_menu_person_center_txt = (ImageView)findViewById(R.id.home_menu_person_center_txt);
        
        community_message_number = (TextView)findViewById(R.id.community_message_number);
        
        /**
         * 添加按钮点击事件
         */
        home_menu_home.setOnClickListener(this);
        home_menu_property_service.setOnClickListener(this);
        home_menu_community.setOnClickListener(this);
        home_menu_person_center.setOnClickListener(this);
    }
    
    @Override
    protected void onResume()
    {
        JRApplication.currentActivity = this.getClass().getName();
        super.onResume();
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        fragmentManager = getSupportFragmentManager();
        tags =
            new String[] {getString(R.string.home_menu_home), getString(R.string.home_menu_property_service),
                getString(R.string.home_menu_community), getString(R.string.home_menu_person_center)};
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
        registerReceiver(communityMessageBroard, loginFilter);
    }
    
    /**
     * 
     * <上传崩溃日志文件到服务器>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void uploadCrashFile()
    {
        File root = new File(FileSystemManager.getCrashPath(HomeFragmentActivity.this));
        File[] listFiles = root.listFiles();
        if (listFiles != null && listFiles.length > 0)
        {
            ArrayList<File> files = new ArrayList<File>();
            for (File file : listFiles)
            {
                files.add(file);
            }
            Map<String, List<File>> fileParameters = new HashMap<String, List<File>>();
            fileParameters.put("file", files);
            ConnectService.instance().connectServiceUploadFile(HomeFragmentActivity.this,
                null,
                fileParameters,
                HomeFragmentActivity.this,
                UploadCrashFileResponse.class,
                URLUtil.UPLOAD_CRASH_FILE,
                Constants.ENCRYPT_NONE);
        }
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
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        Fragment mFragment = fragmentManager.findFragmentByTag(tag);
        if (TextUtils.equals(tag, getString(R.string.home_menu_home)))
        {
            // 当点击了消息tab时，改变控件的图片和文字颜色
            setSelection(R.id.home_menu_home);
            if (mFragment == null)
            {
                mFragment = new MainFragment();
            }
        }
        if (TextUtils.equals(tag, getString(R.string.home_menu_property_service)))
        {
            setSelection(R.id.home_menu_property_service);
            if (mFragment == null)
            {
                mFragment = new PropertyServiceFragment();
            }
        }
        if (TextUtils.equals(tag, getString(R.string.home_menu_community)))
        {
            setSelection(R.id.home_menu_community);
            if (mFragment == null)
            {
                mFragment = new CommunityFragment();
            }
        }
        if (TextUtils.equals(tag, getString(R.string.home_menu_person_center)))
        {
            setSelection(R.id.home_menu_person_center);
            if (mFragment == null)
            {
                mFragment = new PersonCenterFragment();
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
            ft.add(R.id.content, fragment, tag);
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
            case R.id.home_menu_home:
                home_menu_home_img.setImageResource(R.drawable.home_icon_home_press);
                //home_menu_home_txt.setTextColor(getResources().getColor(R.color.home_menu_normal));
                break;
            case R.id.home_menu_property_service:
                home_menu_property_service_img.setImageResource(R.drawable.home_icon_service_press);
                //home_menu_property_service_txt.setTextColor(getResources().getColor(R.color.home_menu_normal));
                break;
            case R.id.home_menu_community:
                home_menu_community_img.setImageResource(R.drawable.home_icon_neighbor_press);
                //home_menu_community_txt.setTextColor(getResources().getColor(R.color.home_menu_normal));
                break;
            case R.id.home_menu_person_center:
                home_menu_person_center_img.setImageResource(R.drawable.home_icon_myhome_press);
                //home_menu_person_center_txt.setTextColor(getResources().getColor(R.color.home_menu_normal));
                break;
        }
    }
    
    /**
     * 
     * <清楚所有选中状态>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void clearSelection()
    {
        home_menu_home_img.setImageResource(R.drawable.home_icon_home);
        home_menu_property_service_img.setImageResource(R.drawable.home_icon_service);
        home_menu_community_img.setImageResource(R.drawable.home_icon_neighbor);
        home_menu_person_center_img.setImageResource(R.drawable.home_icon_myhome);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.home_menu_home:
                setTabSelection(getString(R.string.home_menu_home));
                break;
            case R.id.home_menu_property_service:
                setTabSelection(getString(R.string.home_menu_property_service));
                break;
            case R.id.home_menu_community:
                setTabSelection(getString(R.string.home_menu_community));
                break;
            case R.id.home_menu_person_center:
                setTabSelection(getString(R.string.home_menu_person_center));
                break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (curFragmentTag.equals(getString(R.string.home_menu_home)))
        {
            if (keyCode == KeyEvent.KEYCODE_BACK)
            {
                if (event.getDownTime() - downTime <= 2000)
                {
                    JRApplication.jrApplication.onTerminate();
                }
                else
                {
                    Toast.makeText(HomeFragmentActivity.this, getString(R.string.home_back), Toast.LENGTH_SHORT).show();
                    downTime = event.getDownTime();
                    return true;
                }
            }
        }
        else
        {
            setTabSelection(getString(R.string.home_menu_home));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(communityMessageBroard);
        JRApplication.jrApplication.deleteActivity(this);
    }
    
    @Override
    public void netBack(Object ob)
    {
        /**
         * 向服务器上传崩溃文件
         */
        if (ob instanceof UploadCrashFileResponse)
        {
            UploadCrashFileResponse uploadCrashFileResponse = (UploadCrashFileResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(uploadCrashFileResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(uploadCrashFileResponse.getRetcode()))
                {
                    //发送成功后删除本地文件
                    FileUtil.deleteDirectory(FileSystemManager.getCrashPath(HomeFragmentActivity.this));
                }
            }
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
    
    /**
     * 
     * <获取未读消息数量>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    public int getMessageNumber()
    {
        return Integer.valueOf("".equals(community_message_number.getText().toString()) ? "0"
            : community_message_number.getText().toString());
    }
}
