package com.yuyi.demo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yuyi.demo.R;

/**
 * @author huanghua
 */

public class LoopPagerAdapter extends PagerAdapter {

    private Context mContext;

    private int[] resIds = {R.mipmap.loop_image_1, R.mipmap.loop_image_2};

    public LoopPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return resIds.length;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.loop_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setBackgroundResource(resIds[position]);
        container.addView(view);
        return view;
    }
}
