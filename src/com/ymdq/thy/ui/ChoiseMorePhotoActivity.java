package com.ymdq.thy.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.ChoiseMorePhotosParcelable;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.util.ChoiseMorePhotosUtil;

public class ChoiseMorePhotoActivity extends Activity implements OnItemClickListener,OnClickListener
{
    private ChoiseMorePhotoAdapter adapter; 
    
    private TextView select_num;
    
    private Button scanBtn;
    
    private Button okBtn;
    
    /**
     * 存放被选中的图片的url
     */
    private ArrayList<String> imgUrlList = new ArrayList<String>();
    
    /**
     * 最多选择照片的个数，默认为6张
     */
    private int mostCount = 6;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choise_more_photo_grally);
        mostCount = getIntent().getIntExtra("most_count", 6);
        initView();
    }
    
    private void initView()
    {
        ChoiseMorePhotosParcelable photoParcelable = getIntent().getParcelableExtra("data");
        ArrayList<String> list = getIntent().getStringArrayListExtra("imgUrlList");
        if(list.size() > 0)
        {
            imgUrlList.addAll(list);
        }
        RelativeLayout titleBar = (RelativeLayout)findViewById(R.id.title_bar);
        LinearLayout titleBarBack = (LinearLayout)titleBar.findViewById(R.id.title_back_layout);
        TextView titleBarName = (TextView)titleBar.findViewById(R.id.title_name);
        titleBarBack.setOnClickListener(this);
        titleBarName.setText(photoParcelable.getFileName());
        
        GridView gridview = (GridView)findViewById(R.id.grid_view);
        adapter = new ChoiseMorePhotoAdapter(photoParcelable.getFileImageList(),imgUrlList);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(this);
        
        select_num = (TextView)findViewById(R.id.select_num);
        select_num.setText(imgUrlList.size()+"");
        
        scanBtn = (Button)findViewById(R.id.scan);
        okBtn = (Button)findViewById(R.id.choise_ok);
        scanBtn.setOnClickListener(this);
        okBtn.setOnClickListener(this);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ViewGroup viewG = (ViewGroup)view;
        CheckBox checkBox = (CheckBox)viewG.getChildAt(1);
        if(checkBox.isChecked())
        {
            checkBox.setChecked(false);
            imgUrlList.remove(adapter.getItem(position));
        }
        else
        {
            if(imgUrlList.size() >= mostCount)
            {
                Toast.makeText(ChoiseMorePhotoActivity.this, "最多选择"+mostCount+"张图片", Toast.LENGTH_SHORT).show();
            }
            else
            {
                checkBox.setChecked(true);
                imgUrlList.add((String)adapter.getItem(position));
            }
        }
        select_num.setText(imgUrlList.size()+"");
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title_back_layout:
                Intent i = new Intent(ChoiseMorePhotoActivity.this,ChoiseMorePhotosListActivity.class);
                i.putStringArrayListExtra("imgUrlList", imgUrlList);
                setResult(Constants.CHOISE_MORE_PHOTO_110, i);
                finish();
                break;
            //浏览
            case R.id.scan:
                if(imgUrlList.size() > 0)
                {
                    ArrayList<String> viewList = new ArrayList<String>();
                    for(String img : imgUrlList)
                    {
                        viewList.add("file://" + img);
                    }
                    Intent intent = new Intent(ChoiseMorePhotoActivity.this, ViewPagerActivity.class);
                    intent.putStringArrayListExtra("photoUrls", viewList);
                    intent.putExtra("currentItem", 0);
                    startActivity(intent);
                }
                break;
            //完成
            case R.id.choise_ok:
                Intent ok = new Intent(ChoiseMorePhotoActivity.this,ChoiseMorePhotosListActivity.class);
                ok.putStringArrayListExtra("imgUrlList", imgUrlList);
                setResult(Constants.CHOISE_MORE_PHOTO_111, ok);
                finish();
                break;
            case R.id.checkBox:
                int position = (Integer)v.getTag();
                CheckBox box = (CheckBox)v;
                
                if(box.isChecked())
                {
                    if(imgUrlList.size() >= mostCount)
                    {
                        box.setChecked(false);
                        Toast.makeText(ChoiseMorePhotoActivity.this, "最多选择"+mostCount+"张图片", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        imgUrlList.add((String)adapter.getItem(position));
                    }
                }
                else
                {
                    imgUrlList.remove(adapter.getItem(position));
                }

                select_num.setText(imgUrlList.size()+"");
                break;
            default:
                break;
        }
    }
    
    
    
    public class ChoiseMorePhotoAdapter extends BaseAdapter 
    {
        
//      Context context;
      List<String> data;
      //被选中的图片url
      ArrayList<String> selectedUrlList;
      public Bitmap bitmaps[];
      //        OnItemClickClass onItemClickClass;
      private int index=-1;
      
      List<View> holderlist;
      public ChoiseMorePhotoAdapter(List<String> data,ArrayList<String> selectedUrlList)//,OnItemClickClass onItemClickClass) {
      {
//          this.context=context;
          this.data=data;
          this.selectedUrlList = selectedUrlList;
          //            this.onItemClickClass=onItemClickClass;
          bitmaps=new Bitmap[data.size()];
          holderlist=new ArrayList<View>();
      }
      
      @Override
      public int getCount() 
      {
          return data == null ? null : data.size();
      }
      
      @Override
      public Object getItem(int position) 
      {
          return data == null ? null : data.get(position);
      }
      
      @Override
      public long getItemId(int position) 
      {
          return position;
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
      @Override
      public View getView(int position, View convertView, ViewGroup parent) 
      {
          Holder holder;
          if (position != index && position > index) 
          {
              index=position;
              convertView=LayoutInflater.from(ChoiseMorePhotoActivity.this).inflate(R.layout.choise_more_photos_grally_item, null);
              holder=new Holder();
              holder.imageView=(ImageView) convertView.findViewById(R.id.image_view);
              holder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);
              convertView.setTag(holder);
              holderlist.add(convertView);
          }
          else
          {
              holder= (Holder)holderlist.get(position).getTag();
              convertView=holderlist.get(position);
          }
          if (bitmaps[position] == null)
          {
              ChoiseMorePhotosUtil.getIntence(ChoiseMorePhotoActivity.this).imgExcute(holder.imageView,
                  new ImgClallBackLisner(position), data.get(position));
          }
          else
          {
              holder.imageView.setImageBitmap(bitmaps[position]);
          }
//          convertView.setOnClickListener(new OnPhotoClick(position, holder.checkBox));
          
          if(selectedUrlList.contains(data.get(position)))
          {
              holder.checkBox.setChecked(true);
          }
          
          holder.checkBox.setTag(position);
          holder.checkBox.setOnClickListener(ChoiseMorePhotoActivity.this);
          
          return convertView;
      }
      
      class Holder
      {
          ImageView imageView;
          CheckBox checkBox;
      }
      
      public class ImgClallBackLisner implements ChoiseMorePhotosUtil.ImgCallBack
      {
          int num;
          public ImgClallBackLisner(int num)
          {
              this.num=num;
          }
          
          @Override
          public void resultImgCall(ImageView imageView, Bitmap bitmap)
          {
              bitmaps[num]=bitmap;
              imageView.setImageBitmap(bitmap);
          }
      }
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(ChoiseMorePhotoActivity.this,ChoiseMorePhotosListActivity.class);
        i.putStringArrayListExtra("imgUrlList", imgUrlList);
        setResult(Constants.CHOISE_MORE_PHOTO_110, i);
        super.onBackPressed();
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        adapter.destoryBitmap();
    }
}
