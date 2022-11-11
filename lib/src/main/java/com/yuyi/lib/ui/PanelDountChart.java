package com.yuyi.lib.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yuyi.lib.R;
import com.yuyi.lib.utils.DensityUtil;
import com.yuyi.lib.utils.MathHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huanghua
 */
public class PanelDountChart extends View {

    private int ScrWidth, ScrHeight;

    private float[] mChartPer;
    private int[] mChartColors;
    private String[] mChartLabel;

    private int mCircleLineStrokeWidth = 20;
    private int mCircleTextSize = 32;

    public PanelDountChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCircleLineStrokeWidth = getResources().getDimensionPixelSize(R.dimen.circleLine_stroke_width);
        mCircleTextSize = getResources().getDimensionPixelSize(R.dimen.circleText_size);
    }

    public void setChartPer(float[] per) {
        mChartPer = per;
    }

    public void setChartColor(int[] colors) {
        mChartColors = colors;
    }

    public void setChartLabel(String[] labels) {
        mChartLabel = labels;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChartPer == null) {
            return;
        }
        ScrWidth = this.getWidth();
        ScrHeight = this.getHeight();

        canvas.drawColor(Color.WHITE);

        float margin = DensityUtil.dip2px(getContext(), 7);
        float textMarginLeft = DensityUtil.dip2px(getContext(), 33);
        float textMarginRight = DensityUtil.dip2px(getContext(), 67);

        float cirX = ScrWidth / 2;
        float cirY = ScrHeight / 2 + margin;
        float radius = ScrHeight / 2 - margin * 2;

        float arcLeft = cirX - radius;
        float arcTop = cirY - radius;
        float arcRight = cirX + radius;
        float arcBottom = cirY + radius;
        RectF arcRF0 = new RectF(arcLeft, arcTop, arcRight, arcBottom);

        Paint PaintArc = new Paint();
        PaintArc.setAntiAlias(true);

        Paint paintLabel = new Paint();
        paintLabel.setColor(Color.BLACK);
        paintLabel.setTextSize(mCircleTextSize);
        paintLabel.setAntiAlias(true);

        //位置计算类
        MathHelper xcalc = MathHelper.getInstance();

        float Percentage = 0.0f;
        float CurrPer = -90.0f;
        int i = 0;
        for (i = 0; i < mChartPer.length; i++) {
            Percentage = 360 * (mChartPer[i] / 100);
            Percentage = (float) (Math.round(Percentage * 100)) / 100;
            PaintArc.setColor(mChartColors[i]);
            canvas.drawArc(arcRF0, CurrPer, Percentage, true, PaintArc);
            CurrPer += Percentage;
        }

        List<Float> mPosYList = new ArrayList<>();
        CurrPer = -90.0f;
        for (i = 0; i < mChartPer.length; i++) {
            //将百分比转换为饼图显示角度
            Percentage = 360 * (mChartPer[i] / 100);
            Percentage = (float) (Math.round(Percentage * 100)) / 100;
            xcalc.calcArcEndPointXY(cirX, cirY, radius - radius / 2 / 2, CurrPer + Percentage / 2);
            float x = xcalc.getPosX();
            if (x >= cirX) {
                paintLabel.setColor(Color.BLACK);

                Rect rect = new Rect();
                paintLabel.getTextBounds(mChartLabel[i], 0, mChartLabel[i].length(), rect);
                int h = rect.height();
                int w = rect.width();
                float posY =  xcalc.getPosY();
                boolean isOverLap = isOverLap(mPosYList,xcalc.getPosY(), h);
                if (isOverLap) {
                    posY += h;
                }

                canvas.drawText(mChartLabel[i], ScrWidth - textMarginRight, posY, paintLabel);
                paintLabel.setColor(Color.parseColor("#D1D1D1"));

                posY =  xcalc.getPosY() - h / 2;
                if (isOverLap) {
                    posY += h;
                }
                canvas.drawLine(x, posY, ScrWidth - textMarginRight - margin, posY, paintLabel);
            } else {
                paintLabel.setColor(Color.BLACK);

                Rect rect = new Rect();
                paintLabel.getTextBounds(mChartLabel[i], 0, mChartLabel[i].length(), rect);
                int w = rect.width();
                int h = rect.height();
                float posY =  xcalc.getPosY();
                boolean isOverLap = isOverLap(mPosYList,xcalc.getPosY(), h);
                if (isOverLap) {
                    posY += h;
                }

                canvas.drawText(mChartLabel[i], textMarginLeft, posY, paintLabel);
                paintLabel.setColor(Color.parseColor("#D1D1D1"));

                posY =  xcalc.getPosY() - h / 2;
                if (isOverLap) {
                    posY += h;
                }

                canvas.drawLine(textMarginLeft + w  + margin, posY, x, posY, paintLabel);
            }
            CurrPer += Percentage;
            mPosYList.add(xcalc.getPosY());
        }

        //画圆心
        PaintArc.setColor(Color.WHITE);
        canvas.drawCircle(cirX, cirY, radius - mCircleLineStrokeWidth, PaintArc);
    }

    private boolean isOverLap(List<Float> list, float current, int height) {
        for (Float old : list) {
            if (Math.abs(old - current) < height) {
                return true;
            }
        }
        return false;
    }
}