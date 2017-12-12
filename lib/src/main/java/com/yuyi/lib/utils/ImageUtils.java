package com.yuyi.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.yuyi.lib.R;
import com.yuyi.lib.okhttp.HttpManager;

import java.io.File;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okio.BufferedSink;
import okio.Okio;

/**
 * @author huanghua
 */

public class ImageUtils {

    /**
     * 保存图片到指定目录
     *
     * @param url
     * @param saveDir
     * @param context
     */
    public static void saveImage(String url, String saveDir, Context context) {
        Observable.just(url)
                .map(s -> getImageBytesFromLocal(Uri.parse(s)))
                .map(in -> {
                    if (in != null) {
                        String fileName = HttpManager.getInstance().getFileNameFromUrl(url);
                        File target = new File(saveDir, fileName);
                        if (target.exists()) {
                            return target;
                        } else if (!target.getParentFile().exists()) {
                            target.getParentFile().mkdirs();
                        }
                        try {
                            BufferedSink sink = Okio.buffer(Okio.sink(target));
                            sink.writeAll(Okio.source(in));
                            sink.close();
                            return target;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                })
                .map(file -> {
                    if (file != null && file.exists()) {
                        return file;
                    }
                    HttpManager.getInstance().httpDownload(url, file);
                    return null;
                })
                .doOnNext(file -> {
                    if (file != null && file.exists()) {
                        scanPhoto(file, context);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(file -> {
                    if (file != null && file.exists()) {
                        Toast.makeText(context, R.string.save_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.sava_fail, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static void scanPhoto(File file, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private static InputStream getImageBytesFromLocal(Uri loadUri) {
        if (loadUri == null) {
            return null;
        }
        CacheKey cacheKey =
                DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
        try {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                return ImagePipelineFactory.getInstance()
                        .getMainDiskStorageCache()
                        .getResource(cacheKey)
                        .openStream();
            }
            if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                return ImagePipelineFactory.getInstance()
                        .getSmallImageDiskStorageCache()
                        .getResource(cacheKey)
                        .openStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
