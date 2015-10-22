/*
 * 文 件 名:  PersonInfoActivity.java
 * 版    权:  Linkage Technology Co., Ltd. Copyright 2010-2011,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  user
 * 创建时间:  2014-11-25
 
 */
package com.ymdq.thy.ui.personcenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.UpdatePersonInfoResponse;
import com.ymdq.thy.bean.personcenter.UploadHeadPhotoResponse;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.callback.DialogCancelCallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.BitmapUtil;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.FileSystemManager;
import com.ymdq.thy.util.FileUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;

/**
 * <个人中心>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PersonInfoActivity extends BaseActivity implements OnClickListener
{
    /**
     * 头部
     */
    private LinearLayout llBack, llFinish;
    
    private TextView tvTitle;
    
    private TextView tvFinish;
    
    /**
     * 头像,删除昵称,删除姓名
     */
    private ImageView ivHeadPic, ivNickNameDel, ivNameDel;
    
    /**
     * 更换头像,修改生日,修改性别
     */
    private LinearLayout llHeadPic, llBirth, llSex;
    
    /**
     * 修改昵称,修改姓名
     */
    private EditText etNickName, etName;
    
    /**
     * 显示生日,显示性别
     */
    private TextView tvBirth, tvSex;
    
    /**
     * 用户名,头像地址,昵称,姓名,性别,生日
     */
    private String image, nickname, name, sex, birth;
    
    /**
     * 头像
     */
    private SoftReference<Bitmap> photoReference;
    
    private String[] sexChoise = new String[] {"保密", "男", "女"};
    
    /**
     * 性别索引
     */
    private int bid = 0;
    
    /**
     * 拍摄图片的url地址
     */
    private String photoPath;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 头像地址标记
     */
    private static final String HEAD = "head";
    
    /**
     * 修改后个人信息
     */
    private String uNickName, uName, uBirth, uSex;
    
    private boolean isFromCamera;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info);
        initTitle();
        init();
        initData();
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        llFinish = (LinearLayout)findViewById(R.id.title_call_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
//        tvTitle.setText("个人信息");
        tvTitle.setBackgroundResource(R.drawable.title_gerenxinxi);
        tvFinish = (TextView)findViewById(R.id.title_btn_call);
        tvFinish.setBackgroundResource(R.drawable.title_red_wancheng);
//        tvFinish.setText("完成");
//        tvFinish.setTextColor(getResources().getColorStateList(R.color.selector_color_person_info_edit));
//        tvFinish.setTextSize(15);
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (changedHeadInfo() || changedInfo())
                {
                    uNickName = etNickName.getText().toString().trim();
                    if (GeneralUtils.isNullOrZeroLenght(uNickName))
                    {
                        ToastUtil.makeText(PersonInfoActivity.this, "昵称不能为空");
                        etNickName.setText(nickname);
                        return;
                    }
                    
                    DialogUtil.showTwoButtonDialogCancel(PersonInfoActivity.this,
                        "您修改了个人信息,需要保存么?",
                        new DialogCallBack()
                        {
                            
                            @Override
                            public void dialogBack()
                            {
                                sumbitData();
                            }
                        },
                        new DialogCancelCallBack()
                        {
                            
                            @Override
                            public void dialogCancelBack()
                            {
                                PersonInfoActivity.this.finish();
                            }
                            
                        });
                }
                else
                {
                    PersonInfoActivity.this.finish();
                }
            }
        });
        
        llFinish.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                sumbitData();
            }
        });
    }
    
    private void init()
    {
        ivHeadPic = (ImageView)findViewById(R.id.person_info_head_pic_iv);
        ivNickNameDel = (ImageView)findViewById(R.id.person_info_nickname_delete_iv);
        ivNameDel = (ImageView)findViewById(R.id.person_info_name_delete_iv);
        
        llHeadPic = (LinearLayout)findViewById(R.id.person_info_head_pic_ll);
        llBirth = (LinearLayout)findViewById(R.id.person_info_birth_ll);
        llSex = (LinearLayout)findViewById(R.id.person_info_sex_ll);
        
        etNickName = (EditText)findViewById(R.id.person_info_nickname_et);
        etName = (EditText)findViewById(R.id.person_info_name_et);
        
        tvBirth = (TextView)findViewById(R.id.person_info_birth_tv);
        tvSex = (TextView)findViewById(R.id.person_info_sex_tv);
        
        llHeadPic.setOnClickListener(this);
        llBirth.setOnClickListener(this);
        llSex.setOnClickListener(this);
        ivNickNameDel.setOnClickListener(this);
        ivNameDel.setOnClickListener(this);
        
        dailog = new NetLoadingDailog(this);
    }
    
    private void initData()
    {
        image = Global.getImage();
        nickname = Global.getNickName();
        name = Global.getName();
        sex = Global.getSex();
        birth = Global.getBirth();
        
        if (GeneralUtils.isNotNullOrZeroLenght(birth))
        {
            birth = GeneralUtils.splitToLocalDateOne(birth);
        }
        etNickName.setText(nickname);
        etName.setText(name);
        tvBirth.setText(birth);
        
        if ("0".equals(sex) || "".equals(sex))
        {
            tvSex.setText("保密");
            bid = 0;
            sex = "0";
        }
        if ("1".equals(sex))
        {
            tvSex.setText("男");
            bid = 1;
        }
        if ("2".equals(sex))
        {
            tvSex.setText("女");
            bid = 2;
        }
        
        //初始化头像
        if (GeneralUtils.isNotNullOrZeroLenght(image))
        {
            
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    final Bitmap bitmap =
                        BitmapUtil.getBitmap(image, JRApplication.jrApplication, Global.getUserId(), "head");
                    
                    PersonInfoActivity.this.runOnUiThread(new Runnable()
                    {
                        
                        @Override
                        public void run()
                        {
                            photoReference = new SoftReference<Bitmap>(bitmap);
                            if (photoReference.get() != null)
                            {
                                ivHeadPic.setImageBitmap(photoReference.get());
                            }
                        }
                    });
                    
                }
            }).start();
        }
    }
    
    private void sumbitData()
    {
        uNickName = etNickName.getText().toString().trim();
        if (GeneralUtils.isNullOrZeroLenght(uNickName))
        {
            ToastUtil.makeText(this, "昵称不能为空");
            return;
        }
        
        if (changedInfo())
        {
            dailog.loading();
            Map<String, String> param = new HashMap<String, String>();
            try
            {
                param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
                param.put("nickName", SecurityUtils.encode2Str(uNickName));
                param.put("name", SecurityUtils.encode2Str(uName));
                param.put("sex", SecurityUtils.encode2Str(bid + ""));
                param.put("flag", SecurityUtils.encode2Str("0"));
                param.put("birth", SecurityUtils.encode2Str(GeneralUtils.splitToMinuteNoLine(uBirth)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            
            ConnectService.instance().connectServiceReturnResponse(PersonInfoActivity.this,
                param,
                PersonInfoActivity.this,
                UpdatePersonInfoResponse.class,
                URLUtil.USER_UPDATA_INFO,
                Constants.ENCRYPT_SIMPLE);
        }
        else if (changedHeadInfo())
        {
            uploadHeadPhoto(true);
        }
        else
        {
            PersonInfoActivity.this.finish();
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (ob instanceof UploadHeadPhotoResponse)
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
                    deleteDirectoryHead("");
                    deleteDirectoryHead(HEAD);
                    saveMyBitmap(FileSystemManager.getUserHeadPath(this, Global.getUserId()) + photoPath,
                        photoReference.get());
                    setResult(Constants.User_info_pic);
                    Global.saveImage(FileSystemManager.getUserHeadPath(this, Global.getUserId()) + photoPath);
                    ToastUtil.makeText(PersonInfoActivity.this, "个人信息修改成功");
                    PersonInfoActivity.this.finish();
                }
                else
                {
                    ToastUtil.makeText(PersonInfoActivity.this, uploadHeadPhotoResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(PersonInfoActivity.this);
            }
        }
        
        if (ob instanceof UpdatePersonInfoResponse)
        {
            UpdatePersonInfoResponse updatePersonInfoResponse = (UpdatePersonInfoResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(updatePersonInfoResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(updatePersonInfoResponse.getRetcode()))
                {
                    nickname = uNickName;
                    name = uName;
                    sex = bid + "";
                    birth = uBirth;
                    
                    Global.saveNickName(nickname);
                    Global.saveName(name);
                    Global.saveSex(sex);
                    Global.saveBirth(GeneralUtils.splitToMinuteNoLine(uBirth));
                    
                    if (changedHeadInfo())
                    {
                        uploadHeadPhoto(false);
                    }
                    else
                    {
                        if (dailog != null)
                        {
                            dailog.dismissDialog();
                        }
                        
                        setResult(Constants.User_info_pic);
                        ToastUtil.makeText(PersonInfoActivity.this, "个人信息修改成功");
                        PersonInfoActivity.this.finish();
                    }
                }
                else
                {
                    if (dailog != null)
                    {
                        dailog.dismissDialog();
                    }
                    ToastUtil.makeText(PersonInfoActivity.this, updatePersonInfoResponse.getRetinfo());
                }
            }
            else
            {
                if (dailog != null)
                {
                    dailog.dismissDialog();
                }
                ToastUtil.showError(PersonInfoActivity.this);
            }
        }
    }
    
    /**
     * 检验个人信息是否有修改
     */
    private boolean changedInfo()
    {
        //检验个人信息是否有修改
        uNickName = etNickName.getText().toString().trim();
        uName = etName.getText().toString().trim();
        uBirth = tvBirth.getText().toString().trim();
        uSex = tvSex.getText().toString().trim();
        
        if (nickname.equals(uNickName) && name.equals(uName) && birth.equals(uBirth) && sex.equals(bid + ""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * 检验头像是否有修改
     */
    private boolean changedHeadInfo()
    {
        File file = new File(FileSystemManager.getUserHeadPathTemp(this, Global.getUserId()) + photoPath);
        
        if (file.exists())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.person_info_nickname_delete_iv://清除昵称
                etNickName.setText("");
                break;
            
            case R.id.person_info_name_delete_iv://清除姓名
                etName.setText("");
                break;
            
            case R.id.person_info_head_pic_ll://更换头像
                showPhotoDiaLog();
                break;
            
            case R.id.person_info_birth_ll://修改生日
                setDate(tvBirth);
                break;
            
            case R.id.person_info_sex_ll://性别
                displayDialog();
                break;
            
            default:
                break;
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
                openPic();
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
                imageSelectDialog.dismiss();
                openCamera();
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
    
    private void openPic()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constants.NUM2);
    }
    
    private void openCamera()
    {
        // 先验证手机是否有sdcard
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            try
            {
                File picture = new File(FileSystemManager.getTemporaryPath(PersonInfoActivity.this) + photoPath);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imageFileUri = Uri.fromFile(picture);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(intent, Constants.NUM1);
            }
            catch (ActivityNotFoundException e)
            {
                ToastUtil.makeText(PersonInfoActivity.this, "没有找到储存目录");
            }
        }
        else
        {
            ToastUtil.makeText(PersonInfoActivity.this, "没有储存卡");
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        
        if (requestCode == Constants.NUM1 && resultCode == -1)
        {
            isFromCamera = true;
            File picture = new File(FileSystemManager.getTemporaryPath(this) + photoPath);
            startPhotoZoom(Uri.fromFile(picture));
        }
        
        if (requestCode == Constants.NUM2)
        {
            isFromCamera = false;
            if (data == null)
            {
                return;
            }
            startPhotoZoom(data.getData());
        }
        
        if (requestCode == Constants.NUM3)
        {
            if (data == null)
            {
                if (isFromCamera)
                {
                    openCamera();
                }
                else
                {
                    openPic();
                }
                return;
            }
            Bundle extras = data.getExtras();
            if (extras != null && extras.getParcelable("data") != null)
            {
                photoReference = new SoftReference<Bitmap>((Bitmap)extras.getParcelable("data"));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photoReference.get().compress(Bitmap.CompressFormat.JPEG, 75, stream);
                deleteDirectoryHead("");//清除缓存头像
                saveMyBitmap(FileSystemManager.getUserHeadPathTemp(this, Global.getUserId()) + photoPath,
                    photoReference.get());
                
                ivHeadPic.setImageBitmap(photoReference.get());
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
    private void uploadHeadPhoto(boolean bool)
    {
        File file = new File(FileSystemManager.getUserHeadPathTemp(this, Global.getUserId()) + photoPath);
        ArrayList<File> files = new ArrayList<File>();
        files.add(file);
        Map<String, List<File>> fileParameters = new HashMap<String, List<File>>();
        fileParameters.put("file", files);
        
        if (bool)
        {
            dailog.loading();
        }
        
        Map<String, String> param = new HashMap<String, String>();
        param.put("uId", Global.getUserId());
        
        ConnectService.instance().connectServiceUploadFile(PersonInfoActivity.this,
            param,
            fileParameters,
            PersonInfoActivity.this,
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
     * 显示性别dialog
     */
    private void displayDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择性别");
        builder.setSingleChoiceItems(sexChoise, bid, new DialogInterface.OnClickListener()
        {
            
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                bid = which;
                switch (bid)
                {
                    case 0:
                        tvSex.setText("保密");
                        break;
                    case 1:
                        tvSex.setText("男");
                        break;
                    case 2:
                        tvSex.setText("女");
                        break;
                    
                    default:
                        break;
                }
                
                dialog.dismiss();
            }
            
        });
        builder.show();
    }
    
    /**
     * 
     * <选择生日>
     * <功能详细描述>
     * @param dateText
     * @see [类、类#方法、类#成员]
     */
    private void setDate(final TextView dateText)
    {
        int year = 0;
        int month = 0;
        int day = 0;
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(PersonInfoActivity.this, new OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                String dayStr;
                String monthStr;
                if (monthOfYear < 9)
                {
                    monthStr = "0" + (monthOfYear + 1);
                }
                else
                {
                    monthStr = (monthOfYear + 1) + "";
                }
                
                if ((dayOfMonth + "").length() == 1)
                {
                    dayStr = "0" + dayOfMonth;
                }
                else
                {
                    dayStr = dayOfMonth + "";
                }
                String brothTime = String.valueOf(year) + monthStr + dayStr;
                birth = brothTime;
                brothTime = GeneralUtils.splitToLocalDateOne(brothTime);
                dateText.setText(brothTime);
            }
            
        }, year, month - 1, day);
        datePickerDialog.show();
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
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        deleteDirectoryHead("");
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (changedHeadInfo() || changedInfo())
            {
                uNickName = etNickName.getText().toString().trim();
                if (GeneralUtils.isNullOrZeroLenght(uNickName))
                {
                    ToastUtil.makeText(PersonInfoActivity.this, "昵称不能为空");
                    etNickName.setText(nickname);
                }
                else
                {
                    DialogUtil.showTwoButtonDialogCancel(PersonInfoActivity.this,
                        "您修改了个人信息,需要保存么?",
                        new DialogCallBack()
                        {
                            
                            @Override
                            public void dialogBack()
                            {
                                sumbitData();
                            }
                        },
                        new DialogCancelCallBack()
                        {
                            
                            @Override
                            public void dialogCancelBack()
                            {
                                PersonInfoActivity.this.finish();
                            }
                            
                        });
                }
            }
            else
            {
                PersonInfoActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
