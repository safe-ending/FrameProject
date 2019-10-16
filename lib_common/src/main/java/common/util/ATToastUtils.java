package common.util;

import android.content.Context;

import com.at.arouter.common.R;

/**
 * desc:  公共提示语类
 * author:  yangtao
 * <p>提示语通用规则
 * 1 如果必填项，没填，就写上  ”请输入+ 必填项名称“
 * 2 如果输入的不符合规则，就提示  ”字段+格式不正确，请重新输入“
 * 3  操作成功提示  ”动词（操作）+成功“
 * 4  登录密码方面： 密码格式输入不正确(6-16位数字字母组合)  两次密码输入不同，请重新输入
 * 6、安全或交易密码：6位数字；手势；faceID或fingerID；
 * 7、输入框：请输入+字段
 * 密码、法币值（有的有要求）
 * 8、网络问题、环境问题、系统连接失败等问题，提示语：网络不稳定，请稍后重试。
 * creat:  2019/1/16 16:38
 */

public class ATToastUtils {

    //通用场景1 必填输入为空时
    public static void checkNull(Context context, String name) {
        ToastUtil.show(context.getResources().getString(R.string.checknull1, name));
    }

    //通用场景1.1 必填输入为空带参数 eg “请输入兑换数量（0.01-10000）”
    public static void checkNull(Context context, String name, String count1, String count2) {
        ToastUtil.show(context.getResources().getString(R.string.checknull2, name, count1, count2));
    }

    //通用场景2 输入XXX格式不正确
    public static void checkFormat(Context context, String name) {
        ToastUtil.show(context.getResources().getString(R.string.checkformat1, name));
    }

    //通用场景2.1 带参数 eg:单价格式不正确，请重新输入(0.01-10000)
    public static void checkFormat(Context context, String name, String count1, String count2) {
        ToastUtil.show(context.getResources().getString(R.string.checkformat2, name, count1, count2));

    }

    //通用场景3 操作成功提示
    public static void success(Context context, String name) {
        ToastUtil.show(context.getResources().getString(R.string.checksuccess, name));
    }

    //通用场景3.1 操作失败提示
    public static void failure(Context context, String name) {
        ToastUtil.show(context.getResources().getString(R.string.checkfailure, name));
    }

    //通用场景 直接显示
    public static void show(String name) {
        ToastUtil.show(name);
    }
    public final static int LoginPwdMarkedType = 0;   //登录密码
    public final static int TradePwdMarkedType = 1;   //交易密码

    public final static int EmptyPwdErrorType = 2;
    public final static int EmptyAgainPwdErrorType = 3;
    public final static int FormatPwdErrorType = 4;  //密码格式不对
    public final static int AtypismPwdErrorType = 5;  //新旧密码不一致


    ////密码场景4 密码非空情况时--------专属密码使用，如需增加，直接在PwdMarkedType 增加其他密码，暂有登录和交易两种
    public static void checkPwd(Context context, int pwdType, int errorType) {
        String msg = "";
        switch (errorType) {
            case EmptyPwdErrorType:
                //密码为空

                msg = context.getResources().getString(R.string.checkstate1, (pwdType == LoginPwdMarkedType ? context.getResources().getString(R.string.statelogin) : context.getResources().getString(R.string.state_exchange)));
                break;
            case EmptyAgainPwdErrorType:
                //再次输入的密码为空
                msg = context.getResources().getString(R.string.checkstate2, (pwdType == LoginPwdMarkedType ? context.getResources().getString(R.string.statelogin) : context.getResources().getString(R.string.state_exchange)));
                break;
            case FormatPwdErrorType:
                //密码格式不正确
                msg = context.getResources().getString(R.string.checkstate3, (pwdType == LoginPwdMarkedType ? context.getResources().getString(R.string.statelogin) : context.getResources().getString(R.string.state_exchange)), (pwdType == LoginPwdMarkedType ? context.getResources().getString(R.string.gist1) : context.getResources().getString(R.string.gist2)));
                break;
            case AtypismPwdErrorType:
                //新旧密码不一致
                msg = context.getResources().getString(R.string.checkstate4);
                break;
        }
        ToastUtil.show(msg);

    }

    ////通用场景 暂未开放，敬请期待
    public static void waitBuild(Context context) {
        ToastUtil.show(context.getResources().getString(R.string.checkwait));

    }

    private static final int UNAUTHORIZED = 401;
    private static final int TOO_LARGE = 413;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    //通用场景六 网络请求提示
    //除401token失效   413未压缩图片   其余全报网络异常
    public static void requestErr(Context context,int code) {
        String ex;
        switch (code){
            case UNAUTHORIZED:
                ex = context.getString(R.string.token_out);  //失效
                break;
            case TOO_LARGE:
                ex = context.getString(R.string.large_image);  //压缩图片
                break;

            case FORBIDDEN:
            case NOT_FOUND:
            case REQUEST_TIMEOUT:
            case GATEWAY_TIMEOUT:
            case INTERNAL_SERVER_ERROR:
            case BAD_GATEWAY:
            case SERVICE_UNAVAILABLE:
            default:
                ex = context.getString(R.string.net_err);  //均视为网络错误
                break;
        }

        ToastUtil.show(ex);

    }


}
