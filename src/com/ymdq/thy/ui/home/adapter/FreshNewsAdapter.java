package com.ymdq.thy.ui.home.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Image;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.ui.ViewPagerActivity;
import com.ymdq.thy.ui.community.CommunityTopicDetailsActivity;
import com.ymdq.thy.ui.community.MoveTopicActivity;
import com.ymdq.thy.ui.community.ReportActivity;
import com.ymdq.thy.ui.community.service.CommunityService;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.view.MyGridView;

public class FreshNewsAdapter extends BaseAdapter
{
    private Context context;
    
    private List<Topic> mList;
    
    /**
     *    图片放大动画
     */
    private Animation toLargeAnimation;
    
    private UICallBack callBack;
    
    private ArrayList<String> photoUrls;
    
    public FreshNewsAdapter(Context context, List<Topic> mList, UICallBack callBack)
    {
        this.context = context;
        this.mList = mList;
        this.callBack = callBack;
    }
    
    @Override
    public int getCount()
    {
        return mList == null ? 0 : mList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return mList == null ? null : mList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final Topic entity = mList.get(position);
        HolderView mHolder;
        if (convertView == null)
        {
            mHolder = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.fresh_news_listview_item, null);
            mHolder.comment = (LinearLayout)convertView.findViewById(R.id.comment);
            mHolder.nickName = (TextView)convertView.findViewById(R.id.nick_name);
            mHolder.groupName = (TextView)convertView.findViewById(R.id.group_name);
            mHolder.content = (TextView)convertView.findViewById(R.id.content);
            mHolder.imgFirst = (ImageView)convertView.findViewById(R.id.imgFirst);
            //mHolder.imgGridView = (MyGridView)convertView.findViewById(R.id.photo_gridview);
            mHolder.time = (TextView)convertView.findViewById(R.id.time);
            mHolder.likeLayout = (LinearLayout)convertView.findViewById(R.id.like_layout);
            mHolder.likeImg = (ImageView)convertView.findViewById(R.id.like_img);
            mHolder.like = (TextView)convertView.findViewById(R.id.like);
            mHolder.chat = (TextView)convertView.findViewById(R.id.community_chat);
            mHolder.moreLayout = (LinearLayout)convertView.findViewById(R.id.more_layout);
            mHolder.imgLayout = (RelativeLayout)convertView.findViewById(R.id.imgLayout);
            
            convertView.setTag(mHolder);
        }
        else
        {
            mHolder = (HolderView)convertView.getTag();
        }
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getNickName()))
        {
            mHolder.nickName.setText(entity.getNickName());
        }
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getName()))
        {
            mHolder.groupName.setText(entity.getName());
        }
        mHolder.content.setMovementMethod(null);
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getContent()))
        {
            mHolder.content.setText(entity.getContent());
            mHolder.content.setText(GeneralUtils.clickSpan(mHolder.content,
                context,
                entity.getContent(),
                GeneralUtils.getWebUrl(entity.getContent())),
                BufferType.SPANNABLE);
        }
        if (GeneralUtils.isNotNullOrZeroLenght(entity.getTime()))
        {
            mHolder.time.setText(entity.getTime());
        }
        
        if (entity.getImageList() != null && entity.getImageList().size() > 0)
        {
            mHolder.imgLayout.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(entity.getImageList().get(0).getImageUrlS(),
                mHolder.imgFirst,
                JRApplication.setAllDisplayImageOptions(context, "community_default", "community_default",
                    "community_default"));
            
            mHolder.imgFirst.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View arg0)
                {
                    photoUrls =  new ArrayList<String>();
                    for (Image image : entity.getImageList())
                    {
                        photoUrls.add(image.getImageUrlL());
                    }
                    Intent intent = new Intent(context, ViewPagerActivity.class);
                    intent.putExtra("currentItem", 0);
                    intent.putStringArrayListExtra("photoUrls", photoUrls);
                    context.startActivity(intent);
                }
            });
        }
        else
        {
            mHolder.imgLayout.setVisibility(View.GONE);
        }
        //            mHolder.imgGridView.setVisibility(View.VISIBLE);
        //            mHolder.imgGridView.setAdapter(new PhotoAdapter(context, entity.getImageList(), 80));
        //        }
        //        else
        //        {
        //            mHolder.imgGridView.setVisibility(View.GONE);
        //        }
        mHolder.like.setText(entity.getPraiseNum());
        mHolder.chat.setText(entity.getComment());
        //未赞
        if (Constants.CANCEL_PRAISE.equals(entity.getFlag()))
        {
            mHolder.likeImg.setImageResource(R.drawable.btn_zan);
        }
        else
        {
            mHolder.likeImg.setImageResource(R.drawable.btn_zan_press);
        }
        
        convertView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                Intent topicIntent = new Intent(context, CommunityTopicDetailsActivity.class);
                topicIntent.putExtra("id", entity.getArticleId());
                context.startActivity(topicIntent);
            }
        });
        //响应更多按钮点击事件
        mHolder.moreLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //                Intent i = new Intent(context,ShareDiaActivity.class);
                //                i.putExtra("symbol", 2);
                //                i.putExtra("topic", entity);
                //                i.putExtra("hide_top", true);
                //                i.putExtra("hide_delete", true);
                //                ((Activity)context).startActivity(i);
                DialogUtil.communityEditDiaLog(context, null, null, entity, null, null, new DialogCallBack()
                {
                    
                    @Override
                    public void dialogBack()
                    {
                        if (Global.isLogin())
                        {
                            if (Global.isSuper())
                            {
                                Intent i = new Intent(context, MoveTopicActivity.class);
                                i.putExtra("from_id", entity.getId());
                                i.putExtra("article_id", entity.getArticleId());
                                ((Activity)context).startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(context, "您无权限移动话题", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            DialogUtil.TwoButtonDialogGTLogin(context);
                        }
                    }
                }, new DialogCallBack()
                {
                    
                    @Override
                    public void dialogBack()
                    {
                        if (Global.isLogin())
                        {
                            Intent intent = new Intent(context, ReportActivity.class);
                            intent.putExtra("article_id", entity.getArticleId());
                            ((Activity)context).startActivity(intent);
                        }
                        else
                        {
                            DialogUtil.TwoButtonDialogGTLogin(context);
                        }
                    }
                });
            }
        });
        //响应点赞按钮
        mHolder.likeLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Global.isLogin())
                {
                    if (Constants.CANCEL_PRAISE.equals(entity.getFlag()))
                    {
                        entity.setFlag(Constants.PRAISE);
                        entity.setPraiseNum((Integer.parseInt(entity.getPraiseNum()) + 1) + "");
                    }
                    else
                    {
                        entity.setFlag(Constants.CANCEL_PRAISE);
                        entity.setPraiseNum((Integer.parseInt(entity.getPraiseNum()) - 1) + "");
                    }
                    notifyDataSetChanged();
//                    showAnimation((ImageView)v, position);
                    showAnimation((ImageView)((LinearLayout)v).getChildAt(0), position);
                    CommunityService.instance().addOrCancelPraise(entity.getArticleId(),
                        entity.getFlag(),
                        context,
                        callBack);
                }
                else
                {
                    DialogUtil.TwoButtonDialogGTLogin(context);
                }
            }
        });
        //响应评论按钮点击事件
        mHolder.comment.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, CommunityTopicDetailsActivity.class);
                intent.putExtra("type", Constants.NUM1);
                intent.putExtra("id", entity.getArticleId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    
    /**
     * 
     * <点赞动画效果>
     * <功能详细描述>
     * @param imageView
     * @param position
     * @see [类、类#方法、类#成员]
     */
    private void showAnimation(final ImageView imageView, final int position)
    {
        toLargeAnimation = AnimationUtils.loadAnimation(context, R.anim.group_to_large);
        toLargeAnimation.setAnimationListener(new AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation arg0)
            {
                
            }
            
            @Override
            public void onAnimationRepeat(Animation arg0)
            {
                
            }
            
            @Override
            public void onAnimationEnd(Animation arg0)
            {
                if (Constants.CANCEL_PRAISE.equals(mList.get(position).getFlag()))
                {
                    imageView.setImageResource(R.drawable.btn_zan);
                    imageView.clearAnimation();
                }
                else
                {
                    imageView.setImageResource(R.drawable.btn_zan_press);
                    imageView.clearAnimation();
                }
            }
        });
        imageView.startAnimation(toLargeAnimation);
    }
    
    class HolderView
    {
        ImageView imgFirst;
        
        //用户昵称
        TextView nickName;
        
        //发布时间
        TextView time;
        
        //社区名称
        TextView groupName;
        
        //话题的内容
        TextView content;
        
        //图片列表
        MyGridView imgGridView;
        
        LinearLayout likeLayout;
        
        ImageView likeImg;
        
        TextView like;
        
        LinearLayout comment;
        
        TextView chat;
        
        LinearLayout moreLayout;
        
        RelativeLayout imgLayout;
    }
}
