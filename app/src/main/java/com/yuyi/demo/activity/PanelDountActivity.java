package com.yuyi.demo.activity;

import android.graphics.Color;
import android.view.View;

import com.yuyi.demo.R;
import com.yuyi.demo.databinding.ActivityPaneldountBinding;
import com.yuyi.lib.abs.BaseSwipeBackActivity;
import com.yuyi.lib.ui.PanelDountChart;

/**
 * @author huanghua
 */

public class PanelDountActivity extends BaseSwipeBackActivity {

    @Override
    public View bindLayout() {
        return ActivityPaneldountBinding.inflate(getLayoutInflater()).getRoot();
    }

    @Override
    public void initView() {
        setReturnText(R.string.panel_dount);
        PanelDountChart chart = (PanelDountChart) findViewById(R.id.chart);
        chart.setChartPer(new float[]{35, 13, 52});
        chart.setChartColor(new int[]{Color.parseColor("#91c8ff"), Color.parseColor("#F8F8F8"), Color.parseColor("#3a90e5")});
        chart.setChartLabel(new String[]{"当前", "有效", "已使用"});
        chart.postInvalidate();
    }
}
