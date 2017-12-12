package com.yuyi.demo.download;

import com.yuyi.demo.R;
import com.yuyi.lib.abs.BaseSwipeBackActivity;
import com.yuyi.lib.utils.ActivityUtils;

/**
 * @author huanghua
 */

public class DownloadActivity extends BaseSwipeBackActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_download;
    }

    @Override
    public void initView() {
        setReturnText(R.string.breakpoint);

        DownloadFragment downloadFragment = (DownloadFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (downloadFragment == null) {
            downloadFragment = DownloadFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    downloadFragment, R.id.contentFrame);
        }
        new DownloadPresenter(this, downloadFragment);
    }
}
