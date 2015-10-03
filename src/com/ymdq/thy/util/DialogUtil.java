package com.ymdq.thy.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ymdq.thy.JRApplication;
import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Topic;
import com.ymdq.thy.callback.DialogCallBack;
import com.ymdq.thy.callback.DialogCancelCallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.ui.personcenter.LoginActivity;

/**
 * 
 * <弹出框公共类> <功能详细描述>
 * 
 * @author cyf
 * @version [版本号, 2014-3-24]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DialogUtil
{
    
    /**
     * 
     * <登录两个按钮的弹出框>
     * <功能详细描述>
     * @param context
     * @param title
     * @param content
     * @param callBack
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Dialog loginTwoButtonDialog(Context context, final DialogCallBack callBack)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.two_button_dialog, null);
        Button confirm = (Button)layout.findViewById(R.id.dialog_confirm_bt);
        Button cancel = (Button)layout.findViewById(R.id.dialog_cancel_bt);
        final Dialog dialog = new Dialog(context, R.style.main_dialog);
        dialog.setContentView(layout);
        confirm.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
                callBack.dialogBack();
            }
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    
    /**
     * 
     * <显示两个按钮的弹出框>
     * <功能详细描述>
     * @param context
     * @param title
     * @param content
     * @param callBack
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Dialog showTwoButtonDialog(Context context, String content, final DialogCallBack callBack)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.two_button_content_dialog, null);
        TextView contentTV = (TextView)layout.findViewById(R.id.dialog_content_tv);
        Button confirm = (Button)layout.findViewById(R.id.dialog_confirm_bt);
        Button cancel = (Button)layout.findViewById(R.id.dialog_cancel_bt);
        contentTV.setText(content);
        final Dialog dialog = new Dialog(context, R.style.main_dialog);
        dialog.setContentView(layout);
        confirm.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
                callBack.dialogBack();
            }
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    
    /**
     * 
     * <显示一个按钮的弹出框>
     * <功能详细描述>
     * @param context
     * @param content
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Dialog showOneButtonDialog(Context context, String content)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.one_button_dialog, null);
        TextView contentTV = (TextView)layout.findViewById(R.id.dialog_content_tv);
        Button confirm = (Button)layout.findViewById(R.id.dialog_confirm_bt);
        contentTV.setText(content);
        final Dialog dialog = new Dialog(context, R.style.main_dialog);
        dialog.setContentView(layout);
        confirm.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    
    /**
     * 
     * <显示一个按钮的弹出框>
     * <功能详细描述>
     * @param context
     * @param content
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static void showBtnFinishActivityDialog(final Activity context, String content)
    {
        if (!JRApplication.currentActivity.equals(context.getClass().getName()))
        {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.one_button_dialog, null);
        TextView contentTV = (TextView)layout.findViewById(R.id.dialog_content_tv);
        Button confirm = (Button)layout.findViewById(R.id.dialog_confirm_bt);
        contentTV.setText(content);
        final Dialog dialog = new Dialog(context, R.style.main_dialog);
        dialog.setContentView(layout);
        confirm.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
                context.finish();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                context.finish();
            }
        });
    }
    
    /**
     * 
     * <显示两个按钮的弹出框,点击取消,关闭当前界面>
     * <功能详细描述>
     * @param context
     * @param title
     * @param content
     * @param callBack
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Dialog showTwoButtonDialogCancel(Context context, String content, final DialogCallBack callBack,
        final DialogCancelCallBack cancelCallBack)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.two_button_dialog, null);
        TextView contentTV = (TextView)layout.findViewById(R.id.dialog_content_tv);
        Button confirm = (Button)layout.findViewById(R.id.dialog_confirm_bt);
        Button cancel = (Button)layout.findViewById(R.id.dialog_cancel_bt);
        contentTV.setText(content);
        final Dialog dialog = new Dialog(context, R.style.main_dialog);
        dialog.setContentView(layout);
        confirm.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
                callBack.dialogBack();
            }
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
                cancelCallBack.dialogCancelBack();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    
    /**
     * 
     * <版本更新>
     * <功能详细描述>
     * @param context
     * @param title
     * @param content 要显示的内容为数组
     * @param left
     * @param right
     * @param isUpdate
     * @param callBack
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Dialog showUpdateDialog(Context context, String title, String[] content, String left, String right,
        final String isUpdate, final DialogCallBack callBack)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.two_button_list_dialog, null);
        ListView contentTV = (ListView)layout.findViewById(R.id.dialog_content);
        TextView titleTV = (TextView)layout.findViewById(R.id.dialog_title_tv);
        Button confirm = (Button)layout.findViewById(R.id.dialog_confirm_bt);
        Button cancel = (Button)layout.findViewById(R.id.dialog_cancel_bt);
        if (content.length > 1)
        {
            contentTV.setVisibility(View.VISIBLE);
        }
        else
        {
            contentTV.setVisibility(View.GONE);
        }
        confirm.setText(left);
        cancel.setText(right);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.two_button_list_dialog_item, content);
        contentTV.setAdapter(adapter);
        titleTV.setText(title);
        final Dialog dialog = new Dialog(context, R.style.main_dialog);
        dialog.setContentView(layout);
        confirm.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
                callBack.dialogBack();
            }
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                if ("1".equals(isUpdate))
                {
                    dialog.dismiss();
                    JRApplication.jrApplication.onTerminate();//退出
                }
                else
                {
                    dialog.dismiss();
                }
                
            }
        });
        dialog.setOnKeyListener(getOnKeyListener(isUpdate));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    
    private static OnKeyListener getOnKeyListener(final String isUpdate)
    {
        OnKeyListener key = new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_BACK:
                        if ("1".equals(isUpdate))//强制更新
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    default:
                        break;
                }
                return false;
            }
        };
        return key;
    }
    
    /**
     * 
     * <判断是否登录弹出框>
     * <功能详细描述>
     * @param context
     * @param title
     * @param content
     * @param callBack
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Dialog TwoButtonDialogGTLogin(final Context context)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.two_button_dialog, null);
        Button confirm = (Button)layout.findViewById(R.id.dialog_confirm_bt);
        Button cancel = (Button)layout.findViewById(R.id.dialog_cancel_bt);
        final Dialog dialog = new Dialog(context, R.style.main_dialog);
        dialog.setContentView(layout);
        confirm.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    
    /**
     * 
     * <圈子置顶,取消置顶,删除框,移动框>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public static void communityEditDiaLog(Context context, String isTop, String type, Topic topic,
        final DialogCallBack topBack, final DialogCallBack deleteBack, final DialogCallBack moveBack,
        final DialogCallBack reportBack)
    {
        final Dialog editDialog = new Dialog(context, R.style.image_select_dialog);
        editDialog.setContentView(R.layout.community_edit_select_dialog);
        editDialog.getWindow().getAttributes().width = LayoutParams.MATCH_PARENT;
        editDialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
        editDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
        editDialog.getWindow().setWindowAnimations(R.style.dialog_style);
        TextView topOrCancel = (TextView)editDialog.findViewById(R.id.top_or_cancel);
        TextView cancel = (TextView)editDialog.findViewById(R.id.cancel);
        TextView delete = (TextView)editDialog.findViewById(R.id.delete);
        TextView report = (TextView)editDialog.findViewById(R.id.report);
        TextView move = (TextView)editDialog.findViewById(R.id.move);
        editDialog.show();
        ShareFactory shareFactory = new ShareFactory(editDialog, editDialog.getWindow().getDecorView(), context, topic);
        shareFactory.loadData();
        //隐藏或者显示布局
        if (Global.isSuper())
        {
            move.setVisibility(View.VISIBLE);
        }
        else
        {
            move.setVisibility(View.GONE);
        }
        if (Constants.COMMUNITY_DETAIL_OWNER.equals(type))
        {
            topOrCancel.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            if (Constants.ISTOP.equals(isTop))
            {
                topOrCancel.setText("取消置顶");
                Drawable rightDrawable = context.getResources().getDrawable(R.drawable.pop_icon_quxiaozhiding);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                topOrCancel.setCompoundDrawables(null, rightDrawable, null, null);
            }
            else
            {
                topOrCancel.setText("置顶");
                Drawable rightDrawable = context.getResources().getDrawable(R.drawable.pop_icon_zhiding);
                rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
                topOrCancel.setCompoundDrawables(null, rightDrawable, null, null);
            }
        }
        else
        {
            topOrCancel.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
        //接口回调处理业务逻辑
        move.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                editDialog.dismiss();
                moveBack.dialogBack();
            }
        });
        topOrCancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                editDialog.dismiss();
                topBack.dialogBack();
            }
        });
        report.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                editDialog.dismiss();
                reportBack.dialogBack();
            }
        });
        delete.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                editDialog.dismiss();
                deleteBack.dialogBack();
            }
        });
        cancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                editDialog.dismiss();
            }
        });
    }
}
