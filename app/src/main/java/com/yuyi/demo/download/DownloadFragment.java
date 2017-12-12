package com.yuyi.demo.download;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.yuyi.demo.R;
import com.yuyi.lib.abs.BaseFragment;

/**
 * @author huanghua on 2017/12/11.
 */

public class DownloadFragment extends BaseFragment implements DownloadContract.View {

    private String url = "http://ouga1oem3.bkt.clouddn.com/Player-_3g-2.4-release.apk";

    private Button mDownload = null;

    private ProgressBar mProgressBar = null;

    private DownloadContract.Presenter mPresenter;

    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public void downloadSuccess() {
        mDownload.setText(R.string.download);
    }

    @Override
    public void downloadError(Throwable e) {
        mDownload.setText(R.string.download_fail);
    }

    @Override
    public void downloadProgress(int p) {
        mDownload.setText(R.string.downloading);
        mProgressBar.setProgress(p);
    }

    @Override
    public void setPresenter(DownloadContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_download;
    }

    @Override
    public void initView(View view) {
        mDownload = (Button) view.findViewById(R.id.start_download);
        mDownload.setOnClickListener(v -> mPresenter.startDownload(url));
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBar.setMax(100);
    }

    @Override
    public void initParms(Bundle parms) {

    }
}
