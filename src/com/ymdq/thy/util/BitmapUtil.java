package com.ymdq.thy.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;

import com.ymdq.thy.bean.PicturePropertiesBean;
import com.ymdq.thy.constant.Global;

/**
 * 
 * <图片公共类>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2013-9-17]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BitmapUtil
{
    
    /**
     * 获取圆角位图的方法
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    public static Bitmap toRoundCorner(Context context, Bitmap bitmap, int pixels)
    {
        Bitmap newbmp =
            ThumbnailUtils.extractThumbnail(bitmap,
                DensityUtil.dip2px(context, 60),
                DensityUtil.dip2px(context, 60),
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        Bitmap output = Bitmap.createBitmap(newbmp.getWidth(), newbmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, newbmp.getWidth(), newbmp.getWidth());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(newbmp, rect, rect, paint);
        newbmp.recycle();
        return output;
    }
    
    /**
     * 根据网址获得图片，优先从本地获取，本地没有则从网络下载
     * 
     * @param url  图片网址
     * @param context 上下文
     * @return 图片
     */
    public static Bitmap getBitmap(String url, Context context, String userId, String type)
    {
        String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
        
        File file = null;
        //保存头像地址
        if ("head".equals(type))
        {
            file = new File(FileSystemManager.getUserHeadPath(context, userId), imageName);
        }
        //保存投诉维权服务器上的缩略图的地址
        else if ("complaint_thumb".equals(type))
        {
            file = new File(FileSystemManager.getMallComplaintsPicPath(context, userId), "thumb_" + imageName);
        }
        //保存投诉维权服务器上的原图的地址
        else if ("complaint_org".equals(type))
        {
            file = new File(FileSystemManager.getMallComplaintsPicPath(context, userId), imageName);
        }
        
        if (file.exists())
        {
            return BitmapFactory.decodeFile(file.getPath());
        }
        return loadImageFromUrl(url, file, context);
    }
    
    /**
     * 
     * <根据URL下载图片,并保存到本地>
     * <功能详细描述>
     * @param imageURL
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Bitmap loadImageFromUrl(String imageURL, File file, Context context)
    {
        Bitmap bitmap = null;
        try
        {
            URL url = new URL(imageURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            if (con.getResponseCode() == 200)
            {
                InputStream inputStream = con.getInputStream();
                FileUtil.deleteDirectory(FileSystemManager.getUserHeadPath(context, Global.getUserId()));
                ByteArrayOutputStream OutputStream = new ByteArrayOutputStream();
                FileOutputStream out = new FileOutputStream(file.getPath());
                byte buf[] = new byte[1024 * 20];
                int len = 0;
                while ((len = inputStream.read(buf)) != -1)
                {
                    OutputStream.write(buf, 0, len);
                }
                OutputStream.flush();
                OutputStream.close();
                inputStream.close();
                out.write(OutputStream.toByteArray());
                out.close();
                BitmapFactory.Options imageOptions = new BitmapFactory.Options();
                imageOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                imageOptions.inPurgeable = true;
                imageOptions.inInputShareable = true;
                bitmap = BitmapFactory.decodeFile(file.getPath(), imageOptions);
            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    /***
     * 根据资源文件获取Bitmap
     * 
     * @param context
     * @param drawableId
     * @return
     */
    public static Bitmap ReadBitmapById(Context context, int drawableId)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.ARGB_8888;
        options.inInputShareable = true;
        options.inPurgeable = true;
        InputStream stream = context.getResources().openRawResource(drawableId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        return bitmap;
    }
    
    /**
     * Bitmap 等比例压缩
     */
    public static Bitmap getBitmapScale(Bitmap bitmap, int screenWidth, int screenHight)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scale = (float)screenWidth / w;
        float scale2 = (float)screenHight / h;
        
        // scale = scale < scale2 ? scale : scale2;
        
        // 保证图片不变形.
        matrix.postScale(scale, scale);
        // w,h是原图的属性.
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }
    
    /**
     * <图片按比例大小压缩方法（根据路径获取图片并压缩）>
     * 
     * @return  压缩后图片路径
     */
    public static String getImageScaleByPath(PicturePropertiesBean propertiesBean, Context context)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(propertiesBean.getSrcPath(), newOpts);
        int width = newOpts.outWidth;
        int height = newOpts.outHeight;
        //        float minHeight = 800f;//设置为主流手机分辨率800*480
        
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;// be=1表示不缩放  
        if (height > width && width > propertiesBean.getWidth())
        {
            // 如果宽度大的话根据宽度固定大小缩放  
            be = (int)(newOpts.outWidth / propertiesBean.getWidth());
        }
        else if (width > height && height > propertiesBean.getHeight())
        {
            // 如果高度高的话根据宽度固定大小缩放  
            be = (int)(newOpts.outHeight / propertiesBean.getHeight());
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例  
        newOpts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(propertiesBean.getSrcPath(), newOpts);
        return compressImage(bitmap, context, propertiesBean);// 压缩好比例大小后再进行质量压缩  
    }
    
    /**
     * <质量压缩方法>
     * 
     * @return 压缩后图片路径
     */
    private static String compressImage(Bitmap image, Context context, PicturePropertiesBean propertiesBean)
    {
        File file = null;
        if (image != null)
        {
            try
            {
                int degree = getExifOrientation(propertiesBean.getSrcPath());
                if (degree > 0)
                {
                    Matrix matrix = new Matrix();
                    matrix.setRotate(degree);
                    Bitmap rotateBitmap =
                        Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
                    if (rotateBitmap != null)
                    {
                        image.recycle();
                        image = rotateBitmap;
                    }
                }
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中 
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                //图片大于最大值,则压缩,否则不做任何操作
                if (baos.toByteArray().length > propertiesBean.getMaxSize())
                {
                    baos.reset();
                    // 质量压缩方法，首先压缩options的压缩率
                    image.compress(Bitmap.CompressFormat.JPEG, propertiesBean.getDefaultOption(), baos);
                    // 循环判断如果压缩后图片是否大于200kb,大于继续压缩  
                    while (baos.toByteArray().length > propertiesBean.getMaxSize())
                    {
                        baos.reset();// 重置baos即清空baos  
                        // 这里压缩defaultOptions%，把压缩后的数据存放到baos中  
                        propertiesBean.setDefaultOption(propertiesBean.getDefaultOption() - propertiesBean.getOptions());// 每次都减少option 
                        image.compress(Bitmap.CompressFormat.JPEG, propertiesBean.getDefaultOption(), baos);
                    }
                    while (baos.toByteArray().length < propertiesBean.getMinSize())
                    {
                        baos.reset();// 重置baos即清空baos  
                        // 这里压缩options%，把压缩后的数据存放到baos中  
                        propertiesBean.setDefaultOption(propertiesBean.getDefaultOption() + propertiesBean.getOptions());// 每次都增加option  
                        image.compress(Bitmap.CompressFormat.JPEG, propertiesBean.getDefaultOption(), baos);
                    }
                }
                file = new File(propertiesBean.getDestPath());
                FileOutputStream stream = new FileOutputStream(file);
                if (baos != null)
                {
                    stream.write(baos.toByteArray());
                    stream.flush();
                }
                stream.close();
                baos.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (image != null)
                    image.recycle();
            }
            return file.getPath();
        }
        else
        {
            return "";
        }
    }
    
    /**
     * 
     * <得到 图片旋转 的角度>
     * <功能详细描述>
     * @param filepath
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static int getExifOrientation(String filePath)
    {
        int degree = 0;
        try
        {
            ExifInterface exif = new ExifInterface(filePath);
            int result = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (result)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                
                default:
                    break;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return degree;
    }
}