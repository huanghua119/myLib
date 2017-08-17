package com.yuyi.lib.abs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yuyi.lib.R;

/**
 * @author huanghua
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateView();
        initView();
    }

    public abstract void onCreateView();

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

    public void destroy(View view) {
        finish();
    }
}
