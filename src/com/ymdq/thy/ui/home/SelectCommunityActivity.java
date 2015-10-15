package com.ymdq.thy.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.QueryCommunityListResponse;
import com.ymdq.thy.bean.home.QueryCommunityResponse;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.home.adapter.SelectCommunityAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;

public class SelectCommunityActivity extends BaseActivity implements OnClickListener
{
    private String current_cid = "";
    
    /**
     * 查询小区导航接口，userId是否传参，
     * 注册和添加房屋页面不需要传参，首页根据用户是否登录来判断用户是否传参
     */
    private boolean add_house = false;
    
    private ArrayList<QueryCommunityListResponse> mList;
    
    /**
     * 当前选择的小区名
     */
    private TextView currentCName;
    
    /**
     * 城市名
     */
    private TextView currentCity;
    
    private SelectCommunityAdapter adapter;
    
    private LinearLayout loadingLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_community_listview);
        
        current_cid = getIntent().getStringExtra("user_current_id");
        add_house = getIntent().getBooleanExtra("add_house", false);
        initView();
        reqCommunity();
    }
    
    private void initView()
    {
        LinearLayout back = (LinearLayout)findViewById(R.id.title_back_layout);
        back.setOnClickListener(this);
        loadingLayout = (LinearLayout)findViewById(R.id.loading);
        TextView titleName = (TextView)findViewById(R.id.title_name);
        titleName.setText("选择小区");
        ListView mListView = (ListView)findViewById(R.id.list_view);
        View headView = ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE))
            .inflate(R.layout.select_community_listview_head, null);
        RelativeLayout currentLay = (RelativeLayout)headView.findViewById(R.id.current_layout);
        currentCity = (TextView)headView.findViewById(R.id.current_c_city);
        currentCName = (TextView)headView.findViewById(R.id.current_comm);
        mListView.addHeaderView(headView);
        currentLay.setOnClickListener(this);
        mList = new ArrayList<QueryCommunityListResponse>();
        adapter = new SelectCommunityAdapter(SelectCommunityActivity.this,mList,add_house);
        mListView.setAdapter(adapter);
    }
    
    private void reqCommunity()
    {
        if(!add_house)
        {
            Map<String,String> param = new HashMap<String,String>();
                param.put("uId", Global.getUserId());
            
            ConnectService.instance().connectServiceReturnResponse(SelectCommunityActivity.this,
                param,
                SelectCommunityActivity.this,
                QueryCommunityResponse.class,
                URLUtil.QUERY_COMMUNITY,
                Constants.ENCRYPT_NONE);
        }
        else
        {
            ConnectService.instance().connectServiceReturnResponse(SelectCommunityActivity.this,
                null,
                SelectCommunityActivity.this,
                QueryCommunityResponse.class,
                URLUtil.QUERY_COMMUNITY,
                Constants.ENCRYPT_NONE);
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        loadingLayout.setVisibility(View.GONE);
        if(ob != null)
        {
            QueryCommunityResponse commResp = (QueryCommunityResponse)ob;
            if(GeneralUtils.isNotNullOrZeroLenght(commResp.getRetcode()))
            {
                if(Constants.SUCESS_CODE.equals(commResp.getRetcode()))
                {
                    List<QueryCommunityListResponse> doc = commResp.getDoc();
                    mList.clear();
                    if(doc.size() > 0)
                    {
                        for(int i = 0;i<doc.size();i++)
                        {
                            
                            if(doc.get(i).getcId().equals(current_cid))
                            {
                                currentCity.setText(doc.get(i).getCity());
                                currentCName.setText(doc.get(i).getcName());
                                continue;
                            }
                            mList.add(doc.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
            case R.id.current_layout:
                finish();
                break;
            default:
                break;
        }
    }
    
}
