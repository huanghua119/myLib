package com.yuyi.demo.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.yuyi.demo.R;
import com.yuyi.demo.fragment.HorizontalPagerFragment;
import com.yuyi.demo.fragment.TwoWayPagerFragment;
import com.yuyi.lib.abs.BaseSwipeBackActivity;

/**
 * @author huanghua on 2018/1/17.
 */

public class InfiniteCycleActivity extends BaseSwipeBackActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_infinitecycle;
    }

    @Override
    public void initView() {
        setReturnText(R.string.infinitecycle);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_main);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);

        final NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts);
        navigationTabStrip.setTitles("HOW WE WORK", "WE WORK WITH");
        navigationTabStrip.setViewPager(viewPager);
    }


    class PagerAdapter extends FragmentStatePagerAdapter {

        private final static int COUNT = 3;

        private final static int HORIZONTAL = 0;
        private final static int TWO_WAY = 1;

        PagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            switch (position) {
                case TWO_WAY:
                    return new TwoWayPagerFragment();
                case HORIZONTAL:
                default:
                    return new HorizontalPagerFragment();
            }
        }

        @Override
        public int getCount() {
            return COUNT;
        }
    }
}
