package common.util.crash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;

import common.util.ActivityUtils;
import common.util.FileUtils;
import common.util.ToastUtil;
import common.util.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 错误日志收集，上传
 *
 * @author kongdq
 */
public class CrashHandlerException implements UncaughtExceptionHandler {
    private static final String TAG = CrashHandlerException.class.getCanonicalName();

    //public static final boolean DEBUG = true;
    private UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private static CrashHandlerException mCrashHandlerException;
    private SDCardHelper mSDCardHelper;

    /**
     * 日志的输出格式
     */
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
    private SimpleDateFormat mCrashSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String CRASH_REPORTER_EXTENSION = ".txt";
    private static final String PATH_LOG = FileUtils.PATH_LOG;

    private CrashHandlerException() {
        mSDCardHelper = SDCardHelper.getInstance();
    }

    public static CrashHandlerException getInstance() {
        if (mCrashHandlerException == null) {
            mCrashHandlerException = new CrashHandlerException();
        }
        return mCrashHandlerException;
    }

    public void initCrashHandlerException(Context context) {
        this.mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        if (!handleException(exception) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, exception);
        }
    }

    private void saveCrashInfoToFile(Throwable exception) {
        // 是否挂载SD卡
        if (mSDCardHelper.sdCardMounted()) {
            String logPath = getLogDirectoryPath();
            if (logPath != null) {
                FileWriter fileWriter = null;
                try {
                    fileWriter = new FileWriter(logPath + File.separator + mSimpleDateFormat.format(new Date())
                            + CRASH_REPORTER_EXTENSION, true);

                    fileWriter.write(getLogInfo(exception));

                } catch (IOException e) {
                    Log.e("crash handler", "load file failed...", e.getCause());
                } finally {
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * @param exception
     * @return
     */
    private String getLogInfo(Throwable exception) {
        String versioninfo = getVersionInfo();
        String deviceInfo = getDeviceInfo();
        String crashInfo = getCrashInfo(exception);
        //
        final StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("\n" + "=====Version Info=====" + "\n");
        logBuffer.append("\n" + "=====Version Info=====" + "\n");
        logBuffer.append(versioninfo + "\n");
        logBuffer.append("\n" + "=====Device Info======" + "\n");
        logBuffer.append(deviceInfo + "\n");
        logBuffer.append("=====CrashInfo======" + "\n");
        logBuffer.append("Crash Time" + " : " + mCrashSimpleDateFormat.format(new Date()) + "\n");
        logBuffer.append("\n" + "=====Crash Track Info======" + "\n");
        logBuffer.append(crashInfo + "\n");

        return logBuffer.toString();
    }

    // 处理异常
    private boolean handleException(final Throwable exception) {
        Log.e("crash handler", "CrashHandlerException ... handleException()");
        if (exception == null || mContext == null) {
            return false;
        }
        ToastUtil.show("测试日志提示：LuxorWallet由于异常奔溃，请前往文件目录下/LuxorWallet/log/查看错误信息");

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();

                exception.printStackTrace();
                saveCrashInfoToFile(exception);
                //若需要提交给服务器再调
                try {
                    showCrashDialog(exception);
                } catch (Exception e) {
                    e.printStackTrace();
                    exitSystem();
                }
                Looper.loop();
            }
        }.start();

        return true;
    }

    // 弹出异常停止的对话框
    private void showCrashDialog(final Throwable exception) {
        int uploadButton = mContext.getResources().getIdentifier("sure", "string", mContext.getPackageName());
        int okButton = mContext.getResources().getIdentifier("ignore", "string", mContext.getPackageName());
        int message = mContext.getResources().getIdentifier("app_crash_exception", "string", mContext.getPackageName());
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setMessage(message);
        builder.setTitle("测试日志提示：LuxorWallet由于异常奔溃，请前往文件目录下/LuxorWallet/log/查看错误信息");
//        builder.setPositiveButton(uploadButton, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                //后面有需要再提交到服务器
////                uploadErrorMessage(getLogInfo(exception));
//                exitSystem();
//                dialog.dismiss();
//            }
//        });
//        builder.setNegativeButton(okButton, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                exitSystem();
//                dialog.dismiss();
//            }
//        });
//        dialog = builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
    }

    // 获取异常信息
    private String getCrashInfo(Throwable exception) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);
        printWriter.close();
        String crash = writer.toString();
        return crash;
    }

    // 获取设备信息
    private String getDeviceInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("BRAND" + " : " + Build.BRAND + "\n");
        sb.append("MODEL" + " : " + Build.MODEL + "\n");

        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        String px = "Resolution:" + dm.widthPixels + " * " + dm.heightPixels + "\n";
        sb.append(px);
        sb.append("Android SDK Version" + " : " + Build.VERSION.RELEASE + "\n");
        return sb.toString();
    }

    // 获取版本信息
    private String getVersionInfo() {
        String version = "Unknow Version";
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
            version = "APP Version" + " : " + info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    private String getLogDirectoryPath() {
        String logDirPath = null;
        if (Environment.getExternalStorageDirectory() != null) {
            Date date = new Date();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

            String path = PATH_LOG;
            path += File.separator + simpleDateFormat.format(date);
            File file = new File(path);
            if (!file.isDirectory()) {
                if (file.mkdirs()) {
                    logDirPath = path;
                }
            } else {
                logDirPath = path;
            }
        }

        return logDirPath;
    }

    private void exitSystem() {
        // 退出应用
//        MobclickAgent.onKillProcess(mContext);
        ActivityUtils.getInstance(Utils.getContext()).close();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

    /**
     * 错误信息上传
     */
    private void uploadErrorMessage(final String errorInfo) {
//        final RequestObject request = new RequestObject() {
//            @Override
//            public void reqSuccess(ResultData result) {
//
//            }
//
//            @Override
//            public void reqFail(int failCode, String failInfo) {
//                exitSystem();
//            }
//        };
//        HashMap<String, String> params = new HashMap<>();
//        params.put("errorMsg", errorInfo);
//        params.put("userId", LoginUserHandler.getUserId(mContext));
//
//        VolleyRequest.getInstance(mContext).doPostRequest(request, new ResultData(), "uploadErrorMessage", ConnectUrl.URL_ERROR_UPLOAD, params);
    }

}
