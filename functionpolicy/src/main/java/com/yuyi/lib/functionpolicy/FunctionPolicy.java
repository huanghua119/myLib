package com.yuyi.lib.functionpolicy;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 动态方法调用的jar文件，生成jar后要
 * 使用dx --dex --output functionPolicy.jar functionPolicy.jar转换
 *
 * @author huanghua
 */

public final class FunctionPolicy {

    private Context mContext;

    /**
     * 版本号
     */
    private static final String JAR_VERSION = "1.0";

    public FunctionPolicy(Context context) {
        mContext = context;
    }

    /**
     * 获取jar包的版本号，应用端根据版本号调用FunctionPolicy方法
     *
     * @return
     */
    public String getVersion() {
        return JAR_VERSION;
    }

    public String getTopActivityPackageName() {
        ActivityManager activityManager = (ActivityManager) (mContext
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningTaskInfo> runningTaskInfos = activityManager
                .getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            return f.getPackageName() + "/" + f.getClassName();
        }
        return "";
    }

    public String getTestData(String p1, int p2, float p3, double p4, short p5, long p6, boolean p7,
                              String[] p8, double[] p9, int[] p10, long[] p11, boolean[] p12, short[] p13, float[] p14) {
        JSONObject object = new JSONObject();
        try {
            object.put("p1", p1);
            object.put("p2", p2);
            object.put("p3", p3);
            object.put("p4", p4);
            object.put("p5", p5);
            object.put("p6", p6);
            object.put("p7", p7);
            object.put("p8", p8);
            object.put("p9", p9);
            object.put("p10", p10);
            object.put("p11", p11);
            object.put("p12", p12);
            object.put("p13", p13);
            object.put("p14", p14);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
