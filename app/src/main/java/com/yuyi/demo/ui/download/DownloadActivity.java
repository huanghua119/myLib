package com.yuyi.demo.ui.download;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.lifecycle.ViewModelProvider;

import com.yuyi.demo.R;
import com.yuyi.demo.databinding.ActivityDownloadBinding;
import com.yuyi.lib.abs.BaseSwipeBackActivity;

public class DownloadActivity extends BaseSwipeBackActivity {

    private String url = "http://archive.ubuntukylin.com/software/pool/partner/ukylin-wechat_3.0.0_amd64.deb";

    private ActivityDownloadBinding mBinding;
    private DownloadViewModel mHomeViewModel;
    private Button mDownload;
    private ProgressBar mProgressBar;

    @Override
    public View bindLayout() {
        mHomeViewModel = new ViewModelProvider(this).get(DownloadViewModel.class);
        //mHomeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance((MyApplication) MyLib.getApplicationContext()).create(DownloadViewModel.class);
        mBinding = ActivityDownloadBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void initView() {
        setReturnText(R.string.breakpoint);

        mHomeViewModel.initDownloadInfo(url);
        mDownload = mBinding.startDownload;
        mDownload.setOnClickListener(v -> mHomeViewModel.startDownload(url));
        mProgressBar = mBinding.progressBar;
        mProgressBar.setMax(100);
        mHomeViewModel.mDownloadLiveData.observe(this, data -> {
            switch (data.getStatus()) {
                case DownloadData.STATUS_PROGRESS:
                    mDownload.setText(R.string.downloading);
                    mProgressBar.setProgress(data.getProgress());
                    break;
                case DownloadData.STATUS_PAUSED:
                    mDownload.setText(R.string.download_pause);
                    mProgressBar.setProgress(data.getProgress());
                    break;
                case DownloadData.STATUS_SUCCESS:
                    mDownload.setText(R.string.download_success);
                    mProgressBar.setProgress(0);
                    break;
                case DownloadData.STATUS_ERROR:
                    mDownload.setText(R.string.download_fail);
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHomeViewModel.mDownloadLiveData.removeObservers(this);
    }
}