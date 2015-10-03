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
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.CreateCommunityResponse;
import com.ymdq.thy.bean.community.EditCommunityResponse;
import com.ymdq.thy.bean.community.GroupDetailInfoResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.FileSystemManager;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <创建社区页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityCreateOrEditActivity extends BaseActivity implements OnClickListener
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
     * 确认
     */
    private TextView confirm;
    
    /**
     * 确认布局
     */
    private LinearLayout confirmLayout;
    
    /**
     * 头像布局
     */
    private LinearLayout imageLayout;
    
    /**
     * 头像
     */
    private ImageView image;
    
    /**
     * 社区名称
     */
    private EditText name;
    
    /**
     * 社区公告
     */
    private EditText desc;
    
    /**
     * 拍摄图片的url地址
     */
    private String photoPath;
    
    /**
     * 头像
     */
    private SoftReference<Bitmap> photoReference;
    
    /**
     * 是否选择头像
     */
    private boolean isSelectHead = false;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 创建圈子or编辑圈子
     * 
     **/
    private String type;
    
    /**
     * 圈子基本信息
     */
    private GroupDetailInfoResponse groupDetailInfoResponse;
    
    /**
     * 是否从相机拍照
     */
    private boolean isFromCamera;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_create_or_edit);
        
        init();
        initData();
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
        confirm = (TextView)findViewById(R.id.title_btn_call);
        confirmLayout = (LinearLayout)findViewById(R.id.title_call_layout);
        imageLayout = (LinearLayout)findViewById(R.id.image_layout);
        image = (ImageView)findViewById(R.id.head_img);
        name = (EditText)findViewById(R.id.name);
        desc = (EditText)findViewById(R.id.community_desc);
        
        /**
         * 添加按钮点击事件
         */
        back.setOnClickListener(this);
        confirmLayout.setOnClickListener(this);
        imageLayout.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        type = getIntent().getStringExtra("type");
        if (Constants.CREATE_COMMUNITY.equals(type))
        {
            title.setText("创建圈子");
        }
        else
        {
            title.setText("编辑圈子");
            groupDetailInfoResponse =
                (GroupDetailInfoResponse)getIntent().getSerializableExtra("groupDetailInfoResponse");
            name.setText(groupDetailInfoResponse.getName());
            desc.setText(groupDetailInfoResponse.getDesc());
            name.setSelection(name.getText().toString().length());
            ImageLoader.getInstance().displayImage(groupDetailInfoResponse.getIcon(),
                image,
                JRApplication.setAllDisplayImageOptions(CommunityCreateOrEditActivity.this,
                    "community_default",
                    "community_default",
                    "community_default"));
        }
        confirm.setText("确认");
        confirm.setTextColor(getResources().getColorStateList(R.color.selector_color_community_post));
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
             * 响应确认按钮
             */
            case R.id.title_call_layout:
                String communityName = name.getText().toString().trim();
                String communityDesc = desc.getText().toString().trim();
                if (Constants.CREATE_COMMUNITY.equals(type))
                {
                    if (!isSelectHead)
                    {
                        ToastUtil.makeText(this, "请选择圈子头像");
                    }
                    else if (GeneralUtils.isNullOrZeroLenght(communityName))
                    {
                        ToastUtil.makeText(this, "圈子名称不能为空,请填写圈子名称");
                    }
                    else
                    {
                        createCommunity(communityName, communityDesc);
                    }
                }
                else
                {
                    if (GeneralUtils.isNullOrZeroLenght(communityName))
                    {
                        ToastUtil.makeText(this, "圈子名称不能为空,请填写圈子名称");
                    }
                    else
                    {
                        editCommunity(communityName, communityDesc);
                    }
                }
                
                break;
            /**
             * 响应社区图标时间
             */
            case R.id.image_layout:
                showPhotoDiaLog();
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        /**
         * 调用系统拍照,并且成功.
         */
        if (requestCode == Constants.NUM1 && resultCode == -1)
        {
            File picture = new File(FileSystemManager.getTemporaryPath(CommunityCreateOrEditActivity.this) + photoPath);
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
        if (requestCode == Constants.NUM3)
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
                    saveMyBitmap(FileSystemManager.getCommunityIconPath(CommunityCreateOrEditActivity.this) + photoPath,
                        photoReference.get());
                    image.setImageBitmap(photoReference.get());
                    isSelectHead = true;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    new File(FileSystemManager.getTemporaryPath(CommunityCreateOrEditActivity.this) + photoPath);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageFileUri = Uri.fromFile(picture);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(intent, Constants.NUM1);
            }
            catch (ActivityNotFoundException e)
            {
                ToastUtil.makeText(CommunityCreateOrEditActivity.this, "没有找到储存目录");
            }
        }
        else
        {
            ToastUtil.makeText(CommunityCreateOrEditActivity.this, "没有储存卡");
        }
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
        startActivityForResult(intent, Constants.NUM3);
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
     * 
     * <获取头像>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void showPhotoDiaLog()
    {
        photoPath = System.currentTimeMillis() + ".jpg";
        final Dialog imageSelectDialog = new Dialog(CommunityCreateOrEditActivity.this, R.style.image_select_dialog);
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
     * <创建社区>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void createCommunity(String name, String desc)
    {
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        Map<String, List<File>> fileParameters = new HashMap<String, List<File>>();
        List<File> files = new ArrayList<File>();
        files.add(new File(FileSystemManager.getCommunityIconPath(CommunityCreateOrEditActivity.this) + photoPath));
        fileParameters.put("file", files);
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("name", SecurityUtils.encode2Str(name));
            if (GeneralUtils.isNotNullOrZeroLenght(desc))
                param.put("desc", SecurityUtils.encode2Str(desc));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceUploadFile(this,
            param,
            fileParameters,
            this,
            CreateCommunityResponse.class,
            URLUtil.COMMUNITY_CREATE,
            Constants.ENCRYPT_SIMPLE);
    }
    
    /**
     * 
     * <编辑社区>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void editCommunity(String name, String desc)
    {
        dailog = new NetLoadingDailog(this);
        dailog.loading();
        Map<String, List<File>> fileParameters = new HashMap<String, List<File>>();
        Map<String, String> param = new HashMap<String, String>();
        //选择头像
        try
        {
            if (isSelectHead)
            {
                List<File> files = new ArrayList<File>();
                files.add(new File(FileSystemManager.getCommunityIconPath(CommunityCreateOrEditActivity.this)
                    + photoPath));
                fileParameters.put("logo", files);
                param.put("flag", SecurityUtils.encode2Str("1"));
            }
            else
            {
                param.put("flag", SecurityUtils.encode2Str("0"));
            }
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(groupDetailInfoResponse.getId()));
            param.put("name", SecurityUtils.encode2Str(name));
            if (GeneralUtils.isNotNullOrZeroLenght(desc))
                param.put("desc", SecurityUtils.encode2Str(desc));
        }
        catch (Exception e)
        {
        }
        ConnectService.instance().connectServiceUploadFile(this,
            param,
            fileParameters,
            this,
            EditCommunityResponse.class,
            URLUtil.COMMUNITY_EDIT,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        super.netBack(ob);
        if (null != dailog)
        {
            dailog.dismissDialog();
        }
        /**
         * 创建社区
         */
        if (ob instanceof CreateCommunityResponse)
        {
            CreateCommunityResponse res = (CreateCommunityResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(res.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(res.getRetcode()))
                {
                    ToastUtil.makeText(this, "圈子创建成功！");
                    setResult(Constants.NUM1);
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
        /**
         * 编辑社区
         */
        else if (ob instanceof EditCommunityResponse)
        {
            EditCommunityResponse res = (EditCommunityResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(res.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(res.getRetcode()))
                {
                    ToastUtil.makeText(this, "圈子编辑成功！");
                    setResult(Constants.NUM1);
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
}
