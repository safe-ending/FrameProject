package coremodel.net3.model;


import java.io.Serializable;

/**
 * desc:  结果解析
 * author:  yangtao
 * <p>
 * creat: 2018/8/24 16:05
 */
public class RequestResult<T>
        implements Serializable, IResult {

    public static final int CODE_REQUEST_SUCCESS = 1;//请求正确

    /**
     * 1：成功，0：失败
     */
    public int code;
    public String info;
    public T Data;

    @Override
    public int code() {
        return code;
    }

    @Override
    public String info() {
        return info;
    }

    @Override
    public Object Data() {
        return Data;
    }


}
