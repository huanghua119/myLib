package com.yuyi.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yuyi.demo.R;
import com.yuyi.demo.activity.InfiniteCycleActivity;
import com.yuyi.demo.download.DownloadActivity;
import com.yuyi.demo.activity.FunctionActivity;
import com.yuyi.demo.activity.ImagePreviewActivity;
import com.yuyi.demo.activity.PagerActivity;
import com.yuyi.demo.activity.PanelDountActivity;
import com.yuyi.demo.activity.SwipeDeteleActivity;
import com.yuyi.lib.abs.BaseFragment;
import com.yuyi.lib.utils.DensityUtil;

/**
 * @author huanghua
 */

public class LibFragment extends BaseFragment {

    public static LibFragment newInstance() {
        return new LibFragment();
    }

    private ListView mListView = null;

    private int[] mListText = {R.string.breakpoint, R.string.function_policy, R.string.panel_dount, R.string.image_preview, R.string.pager_view, R.string.swipe_delete, R.string.infinitecycle};

    private Class[] mListClass = {DownloadActivity.class, FunctionActivity.class, PanelDountActivity.class, ImagePreviewActivity.class, PagerActivity.class, SwipeDeteleActivity.class, InfiniteCycleActivity.class};

    @Override
    public int bindLayout() {
        return R.layout.fragment_lib;
    }

    @Override
    public void initView(View view) {
        View titleBar = view.findViewById(R.id.title_bar);
        titleBar.setPadding(0, DensityUtil.getStatusBarHeight(mContext), 0, 0);
        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mListText.length;
            }

            @Override
            public String getItem(int position) {
                return getResources().getString(mListText[position]);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, null);
                }
                TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
                text1.setText(getItem(position));
                text1.setTextColor(getResources().getColor(android.R.color.black));
                return convertView;
            }
        });
        mListView.setOnItemClickListener((parent, v1, position, id) -> {
            if (mListClass.length > position) {
                if (mListClass[position] == null) {
                    return;
                }
                Intent intent = new Intent(mContext, mListClass[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initParms(Bundle parms) {

    }
}
