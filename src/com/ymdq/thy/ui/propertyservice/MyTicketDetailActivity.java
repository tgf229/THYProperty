package com.ymdq.thy.ui.propertyservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Image;
import com.ymdq.thy.bean.home.ImageList;
import com.ymdq.thy.bean.propertyservice.MyTicketDetailPath;
import com.ymdq.thy.bean.propertyservice.MyTicketDetailResponse;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.ViewPagerActivity;
import com.ymdq.thy.ui.home.adapter.PhotoAdapter;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.SecurityUtils;
import com.ymdq.thy.view.GifView;
import com.ymdq.thy.view.MyGridView;

public class MyTicketDetailActivity extends BaseActivity implements OnClickListener, UICallBack
{
    /**
     * 工单id
     */
    private String id;
    
    private LinearLayout loadingLayout;
    
    private LinearLayout clickrefreshLayout;
    
    private TextView clickTextView;
    
    ImageView selfImg;
    
    TextView selfName;
    
    TextView selfTime;
    
    TextView selfContent;
    
    //ImageView type;
    
    private ListView mListView;
    
    private List<MyTicketDetailPath> mList;
    
    private MyTicketDetailAdapter adapter;
    
    private ImageView headImg;
    
    private TextView headName;
    
    private TextView selfOrServer;
    
    private TextView headContent;
    
    private TextView headDesc;
    
    private TextView headTime;
    
    private Button headStep;
    
    private MyGridView headGridView;
    
    private LinearLayout evaluateLayout;
    
    private Button evaluateBtn;
    
    private MyGridView myGridView;
    
    private NetLoadingDailog loadingDialog;
    
    private GifView gif1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_my_ticket_detail);
        id = getIntent().getStringExtra("id");
        initView();
        reqTicketList();
        loadingDialog = new NetLoadingDailog(this);
    }
    
    private void initView()
    {
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setBackgroundResource(R.drawable.title_gongdanxiangqing);
        
        loadingLayout = (LinearLayout)findViewById(R.id.loading_layout);
        gif1 = (GifView)loadingLayout.findViewById(R.id.gif1);
        // 设置背景gif图片资源  
        gif1.setMovieResource(R.raw.jiazai_gif);
        loadingLayout.setVisibility(View.VISIBLE);
        
        clickrefreshLayout = (LinearLayout)findViewById(R.id.click_refresh_layout);
        clickTextView = (TextView)clickrefreshLayout.findViewById(R.id.loading_failed_txt);
        clickrefreshLayout.setVisibility(View.GONE);
        //        clickrefreshLayout.setOnClickListener(this);
        
        //我的布局
        selfImg = (ImageView)findViewById(R.id.self_img);
        selfName = (TextView)findViewById(R.id.self_name);
        selfTime = (TextView)findViewById(R.id.self_time);
        selfContent = (TextView)findViewById(R.id.self_content);
        //type = (ImageView)findViewById(R.id.type);
        
        mListView = (ListView)findViewById(R.id.detail_list_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.property_my_ticket_detail_head_listview, null);
        mListView.addHeaderView(headView);
        mList = new ArrayList<MyTicketDetailPath>();
        adapter = new MyTicketDetailAdapter(this, mList);
        mListView.setAdapter(adapter);
        mListView.setFocusable(false);
        
        headImg = (ImageView)headView.findViewById(R.id.head_img);
        headName = (TextView)headView.findViewById(R.id.head_name);
        selfOrServer = (TextView)headView.findViewById(R.id.self_or_server);
        // selfOrServer.setVisibility(View.GONE);
        headDesc = (TextView)headView.findViewById(R.id.head_desc);
        headContent = (TextView)headView.findViewById(R.id.head_content);
        headTime = (TextView)headView.findViewById(R.id.head_time);
        headStep = (Button)headView.findViewById(R.id.head_step);
        headGridView = (MyGridView)headView.findViewById(R.id.photo_gridview);
        
        myGridView = (MyGridView)findViewById(R.id.photo_gridview);
        evaluateLayout = (LinearLayout)findViewById(R.id.evaluate_layout);
        evaluateBtn = (Button)findViewById(R.id.evaluate_btn);
        //        evaluateBtn.setOnClickListener(this);
    }
    
    /**
     * 
     * <工单详情查询>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void reqTicketList()
    {
        Map<String, String> param = new HashMap<String, String>();
        try
        {
            param.put("id", SecurityUtils.encode2Str(id));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ConnectService.instance().connectServiceReturnResponse(this,
            param,
            this,
            MyTicketDetailResponse.class,
            URLUtil.MY_TICKET_DETAIL,
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
            //                reqTicketList();
            //                break;
            //评价
            case R.id.evaluate_btn:
                Intent eval = new Intent();
                eval.setClass(this, EvaluationActivity.class);
                eval.putExtra("id", id);
                startActivityForResult(eval, Constants.NUM0);
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.NUM0 && resultCode == RESULT_OK)
        {
            reqTicketList();
            loadingDialog.loading();
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        gif1.setPaused(true);
        loadingLayout.setVisibility(View.GONE);
        if (ob != null)
        {
            if (ob instanceof MyTicketDetailResponse)
            {
                loadingDialog.dismissDialog();
                MyTicketDetailResponse resp = (MyTicketDetailResponse)ob;
                if (GeneralUtils.isNotNullOrZeroLenght(resp.getRetcode()))
                {
                    if (Constants.SUCESS_CODE.equals(resp.getRetcode()))
                    {
                        initSelfData(resp);
                        List<MyTicketDetailPath> path = resp.getPath();
                        if (path != null && path.size() > 0)
                        {
                            mListView.setVisibility(View.VISIBLE);
                            initHeadView(path.get(0), path.size());
                            if (path.size() > 1)
                            {
                                path.remove(0);
                                adapter.setSelfName(selfName.getText().toString());
                                mList.clear();
                                mList.addAll(path);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            mListView.setVisibility(View.GONE);
                        }
                        return;
                    }
                }
                clickrefreshLayout.setVisibility(View.VISIBLE);
                clickTextView.setText(Constants.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 
     * <我的工单的头部信息>
     * <功能详细描述>
     * @param entity
     * @see [类、类#方法、类#成员]
     */
    private void initSelfData(MyTicketDetailResponse entity)
    {
        ImageLoader.getInstance().displayImage(entity.getImage(),
            selfImg,
            JRApplication.setRoundDisplayImageOptions(this,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getuName()))
        {
            selfName.setText(entity.getuName());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getTime()))
        {
            selfTime.setText(GeneralUtils.splitToSecond(entity.getTime()));
        }
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getContent()))
        {
            String content = String.format(getResources().getString(R.string.my_ticket_content), entity.getContent());
            selfContent.setText("工单描述：" + content);
        }
        //        if(GeneralUtils.isNotNullOrZeroLenght(entity.getType()))
        //        {
        //            if(Constants.PROPERTY_REPAIR.equals(entity.getType()))
        //            {
        //                type.setImageResource(R.drawable.service_icon_angle_repair);
        //            }
        //            else if(Constants.PROPERTY_COMPLAINT.equals(entity.getType()))
        //            {
        //                type.setImageResource(R.drawable.service_icon_angle_complaint);
        //            }
        //            else if(Constants.PROPERTY_PRAISE.equals(entity.getType()))
        //            {
        //                type.setImageResource(R.drawable.service_icon_angle_thank);
        //            }
        //            else if(Constants.PROPERTY_HELP.equals(entity.getType()))
        //            {
        //                type.setImageResource(R.drawable.service_icon_angle_help);
        //            }
        //            else if(Constants.PROPERTY_SUGGEST.equals(entity.getType()))
        //            {
        //                type.setImageResource(R.drawable.service_icon_angle_suggest);
        //            }
        //        }
        List<ImageList> imgList = entity.getImageList();
        if (imgList != null && imgList.size() > 0)
        {
            myGridView.setVisibility(View.VISIBLE);
            myGridView.setAdapter(new HeadPhotoAdapter(this, imgList, 44));
        }
        else
        {
            myGridView.setVisibility(View.GONE);
        }
        
        if ("3".equals(entity.getStatus()))
        {
            evaluateLayout.setVisibility(View.VISIBLE);
            evaluateBtn.setOnClickListener(this);
        }
        else
        {
            evaluateLayout.setVisibility(View.GONE);
        }
        
    }
    
    /**
     * 
     * <处理流程>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initHeadView(MyTicketDetailPath path, int length)
    {
        ImageLoader.getInstance().displayImage(path.getuImageUrl(),
            headImg,
            JRApplication.setRoundDisplayImageOptions(this,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        if (GeneralUtils.isNotNullOrZeroLenght(path.getuName()))
        {
            headName.setText(path.getuName());
        }
        //        if(GeneralUtils.isNotNullOrZeroLenght(path.getuName()))
        //        {
        //            if(path.getuName().equals(selfName.getText().toString()))
        //            {
        //                selfOrServer.setVisibility(View.INVISIBLE);
        //            }
        //            else
        //            {
        //                selfOrServer.setVisibility(View.VISIBLE);
        //                selfOrServer.setText("客服");
        //            }
        //        }
        if (GeneralUtils.isNotNullOrZeroLenght(path.getDesc()))
        {
            headDesc.setText(path.getDesc());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(path.getContent()))
        {
            headContent.setVisibility(View.VISIBLE);
            headContent.setText("描述：" + path.getContent());
        }
        else
        {
            headContent.setVisibility(View.GONE);
        }
        
        if (GeneralUtils.isNotNullOrZeroLenght(path.getTime()))
        {
            headTime.setText(path.getTime());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(path.getDepName()))
        {
            selfOrServer.setText(path.getDepName());
        }
        
        headStep.setText("STEP" + length);
        
        List<Image> imgList = path.getImageList();
        if (imgList != null && imgList.size() > 0)
        {
            headGridView.setVisibility(View.VISIBLE);
            headGridView.setAdapter(new PhotoAdapter(this, imgList, 85));
        }
        else
        {
            headGridView.setVisibility(View.GONE);
        }
    }
    
    class HeadPhotoAdapter extends BaseAdapter
    {
        private Context context;
        
        protected List<ImageList> photos;
        
        private ArrayList<String> photoUrls;
        
        private int width;
        
        public HeadPhotoAdapter(Context context, List<ImageList> photos, int width)//, String channel)
        {
            this.context = context;
            this.photos = photos;
            photoUrls = new ArrayList<String>();
            this.width = width;
            if (photos != null)
            {
                for (ImageList image : photos)
                {
                    photoUrls.add(image.getImageUrlL());
                }
            }
            
        }
        
        @Override
        public int getCount()
        {
            return photos == null ? 0 : photos.size();
        }
        
        @Override
        public Object getItem(int position)
        {
            return photos.get(position);
        }
        
        @Override
        public long getItemId(int position)
        {
            return position;
        }
        
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            PhotoViewHolder photoViewHolder;
            if (null == convertView)
            {
                photoViewHolder = new PhotoViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.home_fresh_news_gridview_head, null);
                photoViewHolder.pic = (ImageView)convertView.findViewById(R.id.group_dynamic_photo);
                LinearLayout.LayoutParams linearParams =
                    (LinearLayout.LayoutParams)photoViewHolder.pic.getLayoutParams(); //取控件textView当前的布局参数  
                linearParams.width =
                    (context.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(context, width)) / 3;// 控件的高强制设成20
                linearParams.height = linearParams.width;
                photoViewHolder.pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                convertView.setTag(photoViewHolder);
            }
            else
            {
                photoViewHolder = (PhotoViewHolder)convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(photos.get(position).getImageUrlS(),
                photoViewHolder.pic,
                JRApplication.setAllDisplayImageOptions(context,
                    "community_default",
                    "community_default",
                    "community_default"));
            photoViewHolder.pic.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    Intent intent = new Intent(context, ViewPagerActivity.class);
                    intent.putExtra("currentItem", position);
                    intent.putStringArrayListExtra("photoUrls", photoUrls);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
        
        class PhotoViewHolder
        {
            private ImageView pic;
        }
        
    }
    
}
