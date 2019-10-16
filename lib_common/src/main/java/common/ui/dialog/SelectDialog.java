package common.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.at.arouter.common.R;

import java.util.List;

/**
 * Created by 1 on 2016/10/13.
 */
public class SelectDialog {

    public static void showList(Context context, int which, final OnItemSelectedListener listener, List<String> items) {
        show(context, which, "", listener, (String[]) items.toArray());
    }

    public static Dialog show(Context context, int which, final OnItemSelectedListener listener, final String... items) {
        return show(context, which, "", listener,null, items,null);
    }

    public static Dialog show(Context context, int which, final OnItemSelectedStringListener listener, final String... items) {
        return show(context, which, "", null,listener,null, items,null);
    }

    public static Dialog show(Context context, int which, final OnItemSelectedListener listener, String[] items,String[] hints) {
        return show(context, which, "", listener,null, items,hints);
    }

    public static Dialog show(Context context, int which, String title, final OnItemSelectedListener listener, final String... items){
        return show(context, which, title, listener, null,items,null);
    }

    public static Dialog show(Context context, int which, final OnItemSelectedListener listener, final OnCancelListener cancelListener, final String... items){
        return show(context, which, "", listener, cancelListener,items,null);
    }

    public static Dialog show(Context context, int which, String title, final OnItemSelectedListener listener, final OnCancelListener cancelListener, String[] items,String[] hints) {
        return show(context, which, title, listener, null, cancelListener,items,null);
    }

    public static Dialog show(Context context, int which, String title, final OnItemSelectedListener listener, OnItemSelectedStringListener strListener,final OnCancelListener cancelListener, String[] items,String[] hints){
        return show(context, which, title, listener, null, cancelListener,items,null,null,null);
    }

    public static Dialog show(Context context, int which, String title,OnItemSelectedListener listener, String[] items, int[] itemsColor){
        return show(context, which, title, listener, null,null,items,null,itemsColor,null);
    }

    public static Dialog show(Context context, int which, String title, final OnItemSelectedListener listener, OnItemSelectedStringListener strListener,
                              final OnCancelListener cancelListener, String[] items,String[] hints,int[] itemsColor,int[] hintsColor){
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.dialog_bond).create();
        if(context == null || !(context instanceof Activity)){
            return dialog;
        }
        Activity activity = (Activity) context;
        if(activity.isDestroyed() || activity.isFinishing()){
            return dialog;
        }
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_common_select, null);
        View line = view.findViewById(R.id.view_line);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        final LinearLayout linItems = (LinearLayout) view.findViewById(R.id.lin_items);
        //        linItems.removeAllViews();
        for (int i = 0; i < items.length; i++) {
            final int j = i;
            String str = items[i];
            View itemView = inflater.inflate(R.layout.item_common_select, null);
            TextView tvName = itemView.findViewById(R.id.tv_item_name);
            TextView tvHint = itemView.findViewById(R.id.tv_item_hint);
            ImageView imgSelected = itemView.findViewById(R.id.img_selected);
            View lineView = itemView.findViewById(R.id.view_line);
            if (i == items.length - 1) {
                lineView.setVisibility(View.INVISIBLE);
            } else {
                lineView.setVisibility(View.VISIBLE);
            }
            tvName.setText(str);
            if(itemsColor != null && itemsColor.length > i){
                tvName.setTextColor(itemsColor[i]);
            }
            if(hintsColor != null && hintsColor.length > i){
                tvHint.setTextColor(hintsColor[i]);
            }

            tvName.setGravity(which < 0?Gravity.CENTER:Gravity.LEFT);

            if (j == which) {
                imgSelected.setVisibility(View.VISIBLE);
                imgSelected.setSelected(true);
            } else {
                imgSelected.setVisibility(View.INVISIBLE);
            }
            if(hints != null && hints.length > i && !TextUtils.isEmpty(hints[i])){
                tvHint.setVisibility(View.VISIBLE);
                tvHint.setText(hints[i]);
            }else{
                tvHint.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int k = 0; k < items.length; k++) {
                        View view = linItems.getChildAt(k);
                        ImageView imgSelected = (ImageView) view.findViewById(R.id.img_selected);
                        if (k == j) {
                            imgSelected.setVisibility(View.VISIBLE);
                        } else {
                            imgSelected.setVisibility(View.INVISIBLE);
                        }
                    }
                    if(listener != null){
                        listener.onItemSelected(j);
                    }
                    if(strListener != null){
                        strListener.onItemSelected(str);
                    }
                    dialog.dismiss();
                    //                    dialog = null;
                }
            });
            linItems.addView(itemView);
        }

        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(cancelListener != null){
                    cancelListener.onCancel();
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
        win.setGravity(Gravity.BOTTOM);
        win.setWindowAnimations(R.style.mystyle);  //

        return dialog;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int which);
    }

    public interface OnItemSelectedStringListener{
        void onItemSelected(String which);
    }

    public interface OnCancelListener{
        void onCancel();
    }

}
