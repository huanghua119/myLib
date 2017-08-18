package com.yuyi.lib.swipebacklayout.activity;

import com.yuyi.lib.swipebacklayout.SwipeBackLayout;

public interface SwipeBackActivityBase {
    public abstract SwipeBackLayout getSwipeBackLayout();

    public abstract void setSwipeBackEnable(boolean enable);

    public abstract void scrollToFinishActivity();

}
