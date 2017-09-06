package com.yuyi.demo.activity;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;
import com.yuyi.demo.R;
import com.yuyi.demo.adapter.LoopPagerAdapter;
import com.yuyi.demo.adapter.SwipeDataAdapter;
import com.yuyi.lib.abs.BaseSwipeBackActivity;
import com.yuyi.lib.ui.CircleIndicator;
import com.yuyi.lib.ui.loopViewPager.LoopViewPager;
import com.yuyi.lib.utils.MyLog;

/**
 * @author huanghua
 */

public class SwipeDeteleActivity extends BaseSwipeBackActivity {

    private AppBarLayout mAppBarLayout;
    private ButtonBarLayout mReturnLayout;
    private SwipeMenuRecyclerView mRecyclerView;
    private LoopViewPager mLoopView;
    private CircleIndicator mLoopIndicator;

    private CollapsingToolbarLayoutState mCollapsingState;

    @Override
    public int bindLayout() {
        return R.layout.activity_swipedelete;
    }

    @Override
    public void initView() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mReturnLayout = (ButtonBarLayout) findViewById(R.id.return_layout);
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                if (mCollapsingState != CollapsingToolbarLayoutState.EXPANDED) {
                    mCollapsingState = CollapsingToolbarLayoutState.EXPANDED;
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (mCollapsingState != CollapsingToolbarLayoutState.COLLAPSED) {
                    mReturnLayout.setVisibility(View.VISIBLE);
                    mCollapsingState = CollapsingToolbarLayoutState.COLLAPSED;
                }
            } else {
                if (mCollapsingState != CollapsingToolbarLayoutState.INTERNEDIATE) {
                    if (mCollapsingState == CollapsingToolbarLayoutState.COLLAPSED) {
                        mReturnLayout.setVisibility(View.GONE);
                    }
                    mCollapsingState = CollapsingToolbarLayoutState.INTERNEDIATE;
                }
            }
        });
        mReturnLayout.setOnClickListener(this::destroy);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color)));
        mRecyclerView.setAdapter(new SwipeDataAdapter(getApplicationContext()));
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mRecyclerView.setSwipeItemClickListener((v, position) -> {
            MyLog.i("onItemClick:" + position);
            MyLog.i("onItemClick:" + position);
        });
        mRecyclerView.setSwipeMenuItemClickListener(menuBridge -> {
            int itemPosition = menuBridge.getAdapterPosition();
            int position = menuBridge.getPosition();
            MyLog.i("position:" + position + " itemPosition:" + itemPosition);
            menuBridge.closeMenu();
        });

        mLoopView = (LoopViewPager) findViewById(R.id.loop_view);
        mLoopIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        mLoopView.setAdapter(new LoopPagerAdapter(this));
        mLoopIndicator.setViewPager(mLoopView);
        mLoopView.setLooperPic(true);
    }

    private SwipeMenuCreator mSwipeMenuCreator = (swipeLeftMenu, swipeRightMenu, viewType) -> {
        int width = getResources().getDimensionPixelSize(R.dimen.swipe_width);

        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        {
            SwipeMenuItem unreadMenu = new SwipeMenuItem(getApplicationContext())
                    .setBackgroundColorResource(android.R.color.holo_orange_dark)
                    .setText(R.string.flag_unread)
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeLeftMenu.addMenuItem(unreadMenu); // 添加菜单到左侧。
        }

        {
            SwipeMenuItem topMenu = new SwipeMenuItem(getApplicationContext())
                    .setBackgroundColorResource(android.R.color.darker_gray)
                    .setText(R.string.to_top)
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(topMenu);// 添加菜单到右侧。

            SwipeMenuItem deleteMenu = new SwipeMenuItem(getApplicationContext())
                    .setBackgroundColorResource(android.R.color.holo_red_dark)
                    .setText(R.string.delete)
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteMenu); // 添加菜单到右侧。
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoopView != null) {
            mLoopView.release();
            mLoopView = null;
        }
    }

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
}
