package com.hql.wheather.fragment

import android.os.Bundle
import android.view.View
import com.hql.wheather.R
import com.hql.wheather.beans.WheatherBean
import com.hql.wheather.utils.MyLog
import com.hql.wheather.utils.MyUtil
import com.hql.wheather.views.CurveView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by HuangQiangLong on 2017/10/11 0011.
 */
class MoreDayFragment : BaseFragment() {
    lateinit var mCurview: CurveView
    override fun getLayoutID(): Int {
        return R.layout.fragmet_moreday
    }

    companion object {
        var instance: MoreDayFragment = MoreDayFragment()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mCurview = view.findViewById(R.id.my_more_curveview)

    }

    override fun onResume() {
        super.onResume()
        regitEventBus()
    }

    override fun onPause() {
        super.onPause()
        unRegitEventBus()
    }

    fun regitEventBus() {
        EventBus.getDefault().register(this)
    }

    fun unRegitEventBus() {
        EventBus.getDefault().unregister(this)
    }
    @Subscribe
    public fun onEventMainThread(bean: WheatherBean) {

        var TemLeve :ArrayList<String> = ArrayList<String>()

        TemLeve.add(bean.minTem.toString())
        TemLeve.add(bean.maxTem.toString())
        MyLog.d(this,"===WheatherBean   minTem:"+bean.minTem.toString())
        MyLog.d(this,"===WheatherBean    maxTem:"+bean.maxTem.toString())
        var TemDate :ArrayList<String> = ArrayList<String>()

        for (date in 0..bean.dateMap.size-1){
            MyLog.d(this,"===WheatherBean   dateMap:"+bean.dateMap.get(date.toString()))
            TemDate.add( bean.dateMap.get(date.toString())!!)
        }
        var TemData : HashMap<String,HashMap<String,Int>>  = HashMap<String, HashMap<String,Int>>()
        var temdata1 :HashMap<String,Int> = HashMap<String,Int>()
        for (data in 0..bean.highMap.size-1){
            temdata1.put(bean.dateMap.get(data.toString())!!,MyUtil.getNumFromString(bean.highMap.get(data.toString()).toString())!!.toInt())
        }
        TemData.put("temdata1",temdata1)
        var temdata2 :HashMap<String,Int> = HashMap<String,Int>()
        for (data in 0..bean.lowMap.size-1){

            temdata2.put( bean.dateMap.get(data.toString())!!,MyUtil.getNumFromString(bean.lowMap.get(data.toString()).toString())!!.toInt())
        }
        TemData.put("temdata2",temdata2)


        mCurview.setData(TemLeve,TemDate,TemData)
        mCurview.isNeedY(false)
    }

}