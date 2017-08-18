package com.yuyi.lib.abs;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author huanghua
 */

public abstract class BaseActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }
}
