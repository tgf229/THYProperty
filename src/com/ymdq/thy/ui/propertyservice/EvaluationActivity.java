package com.ymdq.thy.ui.propertyservice;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.ImgPicker;
import com.ymdq.thy.bean.PicturePropertiesBean;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.ChoiseMorePhotosListActivity;
import com.ymdq.thy.ui.ViewPagerActivity;
import com.ymdq.thy.ui.propertyservice.adapter.CirclesPostAdapter;
import com.ymdq.thy.util.BitmapUtil;
import com.ymdq.thy.util.FileSystemManager;
import com.ymdq.thy.util.FileUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.MyGridView;

public class EvaluationActivity extends BaseActivity implements OnClickListener,OnItemClickListener,UICallBack
{
    private String id;
    
    private String level;
    
    private MyGridView repairPicGv;
    
    private TextView repairTxtNum;
    
    private EditText editText;
    
    private TextView textNum;
    
    /**
     * 选择图片上限
     */
    protected static final int PICK_MAX = 6;
    
    /**
     * 空数据，用于“添加图片”占位
     */
    public static final ImgPicker BLANK = new ImgPicker("", "", 0);
    
    private CirclesPostAdapter adapter;
    
    /**
     * 缩放尺寸
     */
    protected static final float IMG_SCALE = 640f;
    
    private ArrayList<ImgPicker> imgList;
    
    private String capPath;
    
    private RadioGroup radioGroup;
    
    private NetLoadingDailog commitLoading;
    
    private LinearLayout titleBarRight;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_evaluation);
        id = getIntent().getStringExtra("id");
        if(savedInstanceState != null)
        {
            capPath = savedInstanceState.getString("path");
            imgList = savedInstanceState.getParcelableArrayList("imgList");
        }
        initView();
        initGridView();
        initData();
        commitLoading = new NetLoadingDailog(this);
    }
    
    private void initView()
    {
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setText("评价");
        titleBarRight = (LinearLayout)titleBar.findViewById(R.id.title_call_layout);
        titleBarRight.setOnClickListener(this);
        TextView titleBarTextV = (TextView)titleBar.findViewById(R.id.title_btn_call);
        titleBarTextV.setText("提交");
        titleBarTextV.setTextColor(getResources().getColorStateList(R.color.selector_color_community_post));
        titleBarTextV.setVisibility(View.VISIBLE);
        
        editText = (EditText)findViewById(R.id.exchanged_shopping_order_reason_et);
        textNum = (TextView)findViewById(R.id.textChangeLength);
    }
    
    private void initGridView()
    {
        //上传图片
        repairPicGv = (MyGridView)findViewById(R.id.repair_grid_view);
        repairTxtNum = (TextView)findViewById(R.id.img_txt_num);
        
        // 空数据提示
        View empty = LayoutInflater.from(this).inflate(R.layout.home_circles_post_pick, null);
        empty.setVisibility(View.GONE);
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        ((ViewGroup)repairPicGv.getParent()).addView(empty, 0, params);
        repairPicGv.setEmptyView(empty);
        empty.setOnClickListener(this);
        
        if(imgList == null)
        {
            imgList = new ArrayList<ImgPicker>(PICK_MAX);
        }
        adapter = new CirclesPostAdapter(this, imgList,this);
        repairPicGv.setAdapter(adapter);
        repairPicGv.setOnItemClickListener(this);
        
        //
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
    }
    
    private void initData()
    {
        editText.addTextChangedListener(new TextWatcher(){
            
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
                textNum.setText(editText.getText().length()+"/200");
            }
            
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
                //提交
            case R.id.title_call_layout:
                int id = radioGroup.getCheckedRadioButtonId();
                if(id == R.id.satisfied_1)
                {
                    //满意
                    level = Constants.PROPERTY_PRAISE;
                    reqEvaluate();
                }
                else if(id == R.id.commonly_2)
                {
                    //一般
                    level = Constants.PROPERTY_COMPLAINT;
                    reqEvaluate();
                }
                else if(id == R.id.unsatisfy_3)
                {
                    //不满意
                    level = Constants.PROPERTY_REPAIR;
                    if(GeneralUtils.isNotNullOrZeroLenght(editText.getText().toString().trim()))
                    {
                        reqEvaluate();
                    }
                    else
                    {
                        Toast.makeText(EvaluationActivity.this, "请填写不满意的原因，方便我们重新处理", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(EvaluationActivity.this, "请选择满意度", Toast.LENGTH_SHORT).show();
                }
                
                break;
            case R.id.circle_post_pick_lyt:
                //添加图片
                addImgDiaLog(createPopDiaLog(),0);
                break;
            case R.id.repair_grid_view:
                
                break;
            case R.id.circle_post_img_del:
                //删除
                int index = (Integer)v.getTag();
                adapter.setNotifyPosition(index-1);
                imgList.remove(index);
                syncListThumb();
            default:
                break;
        }
    }
    
    private void reqEvaluate()
    {
        commitLoading.loading();
        titleBarRight.setEnabled(false);
        ArrayList<File> fileList = new ArrayList<File>();
        
        if (imgList.size() > 0)
        {
            File file;
            for (ImgPicker img : imgList)
            {
                if (img.thumb.equals(BLANK.path))
                {
                    continue;
                }
                file = new File(img.thumb);
                if (file.exists() && file.isFile())
                {
                    fileList.add(file);
                }
            }
        }
        
        Map<String,String> param = new HashMap<String,String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("id", SecurityUtils.encode2Str(id));
            param.put("level", SecurityUtils.encode2Str(level));
            param.put("content", SecurityUtils.encode2Str(editText.getText().toString().trim()));
            
            Map<String, List<File>> fileMap = new HashMap<String, List<File>>(1);
            if (fileList.size() > 0)
            {
                param.put("flag", SecurityUtils.encode2Str(Constants.HAVA_IMAGE_FLAG));
                fileMap.put("file", fileList);
                ConnectService.instance().connectServiceUploadFile(this,
                    param,
                    fileMap,
                    this,
                    BaseResponse.class,
                    URLUtil.MY_TICKET_EVALUATE,
                    Constants.ENCRYPT_SIMPLE);
            }
            else
            {
                param.put("flag", SecurityUtils.encode2Str(Constants.NO_IMAGE_FLAG));
                ConnectService.instance().connectServiceReturnResponse(this,
                    param,
                    this,
                    BaseResponse.class,
                    URLUtil.MY_TICKET_EVALUATE,
                    Constants.ENCRYPT_SIMPLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        
        if (position == imgList.size() - 1 && imgList.get(position) == BLANK)
        {
            addImgDiaLog(createPopDiaLog(),imgList.size()-1);
        }
        else
        {
            //点击浏览大图
            Intent pickIntent = new Intent(this, ViewPagerActivity.class);
            //            imgList.remove(BLANK);
            ArrayList<String> urlList = new ArrayList<String>(imgList.size());
            String s;
            for (ImgPicker it : imgList)
            {
                if (it == BLANK)
                    continue;
                s = "file://" + it.path;
                urlList.add(s);
            }
            pickIntent.putStringArrayListExtra("photoUrls", urlList);
            pickIntent.putExtra("currentItem", position);
            startActivity(pickIntent);
        }
    }
    
    private Dialog createPopDiaLog()
    {
        hideSoftInput();
        Dialog popDialog = new Dialog(this, R.style.image_select_dialog);
        popDialog.setContentView(R.layout.image_select_dialog);
        popDialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        popDialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
        popDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        popDialog.getWindow().setWindowAnimations(R.style.dialog_style);
        
        return popDialog;
    }
    
    private void addImgDiaLog(final Dialog popDialog,final int count)
    {
        Button camera = (Button)popDialog.findViewById(R.id.camera);
        Button gallery = (Button)popDialog.findViewById(R.id.gallery);
        Button cancel = (Button)popDialog.findViewById(R.id.cancel);
        
        // 拍摄照片
        camera.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                popDialog.dismiss();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    
                    capPath = FileSystemManager.getPostPath(EvaluationActivity.this);
                    capPath += System.currentTimeMillis() + ".jpg";
                    
                    Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePic.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capPath)));
                    startActivityForResult(takePic, 210);
                }
                else
                {
                    ToastUtil.makeText(EvaluationActivity.this, "请插入SD卡");
                }
            }
        });
        
        // 从相册选取照片
        gallery.setOnClickListener(new OnClickListener()
        {
            //            
            //            @Override
            public void onClick(View v)
            {
                popDialog.dismiss();
                
                Intent photoI = new Intent(EvaluationActivity.this,ChoiseMorePhotosListActivity.class);
                photoI.putExtra("class", EvaluationActivity.class.getName());
                photoI.putExtra("haveCount", count);
                startActivityForResult(photoI, 112);
            }
        });
        
        cancel.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View arg0)
            {
                popDialog.dismiss();
            }
        });
        
        popDialog.show();
    }
    
    /**
     * 隐藏软键盘
     * 
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void hideSoftInput()
    {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null)
        {
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constants.CHOISE_MORE_PHOTO_112 && data != null)
        {
            ArrayList<ImgPicker> list = data.getParcelableArrayListExtra("imgUrlList");
            if(list != null && list.size() > 0)
            {
                addnewPho();
                imgList.addAll(list);
                syncListThumb();
            }
        }
        else if(requestCode == 210 && resultCode == Activity.RESULT_OK)
        {
            //压缩原图得到缩略图
            String toPath = FileSystemManager.getPostPath(this);
            toPath += Constants.USER_INFO + "_" + System.currentTimeMillis() + ".jpg";
            toPath =
                BitmapUtil.getImageScaleByPath(new PicturePropertiesBean(capPath, toPath, IMG_SCALE, IMG_SCALE),this);
            
            ImgPicker img = new ImgPicker(toPath, capPath, 1);
//            addnewPho();
            adapter.setNotifyPosition(-1);
            imgList.add(img);
            syncListThumb();
        }
    }
    
    /**
     * 
     * <添加新的图片是记住当前大小>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void addnewPho()
    {
        if(imgList.contains(BLANK))
        {
            adapter.setNotifyPosition(imgList.size() - 2);
        }
        else
        {
            adapter.setNotifyPosition(imgList.size() - 1);
        }
    }
    
    /**
     * 封装已选择图片数据
     */
    private void syncListThumb()
    {
        int size = imgList.size();
        
        if(imgList.contains(BLANK))
        {
            if(size > PICK_MAX)
            {
                imgList.remove(BLANK);
                repairTxtNum.setText(imgList.size() + "/6");
                repairTxtNum.setVisibility(View.VISIBLE);
            }
            else if(size == 1)
            {
                imgList.remove(BLANK);
                repairTxtNum.setVisibility(View.GONE);
            }
            else
            {
                imgList.remove(BLANK);
                repairTxtNum.setText(imgList.size() + "/6");
                repairTxtNum.setVisibility(View.VISIBLE);
                imgList.add(BLANK);
            }
        }
        else
        {
            if(size >= PICK_MAX)
            {
                imgList.remove(BLANK);
                repairTxtNum.setText(imgList.size() + "/6");
                repairTxtNum.setVisibility(View.VISIBLE);  
            }
            else if(size > 0 && size < 6)
            {
                imgList.remove(BLANK);
                repairTxtNum.setText(imgList.size() + "/6");
                repairTxtNum.setVisibility(View.VISIBLE);
                imgList.add(BLANK);
            }
            else
            {
                imgList.remove(BLANK);
                repairTxtNum.setVisibility(View.GONE);
            }
        }
        adapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("path", capPath);
        outState.putParcelableArrayList("imgList", imgList);
    }
    
    @Override
    public void netBack(Object ob)
    {
        commitLoading.dismissDialog();
        if(ob != null)
        {
            if(ob instanceof BaseResponse)
            {
                BaseResponse resp = (BaseResponse)ob;
                titleBarRight.setEnabled(true);
                if(GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if(Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        Toast.makeText(EvaluationActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                    else
                    {
                        ToastUtil.makeText(EvaluationActivity.this, resp.getRetinfo());
                    }
                }
                else
                {
                    ToastUtil.showError(EvaluationActivity.this);
                }
            }
        }
    }
    
    @Override
    protected void onDestroy()
    {
        // 清理图片 
        FileUtil.deleteDirectory(FileSystemManager.getPostPath(EvaluationActivity.this));
        adapter.destoryBitmap();
        super.onDestroy();
    }
    
}
