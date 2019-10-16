package common.util.permission;


import androidx.annotation.NonNull;

/**
 * 作者 : Joker
 * 创建日期 : 2017-11-13
 * 修改日期 :
 * 版权所有 :
 */

public interface PermissionListener {

    /**
     * 通过授权
     *
     * @param permission
     */
    void permissionGranted(@NonNull String[] permission);

    /**
     * 拒绝授权
     *
     * @param permission
     */
    void permissionDenied(@NonNull String[] permission);
}
