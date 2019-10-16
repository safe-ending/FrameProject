package common.util.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.AppOpsManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 作者 : Joker
 * 创建日期 : 2017-11-13
 * 修改日期 :
 * 版权所有 :
 */

public class PermissionsUtil {

    public static final String TAG = "PermissionGrantor";
    private static HashMap<String, PermissionListener> listenerMap = new HashMap();

    /**
     * 申请授权，当用户拒绝时，会显示默认一个默认的Dialog提示用户
     *
     * @param activity
     * @param listener
     * @param permission 要申请的权限
     */
//    public static void requestPermission(Activity activity, PermissionListener listener, String... permission) {
//        requestPermission(activity, listener, permission, true, null, null);
//    }

    /**
     * 申请授权，当用户拒绝时，会显示一个缺少某权限的Dialog提示用户
     *
     * @param activity
     * @param listener
     * @param permission 要申请的权限
     */
    public static void requestPermission(Activity activity, PermissionListener listener, String perContent, String... permission) {
        requestPermission(activity, listener, permission, true, perContent, null);
    }

    /**
     * 申请授权，当用户拒绝时，可以设置是否显示Dialog提示用户，也可以设置提示用户的文本内容
     *
     * @param activity
     * @param listener
     * @param permission 需要申请授权的权限
     * @param showTip    当用户拒绝授权时，是否显示提示
     * @param tip        当用户拒绝时要显示Dialog设置
     */
    public static void requestPermission(@NonNull Activity activity, @NonNull PermissionListener listener
            , @NonNull String[] permission, boolean showTip, String perContent, @Nullable TipInfo tip) {


        if (listener == null) {
            Log.e(TAG, "listener is null");
            return;
        }

        if (Build.VERSION.SDK_INT < 23) {
            if (PermissionsUtil.hasPermission(activity, permission)) {
                listener.permissionGranted(permission);
            } else {
                listener.permissionDenied(permission);
            }
            Log.e(TAG, "API level : " + Build.VERSION.SDK_INT + "不需要申请动态权限!");
            return;
        }

        String key = String.valueOf(System.currentTimeMillis());
        listenerMap.put(key, listener);
        Intent intent = new Intent(activity, PermissionActivity.class);
        intent.putExtra("permission", permission);
        intent.putExtra("key", key);
        intent.putExtra("showTip", showTip);
        intent.putExtra("per", perContent);
        intent.putExtra("tip", tip);

        activity.startActivity(intent);
    }


    /**
     * 判断权限是否授权
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {

        if (permissions.length == 0) return false;

        for (String per : permissions) {
            int result = PermissionChecker.checkSelfPermission(context, per);
            if (result != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * 系统层的权限判断
     *
     * @param context     上下文
     * @param permissions 申请的权限 Manifest.permission.READ_CONTACTS
     * @return 是否有权限 ：其中有一个获取不了就是失败了
     */
    public static boolean hasSystemPermission(@NonNull Context context, @NonNull String[] permissions) {
        if (permissions.length == 0) return false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        for (String permission : permissions) {
            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) continue;
            int result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result == AppOpsManagerCompat.MODE_IGNORED) return false;
            result = ContextCompat.checkSelfPermission(context, permission);
            if (result != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    /**
     * 判断一组授权结果是否为授权通过
     *
     * @param grantResult
     * @return
     */
    public static boolean isGranted(@NonNull int... grantResult) {

        if (grantResult.length == 0) {
            return false;
        }

        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 跳转到当前应用对应的设置页面
     *
     * @param context
     */
    public static void gotoSetting(@NonNull Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * @param key
     * @return
     */
    static PermissionListener fetchListener(String key) {
        return listenerMap.remove(key);
    }


    public static class TipInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        String title;
        String content;
        String cancel;  //取消按钮文本
        String ensure;  //确定按钮文本

        public TipInfo(@Nullable String title, @Nullable String content, @Nullable String cancel, @Nullable String ensure) {
            this.title = title;
            this.content = content;
            this.cancel = cancel;
            this.ensure = ensure;
        }
    }
}
