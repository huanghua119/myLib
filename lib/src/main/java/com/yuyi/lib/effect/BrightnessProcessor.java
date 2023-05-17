package com.yuyi.lib.effect;

import android.graphics.Color;

/**
 * 调整亮度
 */
public class BrightnessProcessor extends AbsProcessor {

    private int[] brightnessTable = new int[256];

    @Override
    public void initParam(int brightness) {
        if (brightness == 0) {
            return;
        }
        if (brightness > 0) {
            int delta = 255 - brightness;
            float k = 255.f / delta;
            for (int i = 0; i < 256; i++) {
                brightnessTable[i] = clamp((int) (i * k + 0.5f), 0, 255);
            }
        } else {
            float k = brightness / 100.0f;
            float d = 1.f + k;
            for (int i = 0; i < 256; i++) {
                brightnessTable[i] = clamp((int) (i * d + 0.5f), 0, 255);
            }
        }
    }

    @Override
    protected int processEffect(int color, int brightness) {
        if (brightness == 0) {
            return color;
        }
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.rgb(brightnessTable[r], brightnessTable[g], brightnessTable[b]);
    }
}
