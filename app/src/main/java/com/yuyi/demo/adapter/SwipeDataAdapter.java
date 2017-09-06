package com.yuyi.demo.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuyi.demo.R;
import com.yuyi.demo.databinding.SwipeItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huanghua
 */

public class SwipeDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private SwipeItemBinding mBinding;

    private List<ItemData> mDataList;

    public SwipeDataAdapter(Context context) {
        mContext = context;
        initData();
    }

    private void initData() {
        mDataList = new ArrayList<>();
        ItemData data1 = new ItemData();
        data1.name = "张三";
        data1.content = "你不要乱来";
        data1.date = "2017-10-1";
        mDataList.add(data1);

        ItemData data2 = new ItemData();
        data2.name = "李四";
        data2.content = "不要乱来";
        data2.date = "2017-10-2";
        mDataList.add(data2);

        ItemData data3 = new ItemData();
        data3.name = "王五";
        data3.content = "要乱来";
        data3.date = "2017-10-3";
        mDataList.add(data3);

        ItemData data4 = new ItemData();
        data4.name = "张三";
        data4.content = "乱来";
        data4.date = "2017-10-4";
        mDataList.add(data4);

        ItemData data5 = new ItemData();
        data5.name = "张三";
        data5.content = "来";
        data5.date = "2017-10-5";
        mDataList.add(data5);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.swipe_item, parent, false);
        return new DataViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemData data = mDataList.get(position);
        mBinding.itemName.setText(data.name);
        mBinding.itemContent.setText(data.content);
        mBinding.itemDate.setText(data.date);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private class DataViewHolder extends RecyclerView.ViewHolder {

        public DataViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ItemData {
        private String name;
        private String content;
        private String date;
    }
}
