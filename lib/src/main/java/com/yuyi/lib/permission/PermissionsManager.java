package com.yuyi.lib.permission;

import android.Manifest;
import android.app.Activity;

import com.yuyi.lib.MyLib;

/**
 * @author huanghua
 */

public class PermissionsManager {

    private static PermissionsManager mInstance;

    private PermissionsChecker mPermissionsChecker;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };


    private PermissionsManager() {
        mPermissionsChecker = new PermissionsChecker(MyLib.getApplicationContext());
    }

    public static synchronized PermissionsManager getInstance() {
        if (mInstance == null) {
            mInstance = new PermissionsManager();
        }
        return mInstance;
    }

    /**
     * 检查sdcard权限是否要申请
     *
     * @return true 需要申请，false 不需要申请
     */
    public boolean checkSDCardPermission() {
        return checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 检查是否需要申请权限
     *
     * @param permission
     * @return true 需要申请，false 不需要申请
     */
    public boolean checkPermissions(String... permission) {
        return mPermissionsChecker.lacksPermissions(permission);
    }

    /**
     * 检查应用所需权限
     *
     * @return true 需要申请，false 不需要申请
     */
    public boolean checkPermissions() {
        return checkPermissions(PERMISSIONS);
    }

    /**
     * 申请权限
     *
     * @param activity
     */
    public void requestPermissions(Activity activity) {
        mPermissionsChecker.requestPermissions(activity, PERMISSIONS);
    }

    /**
     * 判断权限是否授权
     *
     * @param grantResults
     * @return
     */
    public boolean hasAllPermissionsGranted(int[] grantResults) {
        return mPermissionsChecker.hasAllPermissionsGranted(grantResults);
    }
}
