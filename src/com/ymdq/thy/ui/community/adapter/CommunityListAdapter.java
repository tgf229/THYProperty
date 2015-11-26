package com.ymdq.thy.ui.community.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
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
import com.ymdq.thy.ui.community.CommunityPersonDetailActivity;
import com.ymdq.thy.ui.community.CommunityTopicDetailsActivity;
import com.ymdq.thy.ui.community.MoveTopicActivity;
import com.ymdq.thy.ui.community.ReportActivity;
import com.ymdq.thy.ui.community.service.CommunityService;
import com.ymdq.thy.util.DialogUtil;
import com.ymdq.thy.util.DisplayUtil;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.ToastUtil;

/**
 * 
 * <社区动态适配器>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommunityListAdapter extends BaseAdapter
{
    
    private List<Topic> data;
    
    private Context context;
    
    private UICallBack callBack;
    
    /**
     * 布局类型
     */
    private String type;
    
    /**
     * 创建社区id
     */
    private String userId;
    
    /**
     *    图片放大动画
     */
    private Animation toLargeAnimation;
    
    /**
     * 删除的位置
     */
    public int deletePosition;
    
    private NetLoadingDailog dailog;
    
    private ArrayList<String> photoUrls;
    
    public CommunityListAdapter(List<Topic> data, Context context, UICallBack callBack)
    {
        super();
        this.data = data;
        this.context = context;
        this.callBack = callBack;
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
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final Topic entity = data.get(position);
        ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.community_list_item, null);
            viewHolder.headImage = (ImageView)convertView.findViewById(R.id.head_image);
            viewHolder.vip = (ImageView)convertView.findViewById(R.id.icon_vip);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.content = (TextView)convertView.findViewById(R.id.content);
            viewHolder.photo = (LinearLayout)convertView.findViewById(R.id.photo);
            
            viewHolder.isHot = (ImageView)convertView.findViewById(R.id.ishot_img);
            viewHolder.isVote = (ImageView)convertView.findViewById(R.id.isvote_img);
             
            viewHolder.likeLayout = (LinearLayout)convertView.findViewById(R.id.like_layout);
            viewHolder.likeImg = (ImageView)convertView.findViewById(R.id.like_img);
            viewHolder.likeNum = (TextView)convertView.findViewById(R.id.like_num);
            viewHolder.commentLayout = (LinearLayout)convertView.findViewById(R.id.comment_layout);
            viewHolder.commentNum = (TextView)convertView.findViewById(R.id.comment_num);
            viewHolder.moreLayout = (LinearLayout)convertView.findViewById(R.id.more_layout);
            
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(data.get(position).getImageUrl(),
            viewHolder.headImage,
            JRApplication.setRoundDisplayImageOptions(context,
                "default_head_pic_round",
                "default_head_pic_round",
                "default_head_pic_round"));
        if (Constants.LEVEL_VIP.equals(data.get(position).getUserLevel()))
        {
            viewHolder.vip.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.vip.setVisibility(View.GONE);
        }
        viewHolder.name.setText(data.get(position).getNickName());
        viewHolder.time.setText(data.get(position).getTime());
        //未赞
        if (Constants.CANCEL_PRAISE.equals(data.get(position).getFlag()))
        {
            viewHolder.likeImg.setImageResource(R.drawable.btn_zan);
        }
        else
        {
            viewHolder.likeImg.setImageResource(R.drawable.btn_zan_press);
        }
        //判断是否有内容
        viewHolder.content.setMovementMethod(null);
        viewHolder.content.setText(data.get(position).getContent());
        viewHolder.likeNum.setText(data.get(position).getPraiseNum());
        viewHolder.commentNum.setText(data.get(position).getCommentNum());
        
        if("1".equals(data.get(position).getIsHot()))
        {
            viewHolder.isHot.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.isHot.setVisibility(View.INVISIBLE);
        }
        if("2".equals(data.get(position).getType()) || "3".equals(data.get(position).getType()))
        {
            viewHolder.isVote.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.isVote.setVisibility(View.INVISIBLE);
        }
        
//        if (GeneralUtils.isNotNullOrZeroLenght(data.get(position).getComment()))
//        {
//            viewHolder.content.setVisibility(View.VISIBLE);
//            viewHolder.content.setText(GeneralUtils.clickSpan(viewHolder.content, context, data.get(position)
//                .getContent(), GeneralUtils.getWebUrl(data.get(position).getContent())), BufferType.SPANNABLE);
//        }
//        else
//        {
//            viewHolder.content.setVisibility(View.GONE);
//        }
//        if (entity.getImageList() != null && entity.getImageList().size() > 0)
//        {
//            viewHolder.imgLayout.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(entity.getImageList().get(0).getImageUrlS(),
//                viewHolder.imgFirst,
//                JRApplication.setAllDisplayImageOptions(context, "community_default", "community_default",
//                    "community_default"));
//            
//            viewHolder.imgFirst.setOnClickListener(new OnClickListener()
//            {
//                @Override
//                public void onClick(View arg0)
//                {
//                    photoUrls =  new ArrayList<String>();
//                    for (Image image : entity.getImageList())
//                    {
//                        photoUrls.add(image.getImageUrlL());
//                    }
//                    Intent intent = new Intent(context, ViewPagerActivity.class);
//                    intent.putExtra("currentItem", 0);
//                    intent.putStringArrayListExtra("photoUrls", photoUrls);
//                    context.startActivity(intent);
//                }
//            });
//        }
//        else
//        {
//            viewHolder.imgLayout.setVisibility(View.GONE);
//        }
        viewHolder.photo.removeAllViews();
        //判断是否有图片
        if (GeneralUtils.isNotNullOrZeroSize(data.get(position).getImageList()))
        {
            viewHolder.photo.setVisibility(View.VISIBLE);
            LinearLayout photoLineOne = null;
            LinearLayout photoLineTwo = null;
            final ArrayList<String> photoUrls = new ArrayList<String>();
            for (Image image : data.get(position).getImageList())
            {
                photoUrls.add(image.getImageUrlL());
            }
            for (int i = 0; i < data.get(position).getImageList().size(); i++)
            {
                final int currentItem = i;
                if (i == 0)
                {
                    photoLineOne = new LinearLayout(context);
                    photoLineOne.setPadding(0, 0, 0, DisplayUtil.dip2px(context, 7));
                    viewHolder.photo.addView(photoLineOne);
                }
                if (i == 3)
                {
                    photoLineTwo = new LinearLayout(context);
                    viewHolder.photo.addView(photoLineTwo);
                }
                ImageView pic = new ImageView(context);
                pic.setImageResource(R.drawable.community_default);
                pic.setScaleType(ScaleType.CENTER_CROP);
                if (i < 3)
                {
                    photoLineOne.addView(pic);
                }
                else
                {
                    photoLineTwo.addView(pic);
                }
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)pic.getLayoutParams(); //取控件textView当前的布局参数  
                linearParams.width =
                    (context.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(context, 44)) / 3;
                linearParams.height = linearParams.width;
                if (i != 2 || i != 5)
                    linearParams.setMargins(0, 0, DisplayUtil.dip2px(context, 7), 0);
                pic.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                ImageLoader.getInstance().displayImage(data.get(position).getImageList().get(i).getImageUrlS(),
                    pic,
                    JRApplication.setAllDisplayImageOptions(context,
                        "community_default",
                        "community_default",
                        "community_default"));
                pic.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        //普通话题
                        Intent intent = new Intent(context, ViewPagerActivity.class);
                        intent.putExtra("currentItem", currentItem);
                        intent.putStringArrayListExtra("photoUrls", photoUrls);
                        context.startActivity(intent);
                    }
                });
            }
        }
        else
        {
            viewHolder.photo.setVisibility(View.GONE);
        }
        //圈子详情界面,如果是本人的圈子,则显示置顶等操作
//        if (Constants.COMMUNITY_DETAIL.equals(type) || Constants.COMMUNITY_DETAIL_OWNER.equals(type))
//        {
//            viewHolder.comeText.setVisibility(View.GONE);
//            viewHolder.come.setVisibility(View.GONE);
//            viewHolder.admin.setVisibility(View.VISIBLE);
//            if (userId.equals(data.get(position).getuId()))
//            {
//                viewHolder.admin.setText("圈主");
//                viewHolder.admin.setBackgroundResource(R.drawable.community_quzhu_bg);
//            }
//            else
//            {
//                viewHolder.admin.setText("用户");
//                viewHolder.admin.setBackgroundResource(R.drawable.communiyt_user_bg);
//            }
//            if (Constants.ISTOP.equals(data.get(position).getIsTop()))
//            {
//                viewHolder.upToTop.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                viewHolder.upToTop.setVisibility(View.GONE);
//            }
//        }
        //是否有投票界面
//        if (Constants.TOPIC_TYPE_VOTE.equals(data.get(position).getType()))
//        {
//            viewHolder.vote.setVisibility(View.VISIBLE);
//            viewHolder.yesNum.setText(data.get(position).getYes());
//            viewHolder.noNum.setText(data.get(position).getNo());
//            if (Constants.NO_VOTE.equals(data.get(position).getVoteFlag()))
//            {
//                viewHolder.yes.setBackgroundResource(R.drawable.community_agree);
//                viewHolder.no.setBackgroundResource(R.drawable.community_disagree);
//            }
//            else
//            {
//                if (Constants.AGREE.equals(data.get(position).getVoteFlag()))
//                {
//                    viewHolder.yes.setBackgroundResource(R.drawable.community_agree_clicked);
//                    viewHolder.no.setBackgroundResource(R.drawable.community_disagree);
//                }
//                else
//                {
//                    viewHolder.yes.setBackgroundResource(R.drawable.community_agree);
//                    viewHolder.no.setBackgroundResource(R.drawable.community_disagree_clicked);
//                }
//            }
//        }
//        else
//        {
//            viewHolder.vote.setVisibility(View.GONE);
//        }
        //响应头像按钮点击事件
//        viewHolder.headImage.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (!Constants.COMMUNITU_PERSON_CENTER.equals(type))
//                {
//                    Intent intent = new Intent(context, CommunityPersonDetailActivity.class);
//                    intent.putExtra("queryUId", data.get(position).getuId());
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    context.startActivity(intent);
//                }
//            }
//        });
        //响应反对按钮事件
//        viewHolder.disagree.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (Global.isLogin())
//                {
//                    if (Constants.NO_VOTE.equals(data.get(position).getVoteFlag()))
//                    {
//                        data.get(position).setNo((Integer.parseInt(data.get(position).getNo()) + 1) + "");
//                        data.get(position).setVoteFlag(Constants.DISAGREE);
//                        notifyDataSetChanged();
//                        CommunityService.instance().agreeOrDisagree(data.get(position).getArticleId(),
//                            data.get(position).getVoteFlag(),
//                            context,
//                            callBack);
//                    }
//                }
//                else
//                {
//                    DialogUtil.TwoButtonDialogGTLogin(context);
//                }
//            }
//        });
        //响应赞成按钮点击
//        viewHolder.agree.setOnClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (Global.isLogin())
//                {
//                    if (Constants.NO_VOTE.equals(data.get(position).getVoteFlag()))
//                    {
//                        data.get(position).setYes((Integer.parseInt(data.get(position).getYes()) + 1) + "");
//                        data.get(position).setVoteFlag(Constants.AGREE);
//                        notifyDataSetChanged();
//                        CommunityService.instance().agreeOrDisagree(data.get(position).getArticleId(),
//                            data.get(position).getVoteFlag(),
//                            context,
//                            callBack);
//                    }
//                }
//                else
//                {
//                    DialogUtil.TwoButtonDialogGTLogin(context);
//                }
//            }
//        });
        //响应点赞按钮
        viewHolder.likeLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Global.isLogin())
                {
                    if (Constants.CANCEL_PRAISE.equals(data.get(position).getFlag()))
                    {
                        data.get(position).setFlag(Constants.PRAISE);
                        data.get(position).setPraiseNum((Integer.parseInt(data.get(position).getPraiseNum()) + 1) + "");
                    }
                    else
                    {
                        data.get(position).setFlag(Constants.CANCEL_PRAISE);
                        data.get(position).setPraiseNum((Integer.parseInt(data.get(position).getPraiseNum()) - 1) + "");
                    }
                    notifyDataSetChanged();
                    showAnimation((ImageView)((LinearLayout)v).getChildAt(0), position);
                    CommunityService.instance().addOrCancelPraise(data.get(position).getaId(),
                        data.get(position).getFlag(),
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
        viewHolder.commentLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, CommunityTopicDetailsActivity.class);
                intent.putExtra("type", Constants.NUM1);
                intent.putExtra("id", data.get(position).getaId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        //响应更多按钮点击事件
        viewHolder.moreLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogUtil.communityEditDiaLog(context,
                    data.get(position).getIsTop(),
                    type,
                    data.get(position),
                    new DialogCallBack()
                    {
                        @Override
                        public void dialogBack()
                        {
                            boolean isMore = false;
                            if (Constants.ISTOP.equals(data.get(position).getIsTop()))
                            {
                                data.get(position).setIsTop(Constants.NOTTOP);
                                CommunityService.instance().uPOrDownTopic(data.get(position).getArticleId(),
                                    Constants.GROUP_CANCEL_UP,
                                    data.get(position).getId(),
                                    context,
                                    callBack);
                                data.add(data.remove(position));
                            }
                            else
                            {
                                for (int i = 0; i < data.size(); i++)
                                {
                                    if (Constants.ISTOP.equals(data.get(i).getIsTop()) && i == 2)
                                    {
                                        ToastUtil.makeText(context, "最多只可置顶三个话题");
                                        isMore = true;
                                        break;
                                    }
                                }
                                if (!isMore)
                                {
                                    data.get(position).setIsTop(Constants.ISTOP);
                                    CommunityService.instance().uPOrDownTopic(data.get(position).getArticleId(),
                                        Constants.GROUP_UP,
                                        data.get(position).getId(),
                                        context,
                                        callBack);
                                    data.add(0, data.remove(position));
                                }
                            }
                            notifyDataSetChanged();
                        }
                    },
                    new DialogCallBack()
                    {
                        @Override
                        public void dialogBack()
                        {
                            DialogUtil.showTwoButtonDialog(context, "是否确认删除此话题?", new DialogCallBack()
                            {
                                @Override
                                public void dialogBack()
                                {
                                    dailog = new NetLoadingDailog(context);
                                    dailog.loading();
                                    deletePosition = position;
                                    CommunityService.instance().deleteTopic(data.get(position).getArticleId(),
                                        Constants.GROUP_DELETE,
                                        data.get(position).getId(),
                                        context,
                                        callBack);
                                }
                            });
                        }
                    },
                    new DialogCallBack()
                    {
                        @Override
                        public void dialogBack()
                        {
                            if (Global.isLogin())
                            {
                                if (Global.isSuper())
                                {
                                    Intent moveIntent = new Intent(context, MoveTopicActivity.class);
                                    moveIntent.putExtra("from_id", data.get(position).getId());
                                    moveIntent.putExtra("article_id", data.get(position).getArticleId());
                                    if (callBack instanceof Activity)
                                    {
                                        ((Activity)context).startActivityForResult(moveIntent, Constants.NUM1);
                                    }
                                    else
                                    {
                                        ((Fragment)callBack).startActivityForResult(moveIntent, Constants.NUM1);
                                    }
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
                    },
                    new DialogCallBack()
                    {
                        @Override
                        public void dialogBack()
                        {
                            if (Global.isLogin())
                            {
                                Intent intent = new Intent(context, ReportActivity.class);
                                intent.putExtra("article_id", data.get(position).getArticleId());
                                context.startActivity(intent);
                            }
                            else
                            {
                                DialogUtil.TwoButtonDialogGTLogin(context);
                            }
                        }
                    });
            }
        });
        return convertView;
    }
    
    class ViewHolder
    {
        private ImageView headImage;
        
        private ImageView vip;
        
        private TextView name;
        
        private TextView time;
        
        private TextView content;
        
        private LinearLayout photo;
        
        private LinearLayout likeLayout;
        
        private ImageView likeImg;
        
        private ImageView isHot;
        
        private ImageView isVote;
        
        private LinearLayout commentLayout;
        
        private TextView likeNum;
        
        private TextView commentNum;
        
        private LinearLayout moreLayout;
    }
    
    /**
     * 
     * <设置类型>
     * <圈子详情&&登陆名为圈主1   其他0>
     * @param type
     * @see [类、类#方法、类#成员]
     */
    public void setType(String type)
    {
        this.type = type;
    }
    
    /**
     * 
     * <设置社区管理员id>
     * <功能详细描述>
     * @param userId
     * @see [类、类#方法、类#成员]
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
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
                if (Constants.CANCEL_PRAISE.equals(data.get(position).getFlag()))
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
    
}
