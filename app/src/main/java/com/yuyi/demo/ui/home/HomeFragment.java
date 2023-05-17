package com.yuyi.demo.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yuyi.demo.R;
import com.yuyi.demo.activity.BitmapEffectActivity;
import com.yuyi.demo.activity.PanelDountActivity;
import com.yuyi.demo.databinding.FragmentHomeBinding;
import com.yuyi.demo.ui.download.DownloadActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private int[] mListText = {R.string.breakpoint, R.string.function_policy, R.string.panel_dount, R.string.image_preview, R.string.pager_view, R.string.swipe_delete, R.string.infinitecycle, R.string.bitmap_effect};
    private Class[] mListClass = {DownloadActivity.class, null, PanelDountActivity.class, null, null, null, null, BitmapEffectActivity.class};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ListView listView = binding.listHome;
        listView.setAdapter(new BaseAdapter() {
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
                    convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, null);
                }
                TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
                text1.setText(getItem(position));
                text1.setTextColor(getResources().getColor(android.R.color.black));
                return convertView;
            }
        });
        listView.setOnItemClickListener((parent, v1, position, id) -> {
            if (mListClass.length > position) {
                if (mListClass[position] == null) {
                    return;
                }
                Intent intent = new Intent(getContext(), mListClass[position]);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}