package com.ymdq.thy.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.InitResponse;
import com.ymdq.thy.bean.home.LoopAdvertisementDoc;
import com.ymdq.thy.bean.home.LoopAdvertisementResponse;
import com.ymdq.thy.bean.home.MyCentralMeeageDoc;
import com.ymdq.thy.bean.home.MyCentralMeeageResponse;
import com.ymdq.thy.bean.home.NoReadDelivertyResponse;
import com.ymdq.thy.bean.home.QueryCommunityListResponse;
import com.ymdq.thy.bean.home.QueryCommunityResponse;
import com.ymdq.thy.bean.personcenter.LoginResponse;
import com.ymdq.thy.bean.personcenter.UpdateVersionResponse;
import com.ymdq.thy.bean.propertyservice.PraiseListDoc;
import com.ymdq.thy.bean.propertyservice.ServiceItemDoc;
import com.ymdq.thy.bean.propertyservice.ServiceItemResponse;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.database.MycentralMessageDB;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.sharepref.SharePref;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.ui.home.adapter.HomeImagePagerAdapter;
import com.ymdq.thy.ui.personcenter.LoginActivity;
import com.ymdq.thy.ui.propertyservice.PraiseListActivity;
import com.ymdq.thy.ui.propertyservice.adapter.ServiceItemListAdapter;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.DownApkUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.MyImageView;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

public class MainFragment extends BaseFragment implements OnHeaderRefreshListener, OnClickListener
{
    
    private View view;
    
    /**
     * 头部小区名
     */
    private TextView titteName, homeTitTitle, property_name_txt, property_address_txt;
    
    /**
     * 选择小区布局
     */
    private RelativeLayout titleBar;
    
    private LinearLayout property_info, property_list, property_call;
    
    /**
     * 选择小区下拉图片
     */
    private ImageView titleImg;
    
    /**
     * 小区所属的城市
     */
    private String city;
    
    /**
     * 上下拉刷新
     */
    private PullToRefreshView mPullToRefreshView;
    
    /**
     * 广告位的默认图片
     */
    private MyImageView default_img;
    
    /**
     * 滑动的图片
     */
    private ViewPager banner_Pager;
    
    private HomeImagePagerAdapter circleImagePagerAdapter;
    
    private ArrayList<LoopAdvertisementDoc> businessPlans;
    
    private CirclePageIndicator banner_indicator;
    
    /**
     * 跳转时间
     */
    private final int SKIP_TIME = 5 * 1000;
    
    /**
     * 当前版本号
     */
    private String versionName;
    
    /**
     * 下载apk文件类
     */
    private DownApkUtil downApkUtil;
    
    private String messageInfo;
    
    public static Context mContext;
    
    /**
     * handle接受广告定时跳转,下载apk后安装apk
     */
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                int postion = banner_Pager.getCurrentItem() + 1;
                if (null != businessPlans && businessPlans.size() > 0)
                    banner_Pager.setCurrentItem(postion % businessPlans.size(), true);
                handler.sendEmptyMessageDelayed(0, SKIP_TIME);
            }
        }
        
        @Override
        public void dispatchMessage(Message msg)
        {
            super.dispatchMessage(msg);
            switch (msg.what)
            {
                case Constants.DOWNLOAD:
                    // 设置进度条位置  
                    downApkUtil.updateProgress();
                    break;
                case Constants.DOWNLOAD_FINISH:
                    // 安装文件  
                    downApkUtil.installApk();
                    break;
                case Constants.NO_SD:
                    ToastUtil.makeText(getActivity(), "请插入SD卡");
                    break;
                default:
                    break;
            }
        }
    };
    
    /**
     * 快递的未读消息提示
     */
    private TextView delivertyNum;
    
    /**
     * 我的消息的未读消息提示
     */
    private TextView messageNum;
    
    private LoginSuccessBroard broardcast;
    
    private com.ymdq.thy.view.MyGridView service_item_grid;
    private ServiceItemListAdapter serviceItemListAdapter;
    private List<ServiceItemDoc> serviceItemList;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.main_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        init();
        reqInit();
        reqCommunity();
        reqLoopAdvert();
        reqServiceList();
        reqMyDeliverty();
        registreBroadcast();
        mContext = getActivity();
        updateVersion();
        
    }
    
    @Override
    public void onHiddenChanged(boolean hidden)
    {
        if (!hidden && Global.isLogin())
        {
            reqMyMessage();
            reqMyDeliverty();
        }
        super.onHiddenChanged(hidden);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        reqMyMessage();
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("NewApi")
    private void init()
    {
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.home_main_pull_refresh_view);
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        
        titleBar = (RelativeLayout)view.findViewById(R.id.title_bar);
        titteName = (TextView)view.findViewById(R.id.title_name);
        titleImg = (ImageView)view.findViewById(R.id.img_view);
        titleBar.setClickable(false);
        titleBar.setOnClickListener(this);
        
        banner_Pager = (ViewPager)view.findViewById(R.id.circlepager);
        banner_Pager.setVisibility(View.VISIBLE);
        default_img = (MyImageView)view.findViewById(R.id.default_load_img);
        default_img.setVisibility(View.VISIBLE);
        businessPlans = new ArrayList<LoopAdvertisementDoc>();
        circleImagePagerAdapter = new HomeImagePagerAdapter(getActivity(), businessPlans);
        banner_Pager.setAdapter(circleImagePagerAdapter);
        banner_indicator = (CirclePageIndicator)view.findViewById(R.id.circleindicator);
        banner_indicator.setViewPager(banner_Pager);
        handler.sendEmptyMessageDelayed(0, SKIP_TIME);
        
        property_info = (LinearLayout)view.findViewById(R.id.property_info);
        property_name_txt = (TextView)view.findViewById(R.id.property_name_txt);
        property_address_txt = (TextView)view.findViewById(R.id.property_address_txt);
        property_list = (LinearLayout)view.findViewById(R.id.property_list);
        property_call = (LinearLayout)view.findViewById(R.id.property_call);
        service_item_grid = (com.ymdq.thy.view.MyGridView)view.findViewById(R.id.service_item_grid);
        serviceItemList = new ArrayList<ServiceItemDoc>();
        serviceItemListAdapter = new ServiceItemListAdapter(getActivity(), serviceItemList, this);
        service_item_grid.setAdapter(serviceItemListAdapter);
        
        View listview_head = LayoutInflater.from(getActivity()).inflate(R.layout.main_fragment_listview_head, null);
        Button centralDeliveryBtn = (Button)listview_head.findViewById(R.id.my_central_delivery);
        centralDeliveryBtn.setOnClickListener(this);
        Button centralMessageBtn = (Button)listview_head.findViewById(R.id.my_central_message);
        centralMessageBtn.setOnClickListener(this);
        Button centralcardBtn = (Button)listview_head.findViewById(R.id.my_central_card);
        centralcardBtn.setOnClickListener(this);
        Button centralListBtn = (Button)listview_head.findViewById(R.id.my_central_lists);
        centralListBtn.setOnClickListener(this);
        delivertyNum = (TextView)listview_head.findViewById(R.id.deliverty_num);
        messageNum = (TextView)listview_head.findViewById(R.id.message_num);
        
        property_info.setOnClickListener(this);
        property_list.setOnClickListener(this);
        property_call.setOnClickListener(this);
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        reqLoopAdvert();
        reqServiceList();
        reqMyDeliverty();
        reqMyMessage();
        reqCommunity();
    }
    
    /**
     * 
     * <初始化>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqInit()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("version", GeneralUtils.getVersionName(getActivity()));
        param.put("type", Constants.clientType);
        param.put("model", android.os.Build.MODEL);
        param.put("imei", GeneralUtils.getDeviceId(getActivity()));
        param.put("cId", Global.getCId());
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            MainFragment.this,
            InitResponse.class,
            URLUtil.INIT,
            Constants.ENCRYPT_NONE);
    }
    
    private void reqServiceList()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("cId", Global.getCId());
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            MainFragment.this,
            ServiceItemResponse.class,
            URLUtil.BUS_200901,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <测试-----登陆>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void login()
    {
        //自动登陆
        if (GeneralUtils.isNotNullOrZeroLenght(Global.getUserName())
            && GeneralUtils.isNotNullOrZeroLenght(Global.getPassword())
            && GeneralUtils.isNotNullOrZeroLenght(Global.getUCId()))
        {
            Map<String, String> param = new HashMap<String, String>();
            try
            {
                param.put("username", Global.getUserName());
                param.put("pwd", Global.getPassword());
                param.put("type", Constants.clientType);
                param.put("version", GeneralUtils.getVersionName(getActivity()));
                param.put("imei", GeneralUtils.getDeviceId(getActivity()));
                param.put("cId", Global.getUCId());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            ConnectService.instance().connectServiceReturnResponse(getActivity(),
                param,
                this,
                LoginResponse.class,
                URLUtil.USER_LOGIN,
                Constants.ENCRYPT_NONE);
        }
    }
    
    /**
     * 
     * <请求轮播通告列表>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqLoopAdvert()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("cId", Global.getCId());
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            MainFragment.this,
            LoopAdvertisementResponse.class,
            URLUtil.LOOP_ADVERT,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <我的消息查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqMyMessage()
    {
        if (Global.isLogin())
        {
            Map<String, String> param = new HashMap<String, String>();
            try
            {
                param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
                param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            ConnectService.instance().connectServiceReturnResponse(getActivity(),
                param,
                MainFragment.this,
                MyCentralMeeageResponse.class,
                URLUtil.MY_MESSAGE,
                Constants.ENCRYPT_SIMPLE);
        }
    }
    
    /**
     * 
     * <APP用户提醒(用于查询快递的未读消息数)>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqMyDeliverty()
    {
        if (Global.isLogin())
        {
            Map<String, String> param = new HashMap<String, String>();
            try
            {
                param.put("cId", SecurityUtils.encode2Str(Global.getCId()));
                param.put("uId", SecurityUtils.encode2Str(Global.getUserId()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            ConnectService.instance().connectServiceReturnResponse(getActivity(),
                param,
                MainFragment.this,
                NoReadDelivertyResponse.class,
                URLUtil.NO_READ_DELIVERTY,
                Constants.ENCRYPT_SIMPLE);
        }
    }
    
    /**
     * 
     * <用户登录，请求小区导航>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqCommunity()
    {
        Map<String, String> param = new HashMap<String, String>();
        if (Global.isLogin())
        {
            param.put("uId", Global.getUserId());
        }
        else
        {
            param.put("uId", "");
        }
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            MainFragment.this,
            QueryCommunityResponse.class,
            URLUtil.QUERY_COMMUNITY,
            Constants.ENCRYPT_NONE);
        
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
        filter.addAction(Constants.ADD_HOUSE_BROADCAST);
        filter.addAction(Constants.DELETE_HOUSE_BROADCAST);
        broardcast = new LoginSuccessBroard();
        getActivity().registerReceiver(broardcast, filter);
    }
    
    @Override
    public void netBack(Object ob)
    {
        super.netBack(ob);
        mPullToRefreshView.onHeaderRefreshComplete();
        /**
         * 登陆
         */
        if (ob instanceof LoginResponse)
        {
            LoginResponse loginResponse = (LoginResponse)ob;
            
            if (GeneralUtils.isNotNullOrZeroLenght(loginResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(loginResponse.getRetcode()))
                {
                    Global.saveData(loginResponse, Global.getUserName(), Global.getPassword(), true);
                }
                else
                {
                    Global.setIsLogin(false);
                }
            }
            else
            {
                Global.setIsLogin(false);
            }
        }//初始化
        else if (ob instanceof InitResponse)
        {
            InitResponse initresp = (InitResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(initresp.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(initresp.getRetcode()))
                {
                    SharePref.saveString(SharePref.PROPERTY_NAME, initresp.getName());
                    SharePref.saveString(SharePref.PROPERTY_ADDRESS, initresp.getAddress());
                    SharePref.saveString(SharePref.PROPERTY_TEL, initresp.getTel());
                    SharePref.saveString(SharePref.PROPERTY_LOGO, initresp.getLogo());
                    SharePref.saveString(SharePref.PROPERTY_CONTENT, initresp.getContent());
                    SharePref.saveString(SharePref.PROPERTY_TITLE, initresp.getTitle());
                    SharePref.saveString(SharePref.PROPERTY_TITLEURL, initresp.getTitleUrl());
                    String flag = initresp.getFlag();
                    
                    property_name_txt.setText(SharePref.getString(SharePref.PROPERTY_NAME, "暂无"));
                    property_address_txt.setText(SharePref.getString(SharePref.PROPERTY_ADDRESS, "暂无"));
                    
                    if (flag != null && flag.equals(Constants.EXIT_APP))
                    {
                        JRApplication.jrApplication.onTerminate();
                    }
                }
            }
        }
        //请求轮播通告
        else if (ob instanceof LoopAdvertisementResponse)
        {
            LoopAdvertisementResponse loopAdt = (LoopAdvertisementResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(loopAdt.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(loopAdt.getRetcode()))
                {
                    ArrayList<LoopAdvertisementDoc> tempPlans = loopAdt.getDoc();
                    if (null != tempPlans && tempPlans.size() > 0)
                    {
                        businessPlans.clear();
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = width * 8 / 15;
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                        banner_Pager.setLayoutParams(params);
                        
                        businessPlans.addAll(tempPlans);
                        banner_indicator.setVisibility(View.VISIBLE);
                        banner_Pager.setVisibility(View.VISIBLE);
                        default_img.setVisibility(View.GONE);
                        
                        circleImagePagerAdapter.notifyDataSetChanged();
                        banner_Pager.setCurrentItem(0);
                        banner_indicator.notifyDataSetChanged();
                    }
                    else
                    {
                        banner_indicator.setVisibility(View.GONE);
                        banner_Pager.setVisibility(View.GONE);
                        default_img.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    banner_indicator.setVisibility(View.GONE);
                    banner_Pager.setVisibility(View.GONE);
                    default_img.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                banner_indicator.setVisibility(View.GONE);
                banner_Pager.setVisibility(View.GONE);
                default_img.setVisibility(View.VISIBLE);
                // 请求失败显示默认图片
                businessPlans.clear();
                banner_indicator.notifyDataSetChanged();
            }
        }
        else if(ob instanceof ServiceItemResponse)
        {
            ServiceItemResponse resp = (ServiceItemResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(resp.getRetcode()))
                {
                    List<ServiceItemDoc> doc = resp.getDoc();
                    if (GeneralUtils.isNotNullOrZeroSize(doc))
                    {
                        serviceItemList.clear();
                        serviceItemList.addAll(doc);
                        serviceItemListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        //我的消息查询
        else if (ob instanceof MyCentralMeeageResponse)
        {
            MyCentralMeeageResponse messageResp = (MyCentralMeeageResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(messageResp.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(messageResp.getRetcode()))
                {
                    messageInfo = null;
                    List<MyCentralMeeageDoc> docList = messageResp.getDoc();
                    if (docList != null && docList.size() > 0)
                    {
                        MycentralMessageDB.getInstance(getActivity()).insertDb(Global.getUserId(),
                            Global.getCId(),
                            docList);
                    }
                    queryNoReadMessage();
                }
                else
                {
                    messageInfo = messageResp.getRetcode();
                }
            }
        }
        //邮包列表未读数
        else if (ob instanceof NoReadDelivertyResponse)
        {
            NoReadDelivertyResponse num = (NoReadDelivertyResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(num.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(num.getRetcode()))
                {
                    if (GeneralUtils.isNotNullOrZeroLenght(num.getNewPost()))
                    {
                        
                        Intent intent = new Intent(Constants.COMMUNITY_MESSAGE_NUMBER_BROADCAST);
                        intent.putExtra("number", GeneralUtils.isNullOrZeroLenght(num.getNewReply()) == true ? "0"
                            : num.getNewReply());
                        getActivity().sendBroadcast(intent);
                        if (Integer.valueOf(num.getNewPost()) > 0)
                        {
                            delivertyNum.setVisibility(View.VISIBLE);
                            delivertyNum.setText(num.getNewPost());
                            return;
                        }
                    }
                }
                delivertyNum.setVisibility(View.GONE);
            }
        }
        
        else if (ob instanceof UpdateVersionResponse)
        {
            final UpdateVersionResponse response = (UpdateVersionResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(response.getRetcode()))
            {
                if (response.getRetcode().equals(Constants.SUCESS_CODE))
                {
                    downApkUtil = new DownApkUtil(getActivity(), handler, response.getUrlAddress());
                    
                    String[] contentString = response.getContent().split(";");
                    String cancel = getResources().getString(R.string.set_update_cancel);
                    
                    if ("1".equals(response.getIsUpdate()))
                    {
                        cancel = getResources().getString(R.string.set_update_quit);
                    }
                    DialogUtil.showUpdateDialog(getActivity(),
                        getResources().getString(R.string.updateVersionTitel),
                        contentString,
                        getResources().getString(R.string.updateVersion),
                        cancel,
                        response.getIsUpdate(),
                        new DialogCallBack()
                        {
                            
                            @Override
                            public void dialogBack()
                            {
                                downApkUtil.downApk(response.getIsUpdate());
                                
                            }
                        });
                }
                else
                {
                    //                    ToastUtil.makeText(getActivity(), response.getRetinfo());
                }
            }
            else
            {
                //                ToastUtil.showError(getActivity());
            }
        }
        //小区导航
        else if (ob instanceof QueryCommunityResponse)
        {
            QueryCommunityResponse commRes = (QueryCommunityResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(commRes.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(commRes.getRetcode()))
                {
                    List<QueryCommunityListResponse> doc = commRes.getDoc();
                    int count = doc.size();
                    if (count > 1)
                    {
                        titleBar.setClickable(true);
                        titleImg.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        titleBar.setClickable(false);
                        titleImg.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < doc.size(); i++)
                    {
                        QueryCommunityListResponse entity = doc.get(i);
                        if (Global.getCId().equals(entity.getcId()))
                        {
                            titteName.setText(entity.getcName());
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 
     * <查询我的饿消息的未读消息数>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryNoReadMessage()
    {
        int count = MycentralMessageDB.getInstance(getActivity()).queryNoRead(Global.getUserId(), Global.getCId());
        if (count > 0)
        {
            messageNum.setVisibility(View.VISIBLE);
            messageNum.setText(count + "");
        }
        else
        {
            messageNum.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        //快递信息
            case R.id.my_central_delivery:
                if (Global.isLogin())
                {
                    //                    delivertyNum.setVisibility(View.GONE);
                    Intent delivertyIntent = new Intent(getActivity(), MyCentralDelivertyActivity.class);
                    startActivity(delivertyIntent);
                }
                
                else
                {
                    goToLogin();
                }
                break;
            //我的消息
            case R.id.my_central_message:
                if (Global.isLogin())
                {
                    Intent mymessage = new Intent(getActivity(), MyCentralMeeageActivity.class);
                    if (messageInfo != null)
                    {
                        mymessage.putExtra("messageInfo", messageInfo);
                    }
                    startActivity(mymessage);
                    
                }
                else
                {
                    goToLogin();
                }
                break;
            //我的账单
            case R.id.my_central_card:
                if (Global.isLogin())
                {
                    Intent mycard = new Intent(getActivity(), MyCentralCardActivity.class);
                    startActivity(mycard);
                }
                else
                {
                    goToLogin();
                }
                break;
            //便民信息
            case R.id.my_central_lists:
                //                Intent mylist = new Intent(getActivity(), MyCentralListsActivity.class);
                Intent mylist = new Intent(getActivity(), PraiseListActivity.class);
                startActivity(mylist);
                break;
            //选择小区
            case R.id.title_bar:
                Intent intent = new Intent(getActivity(), SelectCommunityActivity.class);//TranslateTopicActivity
                if (!Global.isLogin())
                {
                    intent.putExtra("add_house", true);
                }
                intent.putExtra("user_current_id", Global.getCId());
                startActivityForResult(intent, Constants.Select_community_cid);
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <提示要登录的界面>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void goToLogin()
    {
        DialogUtil.loginTwoButtonDialog(getActivity(), new DialogCallBack()
        {
            
            @Override
            public void dialogBack()
            {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(i, 0);
            }
        });
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Constants.Person_center_login_code:
                
                break;
            //选择小区
            case Constants.Select_community_cid:
                if (data != null)
                {
                    titteName.setText(data.getStringExtra("c_name"));
                    city = data.getStringExtra("c_name");
                    String c_id = data.getStringExtra("select_cid");
                    if (Global.isLogin())
                    {
                        Global.saveUCId(c_id);
                        login();
                    }
                    Global.saveCId(c_id);
                    reqInit();
                    reqLoopAdvert();
                    reqServiceList();
                    if (Global.isLogin())
                    {
                        reqMyDeliverty();
                        reqMyMessage();
                    }
                    Intent i = new Intent(Constants.SELECT_NEW_COMMUNITY);
                    getActivity().sendBroadcast(i);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(broardcast);
    }
    
    class LoginSuccessBroard extends BroadcastReceiver
    {
        
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //登录成功
            if (Constants.LOGIN_SUCCESS_BROADCAST.equals(intent.getAction()))
            {
                //如果更新了本地cId的值，
                if (intent.getBooleanExtra("replace_cid", false))
                {
                    reqLoopAdvert();
                }
                reqInit();
                reqServiceList();
                reqMyDeliverty();
                reqMyMessage();
                reqCommunity();
            }
            else if (Constants.LOGINOUT_SUCCESS_BROADCAST.equals(intent.getAction()))
            {
                //TODO 退出登录 用户可以选择小区
                messageNum.setVisibility(View.GONE);
                delivertyNum.setVisibility(View.GONE);
                reqCommunity();
            }
            //个人中心添加房屋 or 个人中心删除房屋
            else if (Constants.ADD_HOUSE_BROADCAST.equals(intent.getAction())
                || Constants.DELETE_HOUSE_BROADCAST.equals(intent.getAction()))
            {
                reqCommunity();
            }
            
        }
    }
    
    /**
     * 
     * <检查版本更新>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void updateVersion()
    {
        versionName = GeneralUtils.getVersionName(getActivity());
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("version", "" + versionName);
        param.put("type", "" + Constants.clientType);
        param.put("imei", GeneralUtils.getDeviceId(getActivity()));
        
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            MainFragment.this,
            UpdateVersionResponse.class,
            URLUtil.SET_UPDATE,
            Constants.ENCRYPT_NONE);
    }
    
}
