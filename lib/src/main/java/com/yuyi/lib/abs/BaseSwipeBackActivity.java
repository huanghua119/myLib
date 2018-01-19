package com.yuyi.lib.abs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.yuyi.lib.R;
import com.yuyi.lib.swipebacklayout.activity.SwipeBackActivity;

/**
 * @author huanghua
 */

public abstract class BaseSwipeBackActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(bindLayout());
        resetTitleBar();
        initView();
    }

    public abstract int bindLayout();

    public abstract void initView();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 设置标题栏中间文本内容(默认是不显示)
     *
     * @param resId
     */
    public void setTitle(int resId) {
        TextView tv = (TextView) findViewById(R.id.title);
        if (tv == null) {
            return;
        }
        tv.setVisibility(View.VISIBLE);
        tv.setText(resId);
    }

    /**
     * 设置标题栏中间文本内容(默认是不显示)
     *
     * @param resId
     */
    public void setTitle(String resId) {
        TextView tv = (TextView) findViewById(R.id.title);
        if (tv == null) {
            return;
        }
        tv.setVisibility(View.VISIBLE);
        tv.setText(resId);
    }

    /**
     * 设置标题栏返回按钮内容(默认无内容)
     *
     * @param text
     */
    public void setReturnText(String text) {
        TextView tv = (TextView) findViewById(R.id.return_text);
        if (tv == null) {
            return;
        }
        tv.setText(text);
    }

    /**
     * 设置标题栏返回按钮内容(默认无内容)
     *
     * @param resId
     */
    public void setReturnText(int resId) {
        TextView tv = (TextView) findViewById(R.id.return_text);
        if (tv == null) {
            return;
        }
        tv.setText(resId);
    }

    /**
     * 设置标题栏返回按钮是否显示（默认显示）
     *
     * @param visibility
     */
    public void setReturnVisibility(int visibility) {
        View returnLayout = findViewById(R.id.return_layout);
        if (returnLayout == null) {
            return;
        }
        returnLayout.setVisibility(visibility);
    }

    /**
     * 设置标题栏右侧按钮文本及事件（默认无）
     *
     * @param resId
     * @param listener
     */
    public void setTitleRight(int resId, View.OnClickListener listener) {
        TextView button = (TextView) findViewById(R.id.right_button);
        if (button == null) {
            return;
        }
        button.setVisibility(View.VISIBLE);
        button.setText(resId);
        button.setOnClickListener(listener);
    }

    /**
     * 设置标题栏背景颜色
     *
     * @param resId
     */
    public void setTitleBarColor(int resId) {
        View titleBar = findViewById(R.id.title_bar);
        if (titleBar == null) {
            return;
        }
        titleBar.setBackgroundResource(resId);
    }

    /**
     * 使用沉浸式状态栏，titleBar需要下移
     * 在布局上使用android:fitsSystemWindows="true"即可实现下移
     */
    private void resetTitleBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
//        View titleBar = findViewById(R.id.title_bar);
//        if (titleBar != null) {
//            titleBar.setPadding(0, DensityUtil.getStatusBarHeight(this), 0, 0);
//        }
    }

    public void destroy(View view) {
        finish();
    }
}
