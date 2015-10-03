package com.ymdq.thy.ui.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.home.PaymentListDoc;
import com.ymdq.thy.bean.home.PaymentListResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.ui.home.adapter.PaymentListAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;

public class PaymentListFragment extends BaseFragment implements UICallBack
{
    private View view;
    
    private LinearLayout listviewLayout;
    
    private ListView mListView;
    
    private PaymentListAdapter adapter;
    
    private List<PaymentListDoc> mList;

    private LinearLayout loadingLayout;
    
    private LinearLayout clickrefreshLayout;
    
    private TextView clickTextView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.payment_list_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        initView();
        reqPayList();
    }

    private void initView()
    {
        mListView = (ListView)view.findViewById(R.id.list_view);
        mList = new ArrayList<PaymentListDoc>();
        adapter = new PaymentListAdapter(getActivity(),mList);
        mListView.setAdapter(adapter);
        
        listviewLayout = (LinearLayout)view.findViewById(R.id.listview_layout);
        listviewLayout.setVisibility(View.GONE);
        
        loadingLayout = (LinearLayout)view.findViewById(R.id.loading_layout);
        loadingLayout.setVisibility(View.VISIBLE);
        
        clickrefreshLayout = (LinearLayout)view.findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
    }
    
    /**
     * 
     * <向服务器请求接口>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqPayList()
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
            param, this, 
            PaymentListResponse.class, 
            URLUtil.PAYMENT_LIST, 
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        loadingLayout.setVisibility(View.GONE);
        if(ob != null)
        {
            if(ob instanceof PaymentListResponse)
            {
                PaymentListResponse resp = (PaymentListResponse)ob;
                if(GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if(Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        if(resp.getDoc() != null && resp.getDoc().size() > 0)
                        {
                            listviewLayout.setVisibility(View.VISIBLE);
                            clickrefreshLayout.setVisibility(View.GONE);
                            
                            mList.addAll(resp.getDoc());
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            listviewLayout.setVisibility(View.GONE);
                            clickrefreshLayout.setVisibility(View.VISIBLE);
                            clickTextView.setText("暂无缴费清单信息");
                        }
                    }
                    else
                    {
                        listviewLayout.setVisibility(View.GONE);
                        clickrefreshLayout.setVisibility(View.VISIBLE);
                        clickTextView.setText(resp.getRetinfo());
                    }
                    return;
                }
                listviewLayout.setVisibility(View.GONE);
                clickrefreshLayout.setVisibility(View.VISIBLE);
                clickTextView.setText("请求失败，请稍后再试");
            }
        }
    }
}
