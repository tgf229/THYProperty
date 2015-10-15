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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.BaseResponse;
import com.ymdq.thy.bean.ImgPicker;
import com.ymdq.thy.bean.PicturePropertiesBean;
import com.ymdq.thy.bean.personcenter.HouseInfoDoc;
import com.ymdq.thy.bean.personcenter.HouseInfoResponse;
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
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.FileSystemManager;
import com.ymdq.thy.util.FileUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.MyGridView;

/**
 * 
 * <物业报修>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年12月13日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RepairActivity extends BaseActivity implements OnClickListener,OnItemClickListener,UICallBack
{
    private String type;
    
    private MyGridView repairPicGv;
    
    private TextView repairTxtNum;
    
    private TextView titleBarTextV;
    
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
    
    private LinearLayout choiseHouseLayout;
    
    private ImageView openImageView;
    
    /**
     * 房屋列表
     */
    private List<String> hNameList = new ArrayList<String>();
    
    /**
     * 存放用户的房屋信息，key为hId，value为具体的房屋
     */
    private Map<String,String> hIDList = new HashMap<String,String>();
    
    /**
     * 选择的房屋
     */
    private EditText houseId;
    
    private PopupWindow pop;
    
    private EditText editText;
    
    private TextView textNum;
    
    private RelativeLayout bottomLayout;
    
    private LinearLayout popWinLayout;
    
    private ListView popupWindowListView;
    
    private NetLoadingDailog commitLoading;
    
    private LinearLayout loadingLayout;
    
    private LinearLayout clickrefreshLayout;
    
    private TextView clickTextView;
    
    private ScrollView sv;
    
    private LinearLayout titleBarRight;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_repair);
        type = getIntent().getStringExtra("type");
        
        if(savedInstanceState != null)
        {
            capPath = savedInstanceState.getString("path");
            imgList = savedInstanceState.getParcelableArrayList("imgList");
        }
        initView();
        initData();
        initHomeDataAgain();
        commitLoading = new NetLoadingDailog(this);
    }
    
    private void initView()
    {
        LinearLayout titleBarBack = (LinearLayout)findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        String titleName = getIntent().getStringExtra("title_name");
        if("报修".equals(titleName))
        {
            titleBarName.setBackgroundResource(R.drawable.title_baoxiu);
        }
        else if("求助".equals(titleName))
        {
            titleBarName.setBackgroundResource(R.drawable.title_qiuzhu);
        } 
        else if("投诉".equals(titleName))
        {
            titleBarName.setBackgroundResource(R.drawable.title_tousu);
        }
        else if("建议".equals(titleName))
        {
            titleBarName.setBackgroundResource(R.drawable.title_jianyi);
        }
        else
        {
            titleBarName.setText(titleName);
        }
        titleBarRight = (LinearLayout)findViewById(R.id.title_call_layout);
        titleBarRight.setOnClickListener(this);
        titleBarTextV = (TextView)findViewById(R.id.title_btn_call);
        titleBarTextV.setBackgroundResource(R.drawable.title_red_tijiao);
//        titleBarTextV.setText("提交");
//        titleBarTextV.setTextColor(getResources().getColorStateList(R.color.bb474d));
        titleBarRight.setVisibility(View.GONE);
        
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        clickrefreshLayout = (LinearLayout)findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
//        clickrefreshLayout.setOnClickListener(this);
        
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
        
        choiseHouseLayout = (LinearLayout)findViewById(R.id.choice_house_layout);
        //        choiseHouseLayout.setOnClickListener(this);
        openImageView = (ImageView)findViewById(R.id.open);
        houseId = (EditText)findViewById(R.id.house_id);
        
        editText = (EditText)findViewById(R.id.exchanged_shopping_order_reason_et);
        textNum = (TextView)findViewById(R.id.textChangeLength);
        
        bottomLayout = (RelativeLayout)findViewById(R.id.bottom_layout);
        
        sv = (ScrollView)findViewById(R.id.scroll_view);
        popWinLayout = (LinearLayout)findViewById(R.id.popup_window_layout);
        popupWindowListView = (ListView)findViewById(R.id.popup_window);
        ArrayAdapter<String> popAdapter = new ArrayAdapter<String>(this,R.layout.property_popup_window_textview,hNameList);
        popupWindowListView.setAdapter(popAdapter);
        popupWindowListView.setFocusable(true);
        popupWindowListView.setOnTouchListener(new OnTouchListener(){
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                sv.requestDisallowInterceptTouchEvent(true);
                return false;
            }
            
        });
        
        popupWindowListView.setOnItemClickListener(new OnItemClickListener()
        {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                houseId.setText(hNameList.get(position));
                popWinLayout.setVisibility(View.GONE);
                openImageView.setImageResource(R.drawable.service_icon_arrow_down);
            }
        });
        sv.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                hideSoftInput();
                return false;
            }
        });
    }
    
    private void initData()
    {
        
        if(Constants.PROPERTY_REPAIR.equals(type))
        {
//            houseId.setHint("请选择要报修的房屋");
            editText.setHint("请在此描述您的报修信息");
        }
        else if(Constants.PROPERTY_COMPLAINT.equals(type))
        {
//            houseId.setHint("请选择要投诉的房屋");
            editText.setHint("请在此描述您的投诉信息");
            bottomLayout.setVisibility(View.GONE);
        }
        else if(Constants.PROPERTY_PRAISE.equals(type))
        {
//            houseId.setHint("请选择要表扬的房屋");
            editText.setHint("请在此描述您的表扬信息");
            bottomLayout.setVisibility(View.GONE);
        }
        else if(Constants.PROPERTY_HELP.equals(type))
        {
//            houseId.setHint("请选择要表扬的房屋");
            editText.setHint("请在此描述您需要帮助的信息");
            bottomLayout.setVisibility(View.GONE);
        }
        else if(Constants.PROPERTY_SUGGEST.equals(type))
        {
            editText.setHint("请在此描述您的建议信息");
            bottomLayout.setVisibility(View.GONE);
        }
        
        
        if(imgList == null)
        {
            imgList = new ArrayList<ImgPicker>(PICK_MAX);
        }
        adapter = new CirclesPostAdapter(this, imgList,this);
        repairPicGv.setAdapter(adapter);
        repairPicGv.setOnItemClickListener(this);
        
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
        editText.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if(popWinLayout.getVisibility() == View.VISIBLE)
                {
                    popWinLayout.setVisibility(View.GONE);
                    openImageView.setImageResource(R.drawable.service_icon_arrow_down);
                }
            }
        });
    }
    
    /**
     * 
     * <请求用户的房屋信息>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initHomeDataAgain()
    {
        //        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            HouseInfoResponse.class,
            URLUtil.USER_BINDING_HOUSE_LIST,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //返回
            case R.id.title_back_layout:
                finish();
                break;
//            case R.id.click_refresh_layout:
//                loadingLayout.setVisibility(View.VISIBLE);
//                clickrefreshLayout.setVisibility(View.GONE);
//                initHomeDataAgain();
//                break;
                //提交
            case R.id.title_call_layout:
                if(hNameList.size() <= 0)
                {
                    ToastUtil.makeText(this, "您暂未绑定任何房屋");
                    break;
                }
                if(GeneralUtils.isNullOrZeroLenght(houseId.getText().toString()))
                {
                    ToastUtil.makeText(this, "请选择房屋");
                    break;
                }
                if(GeneralUtils.isNullOrZeroLenght(editText.getText().toString().trim()))
                {
                    String tishi = editText.getHint().toString();
                    ToastUtil.makeText(this, "请填写"+tishi.substring(5));
//                    Toast.makeText(RepairActivity.this, "请填写"+tishi.substring(5), Toast.LENGTH_SHORT).show();
                    break;
                }
                titleBarRight.setEnabled(false);
                reqCommit();
                break;
            case R.id.circle_post_pick_lyt:
                if(popWinLayout.getVisibility() == View.VISIBLE)
                {
                    popWinLayout.setVisibility(View.GONE);
                    openImageView.setImageResource(R.drawable.service_icon_arrow_down);
                }
                //添加图片
                addImgDiaLog(createPopDiaLog(),0);
                break;
            case R.id.circle_post_img_del:
                //删除
                int index = (Integer)v.getTag();
                adapter.setNotifyPosition(index-1);
                imgList.remove(index);
                syncListThumb();
                break;
                //选择房屋
            case R.id.choice_house_layout:
                hideSoftInput();
                if(popWinLayout.getVisibility() == View.VISIBLE)
                {
                    popWinLayout.setVisibility(View.GONE);
                    openImageView.setImageResource(R.drawable.service_icon_arrow_down);
                }
                else
                {
                    String[]tempStr = new String[hNameList.size()];
                    for(int i=0;i<hNameList.size();i++)
                    {
                        tempStr[i] = hNameList.get(i);
                    }
                    ArrayAdapter<String> popAdapter = new ArrayAdapter<String>(this,R.layout.property_popup_window_textview,
                        tempStr);
                    popupWindowListView.setAdapter(popAdapter);
                    popupWindowListView.setFocusable(true);
                    
                    if(hNameList.size() > 2)
                    {
                        popWinLayout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            DisplayUtil.dip2px(this, 150)));
                    }
                    
                    openImageView.setImageResource(R.drawable.service_icon_arrow_up);
                    popWinLayout.setVisibility(View.VISIBLE);;
                    
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <请求服务>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqCommit()
    {
        commitLoading.loading();
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
            param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
            param.put("hId", SecurityUtils.encode2Str(hIDList.get(houseId.getText().toString())));
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("content", SecurityUtils.encode2Str(editText.getText().toString().trim()));
            param.put("type", SecurityUtils.encode2Str(type));
            
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
                    URLUtil.PROPERTY_REPAIR_COMMIT,
                    Constants.ENCRYPT_SIMPLE);
            }
            else
            {
                param.put("flag", SecurityUtils.encode2Str(Constants.NO_IMAGE_FLAG));
                ConnectService.instance().connectServiceReturnResponse(this,
                    param,
                    this,
                    BaseResponse.class,
                    URLUtil.PROPERTY_REPAIR_COMMIT,
                    Constants.ENCRYPT_SIMPLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                    capPath = FileSystemManager.getPostPath(RepairActivity.this);
                    capPath += System.currentTimeMillis() + ".jpg";
                    File picture = new File(capPath);
                    Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePic.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                    startActivityForResult(takePic, 210);
                    
                }
                else
                {
                    ToastUtil.makeText(RepairActivity.this, "请插入SD卡");
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
                
                Intent photoI = new Intent(RepairActivity.this,ChoiseMorePhotosListActivity.class);
                photoI.putExtra("class", RepairActivity.class.getName());
                photoI.putExtra("haveCount", count);
                startActivityForResult(photoI, Constants.CHOISE_MORE_PHOTO_112);
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
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        
        if (position == imgList.size() - 1 && imgList.get(position) == BLANK)
        {
            addImgDiaLog(createPopDiaLog(),imgList.size()-1);
        }
        else
        {
            //点击浏览大图
            Intent pickIntent = new Intent(this, ViewPagerActivity.class);
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
    
    @Override
    public void netBack(Object ob)
    {
        loadingLayout.setVisibility(View.GONE);
        commitLoading.dismissDialog();
        if(ob != null)
        {
            if (ob instanceof HouseInfoResponse)
            {
                HouseInfoResponse houseInfoResponse = (HouseInfoResponse)ob;
                if (GeneralUtils.isNotNullOrZeroLenght(houseInfoResponse.getRetcode()))
                {
                    if (Constants.SUCESS_CODE.equals(houseInfoResponse.getRetcode()))
                    {
                        List<HouseInfoDoc> list = houseInfoResponse.getDoc();
                        if(list != null )//&& list.size() > 0
                        {
                            titleBarRight.setVisibility(View.VISIBLE);
                            sv.setVisibility(View.VISIBLE);
                            for(int i=0;i<list.size();i++)
                            {
                                if(GeneralUtils.isNullOrZeroLenght(list.get(i).getcId()) 
                                    || !list.get(i).getcId().equals(Global.getCId()))
                                {
                                    continue;
                                }
                                
                                if(GeneralUtils.isNotNullOrZeroLenght(list.get(i).gethId())
                                    && GeneralUtils.isNotNullOrZeroLenght(list.get(i).getFlag())
                                    && list.get(i).getFlag().equals("0"))
                                {
                                    StringBuilder builder = new StringBuilder();
                                    builder.append(list.get(i).getcName()).append(list.get(i).getbName())
                                    .append(list.get(i).getdName())
                                    .append(list.get(i).gethName());
                                    hNameList.add(builder.toString());
                                    hIDList.put(builder.toString(), (list.get(i).gethId()));
                                }
                            }
                            if(hNameList.size() > 1)
                            {
                                openImageView.setVisibility(View.VISIBLE);
                                choiseHouseLayout.setOnClickListener(this);
                            }
                            else
                            {
                                if(hNameList.size() == 1)
                                {
                                    houseId.setText(hNameList.get(0));
                                }
                                else
                                {
                                    houseId.setText("您暂未绑定任何房屋");
                                }
                                openImageView.setVisibility(View.GONE);
                                choiseHouseLayout.setFocusable(false);
                            }
                        }
                        else
                        {
                            clickrefreshLayout.setVisibility(View.VISIBLE);
                            clickTextView.setText("此账户未绑定房屋");
                        }
                    }
                    else
                    {
                        clickrefreshLayout.setVisibility(View.VISIBLE);
                        clickTextView.setText(houseInfoResponse.getRetinfo()); 
                    }
                    return;
                }
                clickrefreshLayout.setVisibility(View.VISIBLE);
                clickTextView.setText(Constants.ERROR_MESSAGE);
            }
            else if(ob instanceof BaseResponse)
            {
                BaseResponse resp = (BaseResponse)ob;
                titleBarRight.setEnabled(true);
                if(GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if(Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        Toast.makeText(RepairActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        ToastUtil.makeText(RepairActivity.this, resp.getRetinfo());
                    }
                }
            }
        }
    }
    
    @Override
    protected void onDestroy()
    {
        // 清理图片 
        FileUtil.deleteDirectory(FileSystemManager.getPostPath(RepairActivity.this));
        adapter.destoryBitmap();
        super.onDestroy();
    }
}
