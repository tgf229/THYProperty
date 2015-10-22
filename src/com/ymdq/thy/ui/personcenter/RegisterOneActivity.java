package com.ymdq.thy.ui.personcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.HouseDList;
import com.ymdq.thy.bean.personcenter.HouseDoc;
import com.ymdq.thy.bean.personcenter.HouseResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.home.SelectCommunityActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.ToastUtil;

/**
 * 注册第一步
 * <功能详细描述>
 * 
 * @author  WT
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RegisterOneActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 选择小区
     */
    private LinearLayout llCommunity;
    
    /**
     * 小区所在的城市
     */
    private TextView city;
    
    /**
     * 选择房屋
     */
    private LinearLayout llHouse;
    
    private TextView tvHouse,tvCommunity;
    
    private CheckBox ckAgree;
    
    private Button btSumbit;
    
    /**
     * 房号
     */
    private HouseDList houseDList;
    
    /**
     * 单元号,单元名,楼栋号,楼栋名
     */
    private String unitNo, unitName, buildingNo, buildingName;
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 房屋信息
     */
    private ArrayList<HouseDoc> doc;
    
    private String bindHouseCenter;
    
    private TextView tvAgreement;
    
    /**
     * 小区
     */
    private String user_community_id = "";
    
    private String communityName = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_one);
        initTitle();
        init();
//        initData();
        addListener();
    }
    
    /**
     * 初始化头部
     */
    @SuppressLint("NewApi")
    private void initTitle()
    {
        bindHouseCenter = getIntent().getStringExtra("center_binding_house");
        
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        
        if ("center_binding_house".equals(bindHouseCenter))
        {
            tvTitle.setBackground(getResources().getDrawable(R.drawable.title_bangdingfangwu));
        }
        else
        {
            tvTitle.setBackground(getResources().getDrawable(R.drawable.title_zhuce));
        }
        
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                RegisterOneActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        city = (TextView)findViewById(R.id.city);
        llCommunity = (LinearLayout)findViewById(R.id.person_register_choise_community_ll);
        tvCommunity = (TextView)findViewById(R.id.person_register_choise_community_tv);
        llHouse = (LinearLayout)findViewById(R.id.person_register_choise_house_ll);
        tvHouse = (TextView)findViewById(R.id.person_register_choise_house_tv);
        ckAgree = (CheckBox)findViewById(R.id.register_agreement_ck);
        btSumbit = (Button)findViewById(R.id.register_one_sumbit_bt);
        tvAgreement = (TextView)findViewById(R.id.register_agreement_greement);
        dailog = new NetLoadingDailog(this);
    }
    
    private void addListener()
    {
        tvAgreement.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent intentAgreement = new Intent(RegisterOneActivity.this, AgreementActivity.class);
                startActivity(intentAgreement);
            }
        });
        //选择小区
        llCommunity.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(RegisterOneActivity.this,SelectCommunityActivity.class);
                i.putExtra("user_current_id", user_community_id);
                i.putExtra("add_house", true);
                startActivityForResult(i,Constants.select_community);
            }
        });
        
        /**
         * 选择房号
         */
        llHouse.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if(GeneralUtils.isNullOrZeroLenght(communityName))
                {
                    ToastUtil.makeText(RegisterOneActivity.this, "请先选择小区");
                }
                else
                {
                    Intent intent = new Intent(RegisterOneActivity.this, HouseChoiseBuildingActivity.class);
                    intent.putExtra("community_name",communityName);
                    intent.putExtra("house_building", doc);
                    startActivityForResult(intent, 0);
                }
            }
        });
        
        /**
         * 下一步
         */
        btSumbit.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (GeneralUtils.isNullOrZeroLenght(user_community_id))
                {
                    ToastUtil.makeText(RegisterOneActivity.this, "请选择小区");
                }
                else if (GeneralUtils.isNull(houseDList))
                {
                    ToastUtil.makeText(RegisterOneActivity.this, "请选择房号");
                }
                else if (!ckAgree.isChecked())
                {
                    ToastUtil.makeText(RegisterOneActivity.this, "请阅读并勾选使用协议");
                }
                else
                {
                    Intent intent = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                    intent.putExtra("register_two_building_info", houseDList);//房屋信息,包括ID,icard和phone
                    if ("center_binding_house".equals(bindHouseCenter))
                    {
                        intent.putExtra("center_binding_house", "center_binding_house");//绑定房屋标志位
                    }
                    intent.putExtra("register_cid", user_community_id);//注册用户选择的房屋
                    startActivityForResult(intent, 3);
                }
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (resultCode)
        {
            case Constants.Register_get_building:
                houseDList = (HouseDList)data.getSerializableExtra("building_num");//房号
                unitNo = data.getStringExtra("building_uint_no");//单元号
                unitName = data.getStringExtra("building_uint_name");//单元名
                buildingNo = data.getStringExtra("building_building_no");//楼栋号
                buildingName = data.getStringExtra("building_building_name");//楼栋名
                
                tvHouse.setText(buildingName + unitName + houseDList.gethName());
                break;
            
            case Constants.Register_two_code:
                setResult(Constants.Register_one_code, data);
                RegisterOneActivity.this.finish();
                break;
            case Constants.Register_binding_to_one:
                Intent intent = new Intent();
                setResult(Constants.Register_binding_to_center, intent);
                RegisterOneActivity.this.finish();
                break;
            //选择小区
            case Constants.Select_community_cid:
                if(data != null)
                {
                    city.setText(data.getStringExtra("c_city"));
                    if(!tvCommunity.getText().toString().equals(data.getStringExtra("c_name")))
                    {
                        tvHouse.setText("");
                    }
                    tvCommunity.setText(data.getStringExtra("c_name"));
                    user_community_id = data.getStringExtra("select_cid");
                    communityName = data.getStringExtra("c_name");
                    if(dailog == null)
                    {
                        dailog = new NetLoadingDailog(this);
                    }
                    dailog.loading();
                    initData();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void initData()
    {
        //                doc = TestBean.getHouseResponse();
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("cId", user_community_id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(RegisterOneActivity.this,
            param,
            RegisterOneActivity.this,
            HouseResponse.class,
            URLUtil.HOUSE_QUERY,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        
        if (ob instanceof HouseResponse)
        {
            HouseResponse houseResponse = (HouseResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(houseResponse.getRetcode()))
            {
                if(doc != null)
                    doc.clear();
                if (Constants.SUCESS_CODE.equals(houseResponse.getRetcode()))
                {
                    doc = (ArrayList<HouseDoc>)houseResponse.getDoc();
                }
                else
                {
                    ToastUtil.makeText(RegisterOneActivity.this, houseResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(RegisterOneActivity.this);
            }
        }
    }
}
