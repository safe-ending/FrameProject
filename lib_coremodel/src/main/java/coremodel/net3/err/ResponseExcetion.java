package coremodel.net3.err;

import java.io.Serializable;

/**
 * desc:  错误返回体
 * author:  yangtao
 * <p>
 * creat:  2019/1/22 17:27
 */

public class ResponseExcetion  extends Throwable implements Serializable {

    public String msg;
    public int code;

    public ResponseExcetion(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}
