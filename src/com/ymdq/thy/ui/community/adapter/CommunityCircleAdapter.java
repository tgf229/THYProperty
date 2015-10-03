package com.ymdq.thy.ui.community.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Group;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.community.CommunityGroupDetailActivity;
import com.ymdq.thy.ui.community.service.CommunityService;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;

/**
 * 
 * <社区列表适配器>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityCircleAdapter extends BaseAdapter
{
    private List<Group> data;
    
    private Context context;
    
    private String type;
    
    /**
     * 是否展示删除
     */
    private boolean isSilding;
    
    /**
     * 有删除按钮的位置
     */
    private int lastPosition = -1;
    
    private UICallBack callBack;
    
    /**
     * 加载框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 删除的位置
     */
    public int deletePosition;
    
    public CommunityCircleAdapter(List<Group> data, Context context, String type, UICallBack callBack)
    {
        super();
        this.data = data;
        this.context = context;
        this.type = type;
        this.callBack = callBack;
    }
    
    public CommunityCircleAdapter(List<Group> data, Context context, String type)
    {
        super();
        this.data = data;
        this.context = context;
        this.type = type;
    }
    
    @Override
    public int getCount()
    {
        return data == null ? 0 : data.size();
    }
    
    @Override
    public Object getItem(int arg0)
    {
        return null;
    }
    
    @Override
    public long getItemId(int arg0)
    {
        return 0;
    }
    
    /**
     * 
     * <设置加载框>
     * <功能详细描述>
     * @param dailog
     * @see [类、类#方法、类#成员]
     */
    public NetLoadingDailog getDailog()
    {
        return dailog;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.community_circle_item, null);
            viewHolder.headImage = (ImageView)convertView.findViewById(R.id.head_image);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.userCount = (TextView)convertView.findViewById(R.id.user_count);
            viewHolder.articleCount = (TextView)convertView.findViewById(R.id.article_count);
            viewHolder.desc = (TextView)convertView.findViewById(R.id.desc);
            viewHolder.timeOrNotice = (TextView)convertView.findViewById(R.id.time_or_notice);
            viewHolder.lineAll = (ImageView)convertView.findViewById(R.id.line_all);
            viewHolder.lineToRight = (ImageView)convertView.findViewById(R.id.line_to_right);
            viewHolder.contentLayout = (LinearLayout)convertView.findViewById(R.id.content_layout);
            viewHolder.deleteLayout = (TextView)convertView.findViewById(R.id.delete_layout);
            viewHolder.hsv = (HorizontalScrollView)convertView.findViewById(R.id.hsv);
            LayoutParams lp = viewHolder.contentLayout.getLayoutParams();
            lp.width = context.getResources().getDisplayMetrics().widthPixels;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //我管理的社区支持删除-左滑
        if (Constants.MY_MANAGED_COMMUNITY.equals(type))
        {
            viewHolder.deleteLayout.setVisibility(View.VISIBLE);
            viewHolder.hsv.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    //判断当前删除是否存在
                    if (isSilding)
                    {
                        //判断不是当前item情况下删除就收起来
                        if (position != lastPosition)
                        {
                            notifyDataSetChanged();
                            isSilding = false;
                            return false;
                        }
                    }
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_UP:
                            //获得ViewHolder  
                            //获得HorizontalScrollView滑动的水平方向值.  
                            int scrollX = viewHolder.hsv.getScrollX();
                            //获得操作区域的长度  
                            int actionW = viewHolder.deleteLayout.getWidth();
                            //注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬  
                            //如果水平方向的移动值<操作区域的长度的一半,就复原  
                            if (scrollX < actionW / 2)
                            {
                                
                                viewHolder.hsv.smoothScrollTo(0, 0);
                                isSilding = false;
                            }
                            else
                            //否则的话显示操作区域  
                            {
                                viewHolder.hsv.smoothScrollTo(actionW, 0);
                                isSilding = true;
                                lastPosition = position;
                            }
                            
                            return true;
                    }
                    return false;
                }
            });
            //这里防止删除一条item后,ListView处于操作状态,直接还原  
            if (viewHolder.hsv.getScrollX() != 0)
            {
                viewHolder.hsv.scrollTo(0, 0);
            }
        }
        else
        {
            viewHolder.deleteLayout.setVisibility(View.GONE);
        }
        ImageLoader.getInstance().displayImage(data.get(position).getIcon(),
            viewHolder.headImage,
            JRApplication.setAllDisplayImageOptions(context,
                "community_default",
                "community_default",
                "community_default"));
        viewHolder.name.setText(data.get(position).getName());
        viewHolder.userCount.setText(data.get(position).getUserCount());
        viewHolder.articleCount.setText(data.get(position).getArticleCount());
        if (data.size() - 1 == position)
        {
            viewHolder.lineToRight.setVisibility(View.GONE);
            viewHolder.lineAll.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.lineToRight.setVisibility(View.VISIBLE);
            viewHolder.lineAll.setVisibility(View.GONE);
        }
        //我管理的社区
        if (Constants.MY_MANAGED_COMMUNITY.equals(type) || Constants.HE_MANAGED_COMMUNITY.equals(type))
        {
            viewHolder.desc.setText(GeneralUtils.splitdateToGroup(data.get(position).getTime()));
            viewHolder.timeOrNotice.setText("创建时间");
        }
        else
        {
            viewHolder.desc.setText(data.get(position).getDesc());
            viewHolder.timeOrNotice.setText("公告:");
        }
        viewHolder.contentLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!isSilding)
                {
                    Intent intent = new Intent(context, CommunityGroupDetailActivity.class);
                    intent.putExtra("circleId", data.get(position).getId());
                    intent.putExtra("circleName", data.get(position).getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ((Activity)context).startActivityForResult(intent, Constants.NUM3);
                }
                else
                {
                    isSilding = false;
                    notifyDataSetChanged();
                }
            }
        });
        viewHolder.deleteLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogUtil.showTwoButtonDialog(context, "是否确认删除圈子?", new DialogCallBack()
                {
                    @Override
                    public void dialogBack()
                    {
                        isSilding = false;
                        dailog = new NetLoadingDailog(context);
                        dailog.loading();
                        deletePosition = position;
                        CommunityService.instance().deleteGroup(data.get(position).getId(), context, callBack);
                    }
                });
            }
        });
        
        return convertView;
    }
    
    class ViewHolder
    {
        private ImageView headImage;
        
        private TextView name;
        
        private TextView userCount;
        
        private TextView articleCount;
        
        private TextView desc;
        
        private TextView timeOrNotice;
        
        private ImageView lineToRight;
        
        private ImageView lineAll;
        
        private LinearLayout contentLayout;
        
        private TextView deleteLayout;
        
        private HorizontalScrollView hsv;
    }
}
