package com.yuyi.lib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static int getRawSizeSp(Context ctx, float value) {
        Resources res = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                value, res.getDisplayMetrics());
    }

    public static int getRawSizeDip(Context ctx, float value) {
        Resources res = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, res.getDisplayMetrics());
    }
}