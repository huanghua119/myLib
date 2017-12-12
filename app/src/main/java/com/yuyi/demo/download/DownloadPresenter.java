package com.yuyi.demo.download;

import android.content.Context;

import com.yuyi.lib.download.DownLoadObserver;
import com.yuyi.lib.download.DownloadInfo;
import com.yuyi.lib.download.DownloadManager;
import com.yuyi.lib.utils.MyLog;

/**
 * @author huanghua on 2017/12/11.
 */

public class DownloadPresenter implements DownloadContract.Presenter {

    private final DownloadContract.View mDownloadView;

    private Context mContext;

    public DownloadPresenter(Context context, DownloadContract.View downloadView) {
        mContext = context;
        mDownloadView = downloadView;
        mDownloadView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void startDownload(String url) {
        if (DownloadManager.getInstance().isDownloading(url)) {
            DownloadManager.getInstance().cancel(url);
            mDownloadView.downloadSuccess();
        } else {
            DownloadManager.getInstance().download(url, mContext.getApplicationContext().getFilesDir().getAbsolutePath(), new DownLoadObserver() {

                @Override
                public void onNext(DownloadInfo downloadInfo) {
                    super.onNext(downloadInfo);
                    float p = (downloadInfo.getProgress() / (float) downloadInfo.getTotal()) * 100;
                    MyLog.i("startDownload getProgress:" + p);
                    mDownloadView.downloadProgress((int) p);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (DownloadManager.getInstance().isDownloading(url)) {
                        DownloadManager.getInstance().cancel(url);
                        mDownloadView.downloadError(e);
                    }
                    MyLog.i("startDownload e:" + e.getMessage());
                }

                @Override
                public void onComplete() {
                    mDownloadView.downloadSuccess();
                    MyLog.i("startDownload onComplete");
                }
            });
        }
    }
}
