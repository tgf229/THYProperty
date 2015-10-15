package com.ymdq.thy.ui.community;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.GroupPersonInfoResponse;
import com.ymdq.thy.bean.personcenter.UploadHeadPhotoResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.community.service.CommunityService;
import com.ymdq.thy.ui.community.widget.CircleImgView;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.FileSystemManager;
import com.ymdq.thy.util.FileUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <个人详情界面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-12-2]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityPersonDetailActivity extends FragmentActivity implements OnClickListener, UICallBack
{
    
    /**
     * 返回按钮
     */
    private LinearLayout back;
    
    /**
     * 设置按钮
     */
    private TextView edit;
    
    private LinearLayout editLayout;
    
    /**
     * 头像
     */
    private ImageView head,arrow;
    
    /**
     * 大V
     */
    private ImageView vip;
    
    /**
     * 昵称
     */
    private TextView name;
    
    /**
     * 签名
     */
    private TextView sign;
    
    /**
     * 我的话题
     */
    private TextView myTopic,title;
    
    /**
     * 我的关注
     */
    private TextView myAddedGroup;
    
    /**
     * 我管理的社区
     */
    private TextView myEditGroup;
    
    /**
     * 下划线
     */
    private ImageView cursor;
    
    /**
     * 下划线宽度
     */
    private int bitmapWeight = 80;
    
    /**
     * 前面两个动画图片偏移量
     */
    private int firstOffset;
    
    /**
     * 最后一个的偏移量
     */
    private int lastOffset;
    
    /**
     * 从左往右动画(左->中)
     */
    private Animation animation;
    
    /**
     * 中间偏移量
     */
    private int center;
    
    /**
     * 右边偏移量
     */
    private int right;
    
    /**
     * 当前页
     */
    private String curFragmentTag;
    
    /**
     * 查询id
     */
    private String queryUId;
    
    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;
    
    /**
     * 拍摄图片的url地址
     */
    private String photoPath;
    
    /**
     * 头像
     */
    private SoftReference<Bitmap> photoReference;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 头像地址标记
     */
    private final String HEAD = "head";
    
    /**
     * 个人信息
     */
    private GroupPersonInfoResponse groupPersonInfoResponse;
    
    /**
     * 登陆成功广播
     */
    private LoginSuccessBroard broardcast;
    
    /**
     * 是否从相机拍照
     */
    private boolean isFromCamera;
    
    private RelativeLayout person;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_person_detail);
        JRApplication.jrApplication.addActivity(this);
        init();
        initData();
        initImageView();
        setTabSelection(getString(R.string.my_topic));
        registreBroadcast();
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
//        title.setText("个人中心");
        title.setBackgroundResource(R.drawable.title_gerenzhongxin);
        edit = (TextView)findViewById(R.id.title_btn_call);
        edit.setBackgroundResource(R.drawable.title_red_shezhi);
//        edit.setText("设置");
//        edit.setTextSize(16);
//        edit.setTextColor(getResources().getColor(R.color.bb474d));
        editLayout = (LinearLayout)findViewById(R.id.title_call_layout);
        
        person = (RelativeLayout)findViewById(R.id.person);
        arrow = (ImageView)findViewById(R.id.arrow);
        
        
//        back = (ImageView)findViewById(R.id.back);
//        edit = (ImageView)findViewById(R.id.edit);
        head = (ImageView)findViewById(R.id.head);
        name = (TextView)findViewById(R.id.name);
        sign = (TextView)findViewById(R.id.sign);
        myTopic = (TextView)findViewById(R.id.my_topic);
        myAddedGroup = (TextView)findViewById(R.id.my_added_group);
        myEditGroup = (TextView)findViewById(R.id.my_edit_group);
        cursor = (ImageView)findViewById(R.id.cursor);
        vip = (ImageView)findViewById(R.id.icon_vip);
        
        /**
         * 添加按钮点击事件
         */
        
        editLayout.setOnClickListener(this);
        back.setOnClickListener(this);
        head.setOnClickListener(this);
        myTopic.setOnClickListener(this);
        myAddedGroup.setOnClickListener(this);
        myEditGroup.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        queryUId = getIntent().getStringExtra("queryUId");
        //本用户
        if (Global.getUserId().equals(queryUId))
        {
            person.setOnClickListener(this);
            arrow.setVisibility(View.VISIBLE);
            editLayout.setVisibility(View.VISIBLE);
            head.setClickable(true);
            myTopic.setText("我的话题");
            myAddedGroup.setText("我的关注");
            myEditGroup.setText("我管理的圈子");
        }
        //其他用户
        else
        {
            arrow.setVisibility(View.GONE);
            editLayout.setVisibility(View.GONE);
            head.setClickable(false);
            myTopic.setText("他的话题");
            myAddedGroup.setText("他的关注");
            myEditGroup.setText("他管理的圈子");
        }
        fragmentManager = getSupportFragmentManager();
        CommunityService.instance().queryPersonInfo(queryUId,
            CommunityPersonDetailActivity.this,
            CommunityPersonDetailActivity.this);
    }
    
    @Override
    protected void onResume()
    {
        JRApplication.currentActivity = this.getClass().getName();
        super.onResume();
    }
    
    /**
     * 
     * <初始化动画>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initImageView()
    {
        bitmapWeight = DisplayUtil.dip2px(CommunityPersonDetailActivity.this, bitmapWeight);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        firstOffset = (screenW / 16 * 5 - bitmapWeight) / 2;
        lastOffset = (screenW / 16 * 6 - bitmapWeight) / 2;
        center = firstOffset * 2 + bitmapWeight;// 页卡1 -> 页卡2 偏移量
        right = firstOffset * 3 + lastOffset + bitmapWeight * 2;// 页卡2 -> 页卡3 偏移量
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams)cursor.getLayoutParams(); //取控件textView当前的布局参数  
        linearParams.setMargins(firstOffset, 0, 0, 0);
        cursor.setLayoutParams(linearParams);
    }
    
    /**
     * 
     * <初始化动画>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void loadAnimation(float fromXDelta, float toXDelta)
    {
        cursor.clearAnimation();
        animation = new TranslateAnimation(fromXDelta, toXDelta, 0, 0);
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }
    
    /**
     * 
     * <登录成功后，接收广播消息>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void registreBroadcast()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.LOGIN_SUCCESS_BROADCAST);
        broardcast = new LoginSuccessBroard();
        CommunityPersonDetailActivity.this.registerReceiver(broardcast, filter);
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
        if (TextUtils.equals(tag, getString(R.string.my_topic)))
        {
            // 当点击了消息tab时，改变控件的图片和文字颜色
            setSelection(R.id.my_topic);
            if (mFragment == null)
            {
                mFragment = new CommunityMyTopicFragment();
                ((CommunityMyTopicFragment)mFragment).queryUId = queryUId;
            }
        }
        if (TextUtils.equals(tag, getString(R.string.my_added_group)))
        {
            setSelection(R.id.my_added_group);
            if (mFragment == null)
            {
                mFragment = new CommunityMyAddedFragment();
                ((CommunityMyAddedFragment)mFragment).queryUId = queryUId;
            }
        }
        if (TextUtils.equals(tag, getString(R.string.my_edit_group)))
        {
            setSelection(R.id.my_edit_group);
            if (mFragment == null)
            {
                mFragment = new CommunityMyEditFragment();
                ((CommunityMyEditFragment)mFragment).queryUId = queryUId;
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
        /**
         * 响应我的话题按钮
         */
            case R.id.my_topic:
                if (!getString(R.string.my_topic).equals(curFragmentTag))
                {
                    if (getString(R.string.my_added_group).equals(curFragmentTag))
                    {
                        loadAnimation(center, 0);
                    }
                    else if (getString(R.string.my_edit_group).equals(curFragmentTag))
                    {
                        loadAnimation(right, 0);
                    }
                    myTopic.setTextColor(getResources().getColor(R.color.community_dynamic));
                    myAddedGroup.setTextColor(getResources().getColor(R.color.black_color));
                    myEditGroup.setTextColor(getResources().getColor(R.color.black_color));
                }
                break;
            /**
             * 响应我的关注按钮
             */
            case R.id.my_added_group:
                if (!getString(R.string.my_added_group).equals(curFragmentTag))
                {
                    if (getString(R.string.my_topic).equals(curFragmentTag))
                    {
                        loadAnimation(0, center);
                    }
                    else if (getString(R.string.my_edit_group).equals(curFragmentTag))
                    {
                        loadAnimation(right, center);
                    }
                    myTopic.setTextColor(getResources().getColor(R.color.black_color));
                    myAddedGroup.setTextColor(getResources().getColor(R.color.community_dynamic));
                    myEditGroup.setTextColor(getResources().getColor(R.color.black_color));
                }
                break;
            /**
             * 响应我管理的按钮
             */
            case R.id.my_edit_group:
                if (!getString(R.string.my_edit_group).equals(curFragmentTag))
                {
                    if (getString(R.string.my_added_group).equals(curFragmentTag))
                    {
                        loadAnimation(center, right);
                    }
                    else if (getString(R.string.my_topic).equals(curFragmentTag))
                    {
                        loadAnimation(0, right);
                    }
                    myTopic.setTextColor(getResources().getColor(R.color.black_color));
                    myEditGroup.setTextColor(getResources().getColor(R.color.community_dynamic));
                    myAddedGroup.setTextColor(getResources().getColor(R.color.black_color));
                }
                break;
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        /**
         * 响应我的话题按钮
         */
            case R.id.my_topic:
                setTabSelection(getString(R.string.my_topic));
                break;
            /**
             * 响应我的关注按钮
             */
            case R.id.my_added_group:
                setTabSelection(getString(R.string.my_added_group));
                break;
            /**
             * 响应我管理的按钮
             */
            case R.id.my_edit_group:
                if (Global.getUserId().equals(queryUId))
                {
                    Intent intent = new Intent(CommunityPersonDetailActivity.this, CommunityCircleListActivity.class);
                    intent.putExtra("type", Constants.MY_MANAGED_COMMUNITY);
                    startActivity(intent);
                }
                else
                {
                    setTabSelection(getString(R.string.my_edit_group));
                }
                break;
            /**
             * 响应返回按钮
             */
            case R.id.title_back_layout:
                finish();
                break;
            /**
             * 响应编辑按钮
             */
            case R.id.title_call_layout:
                Intent intent = new Intent(CommunityPersonDetailActivity.this, CommunityPersonInfoEditActivity.class);
                intent.putExtra("person", groupPersonInfoResponse);
                startActivityForResult(intent, Constants.NUM0);
                break;
            case R.id.person:
                Intent intent1 = new Intent(CommunityPersonDetailActivity.this, CommunityPersonInfoEditActivity.class);
                intent1.putExtra("person", groupPersonInfoResponse);
                startActivityForResult(intent1, Constants.NUM0);
                break;
                
            /**
             * 响应头像点击事件
             */
            case R.id.head:
                showPhotoDiaLog();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        /**
         * 圈子个人信息
         */
        if (ob instanceof GroupPersonInfoResponse)
        {
            groupPersonInfoResponse = (GroupPersonInfoResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(groupPersonInfoResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(groupPersonInfoResponse.getRetcode()))
                {
                    showPersonDetail(groupPersonInfoResponse);
                }
                else
                {
                    ToastUtil.makeText(CommunityPersonDetailActivity.this, "获取个人信息失败");
                }
            }
            else
            {
                ToastUtil.makeText(CommunityPersonDetailActivity.this, "获取个人信息失败");
            }
        }
        /**
         * 上传头像
         */
        else if (ob instanceof UploadHeadPhotoResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            UploadHeadPhotoResponse uploadHeadPhotoResponse = (UploadHeadPhotoResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(uploadHeadPhotoResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(uploadHeadPhotoResponse.getRetcode()))
                {
                    head.setImageBitmap(photoReference.get());
                    ToastUtil.makeText(CommunityPersonDetailActivity.this, "头像上传成功");
                    deleteDirectoryHead("");
                    deleteDirectoryHead(HEAD);
                    saveMyBitmap(FileSystemManager.getUserHeadPath(this, Global.getUserId()) + photoPath,
                        photoReference.get());
                    Global.saveImage(FileSystemManager.getUserHeadPath(this, Global.getUserId()) + photoPath);
                }
                else
                {
                    ToastUtil.makeText(CommunityPersonDetailActivity.this, uploadHeadPhotoResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(CommunityPersonDetailActivity.this);
            }
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        JRApplication.jrApplication.deleteActivity(this);
        deleteDirectoryHead("");
        CommunityPersonDetailActivity.this.unregisterReceiver(broardcast);
    }
    
    /**
     * 
     * <展示个人详情>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showPersonDetail(GroupPersonInfoResponse groupPersonInfoResponse)
    {
        name.setText(groupPersonInfoResponse.getNickName());
        sign.setText(groupPersonInfoResponse.getDesc());
        if (BitmapFactory.decodeFile(Global.getImage()) != null)
            head.setImageBitmap(BitmapFactory.decodeFile(Global.getImage()));
        ImageLoader.getInstance().displayImage(groupPersonInfoResponse.getImageUrl(),
            head,
            JRApplication.setAllDisplayImageOptions(CommunityPersonDetailActivity.this,
                "default_head_pic",
                "default_head_pic",
                "default_head_pic"));
        if (Constants.LEVEL_VIP.equals(groupPersonInfoResponse.getUserLevel()))
        {
            vip.setVisibility(View.VISIBLE);
        }
        else
        {
            vip.setVisibility(View.GONE);
        }
    }
    
    /**
     * 
     * <获取头像>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showPhotoDiaLog()
    {
        photoPath = "head_" + System.currentTimeMillis() + ".jpg";
        final Dialog imageSelectDialog = new Dialog(this, R.style.image_select_dialog);
        imageSelectDialog.setContentView(R.layout.image_select_dialog);
        imageSelectDialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        imageSelectDialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
        imageSelectDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        imageSelectDialog.getWindow().setWindowAnimations(R.style.dialog_style);
        Button camera = (Button)imageSelectDialog.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        Button gallery = (Button)imageSelectDialog.findViewById(R.id.gallery);
        gallery.setOnClickListener(this);
        Button cancel = (Button)imageSelectDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        imageSelectDialog.show();
        /**
         * 从相册选取照片
         */
        gallery.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                imageSelectDialog.dismiss();
                selectPhoto();
            }
        });
        /**
         * 从相机拍摄照片
         */
        camera.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // 先验证手机是否有sdcard
                imageSelectDialog.dismiss();
                takePhoto();
            }
            
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View arg0)
            {
                imageSelectDialog.dismiss();
                
            }
        });
    }
    
    /**
     * 
     * <开启相机>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void takePhoto()
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            try
            {
                File picture =
                    new File(FileSystemManager.getTemporaryPath(CommunityPersonDetailActivity.this) + photoPath);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageFileUri = Uri.fromFile(picture);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(intent, Constants.NUM1);
            }
            catch (ActivityNotFoundException e)
            {
                ToastUtil.makeText(CommunityPersonDetailActivity.this, "没有找到储存目录");
            }
        }
        else
        {
            ToastUtil.makeText(CommunityPersonDetailActivity.this, "没有储存卡");
        }
    }
    
    /**
     * 
     * <从相册选取>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void selectPhoto()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.NUM2);
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == Constants.NUM3 && resultCode == Constants.NUM3 && Global.getUserId().equals(queryUId))
        {
            Fragment mFragment = fragmentManager.findFragmentByTag(getString(R.string.my_added_group));
            CommunityMyAddedFragment addedFragment = (CommunityMyAddedFragment)mFragment;
            if (addedFragment != null)
                addedFragment.refresh();
        }
        /**
         * 修改个人信息返回
         */
        if (requestCode == Constants.NUM0 && resultCode == Constants.NUM1)
        {
            name.setText(data.getStringExtra("personName"));
            sign.setText(data.getStringExtra("personDesc"));
            groupPersonInfoResponse.setNickName(data.getStringExtra("personName"));
            groupPersonInfoResponse.setDesc(data.getStringExtra("personDesc"));
        }
        /**
         * 调用系统拍照,并且成功.
         */
        if (requestCode == Constants.NUM1 && resultCode == -1)
        {
            File picture = new File(FileSystemManager.getTemporaryPath(this) + photoPath);
            isFromCamera = true;
            startPhotoZoom(Uri.fromFile(picture));
        }
        /**
         * 调用系统选照片,并且成功.
         */
        if (requestCode == Constants.NUM2 && resultCode == -1)
        {
            if (data == null)
            {
                return;
            }
            isFromCamera = false;
            startPhotoZoom(data.getData());
        }
        /**
         * 剪切成功
         */
        if (requestCode == Constants.NUM4)
        {
            if (data == null)
            {
                if (isFromCamera)
                {
                    takePhoto();
                }
                else
                {
                    selectPhoto();
                }
            }
            else
            {
                Bundle extras = data.getExtras();
                if (extras != null && extras.getParcelable("data") != null)
                {
                    photoReference = new SoftReference<Bitmap>((Bitmap)extras.getParcelable("data"));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photoReference.get().compress(Bitmap.CompressFormat.JPEG, 75, stream);
                    deleteDirectoryHead("");//清除缓存头像
                    saveMyBitmap(FileSystemManager.getUserHeadPathTemp(this, Global.getUserId()) + photoPath,
                        photoReference.get());
                    uploadHeadPhoto();
                }
            }
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    /**
     * 
     * <上传头像>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void uploadHeadPhoto()
    {
        File file = new File(FileSystemManager.getUserHeadPathTemp(this, Global.getUserId()) + photoPath);
        ArrayList<File> files = new ArrayList<File>();
        files.add(file);
        Map<String, List<File>> fileParameters = new HashMap<String, List<File>>();
        fileParameters.put("file", files);
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        
        Map<String, String> param = new HashMap<String, String>();
        param.put("uId", Global.getUserId());
        
        ConnectService.instance().connectServiceUploadFile(CommunityPersonDetailActivity.this,
            param,
            fileParameters,
            CommunityPersonDetailActivity.this,
            UploadHeadPhotoResponse.class,
            URLUtil.USER_UPLOAD_HEAD_PIC,
            Constants.ENCRYPT_NONE);
    }
    
    public void startPhotoZoom(Uri uri)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        
        intent.putExtra("outputX", 180);
        intent.putExtra("outputY", 180);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Constants.NUM4);
    }
    
    public void saveMyBitmap(String pathName, Bitmap b)
    {
        try
        {
            File file = new File(pathName);
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 清除头像缓存
     */
    private void deleteDirectoryHead(String head)
    {
        String headPath = "";
        if (head.equals(HEAD))
        {
            headPath = FileSystemManager.getUserHeadPath(this, Global.getUserId());
        }
        else
        {
            headPath = FileSystemManager.getUserHeadPathTemp(this, Global.getUserId());
        }
        FileUtil.deleteDirectory(headPath);
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
    class LoginSuccessBroard extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //登录成功
            if (Constants.LOGIN_SUCCESS_BROADCAST.equals(intent.getAction()))
            {
                //本用户
                if (Global.getUserId().equals(queryUId))
                {
                    edit.setVisibility(View.VISIBLE);
                    head.setClickable(true);
                    myTopic.setText("我的话题");
                    myAddedGroup.setText("我的关注");
                    myEditGroup.setText("我管理的圈子");
                }
                //其他用户
                else
                {
                    edit.setVisibility(View.GONE);
                    head.setClickable(false);
                    myTopic.setText("他的话题");
                    myAddedGroup.setText("他的关注");
                    myEditGroup.setText("他管理的圈子");
                }
            }
        }
    }
    
}
