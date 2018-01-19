package com.yuyi.lib;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * @author huanghua
 */

public final class MyLib {

    private static Context sApplicationContext;

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    private static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;
    private static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8;

    public static void init(Context context) {
        sApplicationContext = context;

        // Custom fonts
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Monospace-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        checkContext();
        initFrescoConfig();
    }

    public static Context getApplicationContext() {
        checkContext();
        return sApplicationContext;
    }


    private static void checkContext() {
        if (sApplicationContext == null) {
            throw new RuntimeException(
                    "context is null. You must call MyLib.init(context) before " + "using the mad library.");
        }
    }

    private static void initFrescoConfig() {
        final MemoryCacheParams bitmapCacheParams =
                new MemoryCacheParams(MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                        Integer.MAX_VALUE,                     // Max entries in the cache
                        MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                        Integer.MAX_VALUE,                     // Max length of eviction queue
                        Integer.MAX_VALUE);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).build();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(sApplicationContext, okHttpClient)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setBitmapMemoryCacheParamsSupplier(() -> bitmapCacheParams)
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(sApplicationContext).setMaxCacheSize(MAX_DISK_CACHE_SIZE).build())
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(sApplicationContext, config);
    }
}
