package com.yuyi.demo.activity;

import android.widget.Button;
import android.widget.ProgressBar;

import com.yuyi.demo.R;
import com.yuyi.lib.abs.BaseSwipeBackActivity;
import com.yuyi.lib.download.DownLoadObserver;
import com.yuyi.lib.download.DownloadInfo;
import com.yuyi.lib.download.DownloadManager;
import com.yuyi.lib.utils.MyLog;

/**
 * @author huanghua
 */

public class DownloadActivity extends BaseSwipeBackActivity {

    private Button mDownload = null;

    private ProgressBar mProgressBar = null;

    private String url = "http://ouga1oem3.bkt.clouddn.com/Player-_3g-2.4-release.apk";

    @Override
    public int bindLayout() {
        return R.layout.activity_download;
    }

    @Override
    public void initView() {
        setReturnText(R.string.breakpoint);
        mDownload = (Button) findViewById(R.id.start_download);
        mDownload.setOnClickListener(view -> startDownload());

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setMax(100);
    }

    private void startDownload() {
        if (DownloadManager.getInstance().isDownloading(url)) {
            DownloadManager.getInstance().cancel(url);
            mDownload.setText(R.string.download);
        } else {
            DownloadManager.getInstance().download(url, getFilesDir().getAbsolutePath(), new DownLoadObserver() {

                @Override
                public void onNext(DownloadInfo downloadInfo) {
                    super.onNext(downloadInfo);
                    float p = (downloadInfo.getProgress() / (float) downloadInfo.getTotal()) * 100;
                    MyLog.i("startDownload getProgress:" + p);
                    mDownload.setText(R.string.downloading);
                    mProgressBar.setProgress((int) p);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (DownloadManager.getInstance().isDownloading(url)) {
                        mDownload.setText(R.string.download_fail);
                        DownloadManager.getInstance().cancel(url);
                    }
                    MyLog.i("startDownload e:" + e.getMessage());
                }

                @Override
                public void onComplete() {
                    mDownload.setText(R.string.download_success);
                    MyLog.i("startDownload onComplete");
                }
            });
        }
    }
}
