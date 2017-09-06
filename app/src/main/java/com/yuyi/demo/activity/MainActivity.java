package com.yuyi.demo.activity;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.yuyi.demo.R;
import com.yuyi.demo.fragment.LibFragment;
import com.yuyi.lib.abs.BaseActivity;
import com.yuyi.lib.reflect.Reflect;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        mViewPager = (ViewPager) findViewById(R.id.tab_view_pager);
        disableShiftMode();
        setupViewPager();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.navigation_schedule);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.navigation_habit);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_schedule:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_habit:
                    mViewPager.setCurrentItem(1);
                    return true;
                default:
                    break;
            }
            return false;
        });
    }

    private void disableShiftMode() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        Reflect.on(menuView).set("mShiftingMode", false);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            item.setShiftingMode(false);
            item.setChecked(item.getItemData().isChecked());
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(LibFragment.newInstance());
        adapter.addFragment(new Fragment());
        mViewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
