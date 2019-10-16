package common.data;

/*
 *  @描述：    登录时的用户信息
 *  {"user_id":17,
 *  "userName":null,
 *  "nickName":"13266753946",
 *  "icon_url":"http://192.168.0.65:8011/img/default_avatar.png",
 *  "token":"0f299d48912e46f6b0df622be47a6497"}
 */

import java.io.Serializable;

public class UserModel
        implements Serializable
{
    public String user_id;
    public String userName;
    public String nickName;
    public String icon_url;
    public String token;             //




}
