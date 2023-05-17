package com.yuyi.lib.effect;

import android.graphics.Bitmap;
import android.util.Log;

public final class ProcessorManager {

    private static final String TAG = "ProcessorManager";
    private static ProcessorManager mInstance;

    private final BrightnessProcessor mBrightnessProcessor;
    private final ContrastProcessor mContrastProcessor;
    private final TemperatureProcessor mTemperatureProcessor;
    private final SaturationProcessor mSaturationProcessor;

    public static ProcessorManager getInstance() {
        if (mInstance == null) {
            mInstance = new ProcessorManager();
        }
        return mInstance;
    }

    private ProcessorManager() {
        mBrightnessProcessor = new BrightnessProcessor();
        mContrastProcessor = new ContrastProcessor();
        mTemperatureProcessor = new TemperatureProcessor();
        mSaturationProcessor = new SaturationProcessor();
    }

    /**
     * @param baseBitmap  源bitamp
     * @param brightness  亮度
     * @param contrast    对比度
     * @param temperature 色温
     * @param saturation  饱和度
     */
    public Bitmap processorBitmap(Bitmap baseBitmap, int brightness, int contrast, int temperature, int saturation) {
        Log.d(TAG, "processorBitmap start");
        mBrightnessProcessor.initParam(brightness);
        mContrastProcessor.initParam(contrast);
        mSaturationProcessor.initParam(saturation);

        int bitmapWidth = baseBitmap.getWidth();
        int bitmapHeight = baseBitmap.getHeight();
        Bitmap resultBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < bitmapHeight; i++) {
            for (int j = 0; j < bitmapWidth; j++) {
                int newColor = baseBitmap.getPixel(j, i);

                newColor = mBrightnessProcessor.processEffect(newColor, brightness);
                newColor = mContrastProcessor.processEffect(newColor, contrast);
                newColor = mTemperatureProcessor.processEffect(newColor, temperature);
                newColor = mSaturationProcessor.processEffect(newColor, saturation);

                resultBitmap.setPixel(j, i, newColor);
            }
        }
        Log.d(TAG, "processorBitmap end");
        return resultBitmap;
    }
}
