package com.yuyi.lib.effect;

import android.graphics.Color;

/**
 * 调整色温
 */
public class TemperatureProcessor extends AbsProcessor {

    @Override
    protected void initParam(int intensity) {

    }

    @Override
    protected int processEffect(int color, int temperature) {
        if (temperature == 0) {
            return color;
        }
        float scale = temperature / 100f;
        temperature = (int) (scale * 30);

        int r = clamp(Color.red(color) + temperature, 0, 255);
        int g = Color.green(color);
        int b = clamp(Color.blue(color) - temperature, 0, 255);
        return Color.rgb(r, g, b);
    }
}
