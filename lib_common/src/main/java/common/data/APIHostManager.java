package common.data;

/**
 * Created by yangtao on 2019/1/11.
 *
 * release版本的默认域名  以及debug 域名全局变量
 *
 * 注意：retrofit2.3版本下的baseurl需要以/结尾   在Service接口中不能以/开头
 */

public class APIHostManager {
//    http://192.168.0.65:8011/  测试环境http://192.168.0.101:8011/
    public static String Common_Url = "http://192.168.0.101:8011/";//登录、朋友圈
    public static String IM_Url = "http://192.168.0.250:1000/";//IM
    public static String Game_Url = "http://192.168.0.65:8011/";//
}
