package common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.at.arouter.common.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * desc:  新生态公共类
 * author:  yangtao
 * <p>
 * creat:  2018/12/6 10:50
 */

public class CommonUtils {

    /**
     * 复制到粘贴板
     *
     * @param context
     * @param content 复制的内容
     */
    public static void copy(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show(context.getString(R.string.copy_null));
            return;
        }
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        if (cm != null) {
            cm.setPrimaryClip(ClipData.newPlainText("text", content));
            ToastUtil.show(context.getString(R.string.copy));
        }
    }


    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    /**
     * 正则匹配 同ios
     *
     * @param content
     * @return
     */
    public static boolean checkPhone(String content) {
        String p = "^(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$";
        return !TextUtils.isEmpty(content) && content.matches(p);
    }

    /**
     * 正则匹配密码 同ios
     *
     * @param content
     * @return
     */
    public static boolean checkPwd(String content) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$");
        Matcher matcher = p.matcher(content);
        return !TextUtils.isEmpty(content) && matcher.find();
    }


    public static String stringFilter2(String str, String pach) throws PatternSyntaxException {
        //只允许数字和汉字和字母
        String regEx = pach;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    //关闭软键盘
    public static void hintSoftInput(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 隐藏显示软键盘
     *
     * @param activity 当前Activity
     */
    public static void softkeyboard(Activity activity, View editText, boolean status) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && status) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        } else if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    //dilaog关闭软键盘
    public static void autoSoftInput(Dialog dialog, EditText editText) {
        //解决dilaog中EditText无法弹出输入的问题
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //弹出对话框后直接弹出键盘
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 100);
    }



    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取App版本号
     *
     * @return App版本号
     */
    public static String getAppVersionName() {
        if (isSpace(Utils.getContext().getPackageName())) return null;
        try {
            PackageManager pm = Utils.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(Utils.getContext().getPackageName(), 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getImageMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "image/jpeg";
        }
        if (fileName.toLowerCase()
                .contains(".jpg")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase()
                .contains(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.toLowerCase()
                .contains(".png")) {
            return "image/png";
        } else if (fileName.toLowerCase()
                .contains(".gif")) {
            return "image/gif";
        }
        return "";
    }

    /**
     * 判断是否缺少权限
     * true:没有权限
     * false:已开启
     */
    public static boolean lacksPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

}
