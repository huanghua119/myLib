package com.yuyi.lib.abs;

import android.os.Bundle;

/**
 * @author huanghua
 */
public abstract class BaseActivity extends BaseSwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
    }
}
