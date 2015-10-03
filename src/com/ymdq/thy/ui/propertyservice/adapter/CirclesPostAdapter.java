package com.ymdq.thy.ui.propertyservice.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.ImgPicker;
import com.ymdq.thy.ui.propertyservice.RepairActivity;
import com.ymdq.thy.util.ChoiseMorePhotosUtil;
import com.ymdq.thy.util.DisplayUtil;

/**
 * 发帖图片thumb适配器
 * 
 * @author  huangchao
 * @version  V1, July 1, 2014]
 * @see  
 * @since  
 */
public class CirclesPostAdapter extends BaseAdapter
{
    private Context mContext;

    /**
     * 缩略图宽度
     */
    private static int WIDTH = 0;
    
    private static final String BLANK = RepairActivity.BLANK.path;

    private static final String TAG = CirclesPostAdapter.class.getSimpleName();
    
    private ArrayList<ImgPicker> imgList;
    
    private ImageLoader imgLoader;
    
    private OnClickListener listener;
    
    private int index = -1;
    
    private Bitmap[] bitmaps = new Bitmap[6];
    
    /**
     * 图片显示配置
     */
    private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false)
        .showImageOnFail(R.drawable.community_default)
        .showImageOnLoading(R.drawable.community_default)
        .showImageForEmptyUri(R.drawable.community_default)
        .cacheOnDisc(false)
        .cacheInMemory(true)
        .considerExifParams(true)
        .displayer(new FadeInBitmapDisplayer(0))
        .build();
    
    public CirclesPostAdapter(Context context, ArrayList<ImgPicker> list,OnClickListener listener)
    {
        this.mContext = context;
        this.imgList = list;
        this.listener = listener;
        imgLoader = ImageLoader.getInstance();
        WIDTH = mContext.getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(mContext, 100);
        WIDTH /= 3;
    }
    
    @Override
    public int getCount()
    {
        return null != imgList ? imgList.size() : 0;
    }
    
    @Override
    public Object getItem(int position)
    {
        return null != imgList ? imgList.get(position) : null;
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    public void setNotifyPosition(int index)
    {
        this.index = index;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImgPicker img = imgList.get(position);
        
        ViewHolder viewHolder = null;
        if (null == convertView)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_circles_post_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imgItem = convertView.findViewById(R.id.circle_post_item);
            viewHolder.imgAdd = (ImageView)convertView.findViewById(R.id.circle_post_btn_pick);
            viewHolder.imgDel = (ImageView)convertView.findViewById(R.id.circle_post_img_del);
            
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(WIDTH, WIDTH);
            viewHolder.imgItem.setLayoutParams(params);
            viewHolder.imgAdd.setLayoutParams(params);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        
//        convertView.setTag(R.id.circle_img_index, position);
        if(img.path.equals(BLANK))
        {
            viewHolder.imgItem.setVisibility(View.GONE);
            viewHolder.imgAdd.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.imgAdd.setVisibility(View.GONE);
            viewHolder.imgItem.setVisibility(View.VISIBLE);
            
            String s = Uri.fromFile(new File(img.thumb)).toString();
            
            viewHolder.imgDel.setTag(position);
            viewHolder.imgDel.setOnClickListener(listener);
            ImageView photo = (ImageView)viewHolder.imgItem
                .findViewById(R.id.circle_post_img);
            
            if(position > index)
            {
                ChoiseMorePhotosUtil.getIntence(mContext).imgExcute(photo,
                    new ImgClallBackLisner(position), img.thumb);
                
                //                imgLoader.displayImage(s, photo, options);
            }
            else
            {
                photo.setImageBitmap(bitmaps[position]);
            }
        }

        return convertView;
    }
    
    class ViewHolder
    {
        View imgItem;
        ImageView imgDel;
        ImageView imgAdd;
    }
    
    public void destoryBitmap()
    {
        for(int i = 0; i < bitmaps.length ; i++)
        {
            if(bitmaps[i] != null)
            {
                bitmaps[i].recycle();
            }
        }
    }
    
    class ImgClallBackLisner implements ChoiseMorePhotosUtil.ImgCallBack
    {
        int num;
        public ImgClallBackLisner(int num)
        {
            this.num=num;
        }
        
        @Override
        public void resultImgCall(ImageView imageView, Bitmap bitmap)
        {
//            if(bitmaps[num] != null)
//            {
//                bitmaps[num].recycle();
//            }
            bitmaps[num] = bitmap;
            imageView.setImageBitmap(bitmap);
        }
    }
}
