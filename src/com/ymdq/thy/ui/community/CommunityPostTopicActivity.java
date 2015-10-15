package com.ymdq.thy.ui.community;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.ImgPicker;
import com.ymdq.thy.bean.PicturePropertiesBean;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.ChoiseMorePhotosListActivity;
import com.ymdq.thy.ui.ViewPagerActivity;
import com.ymdq.thy.util.BitmapUtil;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.FileSystemManager;
import com.ymdq.thy.util.FileUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <发表话题界面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-26]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityPostTopicActivity extends BaseActivity implements OnClickListener
{
    /**
     * 返回按钮
     */
    private LinearLayout back;
    
    /**
     * 标题
     */
    private TextView title;
    
    /**
     * 发表话题
     */
    private TextView postTopic;
    
    /**
     * 注册点击
     */
    private LinearLayout postTopicLayout;
    
    /**
     * 内容
     */
    private EditText content;
    
    /**
     * 内容长度
     */
    private TextView textChangeLength;
    
    /**
     * 图片选择
     */
    private LinearLayout selectPhoto;
    
    /**
     * 默认图片
     */
    private ImageView defaultPhoto;
    
    /**
     * 是否开启投票
     */
    private LinearLayout vote;
    
    /**
     * 是否开启投票
     */
    private ImageView isVote;
    
    /**
     * 拍摄图片的url地址
     */
    private String photoPath;
    
    /**
     * 缩放尺寸
     */
    protected static final float IMG_SCALE = 640f;
    
    /**
     * url列表数据
     */
    private ArrayList<String> paths = new ArrayList<String>();
    
    /**
     * 具体选择照片布局
     */
    private LinearLayout selectPhotoLayout;
    
    /**
     * 具体选择照片行
     */
    private LinearLayout selectPhotoLine;
    
    /**
     * 照片数量
     */
    private TextView imgNum;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 圈子id
     */
    private String circleId;
    
    /**
     * 大图Url地址
     */
    private ArrayList<String> photoUrls = new ArrayList<String>();
    
    /**
     * 图片缓存
     */
    private HashMap<String, SoftReference<Bitmap>> imageCache;
    
    /**
     * 是否支持投票
     */
    private boolean isSupportVote;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_post_topic);
        
        init();
        initData(savedInstanceState);
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
        postTopic = (TextView)findViewById(R.id.title_btn_call);
        postTopicLayout = (LinearLayout)findViewById(R.id.title_call_layout);
        defaultPhoto = (ImageView)findViewById(R.id.default_photo);
        content = (EditText)findViewById(R.id.content);
        textChangeLength = (TextView)findViewById(R.id.text_change_length);
        selectPhoto = (LinearLayout)findViewById(R.id.select_photo);
        vote = (LinearLayout)findViewById(R.id.vote);
        isVote = (ImageView)findViewById(R.id.is_vote);
        selectPhotoLayout = (LinearLayout)findViewById(R.id.select_photo_layout);
        selectPhotoLine = (LinearLayout)findViewById(R.id.select_photo_line);
        imgNum = (TextView)findViewById(R.id.img_txt_num);
        
        /**
         * 添加按钮点击事件
         */
        back.setOnClickListener(this);
        postTopicLayout.setOnClickListener(this);
        vote.setOnClickListener(this);
        selectPhoto.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            photoPath = savedInstanceState.getString("photoPath");
            paths = (ArrayList<String>)savedInstanceState.getSerializable("paths");
            isSupportVote = savedInstanceState.getBoolean("isSupportVote");
            content.setText(savedInstanceState.getString("content"));
            if (isSupportVote)
            {
                isVote.setBackgroundResource(R.drawable.community_switch_on);
            }
            else
            {
                isVote.setBackgroundResource(R.drawable.community_switch_off);
            }
        }
//        title.setText("发话题");
        title.setBackgroundResource(R.drawable.title_fahuati);
        postTopic.setBackgroundResource(R.drawable.title_red_wancheng);
//        postTopic.setText("发表");
//        postTopic.setTextSize(16);
//        postTopic.setTextColor(getResources().getColorStateList(R.color.bb474d));
        circleId = getIntent().getStringExtra("circleId");
        imageCache = new HashMap<String, SoftReference<Bitmap>>();
        content.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
            
            @Override
            public void afterTextChanged(Editable s)
            {
                textChangeLength.setText(content.getText().length() + "/200");
            }
            
        });
        
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)defaultPhoto.getLayoutParams(); //取控件textView当前的布局参数  
        linearParams.width =
            (CommunityPostTopicActivity.this.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(CommunityPostTopicActivity.this,
                110)) / 3;
        linearParams.height = linearParams.width;
        defaultPhoto.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isSupportVote", isSupportVote);
        outState.putString("photoPath", photoPath);
        outState.putSerializable("paths", paths);
        outState.putSerializable("content", content.getText().toString().trim());
    }
    
    /**
     * 
     * <获取头像>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showPhotoDiaLog()
    {
        photoPath = System.currentTimeMillis() + ".jpg";
        final Dialog imageSelectDialog = new Dialog(CommunityPostTopicActivity.this, R.style.image_select_dialog);
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
                Intent photoI = new Intent(CommunityPostTopicActivity.this, ChoiseMorePhotosListActivity.class);
                photoI.putExtra("class", CommunityPostTopicActivity.class.getName());
                photoI.putExtra("haveCount", paths.size());
                startActivityForResult(photoI, Constants.NUM2);
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
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    try
                    {
                        photoPath = FileSystemManager.getTemporaryPath(CommunityPostTopicActivity.this) + photoPath;
                        File picture = new File(photoPath);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        Uri imageFileUri = Uri.fromFile(picture);
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                        startActivityForResult(intent, Constants.NUM1);
                    }
                    catch (ActivityNotFoundException e)
                    {
                        ToastUtil.makeText(CommunityPostTopicActivity.this, "没有找到储存目录");
                    }
                }
                else
                {
                    ToastUtil.makeText(CommunityPostTopicActivity.this, "没有储存卡");
                }
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        String zoomPath =
            FileSystemManager.getPostPath(CommunityPostTopicActivity.this) + System.currentTimeMillis() + ".jpg";
        //相机
        if (resultCode == RESULT_OK && requestCode == Constants.NUM1)
        {
            {
                BitmapUtil.getImageScaleByPath(new PicturePropertiesBean(photoPath, zoomPath, IMG_SCALE, IMG_SCALE),
                    CommunityPostTopicActivity.this);
                paths.add(zoomPath);
                refreshImageView();
            }
        }
        // 直接从相册获取
        if (resultCode == 112 && data != null && requestCode == Constants.NUM2)
        {
            ArrayList<ImgPicker> list = data.getParcelableArrayListExtra("imgUrlList");
            if (list != null && list.size() > 0)
            {
                for (ImgPicker path : list)
                    paths.add(path.thumb);
                refreshImageView();
            }
        }
    }
    
    /** 
     * 
     * <刷新界面>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void refreshImageView()
    {
        LinearLayout selectPhotoOne = null;
        LinearLayout selectPhotoTwo = null;
        if (paths.size() > 0)
        {
            recycleMemory();
            selectPhoto.setVisibility(View.GONE);
            selectPhotoLayout.setVisibility(View.VISIBLE);
            selectPhotoLine.removeAllViews();
            imgNum.setText((paths.size()) + "/6");
            
            for (int i = 0; i < paths.size(); i++)
            {
                final int currentItem = i;
                FrameLayout picItem =
                    (FrameLayout)LayoutInflater.from(CommunityPostTopicActivity.this)
                        .inflate(R.layout.community_post_image, null);
                ImageView pic = (ImageView)picItem.findViewById(R.id.circle_post_img);
                ImageView delete = (ImageView)picItem.findViewById(R.id.circle_post_img_del);
                delete.setVisibility(View.VISIBLE);
                if (i == 0)
                {
                    selectPhotoOne = new LinearLayout(CommunityPostTopicActivity.this);
                    selectPhotoOne.setPadding(DisplayUtil.dip2px(this, 15),
                        DisplayUtil.dip2px(this, 10),
                        DisplayUtil.dip2px(this, 15),
                        DisplayUtil.dip2px(this, 10));
                    selectPhotoLine.addView(selectPhotoOne);
                }
                if (i == 2)
                {
                    selectPhotoTwo = new LinearLayout(CommunityPostTopicActivity.this);
                    selectPhotoTwo.setPadding(DisplayUtil.dip2px(this, 15),
                        0,
                        DisplayUtil.dip2px(this, 15),
                        DisplayUtil.dip2px(this, 10));
                    selectPhotoLine.addView(selectPhotoTwo);
                }
                if (i < 3)
                {
                    selectPhotoOne.addView(picItem);
                }
                else
                {
                    selectPhotoTwo.addView(picItem);
                }
                if (i != 2 || i != 5)
                    resizeView(picItem);
                Bitmap nowBitmap =
                    zoomPhoto(BitmapFactory.decodeFile(paths.get(i)),
                        (this.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(this, 100)) / 3);
                if (nowBitmap != null)
                {
                    imageCache.put(paths.get(i), new SoftReference<Bitmap>(nowBitmap));
                    nowBitmap = null;
                    pic.setImageBitmap(imageCache.get(paths.get(i)).get());
                }
                else
                {
                    pic.setImageResource(R.drawable.community_default);
                }
                picItem.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        photoUrls.clear();
                        //普通话题
                        for (String path : paths)
                        {
                            photoUrls.add("file://" + path);
                        }
                        Intent intent = new Intent(CommunityPostTopicActivity.this, ViewPagerActivity.class);
                        intent.putExtra("currentItem", currentItem);
                        intent.putStringArrayListExtra("photoUrls", photoUrls);
                        CommunityPostTopicActivity.this.startActivity(intent);
                    }
                });
                delete.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        paths.remove(currentItem);
                        refreshImageView();
                    }
                });
            }
            if (paths.size() < 3)
            {
                FrameLayout picItem =
                    (FrameLayout)LayoutInflater.from(CommunityPostTopicActivity.this)
                        .inflate(R.layout.community_post_image, null);
                selectPhotoOne.addView(picItem);
                resizeView(picItem);
                picItem.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        showPhotoDiaLog();
                    }
                });
            }
            else if (paths.size() < 6)
            {
                FrameLayout picItem =
                    (FrameLayout)LayoutInflater.from(CommunityPostTopicActivity.this)
                        .inflate(R.layout.community_post_image, null);
                selectPhotoTwo.addView(picItem);
                resizeView(picItem);
                picItem.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        showPhotoDiaLog();
                    }
                });
            }
        }
        else
        {
            selectPhoto.setVisibility(View.VISIBLE);
            selectPhotoLayout.setVisibility(View.GONE);
        }
    }
    
    /**
     * 
     * <动态计算选择图片框的高和宽>
     * <功能详细描述>
     * @param view
     * @see [类、类#方法、类#成员]
     */
    private void resizeView(View view)
    {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)view.getLayoutParams(); //取控件textView当前的布局参数  
        linearParams.width = (this.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(this, 100)) / 3;
        linearParams.height = linearParams.width;
        linearParams.setMargins(0, 0, DisplayUtil.dip2px(this, 10), 0);
        view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }
    
    /**
     * 
     * <发表话题>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void postTopic(String content)
    {
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        Map<String, List<File>> fileParameters = new HashMap<String, List<File>>();
        List<File> files = new ArrayList<File>();
        for (String path : paths)
        {
            files.add(new File(path));
        }
        fileParameters.put("file", files);
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(circleId));
            param.put("content", SecurityUtils.encode2Str(content));
            if (paths.size() > 0)
                param.put("flag", SecurityUtils.encode2Str("1"));
            else
                param.put("flag", SecurityUtils.encode2Str("0"));
            param.put("phoneType", SecurityUtils.encode2Str(Constants.clientType));
            param.put("model", SecurityUtils.encode2Str(android.os.Build.MODEL));
            if (isSupportVote)
                param.put("type", SecurityUtils.encode2Str(Constants.VOTE));
            else
                param.put("type", SecurityUtils.encode2Str(Constants.NORMAL));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceUploadFile(CommunityPostTopicActivity.this,
            param,
            fileParameters,
            CommunityPostTopicActivity.this,
            BaseResponse.class,
            URLUtil.COMMUNITY_POST_TOPIC,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        /**
         * 响应返回按钮
         */
            case R.id.title_back_layout:
                finish();
                break;
            /**
             * 响应发帖按钮
             */
            case R.id.title_call_layout:
                if (GeneralUtils.isNullOrZeroLenght(content.getText().toString().trim()))
                {
                    ToastUtil.makeText(this, "说点什么吧！");
                }
                else
                {
                    postTopic(content.getText().toString().trim());
                }
                break;
            /**
             * 响应开启投票按钮
             */
            case R.id.vote:
                if (isSupportVote)
                {
                    isSupportVote = false;
                    isVote.setBackgroundResource(R.drawable.community_switch_off);
                }
                else
                {
                    isSupportVote = true;
                    isVote.setBackgroundResource(R.drawable.community_switch_on);
                }
                break;
            /**
             * 响应选择照片按钮
             */
            case R.id.select_photo:
                showPhotoDiaLog();
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        super.netBack(ob);
        if (null != dailog)
        {
            dailog.dismissDialog();
        }
        
        if (ob instanceof BaseResponse)
        {
            BaseResponse res = (BaseResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(res.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(res.getRetcode()))
                {
                    ToastUtil.makeText(this, "发表成功！");
                    setResult(Constants.NUM2);
                    finish();
                }
                else
                {
                    ToastUtil.makeText(this, res.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(this);
            }
        }
    }
    
    @Override
    protected void onDestroy()
    {
        recycleMemory();
        FileUtil.deleteDirectory(FileSystemManager.getPostPath(this));
        super.onDestroy();
    }
    
    /**
     * 
     * <释放缓存>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void recycleMemory()
    {
        Iterator iter = imageCache.keySet().iterator();
        while (iter.hasNext())
        {
            Object key = iter.next();
            SoftReference<Bitmap> softReference = (SoftReference<Bitmap>)imageCache.get(key);
            Bitmap bitmap = softReference.get();
            if (bitmap != null && !bitmap.isRecycled())
            {
                bitmap.recycle();
            }
        }
        imageCache.clear();
    }
    
    /**
     * 
     * <生成缩略图>
     * <功能详细描述>
     * @param nowPhoto
     * @param zoomToPitmap
     * @return
     * @see [类、类#方法、类#成员]
     */
    private Bitmap zoomPhoto(Bitmap nowPhoto, int width)
    {
        Bitmap newbmp = ThumbnailUtils.extractThumbnail(nowPhoto, width, width, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return newbmp;
    }
    
}
