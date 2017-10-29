package com.hql.wheather.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TableLayout
import com.hql.wheather.R
import com.hql.wheather.adapter.TabFragmentAdapter
import com.hql.wheather.beans.WheatherBean
import com.hql.wheather.fragment.MoreDayFragment
import com.hql.wheather.fragment.TodayFragment
import com.hql.wheather.services.WheatherService
import com.hql.wheather.utils.MyLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class WheatherShowActivity : BaseActivity() {
    lateinit var mService:WheatherService
    lateinit var mTabTitle :TabLayout
    lateinit var mFragmentContainer:LinearLayout
    lateinit var mActivityBackGound:LinearLayout
    lateinit var mTabAdapter:TabFragmentAdapter
    lateinit var mViewPage:ViewPager

     var mTabName :ArrayList<String> = ArrayList<String>()
     var mFragmentList:ArrayList<Fragment> = ArrayList<Fragment>()

    override fun getContainerViewId(): Int {
       return R.layout.activity_wheather_show
    }

    override fun getFragmentContainerViewId(): Int {
        return R.id.fragment_container
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initState()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        initEventBus()
        initView()
        initData()

    }
    fun initView(){
        mTabTitle = findViewById(R.id.tab_title)as TabLayout;
        mFragmentContainer = findViewById(getFragmentContainerViewId()) as LinearLayout
        mViewPage = findViewById(R.id.cotainer_viewpage) as ViewPager
        mActivityBackGound = findViewById(R.id.main_back) as LinearLayout
        mTabTitle.setTabTextColors(Color.WHITE,Color.WHITE)
    }
    fun initData(){
        initTabName()
        initFragment()
        initAdapter()
        initService()
    }
    fun initTabName(){
        mTabName.clear()
        mTabName.add(getString(R.string.tab_today))
        mTabName.add(getString(R.string.tab_moreday))
    }
    fun initFragment(){
        mFragmentList.clear()
        mFragmentList.add(TodayFragment.instanse)
        mFragmentList.add(MoreDayFragment.instance)
    }
    fun initAdapter(){
        mTabAdapter = TabFragmentAdapter(supportFragmentManager,mFragmentList,mTabName)
        mViewPage.adapter = mTabAdapter

        mTabTitle.setupWithViewPager(mViewPage)
        mTabTitle.tabMode = TabLayout.MODE_FIXED
    }
    fun initService(){
        var serviceIntent = Intent(this,WheatherService::class.java)
        bindService(serviceIntent,mConnection, Context.BIND_AUTO_CREATE)
        MyLog.d(this,"initService=====================")

    }
    val mConnection : ServiceConnection = object:ServiceConnection{
        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            MyLog.d(this,name.toString())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            MyLog.d(this,"onServiceDisconnected")
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            MyLog.d(this,"onServiceConnected===================")
            mService = (service as WheatherService.WheatherServiceBinder).getWheatherService()
           // mService.setDataCallBackListener(this@WheatherShowActivity)
            mService.test()
            mService.updateWheather()
            MyLog.d(this,"onServiceConnected==========222=========")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null!= mService){
            unbindService(mConnection);
        }
        EventBus.getDefault().unregister(this)
    }
    fun initEventBus(){
        EventBus.getDefault().register(this)
    }
    @Subscribe
    public fun onEventMainThread(bean: WheatherBean) {
        var type = bean.typeMap.get("1")

        when(type){
            getString(R.string.type_qing)-> mActivityBackGound.setBackgroundResource(R.mipmap.weather_sunny_day_scene_preview)
            getString(R.string.type_yu)-> mActivityBackGound.setBackgroundResource(R.mipmap.weather_rain_day_scene_preview)
            getString(R.string.type_yin)-> mActivityBackGound.setBackgroundResource(R.mipmap.weather_cloudy_day_scene_preview)
        }
    }

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     *
     */
    private fun initState() {

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            //
            val linear_bar = findViewById(R.id.ll) as LinearLayout
            linear_bar.visibility = View.VISIBLE
            //获取到状态栏的高度
            val statusHeight = getStatusBarHeight()
            //动态的设置隐藏布局的高度
            val params = linear_bar.layoutParams as LinearLayout.LayoutParams
            params.height = statusHeight
            linear_bar.layoutParams = params
        }
    }

    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    private fun getStatusBarHeight(): Int {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = Integer.parseInt(field.get(obj).toString())
            return resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }
}
