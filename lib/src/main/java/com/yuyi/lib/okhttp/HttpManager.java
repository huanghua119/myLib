package com.yuyi.lib.okhttp;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

public class HttpManager {

    private OkHttpClient mClient = null;

    private static HttpManager mInstance = null;

    public interface StringCallback {
        void onFailure(int code, String message);

        void onSussess(String message);
    }

    private class HttpCallback implements Callback {

        private StringCallback mCallback;

        private HttpCallback(StringCallback callback) {
            mCallback = callback;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            if (mCallback != null) {
                mCallback.onFailure(-1, e.getMessage());
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (mCallback == null) {
                return;
            }
            if (response != null && response.isSuccessful()) {
                mCallback.onSussess(response.body().string());
            } else if (response == null) {
                mCallback.onFailure(-2, "response is null!");
            } else {
                mCallback.onFailure(response.code(), response.message());
            }
        }
    }

    private HttpManager() {
        if (mClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS);
            mClient = builder.build();
        }
    }

    public static HttpManager getInstance() {
        if (mInstance == null) {
            synchronized (HttpManager.class) {
                if (mInstance == null) {
                    mInstance = new HttpManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * Get请求
     *
     * @param url
     * @param callback
     */
    public void doGet(String url, StringCallback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new HttpCallback(callback));
    }

    /**
     * Post请求发送键值对数据
     *
     * @param url
     * @param mapParams
     * @param callback
     */
    public void doPost(String url, Map<String, String> mapParams, StringCallback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : mapParams.keySet()) {
            builder.add(key, mapParams.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new HttpCallback(callback));
    }

    /**
     * Post请求发送JSON数据
     *
     * @param url
     * @param jsonParams
     * @param callback
     */
    public void doPost(String url, String jsonParams, StringCallback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(new HttpCallback(callback));
    }

    /**
     * 上传文件
     *
     * @param url
     * @param pathName
     * @param fileName
     * @param callback
     */
    public void doFile(String url, String pathName, String fileName, Callback callback) {
        //判断文件类型
        MediaType MEDIA_TYPE = MediaType.parse(judgeType(pathName));
        //创建文件参数
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(MEDIA_TYPE.type(), fileName,
                        RequestBody.create(MEDIA_TYPE, new File(pathName)));
        //发出请求参数
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "9199fdef135c122")
                .url(url)
                .post(builder.build())
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(callback);

    }

    /**
     * 根据文件路径判断MediaType
     *
     * @param path
     * @return
     */
    private String judgeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public void httpDownload(String url, File target) throws Exception {
        Request request = new Request.Builder().url(url).build();
        Response response = mClient.newCall(request).execute();
        if (response.isSuccessful()) {
            BufferedSink sink = Okio.buffer(Okio.sink(target));
            sink.writeAll(response.body().source());
            sink.close();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public String getFileNameFromUrl(String url) {
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }
        return filename;
    }
}
