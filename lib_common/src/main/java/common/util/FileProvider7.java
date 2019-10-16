package common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;


import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.at.arouter.common.R;
import common.data.Constants;

import java.io.File;

/**
 * 在官方7.0的以上的系统中，尝试传递 file://URI可能会触发FileUriExposedException。
 */
public class FileProvider7 {
    public static final int REQUEST_8_INSTALL_PERMISSION = 0X2001;

    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static Uri getUriForFile24(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context,
                Constants.PROVIDE,
                file);
        return fileUri;
    }


    public static void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);      //com.at.wallet.fileprovider
            Uri contentUri = FileProvider.getUriForFile(context, Constants.PROVIDE, new File(file.getAbsolutePath()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    ToastUtil.show(context.getResources().getString(R.string.install_t4));
                    startInstallPermissionSettingActivity(context);
                    return;
                }
            }

        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        }
        context.startActivity(intent);
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startInstallPermissionSettingActivity(Context context) {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ((Activity)context).startActivityForResult(intent,REQUEST_8_INSTALL_PERMISSION);
    }
}