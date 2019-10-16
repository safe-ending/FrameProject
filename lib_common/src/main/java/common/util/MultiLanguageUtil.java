package common.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;


import common.data.AppPref;
import common.data.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

/**
 * 多语言工具类
 * Created by wzq on 2018/8/1.
 */
public class MultiLanguageUtil {

    public static final String TYPE_CN = "cn";
    public static final String TYPE_EN = "en";
    public static final String TYPE_jp = "jp";
    public static final String Korea = "Korea";
    public static final String Français = "Français";
    public static final String Deutsc = "Deutsc";
    public static final String In_Italiano = "In Italiano";
    public static final String El_español = "El español";
    public static final String Nederlands = "Nederlands";

    /**
     * 获取系统语言类型
     */
    public static String getSystemLanguageType() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        if (locale.getLanguage().equals("zh")) {
//            if (locale.getCountry().equals("CN")) {
            return TYPE_CN;//简体中文
//            }else {
//                return MultiLanguageType.TW;//繁体中文
//            }
        } else if (locale.getLanguage().equals(TYPE_EN)) {
            return TYPE_EN;
        }
        return TYPE_CN;
    }

    /**
     * 更新本地语言环境
     *
     * @param context 应用上下文 (在Android 8.0 以上不能是 ApplicationContext)
     */
    public static void autoUpdateLanguageEnviroment(Context context) {
        AppPref appPref = new AppPref(context, AppPref.APP_PREF);
        //语言环境适配顺序 ：用户自定义配置 - app配置 - 系统配置
        String type = appPref.getLanguage();

        if (type != null) {
            //优先取用户自定义配置
            updateLanguageEnviroment(context, type);
        } else {
            //取系统语言
            updateLanguageEnviroment(context, getSystemLanguageType());
        }
    }

    /**
     * 更新语言环境
     *
     * @param context      应用上下文 (在Android 8.0 以上不能是 ApplicationContext)
     * @param languageType 语言类型
     */
    public static void updateLanguageEnviroment(Context context, String languageType) {
        if (languageType == null || context == null) {
            return;
        }
        AppPref appPref = new AppPref(context, AppPref.APP_PREF);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();

        Locale locale;
        switch (languageType) {
            case TYPE_CN:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case TYPE_EN:
                locale = Locale.ENGLISH;
                break;

            default:
                locale = Locale.SIMPLIFIED_CHINESE;
                languageType = TYPE_CN;
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            config.setLocales(new LocaleList(locale));
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
        ((Activity)context).recreate();

        //同步更新 - app语言环境配置记录更新
        appPref.setLanguage(languageType);
        EventBus.getDefault().post(Constants.EVENT_REFRESH_LANGUAGE);
    }
}