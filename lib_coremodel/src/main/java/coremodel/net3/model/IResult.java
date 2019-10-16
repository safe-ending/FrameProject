package coremodel.net3.model;

/**
 * desc:  请求结果
 * author:  yangtao
 * <p>
 * creat: 2018/8/24 16:05
 */

public interface IResult {
    public int code();
    public String info();
    public Object Data();
}
