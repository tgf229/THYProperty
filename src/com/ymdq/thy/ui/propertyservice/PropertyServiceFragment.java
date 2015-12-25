package com.ymdq.thy.ui.propertyservice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.sharepref.SharePref;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.ui.personcenter.LoginActivity;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.ToastUtil;

public class PropertyServiceFragment extends BaseFragment implements OnClickListener
{
    private View view;
    
    private TextView address;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.property_service_fragment, null);
        initView();
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }
    
    private void initView()
    {
        address = (TextView)view.findViewById(R.id.property_address);
        address.setText(SharePref.getString(SharePref.PROPERTY_ADDRESS, "暂无"));
        
        Button ticketBtn = (Button)view.findViewById(R.id.my_ticket);
        ticketBtn.setOnClickListener(this);
        Button callBtn = (Button)view.findViewById(R.id.call);
        callBtn.setOnClickListener(this);
        LinearLayout repairLayout = (LinearLayout)view.findViewById(R.id.repair_layout);
        repairLayout.setOnClickListener(this);
        LinearLayout complaintLayout = (LinearLayout)view.findViewById(R.id.complaint_layout);
        complaintLayout.setOnClickListener(this);
//        LinearLayout thanksLayout = (LinearLayout)view.findViewById(R.id.thanks_layout);
//        thanksLayout.setOnClickListener(this);
        LinearLayout helpLayout = (LinearLayout)view.findViewById(R.id.help_layout);
        helpLayout.setOnClickListener(this);
        LinearLayout suggestLayout = (LinearLayout)view.findViewById(R.id.suggest_layout);
        suggestLayout.setOnClickListener(this);
        
//        ImageView topBgImg = (ImageView)view.findViewById(R.id.top_bg);
//        int width = getResources().getDisplayMetrics().widthPixels;
//        int height = width*200/640;
//        topBgImg.setScaleType(ScaleType.FIT_XY);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,height);
//        topBgImg.setLayoutParams(params);
    }
    
    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        if(!hidden)
            address.setText(SharePref.getString(SharePref.PROPERTY_ADDRESS, "暂无"));
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            //我的工单
            case R.id.my_ticket:
                if(Global.isLogin())
                {
                    Intent ticket = new Intent(getActivity(),MyTicketActivity.class);
                    startActivity(ticket);
                }
                else
                {
                    goToLogin();
                }
                break;
                //一键呼叫
            case R.id.call:
                if(GeneralUtils.isNullOrZeroLenght(SharePref.getString(SharePref.PROPERTY_TEL, "")))
                {
                    ToastUtil.makeText(getActivity(), "暂无号码信息");
                    break;
                }
                DialogUtil.showTwoButtonDialog(getActivity(),"您是否拨打物业号码：\n"+SharePref.getString(SharePref.PROPERTY_TEL, ""),
                    new DialogCallBack(){

                    @Override
                    public void dialogBack()
                    {
                        Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+SharePref.getString(SharePref.PROPERTY_TEL, "")));
                        startActivity(callIntent);
                    }});
                break;
                //物业报修
            case R.id.repair_layout:
                if(Global.isLogin())
                {
                    Intent repairIntent = new Intent(getActivity(),RepairActivity.class);
                    repairIntent.putExtra("title_name", "报修");
                    repairIntent.putExtra("type", Constants.PROPERTY_REPAIR);
                    startActivity(repairIntent);
                }
                else
                {
                    goToLogin();
                }
                break;
                //投诉
            case R.id.complaint_layout:
                if(Global.isLogin())
                {
                    Intent complaintIntent = new Intent(getActivity(),RepairActivity.class);
                    complaintIntent.putExtra("title_name", "投诉");
                    complaintIntent.putExtra("type", Constants.PROPERTY_COMPLAINT);
                    startActivity(complaintIntent);
                }
                else
                {
                    goToLogin();
                }
                break;
                //表扬感谢
//            case R.id.thanks_layout:
//                if(Global.isLogin())
//                {
//                    Intent thankstIntent = new Intent(getActivity(),RepairActivity.class);
//                    thankstIntent.putExtra("title_name", "表扬");
//                    thankstIntent.putExtra("type", Constants.PROPERTY_PRAISE);
//                    startActivity(thankstIntent);
//                }
//                else
//                {
//                    goToLogin();
//                }
//                break;
                //求助
            case R.id.help_layout:
                if(Global.isLogin())
                {
                    Intent complaintIntent = new Intent(getActivity(),RepairActivity.class);
                    complaintIntent.putExtra("title_name", "求助");
                    complaintIntent.putExtra("type", Constants.PROPERTY_HELP);
                    startActivity(complaintIntent);
                }
                else
                {
                    goToLogin();
                }
                break;
                //建议
            case R.id.suggest_layout:
                if(Global.isLogin())
                {
                    Intent suggestIntent = new Intent(getActivity(),RepairActivity.class);
                    suggestIntent.putExtra("title_name", "建议");
                    suggestIntent.putExtra("type", Constants.PROPERTY_SUGGEST);
                    startActivity(suggestIntent);
                }
                else
                {
                    goToLogin();
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     * <提示去登录>
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
                getActivity().startActivityForResult(i, Constants.NUM0);
            }
        });
    }
    
}
