package com.yuyi.demo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yuyi.demo.R;
import com.yuyi.demo.databinding.ActivityBitmapEffectBinding;
import com.yuyi.lib.abs.BaseSwipeBackActivity;
import com.yuyi.lib.effect.ProcessorManager;

public class BitmapEffectActivity extends BaseSwipeBackActivity {

    @Override
    public View bindLayout() {
        return ActivityBitmapEffectBinding.inflate(getLayoutInflater()).getRoot();
    }

    @Override
    public void initView() {
        setReturnText(R.string.bitmap_effect);
        ImageView imageView = (ImageView) findViewById(R.id.pic);
        imageView.setImageResource(R.drawable.hh);
        Button apply = (Button) findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hh);
                Bitmap copyBitmap = ProcessorManager.getInstance().processorBitmap(bitmap, -30, -10, -16, -18);
                imageView.setImageBitmap(copyBitmap);
            }
        });

    }
}