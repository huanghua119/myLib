/*
 * Copyright (C) 2016 hejunlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yuyi.lib.ui.loopViewPager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class LoopViewPager extends ViewPager {

    private static final String TAG = LoopViewPager.class.getSimpleName();
    private static final boolean DEFAULT_BOUNDARY_CASHING = false;
    private static final boolean DEFAULT_BOUNDARY_LOOPING = true;
    private static final int DELAY_LONG_DUTATION = 5000;
    private static final int MSG_LOOP_PICTURE = 1001;

    private LoopPagerAdapterWrapper mAdapter;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;
    private boolean mBoundaryLooping = DEFAULT_BOUNDARY_LOOPING;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private boolean mIsLoopPicture = false;
    private boolean mIsScrollable = true;

    /**
     * when set looperpic has true, will handle this action
     * @return Handler
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (mIsLoopPicture && mAdapter.getCount() > 0) {
                int position = getCurrentItem() + 1;
                setCurrentItem(position);
            }
        }
    };

    /**
     * helper function which may be used when implementing FragmentPagerAdapter
     *
     * @return (position-1)%count
     */
    public static int toRealPosition(int position, int count) {
        position = position - 1;
        if (position < 0) {
            position += count;
        } else {
            position = position % count;
        }
        return position;
    }

    /**
     * If set to true, the boundary views (i.e. first and last) will never be
     * destroyed This may help to prevent "blinking" of some views
     */
    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(flag);
        }
    }

    public void setLooperPic(boolean looperPic) {
        mIsLoopPicture = looperPic;
        loopPictureIfNeed();
    }

    public void loopPictureIfNeed() {
        mHandler.removeMessages(MSG_LOOP_PICTURE);
        mHandler.sendEmptyMessageDelayed(MSG_LOOP_PICTURE, DELAY_LONG_DUTATION);
    }

    public void setBoundaryLooping(boolean flag) {
        mBoundaryLooping = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryLooping(flag);
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new LoopPagerAdapterWrapper(adapter);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        mAdapter.setBoundaryLooping(mBoundaryLooping);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        addOnPageChangeListener(listener);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners == null) {
            mOnPageChangeListeners = new ArrayList<>();
        }
        mOnPageChangeListeners.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.remove(listener);
        }
    }

    @Override
    public void clearOnPageChangeListeners() {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.clear();
        }
    }

    public LoopViewPager(Context context) {
        super(context);
        init(context);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        if (onPageChangeListener != null) {
            super.removeOnPageChangeListener(onPageChangeListener);
        }
        super.addOnPageChangeListener(onPageChangeListener);
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOnPageChangeListeners != null) {
                    for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                        OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                        if (listener != null) {
                            listener.onPageSelected(realPosition);
                        }
                    }
                }
            }
            loopPictureIfNeed();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0 && mPreviousOffset == 0 && (position == 0
                        || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;

            if (mOnPageChangeListeners != null) {
                for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                    OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                    if (listener != null) {
                        if (realPosition != mAdapter.getRealCount() - 1) {
                            listener.onPageScrolled(realPosition, positionOffset,
                                    positionOffsetPixels);
                        } else {
                            if (positionOffset > .5) {
                                listener.onPageScrolled(0, 0, 0);
                            } else {
                                listener.onPageScrolled(realPosition, 0, 0);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = LoopViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE && (position == 0
                        || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            if (mOnPageChangeListeners != null) {
                for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                    OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            default:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mDispatchListener != null) {
            mDispatchListener.onDispatchKeyEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    private OnDispatchTouchEventListener mDispatchListener;
    public void setOnDispatchTouchEventListener(OnDispatchTouchEventListener listener) {
        mDispatchListener = listener;
    }
    public static interface OnDispatchTouchEventListener {
        void onDispatchKeyEvent(MotionEvent event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public void setScrollable(boolean Scrollable) {
        mIsScrollable = Scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mIsScrollable == false) {
            return false;
        } else {
            return super.onInterceptTouchEvent(event);
        }
    }

    public void release() {
        mHandler.removeMessages(MSG_LOOP_PICTURE);
        if (onPageChangeListener != null) {
            super.removeOnPageChangeListener(onPageChangeListener);
        }
        mHandler = null;
    }
}
