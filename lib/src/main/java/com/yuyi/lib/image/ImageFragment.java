package com.yuyi.lib.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yuyi.lib.R;
import com.yuyi.lib.abs.BaseFragment;
import com.yuyi.lib.image.photodrawee.PhotoDraweeView;
import com.yuyi.lib.utils.MyLog;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * @author huanghua
 */

public class ImageFragment extends BaseFragment {

    public static ImageFragment newInstance(String url) {
        ImageFragment mFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    private PhotoDraweeView mPhotoDraweeView = null;
    private SmoothProgressBar mSmoothProgressBar = null;
    private TextView mTvInfo = null;
    private WebView mWebView = null;
    private View mRootView = null;

    private String mUrl = "";
    private int mBackgroundId = android.R.color.black;

    @Override
    public int bindLayout() {
        return R.layout.preview_item_layout;
    }

    @Override
    public void initView(View view) {
        mRootView = view.findViewById(R.id.root_view);
        mRootView.setBackgroundResource(mBackgroundId);
        mPhotoDraweeView = (PhotoDraweeView) view.findViewById(R.id.imagea);
        mSmoothProgressBar = (SmoothProgressBar) view.findViewById(R.id.progress);
        mTvInfo = (TextView) view.findViewById(R.id.tvInfo);
        mWebView = (WebView) view.findViewById(R.id.webview);
        mSmoothProgressBar.setIndeterminate(true);
        mPhotoDraweeView.setOnViewTapListener((view1, x, y) -> getActivity().finish());
        doBusiness();
    }

    @Override
    public void initParms(Bundle parms) {
        if (parms != null && parms.containsKey("url")) {
            mUrl = parms.getString("url");
        }
    }

    /**
     * 设置背景颜色
     *
     * @param resId
     */
    public void setBackground(int resId) {
        mBackgroundId = resId;
        if (mRootView != null) {
            mRootView.setBackgroundResource(mBackgroundId);
        }
    }

    /**
     * 加载图片,调用这个方法可以重新加载
     */
    public void doBusiness() {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mUrl))
                .setProgressiveRenderingEnabled(true)
                .build();

        GenericDraweeHierarchy hierarchy =
                new GenericDraweeHierarchyBuilder(getResources()).setProgressBarImage(
                        new ImageLoadProgressBar(level -> {
                            if (level > 100 && mSmoothProgressBar.getVisibility() == View.VISIBLE) {
                                mSmoothProgressBar.setVisibility(View.GONE);
                            }
                        }, getThemeColor(getActivity()))).build();
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setControllerListener(listener);
        controller.setImageRequest(request);
        controller.setOldController(mPhotoDraweeView.getController());
        controller.setAutoPlayAnimations(true);
        mPhotoDraweeView.setHierarchy(hierarchy);
        mPhotoDraweeView.setController(controller.build());

    }

    private BaseControllerListener<ImageInfo> listener = new BaseControllerListener<ImageInfo>() {

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            MyLog.i("onFailure:" + throwable.getMessage());
            mSmoothProgressBar.setVisibility(View.GONE);
            mTvInfo.setVisibility(View.VISIBLE);
            mTvInfo.setText(R.string.load_pic_fail);
        }

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
            MyLog.i("onFinalImageSet");
            if (imageInfo == null) {
                return;
            }
            if (imageInfo.getWidth() > 1024 || imageInfo.getHeight() > 1024) {
                MyLog.i("这个是长图");
                readLargePicture(mWebView);
                return;
            }
            mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            mSmoothProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onSubmit(String id, Object callerContext) {
            super.onSubmit(id, callerContext);
            MyLog.i("onSubmit");
            mSmoothProgressBar.setVisibility(View.VISIBLE);
            mTvInfo.setVisibility(View.GONE);
        }
    };

    private int getThemeColor(@NonNull Context context) {
        return getThemeAttrColor(context, R.attr.colorPrimary);
    }

    private int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void readLargePicture(final WebView large) {
        mPhotoDraweeView.setVisibility(View.INVISIBLE);
        large.getSettings().setJavaScriptEnabled(true);
        large.getSettings().setUseWideViewPort(true);
        large.getSettings().setLoadWithOverviewMode(true);
        large.getSettings().setBuiltInZoomControls(true);
        large.getSettings().setDisplayZoomControls(false);

        large.setVerticalScrollBarEnabled(false);
        large.setHorizontalScrollBarEnabled(false);
        large.addJavascriptInterface(new JavaScriptObject(mContext), "picturejs");

//        String str1 = "file://" + file.getAbsolutePath().replace("/mnt/sdcard/", "/sdcard/");
        String str2 = "<html>\n<head>\n     <style>\n          html,body{background:#3b3b3b;margin:0;padding:0;}          *{-webkit-tap-highlight-color:rgba(0, 0, 0, 0);}\n     </style>\n     <script type=\"text/javascript\">\n     var imgUrl = \""
                + mUrl
                + "\";"
                + "     var objImage = new Image();\n"
                + "     var realWidth = 0;\n"
                + "     var realHeight = 0;\n"
                + "\n"
                + "     function onLoad() {\n"
                + "          objImage.onload = function() {\n"
                + "               realWidth = objImage.width;\n"
                + "               realHeight = objImage.height;\n"
                + "\n"
                + "               document.gagImg.src = imgUrl;\n"
                + "               onResize();\n"
                + "          }\n"
                + "          objImage.src = imgUrl;\n"
                + "     }\n"
                + "\n"
                + "     function imgOnClick() {\n"
                + "			window.picturejs.onClick();"
                + "     }\n"
                + "     function onResize() {\n"
                + "          var scale = 1;\n"
                + "          var newWidth = document.gagImg.width;\n"
                + "          if (realWidth > newWidth) {\n"
                + "               scale = realWidth / newWidth;\n"
                + "          } else {\n"
                + "               scale = newWidth / realWidth;\n"
                + "          }\n"
                + "\n"
                + "          hiddenHeight = Math.ceil(30 * scale);\n"
                + "          document.getElementById('hiddenBar').style.height = hiddenHeight + \"px\";\n"
                + "          document.getElementById('hiddenBar').style.marginTop = -hiddenHeight + \"px\";\n"
                + "     }\n"
                + "     </script>\n"
                + "</head>\n"
                + "<body onload=\"onLoad()\" onresize=\"onResize()\" onclick=\"Android.toggleOverlayDisplay();\">\n"
                + "     <table style=\"width: 100%;height:100%;\">\n"
                + "          <tr style=\"width: 100%;\">\n"
                + "               <td valign=\"middle\" align=\"center\" style=\"width: 100%;\">\n"
                + "                    <div style=\"display:block\">\n"
                + "                         <img name=\"gagImg\" src=\"\" width=\"100%\" style=\"\" onclick=\"imgOnClick()\" />\n"
                + "                    </div>\n"
                + "                    <div id=\"hiddenBar\" style=\"position:absolute; width: 0%; background: #3b3b3b;\"></div>\n"
                + "               </td>\n" + "          </tr>\n" + "     </table>\n" + "</body>\n" + "</html>";
        large.loadDataWithBaseURL("file:///android_asset/", str2, "text/html", "utf-8", null);

        large.setTag(new Object());
        large.postDelayed(() -> large.setVisibility(View.VISIBLE), 500);
    }

    private class JavaScriptObject {
        Context mContxt;

        private JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface
        public void onClick() {
            getActivity().finish();
        }
    }
}
