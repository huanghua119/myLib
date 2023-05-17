package com.yuyi.lib.effect;

public abstract class AbsProcessor {

    protected abstract void initParam(int intensity);

    protected abstract int processEffect(int color, int intensity);

    protected int clamp(int value, int mix, int max) {
        if (value < mix) {
            value = mix;
        } else if (value > max) {
            value = max;
        }
        return value;
    }
}
