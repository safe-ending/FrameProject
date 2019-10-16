package common.util;

import android.content.Context;

import cc.duduhuo.applicationtoast.AppToast;


public class ToastUtil {

    public static void show(String message) {
        AppToast.showToast(message);
    }

    public static void show(Context context, String message) {
        AppToast.showToast(message);
    }

}
