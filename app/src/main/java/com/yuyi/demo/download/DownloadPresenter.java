package com.yuyi.demo.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.yuyi.lib.download.DownLoadObserver;
import com.yuyi.lib.download.DownloadInfo;
import com.yuyi.lib.download.DownloadManager;
import com.yuyi.lib.utils.MyLog;

import java.io.File;

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
    public void initDownloadInfo(String url) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            FileDownloader.getImpl().addServiceConnectListener(new FileDownloadConnectListener() {
                @Override
                public void connected() {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        String path = createPath(url);
                        int id = FileDownloadUtils.generateId(url, path);
                        int status = FileDownloader.getImpl().getStatus(id, path);
                        int progress = (int) (FileDownloader.getImpl().getSoFar(id) / (double) FileDownloader.getImpl().getTotal(id) * 100);
                        if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                                status == FileDownloadStatus.connected || status == FileDownloadStatus.progress) {
                            mDownloadView.downloadProgress(progress);
                        } else if (status == FileDownloadStatus.paused) {
                            mDownloadView.downloadPaused(progress);
                        }
                    });
                }

                @Override
                public void disconnected() {

                }
            });
        } else {
            String path = createPath(url);
            int id = FileDownloadUtils.generateId(url, path);
            int status = FileDownloader.getImpl().getStatus(id, path);
            int progress = (int) (FileDownloader.getImpl().getSoFar(id) / (double) FileDownloader.getImpl().getTotal(id) * 100);
            if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                    status == FileDownloadStatus.connected || status == FileDownloadStatus.progress) {
                mDownloadView.downloadProgress(progress);
            } else if (status == FileDownloadStatus.paused) {
                mDownloadView.downloadPaused(progress);
            }
        }
    }

    @Override
    public void startDownload(String url) {
        String path = createPath(url);
        int id = FileDownloadUtils.generateId(url, path);
        int status = FileDownloader.getImpl().getStatus(id, path);
        MyLog.i("startDownload status:" + status);
        if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                status == FileDownloadStatus.connected || status == FileDownloadStatus.progress) {
            FileDownloader.getImpl().pause(id);
        } else {
            FileDownloadListener listener = new FileDownloadListener() {
                @Override
                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    int value = 0;
                    if (totalBytes != 0) {
                        value = (int) (soFarBytes / (double) totalBytes * 100);
                    }
                    MyLog.i("startDownload pending:" + value);
                    mDownloadView.downloadProgress(value);
                }

                @Override
                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    int value = 0;
                    if (totalBytes != 0) {
                        value = (int) (soFarBytes / (double) totalBytes * 100);
                    }
                    MyLog.i("startDownload getProgress:" + value);
                    mDownloadView.downloadProgress(value);
                }

                @Override
                protected void completed(BaseDownloadTask task) {
                    MyLog.i("startDownload onComplete");
                    mDownloadView.downloadSuccess();
                    File file = new File(task.getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                }

                @Override
                protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    int value = 0;
                    if (totalBytes != 0) {
                        value = (int) (soFarBytes / (double) totalBytes * 100);
                    }
                    MyLog.i("startDownload paused:" + value);
                    mDownloadView.downloadPaused(value);
                }

                @Override
                protected void error(BaseDownloadTask task, Throwable e) {
                    mDownloadView.downloadError(e);
                    MyLog.i("startDownload e:" + e.getMessage());
                }

                @Override
                protected void warn(BaseDownloadTask task) {

                }
            };
            BaseDownloadTask task = FileDownloader.getImpl().create(url);
            //task.setTag(fontBean);
            task.setPath(createPath(url));
            task.setListener(listener);
            task.asInQueueTask().enqueue();
            FileDownloader.getImpl().start(listener, false);
        }
    }

    public String createPath(final String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return FileDownloadUtils.getDefaultSaveFilePath(url);
    }
}
