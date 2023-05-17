package com.yuyi.lib.effect;

import android.graphics.Color;

/**
 * 调整对比度
 */
public class ContrastProcessor extends AbsProcessor {

    private int[] contrastTable = new int[256];
    private final int brightness = 128;

    @Override
    protected void initParam(int contrast) {
        if (contrast == 0) {
            return;
        }
        float contrast1 = (100 + contrast) / 100.0f;
        if (Float.compare(contrast1, 1.0f) == 0) {
            contrastTable = null;
            return;
        }
        contrastTable = new int[256];
        for (int i = 0; i < 256; i++) {
            contrastTable[i] = clamp((int) ((i - 128) * contrast1 + brightness + 0.5f), 0, 255);
        }
    }

    @Override
    protected int processEffect(int color, int contrast) {
        if (contrast == 0) {
            return color;
        }
        if (contrastTable == null) {
            return color;
        }
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.rgb(contrastTable[r], contrastTable[g], contrastTable[b]);
    }
}
