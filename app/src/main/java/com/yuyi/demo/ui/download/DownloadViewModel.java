package com.yuyi.demo.ui.download;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.yuyi.lib.utils.MyLog;

import java.io.File;

public class DownloadViewModel extends ViewModel {

    public final MutableLiveData<DownloadData> mDownloadLiveData;
    private FileDownloadListener mFileDownloadListener;

    public DownloadViewModel() {
        MyLog.i("DownloadViewModel new");
        mDownloadLiveData = new MutableLiveData<>();
    }

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
                            mDownloadLiveData.postValue(new DownloadData(progress, DownloadData.STATUS_PROGRESS));
                        } else if (status == FileDownloadStatus.paused) {
                            mDownloadLiveData.postValue(new DownloadData(progress, DownloadData.STATUS_PAUSED));
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
                mDownloadLiveData.postValue(new DownloadData(progress, DownloadData.STATUS_PROGRESS));
                start(url);
            } else if (status == FileDownloadStatus.paused) {
                mDownloadLiveData.postValue(new DownloadData(progress, DownloadData.STATUS_PAUSED));
            }
        }
    }

    public void startDownload(String url) {
        String path = createPath(url);
        int id = FileDownloadUtils.generateId(url, path);
        int status = FileDownloader.getImpl().getStatus(id, path);
        MyLog.i("startDownload status:" + status);
        if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                status == FileDownloadStatus.connected || status == FileDownloadStatus.progress) {
            FileDownloader.getImpl().pause(id);
        } else {
            start(url);
        }
    }

    private void start(String url) {
        MyLog.i("start mFileDownloadListener:" + mFileDownloadListener);
        if (mFileDownloadListener == null) {
            mFileDownloadListener = new FileDownloadListener() {
                @Override
                protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    int value = 0;
                    if (totalBytes != 0) {
                        value = (int) (soFarBytes / (double) totalBytes * 100);
                    }
                    MyLog.i("startDownload pending:" + value);
                    mDownloadLiveData.postValue(new DownloadData(value, DownloadData.STATUS_PROGRESS));
                }

                @Override
                protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    int value = 0;
                    if (totalBytes != 0) {
                        value = (int) (soFarBytes / (double) totalBytes * 100);
                    }
                    MyLog.i("startDownload getProgress:" + value);
                    mDownloadLiveData.postValue(new DownloadData(value, DownloadData.STATUS_PROGRESS));
                }

                @Override
                protected void completed(BaseDownloadTask task) {
                    MyLog.i("startDownload onComplete");
                    mDownloadLiveData.postValue(new DownloadData(100, DownloadData.STATUS_SUCCESS));
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
                    mDownloadLiveData.postValue(new DownloadData(value, DownloadData.STATUS_PAUSED));
                }

                @Override
                protected void error(BaseDownloadTask task, Throwable e) {
                    mDownloadLiveData.postValue(new DownloadData(0, DownloadData.STATUS_ERROR));
                    MyLog.i("startDownload e:" + e.getMessage());
                }

                @Override
                protected void warn(BaseDownloadTask task) {

                }
            };
            BaseDownloadTask task = FileDownloader.getImpl().create(url);
            //task.setTag(fontBean);
            task.setPath(createPath(url));
            task.setListener(mFileDownloadListener);
            task.asInQueueTask().enqueue();
            FileDownloader.getImpl().replaceListener(url, mFileDownloadListener);
        }
        FileDownloader.getImpl().start(mFileDownloadListener, false);
    }

    public String createPath(final String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return FileDownloadUtils.getDefaultSaveFilePath(url);
    }
}