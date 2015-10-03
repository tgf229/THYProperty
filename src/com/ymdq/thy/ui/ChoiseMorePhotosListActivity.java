package com.ymdq.thy.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.ChoiseMorePhotosParcelable;
import com.ymdq.thy.bean.ImgPicker;
import com.ymdq.thy.bean.PicturePropertiesBean;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.util.BitmapUtil;
import com.ymdq.thy.util.ChoiseMorePhotosUtil;
import com.ymdq.thy.util.FileSystemManager;
import com.ymdq.thy.util.ChoiseMorePhotosUtil.ImgCallBack;

public class ChoiseMorePhotosListActivity extends Activity implements OnItemClickListener,OnClickListener
{
    private ListView mListView;
    
    private List<HashMap<String,String>> listdata;
    
    private ImgFileListAdapter adapter;
    
    private List<ChoiseMorePhotosParcelable> locallist;
    
    private ArrayList<String> imgUrlList = new ArrayList<String>();
    
    private String nextClass;

    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            adapter.setBitmap();
            adapter.notifyDataSetChanged();
        };
    };
    
    /**
     * 缩放尺寸
     */
    protected static final float IMG_SCALE = 640f;
    
    /**
     * 已经选择照片的个数，默认为0张
     */
    private int haveCount = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choise_more_photos_list);
        
        nextClass = getIntent().getStringExtra("class");
        haveCount = getIntent().getIntExtra("haveCount", 0);
        initView();
        
//        new Thread()
//        {
//            public void run()
//            {
                locallist = ChoiseMorePhotosUtil.getIntence(ChoiseMorePhotosListActivity.this).localImgFileList();
                Log.i("info", "locallist:"+locallist.size());
                if (locallist!=null) 
                {
//                    bitmap=new Bitmap[locallist.size()];
                    for (int i = 0; i < locallist.size(); i++) 
                    {
                        HashMap<String, String> map=new HashMap<String, String>();
                        map.put("filecount", locallist.get(i).getFileImageList().size()+"");//+"张"
                        map.put("imgpath", locallist.get(i).getFileImageList().get(0) == null ? 
                            null : (locallist.get(i).getFileImageList().get(0)));
                        map.put("filename", locallist.get(i).getFileName());
                        listdata.add(map);
                    }
                    handler.sendEmptyMessage(0);
                }
//            };
//        }.start();
        
        mListView.setOnItemClickListener(this);
    }
    
    private void initView()
    {
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setText("选择照片");
        LinearLayout titleBarRight = (LinearLayout)titleBar.findViewById(R.id.title_call_layout);
        titleBarRight.setOnClickListener(this);
        TextView titleBarTextV = (TextView)titleBar.findViewById(R.id.title_btn_call);
        titleBarTextV.setText("完成");
        titleBarTextV.setTextColor(getResources().getColorStateList(R.color.selector_color_community_post));
        titleBarTextV.setVisibility(View.VISIBLE);
        
        mListView = (ListView)findViewById(R.id.list_view);
        
        listdata = new ArrayList<HashMap<String,String>>();
        adapter = new ImgFileListAdapter(ChoiseMorePhotosListActivity.this,listdata);
        mListView.setAdapter(adapter);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent=new Intent(this,ChoiseMorePhotoActivity.class);
        Bundle bundle=new Bundle();
        bundle.putParcelable("data", locallist.get(position));
        bundle.putStringArrayList("imgUrlList", imgUrlList);
        int count = 6 - haveCount;
        bundle.putInt("most_count", count);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.CHOISE_MORE_PHOTO_110);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                finish();
                break;
            //完成
            case R.id.title_call_layout:
                try
                {
                    ArrayList<ImgPicker> imgList = thumbImgs();
                    Intent ok = new Intent(ChoiseMorePhotosListActivity.this,Class.forName(nextClass));
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("imgUrlList", imgList);
                    ok.putExtras(bundle);
                    setResult(Constants.CHOISE_MORE_PHOTO_112, ok);
                    finish();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.CHOISE_MORE_PHOTO_110 && resultCode == Constants.CHOISE_MORE_PHOTO_110 && data != null)
        {
            addImgUrl(data);
        }
        else if(requestCode == Constants.CHOISE_MORE_PHOTO_110 && resultCode == Constants.CHOISE_MORE_PHOTO_111 && data != null)
        {
            addImgUrl(data);
            try
            {
                
                ArrayList<ImgPicker> imgList = thumbImgs();
                Intent ok = new Intent(ChoiseMorePhotosListActivity.this,Class.forName(nextClass));
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("imgUrlList", imgList);
                ok.putExtras(bundle);
                setResult(Constants.CHOISE_MORE_PHOTO_112, ok);
                finish();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * <压缩图片>
     * <功能详细描述>
     * @param imgPickerList
     * @see [类、类#方法、类#成员]
     */
    private ArrayList<ImgPicker> thumbImgs()
    {
        ArrayList<ImgPicker> imgPickerList = new ArrayList<ImgPicker>();
        for(int i=0;i<imgUrlList.size();i++)
        {
            String url = imgUrlList.get(i);
            String toPath = FileSystemManager.getPostPath(this);
            toPath += System.currentTimeMillis() + ".jpg";
            toPath =
                BitmapUtil.getImageScaleByPath(new PicturePropertiesBean(url, toPath, IMG_SCALE, IMG_SCALE),this);
            
            if (!TextUtils.isEmpty(toPath))
            {
                ImgPicker img = new ImgPicker(toPath, url, 1);
                imgPickerList.add(img);
            }
            
        }
        return imgPickerList;
    }

    private void addImgUrl(Intent data)
    {
        ArrayList<String> list = data.getStringArrayListExtra("imgUrlList");
        for(String tempUrl : list)
        {
            if(imgUrlList.contains(tempUrl))
            {
                continue;
            }
            else
            {
                imgUrlList.add(tempUrl);
            }
        }
    }
    
    class ImgFileListAdapter extends BaseAdapter
    {
        
        Context context;
        
        List<HashMap<String, String>> listdata;
        
        Bitmap[] bitmaps;
        
        private int index=-1;
        
        List<View> holderlist;
        
        public ImgFileListAdapter(Context context,List<HashMap<String, String>> listdata)
        {
            this.context=context;
            this.listdata=listdata;
            holderlist=new ArrayList<View>();
        }
        
        @Override
        public int getCount()
        {
            return listdata == null ? null : listdata.size();
        }
        
        @Override
        public Object getItem(int position)
        {
            return listdata == null ? null : listdata.get(position);
        }
        
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }
        
        public void setBitmap()
        {
            bitmaps=new Bitmap[listdata.size()];
        }
        
        @Override
        public View getView(final int position, View contentView, ViewGroup parent) 
        {
            Holder holder;
            if (contentView == null)//(position != index && position > index) 
            {
                holder = new Holder();
                contentView=LayoutInflater.from(context).inflate(R.layout.choise_more_photos_list_item, null);
                holder.photo_imgview=(ImageView) contentView.findViewById(R.id.filephoto_imgview);
                holder.filecount_textview=(TextView) contentView.findViewById(R.id.filecount_textview);
                holder.filename_textView=(TextView) contentView.findViewById(R.id.filename_textview);
                contentView.setTag(holder);
            }
            else
            {
                holder= (Holder)contentView.getTag();
            }
            
            holder.filename_textView.setText(listdata.get(position).get("filename"));
            holder.filecount_textview.setText(listdata.get(position).get("filecount"));
            if(bitmaps != null && bitmaps.length > 0)
            {
                if (bitmaps[position] == null)
                {
                    ChoiseMorePhotosUtil.getIntence(context).imgExcute(holder.photo_imgview,new ImgCallBack(){
                        @Override
                        public void resultImgCall(ImageView imageView, Bitmap bitmap)
                        {
                            bitmaps[position] = bitmap;
                            imageView.setImageBitmap(bitmap);
                        }
                    }, listdata.get(position).get("imgpath"));
                }
                else
                {
                    holder.photo_imgview.setImageBitmap(bitmaps[position]);
                }
                
            }
            
            return contentView;
        }
        
        class Holder
        {
            public ImageView photo_imgview;
            public TextView filecount_textview;
            public TextView filename_textView;
        }
    }
}
