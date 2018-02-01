package com.yuyi.demo.download;

import com.yuyi.demo.abs.BasePresenter;
import com.yuyi.demo.abs.BaseView;

/**
 * @author huanghua on 2017/12/11.
 */

public interface DownloadContract {

    interface View extends BaseView<Presenter> {
        void downloadSuccess();

        void downloadError(Throwable e);

        void downloadProgress(int progress);

        void downloadPaused(int progress);
    }

    interface Presenter extends BasePresenter {
        void initDownloadInfo(String url);

        void startDownload(String url);
    }
}
