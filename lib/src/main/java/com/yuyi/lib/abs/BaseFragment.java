package com.yuyi.lib.abs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author huanghua
 */

public abstract class BaseFragment extends Fragment {

    protected View mContextView = null;

    protected Context mContext = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mContextView == null) {
            initParms(getArguments());
            mContextView = inflater.inflate(bindLayout(), container, false);
        }
        return mContextView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView(view);
    }

    public abstract int bindLayout();

    public abstract void initView(View view);

    public abstract void initParms(Bundle parms);
}
