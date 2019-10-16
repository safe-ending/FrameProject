package com.frameproject.host;


import common.model.BaseModel;

/**
 * desc:  域名
 * author:  yangtao
 * <p>
 * creat:  2018/9/14 16:41
 */

public class HostModel extends BaseModel {
    public String environment;
    public String name_one;
    public String host_one;

    public String name_two;
    public String host_two;

    public String game;

    public HostModel(String environment, String name_one, String host_one, String name_two, String host_two, String game) {
        this.environment = environment;
        this.name_one = name_one;
        this.host_one = host_one;
        this.name_two = name_two;
        this.host_two = host_two;
        this.game = game;
    }
}
