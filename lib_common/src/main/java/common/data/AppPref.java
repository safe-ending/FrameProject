package common.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * desc:  本地缓存信息
 * author:  yangtao
 * <p>
 * creat: 2018/8/24 16:05
 */

public class AppPref {
    //公用管理对象 退出不清除配置
    public final static String APP_PREF = "app_preferences";
    //临时管理对象 退出清空
    public final static String TEMP_PREF = "temp_preferences";

    //语言选择
    public final static String KEY_APP_LANGUAGE = "key_app_language";

    public final static String KEY_HOST = "key_host";


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    //默认钱包配置
    public AppPref(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            name = TEMP_PREF;
        } else {
            name = APP_PREF;
        }
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    //语言切换
    public String getLanguage() {
        return sp.getString(KEY_APP_LANGUAGE, "cn");
    }

    public void setLanguage(String typeKey) {
        if (TextUtils.isEmpty(typeKey)) {
            editor.remove(KEY_APP_LANGUAGE);
        } else {
            editor.putString(KEY_APP_LANGUAGE, typeKey);
        }
        editor.commit();
    }


    //----------host
    public String getHost() {
        return sp.getString(KEY_HOST, APIHostManager.Common_Url + "," + APIHostManager.IM_Url + "," + APIHostManager.Game_Url);
    }

    public void setHost(String host) {
        editor.putString(KEY_HOST, host);
        editor.commit();
    }

}
