package com.yuyi.demo.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yuyi.demo.R;
import com.yuyi.lib.abs.BaseActivity;

public class MainActivity extends BaseActivity {

    private ListView mListView = null;

    private int[] mListText = {R.string.breakpoint, R.string.function_policy, R.string.panel_dount, R.string.image_preview, R.string.pager_view};

    private Class[] mListClass = {DownloadActivity.class, FunctionActivity.class, PanelDountActivity.class, ImagePreviewActivity.class, PagerActivity.class};

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        setReturnVisibility(View.GONE);
        setTitle(R.string.app_name);
        mListView = (ListView) findViewById(R.id.listview);
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
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(android.R.layout.simple_list_item_1, null);
                }
                TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
                text1.setText(getItem(position));
                text1.setTextColor(getResources().getColor(android.R.color.black));
                return convertView;
            }
        });
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            if (mListClass.length > position) {
                if (mListClass[position] == null) {
                    return;
                }
                Intent intent = new Intent(this, mListClass[position]);
                startActivity(intent);
            }
        });
    }
}
