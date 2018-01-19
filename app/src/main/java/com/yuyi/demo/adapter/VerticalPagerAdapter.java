package com.yuyi.demo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuyi.demo.R;

public class VerticalPagerAdapter extends PagerAdapter {

    private final LibraryObject[] TWO_WAY_LIBRARIES = new LibraryObject[]{
            new LibraryObject(
                    R.drawable.ic_fintech,
                    "Fintech"
            ),
            new LibraryObject(
                    R.drawable.ic_delivery,
                    "Delivery"
            ),
            new LibraryObject(
                    R.drawable.ic_social,
                    "Social network"
            ),
            new LibraryObject(
                    R.drawable.ic_ecommerce,
                    "E-commerce"
            ),
            new LibraryObject(
                    R.drawable.ic_wearable,
                    "Wearable"
            ),
            new LibraryObject(
                    R.drawable.ic_internet,
                    "Internet of things"
            )
    };

    private LayoutInflater mLayoutInflater;

    public VerticalPagerAdapter(final Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return TWO_WAY_LIBRARIES.length;
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = mLayoutInflater.inflate(R.layout.horizontal_item, container, false);

        setupItem(view, TWO_WAY_LIBRARIES[position]);

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }

    private void setupItem(final View view, final LibraryObject libraryObject) {
        final TextView txt = (TextView) view.findViewById(R.id.txt_item);
        txt.setText(libraryObject.getTitle());

        final ImageView img = (ImageView) view.findViewById(R.id.img_item);
        img.setImageResource(libraryObject.getRes());
    }

    class LibraryObject {

        private String mTitle;
        private int mRes;

        LibraryObject(final int res, final String title) {
            mRes = res;
            mTitle = title;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(final String title) {
            mTitle = title;
        }

        public int getRes() {
            return mRes;
        }

        public void setRes(final int res) {
            mRes = res;
        }
    }
}
