package common.data;

/**
 * 常量类
 */
public class Constants {

    public static final String PROVIDE = "com.zhige.zchat.provider";


    //分页数据
    public static final int PAGE_SIZE = 20;
    //分页刷新
    public static final String PAGE_REFRESH = "refresh";
    //分页加载
    public static final String PAGE_LOAD = "load";

    //dabinding的数据绑定
    public static final String SAVED_STATE_DATA_HANDLER = "SAVED_STATE_DATA_HANDLER";

    //资产   折合usd保留2位，数量4位，矿工费6位和手续费 4位
    public static final int DOT_COUNT = 8;//币
    public static final int DOT_COUNT_MINER = 6;//矿工费
    public static final int DOT_COUNT_FEE = 6;//手续费
    public static final int DOT_COUNT_MONEY = 2;//钱
    public static final int DOT_COUNT_NUMBER = 4;//数量

    //语言切换
    public static final String EVENT_REFRESH_LANGUAGE = "language_change";

}
