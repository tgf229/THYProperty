package com.ymdq.thy.ui.personcenter;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseInfoDoc;
import com.ymdq.thy.bean.personcenter.HouseInfoResponse;
import com.ymdq.thy.bean.personcenter.UnBindingHouseResponse;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.util.BitmapUtil;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.MyListView;

/**
 * 
 * <我的家>
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-25]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PersonCenterFragment extends BaseFragment implements OnClickListener
{
    private View mView;
    
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 编辑个人信息,没有房屋时添加房屋,密码管理,设置,拥有房屋时编辑房屋
     */
    private LinearLayout llName, llAddHome, llManagePass, llSet, llShowEdit, llNoHouse;
    
    /**
     * 头像显示
     */
    private ImageView ivHeadPic;
    
    /**
     * 昵称,添加房屋,编辑房屋
     */
    private TextView tvName, tvAddHouse, tvEditHouse, tvFinishEdit;
    
    /**
     * 房屋列表
     */
    private MyListView listView;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 头像
     */
    private SoftReference<Bitmap> photoReference;
    
    /**
     * 房屋列表
     */
    private ArrayList<HouseInfoDoc> infoDocs;
    
    private PersonCenterHouseAdapter adapter;
    
    private boolean ifEditState;
    
    private LoginSuccessBroard broardcast;
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case Constants.NUM0:
                    photoReference = new SoftReference<Bitmap>((Bitmap)msg.obj);
                    if (photoReference.get() != null)
                    {
                        ivHeadPic.setImageBitmap(photoReference.get());
                    }
                    break;
                
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.person_center, null);
        return mView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initTitle();
        init();
        initData();
        registreBroadcast();
        if (Global.isLogin())
        {
            initHomeData();
        }
        
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        llBack = (LinearLayout)mView.findViewById(R.id.title_back_layout);
        tvTitle = (TextView)mView.findViewById(R.id.title_name);
//        tvTitle.setText("我的家");
        tvTitle.setBackgroundResource(R.drawable.title_wodejia);
        llBack.setVisibility(View.INVISIBLE);
    }
    
    private void init()
    {
        dailog = new NetLoadingDailog(getActivity());
        llName = (LinearLayout)mView.findViewById(R.id.person_center_name_ll);
        llAddHome = (LinearLayout)mView.findViewById(R.id.person_center_no_house_add);
        llManagePass = (LinearLayout)mView.findViewById(R.id.person_center_manage_pass_ll);
        llSet = (LinearLayout)mView.findViewById(R.id.person_center_set_ll);
        llShowEdit = (LinearLayout)mView.findViewById(R.id.person_center_have_house_add_edit);
        llNoHouse = (LinearLayout)mView.findViewById(R.id.person_center_no_house_ll);
        
        ivHeadPic = (ImageView)mView.findViewById(R.id.person_center_head_pic_iv);
        listView = (MyListView)mView.findViewById(R.id.person_center_house_lv);
        adapter = new PersonCenterHouseAdapter(getActivity(), infoDocs, PersonCenterFragment.this, false);
        listView.setAdapter(adapter);
        GeneralUtils.setListViewHeightBasedOnChildrenExtend(listView);
        
        /**
         * 昵称,添加房屋,编辑房屋
         */
        tvName = (TextView)mView.findViewById(R.id.person_center_name_tv);
        tvAddHouse = (TextView)mView.findViewById(R.id.person_center_add_house_tv);
        tvEditHouse = (TextView)mView.findViewById(R.id.person_center_edit_house_tv);
        tvFinishEdit = (TextView)mView.findViewById(R.id.person_center_finish_edit_house_tv);
        
        llName.setOnClickListener(this);
        llAddHome.setOnClickListener(this);
        llManagePass.setOnClickListener(this);
        llSet.setOnClickListener(this);
        tvAddHouse.setOnClickListener(this);
        tvEditHouse.setOnClickListener(this);
        tvFinishEdit.setOnClickListener(this);
    }
    
    private void initData()
    {
        if (Global.isLogin())
        {
            showPersonInfo();
        }
    }
    
    private void initHomeData()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            PersonCenterFragment.this,
            HouseInfoResponse.class,
            URLUtil.USER_BINDING_HOUSE_LIST,
            Constants.ENCRYPT_SIMPLE);
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
        filter.addAction(Constants.LOGINOUT_SUCCESS_BROADCAST);
        broardcast = new LoginSuccessBroard();
        getActivity().registerReceiver(broardcast, filter);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.person_center_name_ll://登录
                
                if (Global.isLogin())
                {
                    Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
                    startActivityForResult(intent, 6);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 7);
                }
                
                break;
            case R.id.person_center_no_house_add:

                if (Global.isLogin())
                {
                    Intent bindHouseNo = new Intent(getActivity(), RegisterOneActivity.class);
                    bindHouseNo.putExtra("center_binding_house", "center_binding_house");//绑定房屋标志位
                    startActivityForResult(bindHouseNo, 9);
                }
                else
                {
                    DialogUtil.loginTwoButtonDialog(getActivity(), new DialogCallBack()
                    {
                        
                        @Override
                        public void dialogBack()
                        {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivityForResult(intent, 10);
                        }
                    });
                }
                
                break;
            case R.id.person_center_manage_pass_ll://管理密码
                if (Global.isLogin())
                {
                    Intent manageIntent = new Intent(getActivity(), ManagePasswordActivity.class);
                    startActivityForResult(manageIntent, 16);
                }
                else
                {
                    DialogUtil.loginTwoButtonDialog(getActivity(), new DialogCallBack()
                    {
                        
                        @Override
                        public void dialogBack()
                        {
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivityForResult(intent, 12);
                        }
                    });
                }
                break;
            case R.id.person_center_set_ll://设置
                Intent intentSet = new Intent(getActivity(), SetActivity.class);
                startActivityForResult(intentSet, 13);
                break;
            
            case R.id.person_center_add_house_tv://添加房屋
                Intent bindHouse = new Intent(getActivity(), RegisterOneActivity.class);
                bindHouse.putExtra("center_binding_house", "center_binding_house");//绑定房屋标志位
                startActivityForResult(bindHouse, 8);
                break;
            
            case R.id.person_center_edit_house_tv://编辑房屋
                llShowEdit.setVisibility(View.INVISIBLE);
                tvFinishEdit.setVisibility(View.VISIBLE);
                updataData(infoDocs, true);
                ifEditState = true;
                break;
            
            case R.id.person_center_finish_edit_house_tv://完成编辑房屋
                llShowEdit.setVisibility(View.VISIBLE);
                tvFinishEdit.setVisibility(View.GONE);
                updataData(infoDocs, false);
                ifEditState = false;
                break;
            
            default:
                break;
        }
    }
    
    private void showPersonInfo()
    {
        tvName.setText(Global.getNickName());
        if (GeneralUtils.isNotNullOrZeroLenght(Global.getImage()))
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Message msg = new Message();
                    msg.what = Constants.NUM0;
                    msg.obj =
                        BitmapUtil.getBitmap(Global.getImage(), JRApplication.jrApplication, Global.getUserId(), "head");
                    handler.sendMessage(msg);
                }
            }).start();
        }
    }
    
    private void showHouseList()
    {
        if (GeneralUtils.isNotNullOrZeroSize(infoDocs))
        {
            llNoHouse.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            if (ifEditState)
            {
                llShowEdit.setVisibility(View.INVISIBLE);
                tvFinishEdit.setVisibility(View.VISIBLE);
                updataData(infoDocs, true);
            }
            else
            {
                llShowEdit.setVisibility(View.VISIBLE);
                tvFinishEdit.setVisibility(View.GONE);
                updataData(infoDocs, false);
            }
            
            llAddHome.setVisibility(View.GONE);
        }
        else
        {
            llNoHouse.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            llShowEdit.setVisibility(View.INVISIBLE);
            llAddHome.setVisibility(View.VISIBLE);
            tvFinishEdit.setVisibility(View.GONE);
            ifEditState = false;
        }
    }
    
    /**
     * 更新房屋列表
     */
    private void updataData(ArrayList<HouseInfoDoc> infoDocs, boolean showDelete)
    {
        adapter.updata(infoDocs, showDelete);
    }
    
    /**
     * 解绑房屋
     */
    public void unBindingHouse(int position)
    {
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            param.put("hId", SecurityUtils.encode2Str(infoDocs.get(position).gethId()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            PersonCenterFragment.this,
            UnBindingHouseResponse.class,
            URLUtil.USER_UNBINDING_HOUSE,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Constants.Person_center_login_code:
                //                initData();
                //                initHomeData();
                break;
            
            case Constants.User_info_pic:
                initData();
                break;
            
            case Constants.Register_binding_to_center:
                initHomeData();
                //发送广播，来更改首页的选择房屋下拉按钮状态
                Intent i = new Intent(Constants.ADD_HOUSE_BROADCAST);
                getActivity().sendBroadcast(i);
                break;
            
            case Constants.User_login_out:

                break;
            
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (ob instanceof UnBindingHouseResponse)
        {
            UnBindingHouseResponse unBindingHouseResponse = (UnBindingHouseResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(unBindingHouseResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(unBindingHouseResponse.getRetcode()))
                {
                    initHomeData();
                    Intent i = new Intent(Constants.DELETE_HOUSE_BROADCAST);
                    getActivity().sendBroadcast(i);
                }
                else
                {
                    if (dailog != null)
                    {
                        dailog.dismissDialog();
                    }
                    ToastUtil.makeText(getActivity(), unBindingHouseResponse.getRetinfo());
                }
            }
            else
            {
                if (dailog != null)
                {
                    dailog.dismissDialog();
                }
                ToastUtil.showError(getActivity());
            }
        }
        
        if (ob instanceof HouseInfoResponse)
        {
            if (dailog != null)
            {
                dailog.dismissDialog();
            }
            
            HouseInfoResponse houseInfoResponse = (HouseInfoResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(houseInfoResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(houseInfoResponse.getRetcode()))
                {
                    infoDocs = new ArrayList<HouseInfoDoc>();
                    infoDocs = (ArrayList<HouseInfoDoc>)houseInfoResponse.getDoc();
                    showHouseList();
                }
                else
                {
                    
                    ToastUtil.makeText(getActivity(), houseInfoResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(getActivity());
            }
        }
    }
    
    private void initHomeDataAgain()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            PersonCenterFragment.this,
            HouseInfoResponse.class,
            URLUtil.USER_BINDING_HOUSE_LIST,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void onHiddenChanged(boolean hidden)
    {
        if (!hidden && Global.isLogin())
        {
            initData();
            initHomeDataAgain();
        }
        super.onHiddenChanged(hidden);
    }
    
    private class LoginSuccessBroard extends BroadcastReceiver
    {
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //登录成功
            if (Constants.LOGIN_SUCCESS_BROADCAST.equals(intent.getAction()))
            {
                initData();
                initHomeData();
            }
            else if (Constants.LOGINOUT_SUCCESS_BROADCAST.equals(intent.getAction()))
            {
                tvName.setText("请登录");
                ivHeadPic.setImageResource(R.drawable.default_head_pic);
                listView.setVisibility(View.GONE);
                llShowEdit.setVisibility(View.INVISIBLE);
                llAddHome.setVisibility(View.VISIBLE);
                tvFinishEdit.setVisibility(View.GONE);
                ifEditState = false;
            }
        }
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(broardcast);
    }
}
