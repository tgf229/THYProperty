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
import com.ymdq.thy.bean.home.AccountManageDoc;
import com.ymdq.thy.bean.home.AccountManageResponse;
import com.ymdq.thy.bean.home.HomeList;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.ui.home.adapter.AccountManageAdapter;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.view.GifView;

/**
 * 
 * <账单管理>
 * <功能详细描述>
 * 
 * @author  sunqing
 * @version  [版本号, 2014年12月13日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class AccountManageFragment extends BaseFragment implements OnClickListener, UICallBack
{
    private View view;
    
    private List<AccountManageDoc> mList;
    
    private AccountManageAdapter adapter;
    
    private ListView mListView;
    
    private LinearLayout loadingLayout;
    
    private LinearLayout clickrefreshLayout;
    
    private TextView clickTextView;
    
    private GifView gif1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.home_account_manage_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //        Toast.makeText(getActivity(), "AccountManageFragment", Toast.LENGTH_SHORT).show();
        init();
        initData();
    }
    
    private void init()
    {
        mListView = (ListView)view.findViewById(R.id.list_view);
        mList = new ArrayList<AccountManageDoc>();
        adapter = new AccountManageAdapter(getActivity(), mList, this);
        mListView.setAdapter(adapter);
        mListView.setVisibility(View.GONE);
        
        loadingLayout = (LinearLayout)view.findViewById(R.id.loading_layout);
        gif1 = (GifView)loadingLayout.findViewById(R.id.gif1);
        // 设置背景gif图片资源  
        gif1.setMovieResource(R.raw.jiazai_gif);
        loadingLayout.setVisibility(View.VISIBLE);
        
        clickrefreshLayout = (LinearLayout)view.findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
        //        clickrefreshLayout.setOnClickListener(this);
    }
    
    private void initData()
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
            this,
            AccountManageResponse.class,
            URLUtil.ACCOUNT_MANAGET,
            Constants.ENCRYPT_SIMPLE);
    }
    
    @Override
    public void netBack(Object ob)
    {
        gif1.setPaused(true);
        loadingLayout.setVisibility(View.GONE);
        if (ob != null)
        {
            if (ob instanceof AccountManageResponse)
            {
                AccountManageResponse resp = (AccountManageResponse)ob;
                if (GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if (Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        List<AccountManageDoc> docList = resp.getDoc();
                        if (docList != null && docList.size() > 0)
                        {
                            mListView.setVisibility(View.VISIBLE);
                            clickrefreshLayout.setVisibility(View.GONE);
                            
                            for (int i = 0; i < docList.size(); i++)
                            {
                                List<HomeList> homeList = docList.get(i).gethList();
                                if (homeList != null && homeList.size() > 0)
                                {
                                    mList.add(docList.get(i));
                                }
                            }
                            if (mList.size() > 0)
                            {
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                mListView.setVisibility(View.GONE);
                                clickrefreshLayout.setVisibility(View.VISIBLE);
                                clickTextView.setText("暂无账单管理信息");
                            }
                        }
                        else
                        {
                            mListView.setVisibility(View.GONE);
                            clickrefreshLayout.setVisibility(View.VISIBLE);
                            clickTextView.setText("暂无账单管理信息");
                        }
                    }
                    else
                    {
                        mListView.setVisibility(View.GONE);
                        clickrefreshLayout.setVisibility(View.VISIBLE);
                        clickTextView.setText(resp.getRetinfo());
                    }
                    return;
                }
                mListView.setVisibility(View.GONE);
                clickrefreshLayout.setVisibility(View.VISIBLE);
                clickTextView.setText("请求失败，请稍后再试");
            }
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        
        //物业费用
        //            case R.id.deliverty_layout:
        //                int position = (Integer)v.getTag();
        //                AccountManageDoc doc = (AccountManageDoc)adapter.getItem(position);
        //                
        //                Intent deltyIntent = new Intent(getActivity(),AccountManagePaymentActivity.class);
        //                Bundle bundle = new Bundle();
        //                bundle.putSerializable("doc", doc);
        //                bundle.putString("type", "1");
        //                deltyIntent.putExtras(bundle);
        //                startActivity(deltyIntent);
        //                break;
        /**
         * 响应失败页面点击事件
         */
        //            case R.id.click_refresh_layout:
        //                loadingLayout.setVisibility(View.VISIBLE);
        //                clickrefreshLayout.setVisibility(View.GONE);
        //                initData();
            default:
                break;
        }
    }
}
