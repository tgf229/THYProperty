package com.ymdq.thy.ui.community;

import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.CommunitySquareResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseFragment;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.ToastUtil;
import com.ymdq.thy.view.PullToRefreshView;
import com.ymdq.thy.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 
 * <社区广场页面>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-18]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunitySquareFragment extends BaseFragment implements UICallBack, OnClickListener,
    OnHeaderRefreshListener
{
    
    private View view;
    
    /**
     * 请求失败展示信息
     */
    private TextView errorMessage;
    
    /**
     * 数据加载框
     */
    private LinearLayout loadingLayout;
    
    /**
     * 加载失败显示界面
     */
    private LinearLayout loadingFailedLayout;
    
    /**
     * 上下拉刷新
     */
    private PullToRefreshView mPullToRefreshView;
    
    /**
     * 官方推荐
     */
    private LinearLayout officialRecommendation;
    
    /**
     * 官方推荐更多
     */
    private LinearLayout officialRecommendationMore;
    
    /**
     * 官方推荐话题跟圈子
     */
    private LinearLayout ORTopicAndGroup;
    
    /**
     * 热门社区
     */
    private LinearLayout popularCommunity;
    
    /**
     * 热门社区更多
     */
    private LinearLayout popularCommunityMore;
    
    /**
     * 热门社区话题跟圈子
    //     */
    private LinearLayout PCTopicAndGroup;
    
    /**
     * 是否是第一次加载
     */
    private boolean isFirstLoading = true;
    
    /**
     * 切换广播
     */
    private CellBroard cellBroard;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.community_square_fragment, null);
        return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        init();
        initData();
        registreBroadcast();
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        loadingLayout = (LinearLayout)view.findViewById(R.id.loading_layout);
        errorMessage = (TextView)view.findViewById(R.id.loading_failed_txt);
        loadingFailedLayout = (LinearLayout)view.findViewById(R.id.loading_failed);
        mPullToRefreshView = (PullToRefreshView)view.findViewById(R.id.pull_refresh_view);
        officialRecommendation = (LinearLayout)view.findViewById(R.id.official_recommendation);
        officialRecommendationMore = (LinearLayout)view.findViewById(R.id.official_recommendation_more);
        ORTopicAndGroup = (LinearLayout)view.findViewById(R.id.official_recommendation_top_and_group);
        popularCommunity = (LinearLayout)view.findViewById(R.id.popular_community);
        popularCommunityMore = (LinearLayout)view.findViewById(R.id.popular_community_more);
        PCTopicAndGroup = (LinearLayout)view.findViewById(R.id.popular_community_top_and_group);
        
        /**
         * 添加按钮点击事件
         */
        mPullToRefreshView.setOnHeaderRefreshListener(this);
        officialRecommendationMore.setOnClickListener(this);
        popularCommunityMore.setOnClickListener(this);
        loadingFailedLayout.setOnClickListener(this);
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        queryCommunitySquare();
    }
    
    /**
     * 
     * <话题分享广播注册>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void registreBroadcast()
    {
        cellBroard = new CellBroard();
        IntentFilter cellFilter = new IntentFilter();
        cellFilter.addAction(Constants.SELECT_NEW_COMMUNITY);
        getActivity().registerReceiver(cellBroard, cellFilter);
        
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(Constants.LOGIN_SUCCESS_BROADCAST);
        getActivity().registerReceiver(cellBroard, loginFilter);
    }
    
    /**
     * <查询广场>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void queryCommunitySquare()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("cId", Global.getCId());
        ConnectService.instance().connectServiceReturnResponse(getActivity(),
            param,
            CommunitySquareFragment.this,
            CommunitySquareResponse.class,
            URLUtil.COMMUNITY_SQUARE_QUERY,
            Constants.ENCRYPT_NONE);
    }
    
    @Override
    public void onHeaderRefresh(PullToRefreshView view)
    {
        queryCommunitySquare();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        /**
         * 响应失败页面点击事件
         */
            case R.id.loading_failed:
                loadingFailedLayout.setVisibility(View.GONE);
                loadingLayout.setVisibility(View.VISIBLE);
                queryCommunitySquare();
                break;
            /**
             * 响应官方推荐更多
             */
            case R.id.official_recommendation_more:
                Intent officialIntent = new Intent(getActivity(), CommunityCircleListActivity.class);
                officialIntent.putExtra("type", Constants.OFFICIAL_RECOMMENDATION);
                startActivity(officialIntent);
                break;
            /**
             * 响应热门社区更多
             */
            case R.id.popular_community_more:
                Intent popularIntent = new Intent(getActivity(), CommunityCircleListActivity.class);
                popularIntent.putExtra("type", Constants.POPULAR_COMMUNITY);
                startActivity(popularIntent);
                break;
            
            default:
                break;
        }
    }
    
    /**
     * 
     * <展示广场内容>
     * <功能详细描述>
     * @param communitySquareResponse
     * @see [类、类#方法、类#成员]
     */
    private void showSquare(final CommunitySquareResponse communitySquareResponse)
    {
        ORTopicAndGroup.removeAllViews();
        //官方推荐布局
        if (communitySquareResponse.getOcArticleList() != null && communitySquareResponse.getOcArticleList().size() > 0)
        {
            officialRecommendation.setVisibility(View.VISIBLE);
            //官方话题布局
            if (GeneralUtils.isNotNullOrZeroSize(communitySquareResponse.getOcArticleList()))
            {
                for (int i = 0; i < communitySquareResponse.getOcArticleList().size(); i++)
                {
                    final int position = i;
                    LinearLayout communityDynamicTopicItem =
                        (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.community_square_topic_item,
                            null);
                    ORTopicAndGroup.addView(communityDynamicTopicItem);
                    ImageView image = (ImageView)communityDynamicTopicItem.findViewById(R.id.image);
                    TextView content = (TextView)communityDynamicTopicItem.findViewById(R.id.content);
                    TextView comeFrom = (TextView)communityDynamicTopicItem.findViewById(R.id.come_from);
                    ImageLoader.getInstance()
                        .displayImage(communitySquareResponse.getOcArticleList().get(i).getImage(),
                            image,
                            JRApplication.setAllDisplayImageOptions(getActivity(),
                                "community_default_square",
                                "community_default_square",
                                "community_default_square"));
                    content.setText(communitySquareResponse.getOcArticleList().get(i).getContent());
                    comeFrom.setText(communitySquareResponse.getOcArticleList().get(i).getFrom());
                    communityDynamicTopicItem.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(getActivity(), CommunityTopicDetailsActivity.class);
                            intent.putExtra("id", communitySquareResponse.getOcArticleList().get(position).getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }
            }
            //官方社区
            LinearLayout communityDynamicGroup = new LinearLayout(getActivity());
            communityDynamicGroup.setPadding(DisplayUtil.dip2px(getActivity(), 15),
                DisplayUtil.dip2px(getActivity(), 10),
                DisplayUtil.dip2px(getActivity(), 15),
                DisplayUtil.dip2px(getActivity(), 15));
            ORTopicAndGroup.addView(communityDynamicGroup);
            for (int i = 0; i < communitySquareResponse.getOcList().size(); i++)
            {
                final int position = i;
                LinearLayout communityDynamicGroupItem =
                    (LinearLayout)LayoutInflater.from(getActivity())
                        .inflate(R.layout.community_square_group_item, null);
                communityDynamicGroup.addView(communityDynamicGroupItem);
                ImageView groupImage = (ImageView)communityDynamicGroupItem.findViewById(R.id.group_image);
                TextView groupName = (TextView)communityDynamicGroupItem.findViewById(R.id.group_name);
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)groupImage.getLayoutParams(); //取控件textView当前的布局参数  
                linearParams.width =
                    (getActivity().getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(getActivity(),
                        80)) / 4;
                linearParams.height = linearParams.width;
                groupImage.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                ImageLoader.getInstance().displayImage(communitySquareResponse.getOcList().get(i).getIcon(),
                    groupImage,
                    JRApplication.setRoundDisplayImageOptions(getActivity(),
                        "community_default_circle",
                        "community_default_circle",
                        "community_default_circle"));
                groupName.setText(communitySquareResponse.getOcList().get(i).getName());
                if (i != 3)
                {
                    LinearLayout.LayoutParams officialParams =
                        (LinearLayout.LayoutParams)communityDynamicGroupItem.getLayoutParams();
                    officialParams.setMargins(0, 0, DisplayUtil.dip2px(getActivity(), 16), 0);
                    communityDynamicGroupItem.setLayoutParams(officialParams);
                }
                communityDynamicGroupItem.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getActivity(), CommunityGroupDetailActivity.class);
                        intent.putExtra("circleId", communitySquareResponse.getOcList().get(position).getId());
                        intent.putExtra("circleName", communitySquareResponse.getOcList().get(position).getName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
            ImageView line = new ImageView(getActivity());
            ORTopicAndGroup.addView(line, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            line.setBackgroundResource(R.drawable.line_grey);
        }
        else
        {
            officialRecommendation.setVisibility(View.GONE);
        }
        //热门社区布局
        PCTopicAndGroup.removeAllViews();
        if (communitySquareResponse.getHcList() != null && communitySquareResponse.getHcList().size() > 0)
        {
            popularCommunity.setVisibility(View.VISIBLE);
            //热门话题布局
            if (GeneralUtils.isNotNullOrZeroSize(communitySquareResponse.getHcArticleList()))
            {
                for (int i = 0; i < communitySquareResponse.getHcArticleList().size(); i++)
                {
                    final int position = i;
                    LinearLayout communityDynamicTopicItem =
                        (LinearLayout)LayoutInflater.from(getActivity()).inflate(R.layout.community_square_topic_item,
                            null);
                    PCTopicAndGroup.addView(communityDynamicTopicItem);
                    ImageView image = (ImageView)communityDynamicTopicItem.findViewById(R.id.image);
                    TextView content = (TextView)communityDynamicTopicItem.findViewById(R.id.content);
                    TextView comeFrom = (TextView)communityDynamicTopicItem.findViewById(R.id.come_from);
                    ImageLoader.getInstance()
                        .displayImage(communitySquareResponse.getHcArticleList().get(i).getImage(),
                            image,
                            JRApplication.setAllDisplayImageOptions(getActivity(),
                                "community_default_square",
                                "community_default_square",
                                "community_default_square"));
                    content.setText(communitySquareResponse.getHcArticleList().get(i).getContent());
                    comeFrom.setText(communitySquareResponse.getHcArticleList().get(i).getFrom());
                    communityDynamicTopicItem.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(getActivity(), CommunityTopicDetailsActivity.class);
                            intent.putExtra("id", communitySquareResponse.getHcArticleList().get(position).getId());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }
            }
            //热门社区
            LinearLayout communityPopularGroup = new LinearLayout(getActivity());
            communityPopularGroup.setPadding(DisplayUtil.dip2px(getActivity(), 15),
                DisplayUtil.dip2px(getActivity(), 10),
                DisplayUtil.dip2px(getActivity(), 15),
                DisplayUtil.dip2px(getActivity(), 15));
            PCTopicAndGroup.addView(communityPopularGroup);
            for (int i = 0; i < communitySquareResponse.getHcList().size(); i++)
            {
                final int position = i;
                LinearLayout communityDynamicGroupItem =
                    (LinearLayout)LayoutInflater.from(getActivity())
                        .inflate(R.layout.community_square_group_item, null);
                communityPopularGroup.addView(communityDynamicGroupItem);
                ImageView groupImage = (ImageView)communityDynamicGroupItem.findViewById(R.id.group_image);
                TextView groupName = (TextView)communityDynamicGroupItem.findViewById(R.id.group_name);
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)groupImage.getLayoutParams(); //取控件textView当前的布局参数  
                linearParams.width =
                    (getActivity().getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(getActivity(),
                        80)) / 4;
                linearParams.height = linearParams.width;
                groupImage.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                ImageLoader.getInstance().displayImage(communitySquareResponse.getHcList().get(i).getIcon(),
                    groupImage,
                    JRApplication.setRoundDisplayImageOptions(getActivity(),
                        "community_default_circle",
                        "community_default_circle",
                        "community_default_circle"));
                groupName.setText(communitySquareResponse.getHcList().get(i).getName());
                if (i != 3)
                {
                    LinearLayout.LayoutParams officialParams =
                        (LinearLayout.LayoutParams)communityDynamicGroupItem.getLayoutParams();
                    officialParams.setMargins(0, 0, DisplayUtil.dip2px(getActivity(), 16), 0);
                    communityDynamicGroupItem.setLayoutParams(officialParams);
                }
                communityDynamicGroupItem.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getActivity(), CommunityGroupDetailActivity.class);
                        intent.putExtra("circleId", communitySquareResponse.getHcList().get(position).getId());
                        intent.putExtra("circleName", communitySquareResponse.getHcList().get(position).getName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
            ImageView line = new ImageView(getActivity());
            PCTopicAndGroup.addView(line, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            line.setBackgroundResource(R.drawable.line_grey);
        }
        else
        {
            popularCommunity.setVisibility(View.GONE);
        }
        
    }
    
    @Override
    public void netBack(Object ob)
    {
        /**
         * 广场查询
         */
        if (ob instanceof CommunitySquareResponse)
        {
            loadingLayout.setVisibility(View.GONE);
            mPullToRefreshView.onHeaderRefreshComplete();
            CommunitySquareResponse communitySquareResponse = (CommunitySquareResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(communitySquareResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(communitySquareResponse.getRetcode()))
                {
                    mPullToRefreshView.setVisibility(View.VISIBLE);
                    isFirstLoading = false;
                    showSquare(communitySquareResponse);
                }
                else
                {
                    if (isFirstLoading)
                    {
                        loadingFailedLayout.setVisibility(View.VISIBLE);
                        mPullToRefreshView.setVisibility(View.GONE);
                        errorMessage.setText(communitySquareResponse.getRetinfo());
                    }
                    else
                    {
                        ToastUtil.makeText(getActivity(), communitySquareResponse.getRetinfo());
                    }
                }
            }
            else
            {
                if (isFirstLoading)
                {
                    loadingFailedLayout.setVisibility(View.VISIBLE);
                    mPullToRefreshView.setVisibility(View.GONE);
                    errorMessage.setText(Constants.ERROR_MESSAGE);
                }
                else
                {
                    ToastUtil.showError(getActivity());
                }
                
            }
        }
    }
    
    /**
     * 
     * <切换小区成功刷新>
     * <功能详细描述>
     * 
     * @author  cyf
     * @version  [版本号, 2014-12-5]
     * @see  [相关类/方法]
     * @since  [产品/模块版本]
     */
    class CellBroard extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //登录成功
            if (Constants.LOGIN_SUCCESS_BROADCAST.equals(intent.getAction())
                || Constants.SELECT_NEW_COMMUNITY.equals(intent.getAction()))
            {
                queryCommunitySquare();
            }
        }
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(cellBroard);
    }
}
