package com.yuyi.lib.effect;

import android.graphics.Color;

/**
 * 饱和度的定义大家可以自行查维基百科。饱和度又名色度、彩度。
 * 直观而言，饱和度就是色彩的鲜艳程度或者饱和程度。
 * 从白色黑色以及白黑之间的所有灰色其饱和度都为0。
 * 饱和度越高说明包含某种颜色的成分越大。
 * 其实可以理解成某一个纯色掺和了一些灰色或黑色或白色。
 * 因为灰色中包含 rgb 三种成分，所以也就导致了原本的 rgb 纯色100%占比开始下降。
 * 根据加入灰色的量，使得另外两种颜色成分的增加，原本纯色的饱和度进一步下降。
 */
public class SaturationProcessor extends AbsProcessor {

    private static final float Rf = 0.2999f;
    private static final float Gf = 0.587f;
    private static final float Bf = 0.114f;
    private float S;
    private float MS;
    private float Rt;
    private float Gt;
    private float Bt;


    @Override
    protected void initParam(int saturation) {
        if (saturation == 0) {
            return;
        }
        S = saturation / 100.f + 1;
        MS = 1.0f - S;
        Rt = Rf * MS;
        Gt = Gf * MS;
        Bt = Bf * MS;
    }

    @Override
    protected int processEffect(int color, int saturation) {
        if (saturation == 0) {
            return color;
        }
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        float Rc = r * (Rt + S) + g * Gt + b * Bt + 0.5f;
        float Gc = r * Rt + g * (Gt + S) + b * Bt + 0.5f;
        float Bc = r * Rt + g * Gt + b * (Bt + S) + 0.5f;

        r = clamp((int) Rc, 0, 255);
        g = clamp((int) Gc, 0, 255);
        b = clamp((int) Bc, 0, 255);
        return Color.rgb(r, g, b);
    }
}
