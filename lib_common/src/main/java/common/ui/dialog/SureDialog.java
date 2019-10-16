package common.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.at.arouter.common.R;

/**
 * Created by lky on 2018/8/21
 */
public class SureDialog {

    private static SureDialog commonSureDialog;

    private SureDialog() {

    }

    public static SureDialog getInstance() {
        if (commonSureDialog == null) {
            synchronized (SureDialog.class) {
                if (commonSureDialog == null) {
                    commonSureDialog = new SureDialog();
                }
            }
        }
        return commonSureDialog;
    }

    private void showDialog(Context context, String msg, final OnSureClickListener listener) {
        showDialog(context, "", msg, listener);
    }

    private void showDialog(Context context, String msg, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        showDialog(context, "", msg, listener, cancelClickListener);
    }

    private void showDialog(Context context, String title, String msg, final OnSureClickListener listener) {
        showDialog(context, title, msg, listener, null);
    }

    /**
     * 只有确定没有取消按钮的提示框
     */
    public void showSingleDialog(Context context, CharSequence msg,final OnSureClickListener listener){
        showSingleDialog(context,"",msg,"",listener,true);
    }

    /**
     * 只有确定没有取消按钮的提示框
     */
    public void showSingleDialog(Context context, CharSequence msg, String sureText,final OnSureClickListener listener, boolean cancelable){
        showSingleDialog(context,"",msg,sureText,listener,cancelable);
    }

    /**
     * 只有确定没有取消按钮的提示框
     */
    public void showSingleDialog(Context context, CharSequence msg, final OnSureClickListener listener, boolean cancelable){
        showSingleDialog(context,"",msg,"",listener,cancelable);
    }

    /**
     * 只有确定没有取消按钮的提示框
     */
    public void showSingleDialog(Context context, CharSequence title, CharSequence msg, final OnSureClickListener listener, boolean cancelable){
        showSingleDialog(context,title,msg,"",listener,cancelable);
    }


    /**
     * 只有确定没有取消按钮的提示框
     */
    public void showSingleDialog(Context context, CharSequence title, CharSequence msg, String sureText,final OnSureClickListener listener, boolean cancelable){
        if(context == null || !(context instanceof Activity)){
            return ;
        }
        Activity activity = (Activity) context;
        if(activity.isDestroyed() || activity.isFinishing()){
            return ;
        }
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.dialog_bond).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(cancelable);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_common_sure, null);
        TextView textView = (TextView) view.findViewById(R.id.dialog_text);
        TextView tvtitle = (TextView) view.findViewById(R.id.dialog_title);
        TextView tvSure = (TextView) view.findViewById(R.id.dialog_sure);
        TextView tvCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        if (TextUtils.isEmpty(title)) {
            textView.setVisibility(View.GONE);
            tvtitle.setText(msg);
        } else {
            textView.setVisibility(View.VISIBLE);
            tvtitle.setText(title);
            textView.setText(msg);
        }
        tvCancel.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(sureText)) {
            tvSure.setText(sureText);
        }else{
            tvSure.setText(R.string.sure);
          }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSureClick();
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setContentView(view);
        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }


    private void showDialog(Context context, String title, String msg, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        showDialog(context,title,msg,"","",true,listener,cancelClickListener);
    }

    private void showDialog(Context context, String msg, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener, boolean cancelable) {
        showDialog(context,"",msg,"","",true,listener,cancelClickListener);
    }

    public void showDialog(Context context, String msg, String cancelText, String sureText, final OnSureClickListener listener) {
        showDialog(context,"",msg,cancelText,sureText,true,listener,null);
    }

    public void showDialog(Context context,View childView,OnSureClickListener listener){
        showDialog(context,"",0,childView,listener);
    }

    public void showDialog(Context context,String sureTxt,int sureColor,View childView,OnSureClickListener listener){
        showDialog(context,"","","",0,sureTxt,sureColor,false,childView,listener,null);
    }

    public void showDialog(Context context, String msg, String cancelText, String sureText, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        showDialog(context,"",msg,cancelText,sureText,true,listener,cancelClickListener);
    }

    public void showDialog(Context context, String msg, String cancelText, String sureText, boolean isCancel, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        showDialog(context,"",msg,cancelText,sureText,isCancel,listener,cancelClickListener);
    }

    public void showDialog(Context context, String msg, String sureText,int sureColor, final OnSureClickListener listener){
        showDialog(context,"",msg,sureText,sureColor,listener);
    }

    public void showDialog(Context context, String title,String msg, String sureText,int sureColor, final OnSureClickListener listener){
        showDialog(context,title,msg,"",0,sureText,sureColor,true,null,listener,null);
    }

    public void showDialog(Context context, String title,String msg,String cancelText, String sureText, boolean isCancel, final OnSureClickListener listener) {
        showDialog(context,title,msg,cancelText,sureText,isCancel,listener,null);
    }
    private void showDialog(Context context, String title, String msg, String cancelText, String sureText, boolean isCancel, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener){
        showDialog(context,title,msg,cancelText,0,sureText,0,isCancel,null,listener,cancelClickListener);
    }


    private void showDialog(Context context, String title, String msg, String cancelText, int cancelColor,String sureText, int sureColor,boolean isCancel, View childView,final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        if(context == null || !(context instanceof Activity)){
            return ;
        }
        Activity activity = (Activity) context;
        if(activity.isDestroyed() || activity.isFinishing()){
            return ;
        }
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.dialog_bond).create();
        dialog.setCanceledOnTouchOutside(isCancel);
        dialog.setCancelable(isCancel);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_common_sure, null);
        TextView textView = (TextView) view.findViewById(R.id.dialog_text);
        TextView tvSure = (TextView) view.findViewById(R.id.dialog_sure);
        TextView tvtitle = (TextView) view.findViewById(R.id.dialog_title);
        TextView tvCancel = (TextView) view.findViewById(R.id.dialog_cancel);
        FrameLayout frameLayout = view.findViewById(R.id.common_sure_child_layout);
        if(childView != null && childView.getParent() == null){
            frameLayout.addView(childView);
            frameLayout.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(title)) {
            tvtitle.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(msg);
        } else {
            textView.setVisibility(View.VISIBLE);
            tvtitle.setText(title);
            textView.setText(msg);
        }

        if (TextUtils.isEmpty(msg)) {
            textView.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(sureText)) {
            tvSure.setText(sureText);
        }
        if (!TextUtils.isEmpty(cancelText)) {
            tvCancel.setText(cancelText);
        }
        if(sureColor != 0){
            tvSure.setTextColor(sureColor);
        }
        if(cancelColor != 0){
            tvCancel.setTextColor(cancelColor);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //                dialog = null;
                if (cancelClickListener != null) {
                    cancelClickListener.onCancelClickListener();
                }
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onSureClick();
                }
                //                dialog = null;
            }
        });
        dialog.show();
        dialog.setContentView(view);
        Window win = dialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }


    public static void show(Context context, String title, String msg, final OnSureClickListener listener) {
        getInstance().showDialog(context, title, msg, listener);
    }

    public static void show(Context context, String msg, final OnSureClickListener listener) {
        getInstance().showDialog(context, msg, listener);
    }

    public static void show(Context context, String title, String msg, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        getInstance().showDialog(context, title, msg, listener, cancelClickListener);
    }

    public static void show(Context context, String msg, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        getInstance().showDialog(context, msg, listener, cancelClickListener);
    }

    public static void show(Context context, String msg, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener, boolean cancelable) {
        getInstance().showDialog(context, msg, listener, cancelClickListener, cancelable);
    }



    public static void show(Context context, String msg, String cancelText, String sureText, final OnSureClickListener listener) {
        getInstance().showDialog(context, msg, cancelText, sureText, listener);
    }

    public static void show(Context context, String msg, String cancelText, String sureText, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        getInstance().showDialog(context, msg, cancelText, sureText, listener, cancelClickListener);
    }

    public static void show(Context context, String msg, String cancelText, String sureText,boolean isCancel, final OnSureClickListener listener, final OnCancelClickListener cancelClickListener) {
        getInstance().showDialog(context, msg, cancelText, sureText, isCancel, listener, cancelClickListener);
    }

    /**
     * 只有确定没有取消按钮的提示框
     */
    public static void showHint(Context context, CharSequence msg, final OnSureClickListener listener){
        getInstance().showSingleDialog(context,msg,listener);
    }

    /**
     * 只有确定没有取消按钮的提示框
     */
    public static void showHint(Context context, CharSequence msg, String sureText,final OnSureClickListener listener, boolean cancelable){
        getInstance().showSingleDialog(context,msg,sureText,listener,cancelable);
    }

    /**
     * 只有确定没有取消按钮的提示框
     */
    public static void showHint(Context context, CharSequence msg, final OnSureClickListener listener, boolean cancelable){
        getInstance().showSingleDialog(context,msg,listener,cancelable);
    }

    /**
     * 只有确定没有取消按钮的提示框
     */
    public static void showHint(Context context, CharSequence title,CharSequence msg, final OnSureClickListener listener){
        getInstance().showSingleDialog(context,title,msg,listener,true);
    }

    public interface OnSureClickListener {
        void onSureClick();
    }

    public interface OnCancelClickListener {
        void onCancelClickListener();
    }
}
