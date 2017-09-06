package com.yuyi.demo.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.yuyi.demo.R
import com.yuyi.lib.abs.BaseSwipeBackActivity
import com.yuyi.lib.ui.PagerAdapter
import com.yuyi.lib.ui.PagerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PagerActivity : BaseSwipeBackActivity() {

    private var mPagerView: PagerView? = null
    private var mPagerAdapter: AppPagerAdpater? = null
    private var mLoadingBar: ProgressBar? = null

    override fun bindLayout(): Int {
        return R.layout.activity_pager
    }

    override fun initView() {
        setReturnText(R.string.pager_view)
        mLoadingBar = findViewById(R.id.loading_app) as ProgressBar
        mPagerView = findViewById(R.id.pager_view) as PagerView
        mPagerAdapter = AppPagerAdpater(applicationContext)
        mPagerView!!.setAdapter(mPagerAdapter)
        mPagerView!!.setOnItemClickListener { item, pos -> startActivity((item as AppInfo).startIntent) }
        showPagerView()
    }

    private fun showPagerView() {
        mLoadingBar!!.visibility = View.VISIBLE
        mPagerView!!.visibility = View.GONE
        Observable.just(mPagerAdapter)
                .map { getInstallApp() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { apps ->
                    run {
                        mLoadingBar!!.visibility = View.GONE
                        mPagerView!!.visibility = View.VISIBLE
                        mPagerAdapter!!.setData(apps)
                        mPagerView!!.refreshView()
                    }
                }
    }

    private fun getInstallApp(): List<AppInfo> {
        val appList = ArrayList<AppInfo>()
        val pkgList = packageManager.getInstalledPackages(0)
        pkgList.filter {
            applicationContext.packageManager.getLaunchIntentForPackage(it.applicationInfo.packageName) != null
        }.forEach {
            appList.add(AppInfo(applicationContext, it.applicationInfo))
        }
        return appList
    }

    private class AppInfo(context: Context, appInfo: ApplicationInfo) {
        var icon: Drawable? = null
        var name: String? = null
        var startIntent: Intent? = null

        init {
            val pm = context.packageManager
            name = appInfo.loadLabel(pm).toString()
            icon = appInfo.loadIcon(pm)
            startIntent = pm.getLaunchIntentForPackage(appInfo.packageName)
        }
    }

    private class AppPagerAdpater(context: Context) : PagerAdapter<AppInfo>(context, ArrayList<AppInfo>(6)) {

        override fun onBindView(view: View, item: AppInfo, position: Int) {
            val iconView = view.findViewById(R.id.item_app_icon) as ImageView
            iconView.setImageDrawable(item.icon)
            val nameView = view.findViewById(R.id.item_app_name) as TextView
            nameView.text = item.name
        }

        override fun getItemLayoutId(position: Int, item: AppInfo?): Int {
            return R.layout.item_launcher_app
        }

        fun setData(list: List<AppInfo>) {
            mList = list
        }
    }
}
