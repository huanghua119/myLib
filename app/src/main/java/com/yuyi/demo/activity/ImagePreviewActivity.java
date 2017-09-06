package com.yuyi.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.yuyi.demo.R;
import com.yuyi.lib.abs.BaseSwipeBackActivity;
import com.yuyi.lib.bean.FileBean;
import com.yuyi.lib.image.ImageFragment;
import com.yuyi.lib.utils.FileUtil;
import com.yuyi.lib.utils.ImageUtils;
import com.yuyi.lib.utils.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huanghua
 */

public class ImagePreviewActivity extends BaseSwipeBackActivity {

    public static void startActivity(Context context, int currentItem, ArrayList<String> extraPics) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra("currentItem", currentItem);
        intent.putExtra("extraPics", extraPics);
        context.startActivity(intent);
    }

    private String url1 = "http://image6.huangye88.com/2013/03/28/2a569ac6dbab1216.jpg";

    private String url2 = "http://img2081.poco.cn/mypoco/myphoto/20130102/15/37946792201301021535501010906275817_001.jpg";

    private static final String SAVE_PATH = Environment.getExternalStorageDirectory().getPath() + "/mylib/pic/";

    private ViewPager mViewPager;

    private Map<Integer, ImageFragment> mFragmentMap = new HashMap<>();
    private List<String> mExtraPics = new ArrayList<>();
    private int mCurrentItem = 0;

    @Override
    public int bindLayout() {
        return R.layout.activity_image_preview;
    }

    @Override
    public void initView() {
        mExtraPics.clear();
        Intent extraIntent = getIntent();
        if (extraIntent != null) {
            if (extraIntent.hasExtra("extraPics")) {
                mExtraPics.addAll(extraIntent.getStringArrayListExtra("extraPics"));
            }
            if (extraIntent.hasExtra("currentItem")) {
                mCurrentItem = extraIntent.getIntExtra("extraPic", 0);
            }
        }
        mExtraPics.add(url1);
        mExtraPics.add(url2);
        setTitleBarColor(android.R.color.transparent);
        setTitleRight(R.string.save, v -> ImageUtils.saveImage(mExtraPics.get(mCurrentItem), SAVE_PATH, getApplicationContext()));
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        FileUtil.queryFiles(this, new String[]{"gif"}, list -> {
            for (FileBean fileBean : list) {
                mExtraPics.add(fileBean.getPath());
            }
            initViewPager();
            initCurrentItem();
        });
    }

    private void initViewPager() {
        mViewPager.setAdapter(new ImageViewAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                setReturnText((mCurrentItem + 1) + "/" + mExtraPics.size());
            }
        });
    }

    private void initCurrentItem() {
        if (mCurrentItem < 0) {
            mCurrentItem = 0;
        }
        mViewPager.setCurrentItem(mCurrentItem);
        setReturnText((mCurrentItem + 1) + "/" + mExtraPics.size());
    }

    private class ImageViewAdapter extends FragmentStatePagerAdapter {

        ImageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ImageFragment fragment = mFragmentMap.get(position);
            if (fragment == null) {
                fragment = ImageFragment.newInstance(mExtraPics.get(position));
                mFragmentMap.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mExtraPics.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                mFragmentMap.put(position, (ImageFragment) object);
            }
        }
    }

}
