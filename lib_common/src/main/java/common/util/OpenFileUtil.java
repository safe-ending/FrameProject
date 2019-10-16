package common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 打开本地各种文件
 */
public class OpenFileUtil {

    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp",
             "video/3gpp"},

            {".apk",
             "application/vnd.android.package-archive"},

            {".asf",
             "video/x-ms-asf"},

            {".avi",
             "video/x-msvideo"},

            {".bin",
             "application/octet-stream"},

            {".bmp",
             "image/bmp"},

            {".c",
             "text/plain"},

            {".class",
             "application/octet-stream"},

            {".conf",
             "text/plain"},

            {".cpp",
             "text/plain"},

            {".doc",
             "application/msword"},

            {".docx",
             "application/msword"},

            {".exe",
             "application/octet-stream"},

            {".gif",
             "image/gif"},

            {".gtar",
             "application/x-gtar"},

            {".gz",
             "application/x-gzip"},

            {".h",
             "text/plain"},

            {".htm",
             "text/html"},

            {".html",
             "text/html"},

            {".jar",
             "application/java-archive"},

            {".java",
             "text/plain"},

            {".jpeg",
             "image/jpeg"},

            {".jpg",
             "image/jpeg"},

            {".js",
             "application/x-javascript"},

            {".log",
             "text/plain"},

            {".m3u",
             "audio/x-mpegurl"},

            {".m4a",
             "audio/mp4a-latm"},

            {".m4b",
             "audio/mp4a-latm"},

            {".m4p",
             "audio/mp4a-latm"},

            {".m4u",
             "video/vnd.mpegurl"},

            {".m4v",
             "video/x-m4v"},

            {".mov",
             "video/quicktime"},

            {".mp2",
             "audio/x-mpeg"},

            {".mp3",
             "audio/x-mpeg"},

            {".mp4",
             "video/mp4"},

            {".mpc",
             "application/vnd.mpohun.certificate"},

            {".mpe",
             "video/mpeg"},

            {".mpeg",
             "video/mpeg"},

            {".mpg",
             "video/mpeg"},

            {".mpg4",
             "video/mp4"},

            {".mpga",
             "audio/mpeg"},

            {".msg",
             "application/vnd.ms-outlook"},

            {".ogg",
             "audio/ogg"},

            {".pdf",
             "application/pdf"},

            {".png",
             "image/png"},

            {".pps",
             "application/vnd.ms-powerpoint"},

            {".ppt",
             "application/vnd.ms-powerpoint"},

            {".pptx",
             "application/vnd.ms-powerpoint"},

            {".prop",
             "text/plain"},

            {".rar",
             "application/x-rar-compressed"},

            {".rc",
             "text/plain"},

            {".rmvb",
             "audio/x-pn-realaudio"},

            {".rtf",
             "application/rtf"},

            {".sh",
             "text/plain"},

            {".tar",
             "application/x-tar"},

            {".tgz",
             "application/x-compressed"},

            {".txt",
             "text/plain"},

            {".wav",
             "audio/x-wav"},

            {".wma",
             "audio/x-ms-wma"},

            {".wmv",
             "audio/x-ms-wmv"},

            {".wps",
             "application/vnd.ms-works"},

            {".xls",
             "application/vnd.ms-excel"},

            {".xlsx",
             "application/vnd.ms-excel"},

            //{".xml",    "text/xml"},
            {".xml",
             "text/plain"},

            {".z",
             "application/x-compress"},

            {".zip",
             "application/zip"},

            {"",
             "*/*"}};


    private static String getMIMEType(File file) {

        String type  = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) { return type; }
    /* 获取文件的后缀名 */
        String fileType = fName.substring(dotIndex, fName.length())
                               .toLowerCase();
        if (fileType == null || "".equals(fileType)) { return type; }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (fileType.equals(MIME_MapTable[i][0])) { type = MIME_MapTable[i][1]; }
        }
        return type;
    }


        public static void openFile(Context context, String filePath) {
            Intent intent = new Intent();
            File   file   = new File(filePath);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//设置标记

            //7.0以上需要
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setAction(Intent.ACTION_VIEW);//动作，查看
            intent.setDataAndType(FileProvider7.getUriForFile(context, file), getMIMEType(file));//设置类型
            context.startActivity(intent);
        }


    /*public static void openFile(Context context, String filePath) {
        File file = new File(filePath);
        *//* 取得扩展名 *//*
        String end = file.getName()
                         .substring(file.getName()
                                        .lastIndexOf(".") + 1,
                                    file.getName()
                                        .length())
                         .toLowerCase(Locale.getDefault());
        Intent intent = null;

        *//* 依扩展名的类型决定MimeType *//*
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals(
                "ogg") || end.equals("wav"))
        {
            intent = getAudioFileIntent(context, filePath);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            intent = getVideoFileIntent(context, filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals(
                "bmp"))
        {
            intent = getImageFileIntent(context, filePath);
        } else if (end.equals("apk")) {
            intent = getApkFileIntent(context, filePath);
        } else if (end.equals("ppt")) {
            intent = getPptFileIntent(context, filePath);
        } else if (end.equals("xls")) {
            intent = getExcelFileIntent(context, filePath);
        } else if (end.equals("doc")) {
            intent = getWordFileIntent(context, filePath);
        } else if (end.equals("pdf")) {
            intent = getPdfFileIntent(context, filePath);
        } else if (end.equals("chm")) {
            intent = getChmFileIntent(context, filePath);
        } else if (end.equals("txt")) {
            intent = getTextFileIntent(context, filePath, false);
        } else {
            intent = getAllIntent(context, filePath);
        }

        context.startActivity(intent);
    }*/

    // Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(Context context, String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)), "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(Context context, String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)),
                              "application/vnd.android.package-archive");
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)), "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)), "audio/*");
        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(Context context, String param) {

        Uri uri = Uri.parse(param)
                     .buildUpon()
                     .encodedAuthority("com.android.htmlfileprovider")
                     .scheme("content")
                     .encodedPath(param)
                     .build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)), "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)),
                              "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)),
                              "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)),
                              "application/msword");
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)),
                              "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(Context context, String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri = Uri.parse(param);
            intent.setDataAndType(uri, "text/plain");
        } else {
            intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)),
                                  "text/plain");
        }
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(Context context, String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(FileProvider7.getUriForFile(context, new File(param)),
                              "application/pdf");
        return intent;
    }

}