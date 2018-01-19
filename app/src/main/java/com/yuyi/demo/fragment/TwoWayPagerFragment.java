package com.yuyi.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.yuyi.demo.R;
import com.yuyi.demo.adapter.HorizontalPagerAdapter;

public class TwoWayPagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two_way, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager =
                (HorizontalInfiniteCycleViewPager) view.findViewById(R.id.hicvp);
        horizontalInfiniteCycleViewPager.setAdapter(new HorizontalPagerAdapter(getContext(), true));
        //horizontalInfiniteCycleViewPager.setScrollDuration(3000);
        //horizontalInfiniteCycleViewPager.startAutoScroll(true);

//
//        horizontalInfiniteCycleViewPager.setScrollDuration(500);
//        horizontalInfiniteCycleViewPager.setPageDuration(1000);
//        horizontalInfiniteCycleViewPager.setInterpolator(null);
//        horizontalInfiniteCycleViewPager.setMediumScaled(true);
//        horizontalInfiniteCycleViewPager.setMaxPageScale(1.0F);
//        horizontalInfiniteCycleViewPager.setMinPageScale(0.7F);
//        horizontalInfiniteCycleViewPager.setCenterPageScaleOffset(0.0F);
//        horizontalInfiniteCycleViewPager.setMinPageScaleOffset(0.0F);
    }
}
