package com.hql.wheather.fragment

import android.app.ActionBar
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.hql.wheather.R
import com.hql.wheather.beans.ActionBean
import com.hql.wheather.beans.WheatherBean
import com.hql.wheather.services.WheatherService
import com.hql.wheather.utils.MyLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.LinearInterpolator


/**
 * Created by HuangQiangLong on 2017/10/11 0011.
 */
class TodayFragment : BaseFragment() ,WheatherService.DataCallBack{
lateinit var mLocation :TextView
    lateinit var mFreash:ImageView
    lateinit var mAddress :TextView
    lateinit var mType :TextView
    lateinit var mTtempHigh :TextView
    lateinit var mTtempLow :TextView
    lateinit var mFx :TextView
    lateinit var mFl :TextView
    lateinit var mPm25 :TextView
    lateinit var mNotice :TextView
    lateinit var mShidu :TextView
    lateinit var mAqi :TextView
    lateinit var mTem10:ImageView
    lateinit var mTem1:ImageView
    lateinit var mTypePic:ImageView
    override fun getLayoutID(): Int {
        return R.layout.fragment_today
    }

    companion object {
        var instanse :TodayFragment = TodayFragment()
    }

    override fun onResume() {
        super.onResume()
        regitEventBus()
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }
    override fun initView(view: View, savedInstanceState: Bundle?) {
        mAddress = view.findViewById(R.id.tv_address)
        mType = view.findViewById(R.id.tv_type)
        mTtempHigh = view.findViewById(R.id.tv_temp_high)
        mTtempLow = view.findViewById(R.id.tv_temp_low)
        mFx = view.findViewById(R.id.tv_temp_wind_fx)
        mFl = view.findViewById(R.id.tv_temp_fl)
        mPm25 = view.findViewById(R.id.tv_pm25)
        mNotice = view.findViewById(R.id.tv_notice)
        mAqi  = view.findViewById(R.id.tv_aqi)
        mShidu = view.findViewById(R.id.tv_shidu)
        mTem10 = view.findViewById(R.id.iv_tem_10)
        mTem1 = view.findViewById(R.id.iv_tem_1)
        mTypePic = view.findViewById(R.id.iv_type_pic)
        mLocation = view.findViewById(R.id.tv_address)
        mFreash = view.findViewById(R.id.imag_add_address)

        mFreash.setOnClickListener{
            MyLog.d(this,"============mLocation click")
            var bean:ActionBean = ActionBean()
            bean.action = 0
           EventBus.getDefault().post(bean)
            startAnimation()
        }

    }
    override fun getWheatherInfoCallBack(bean:WheatherBean) {
        MyLog.d(this,"getWheatherInfoCallBack==="+bean.toString())
      }
    override fun onDestroy() {
        super.onDestroy()

    }
    fun regitEventBus(){
        EventBus.getDefault().register(this)
    }
    @Subscribe
    public fun onEventMainThread(bean: WheatherBean) {
        MyLog.d(this,"onEventMainThread======getWheatherInfoCallBack=="+bean.toString())

        mAddress.text = bean.city
        mShidu.text = bean.shidu
        mPm25.text=bean.pm25
        //mPm25.text= bean.pm10

        mNotice.text=bean.ganmao



        //var dateMap = HashMap<String, String>()//日期
       // var sunriseMap = HashMap<String, String>()//日出
       mTtempHigh.text = bean.highMap.get("1")// var highMap = HashMap<String, String>()//最高温
        mTtempLow.text = bean.lowMap.get("1")//var lowMap = HashMap<String, String>()//最低温
        //var sunsetMap = HashMap<String, String>()//日落
        mAqi.text = bean.aqiMap.get("1")//var aqiMap = HashMap<String, String>()//空气质量
       mFx.text = bean.fxMap.get("1")// var fxMap = HashMap<String, String>()//风向
        mFl.text = bean.flMap.get("1")//var flMap = HashMap<String, String>()//风力
     // mType =  // var typeMap = HashMap<String, String>()//": "晴",
      mNotice.text = bean.noticeMap.get("0")//  var noticeMap = HashMap<String, String>()//": "天气干燥，请适当增加室内湿度"
      var wendu =  bean.wendu.toFloat()

       var tem10 = (wendu/10).toInt()

        var tem1 = wendu%10

        MyLog.d(this,"wendu============="+tem10 +"=="+tem1)
        when(tem10){
            0-> mTem10.setImageResource(R.mipmap.a_0)
            1-> mTem10.setImageResource(R.mipmap.a_1)
            2-> mTem10.setImageResource(R.mipmap.a_2)
            3-> mTem10.setImageResource(R.mipmap.a_3)
            4-> mTem10.setImageResource(R.mipmap.a_4)
            5-> mTem10.setImageResource(R.mipmap.a_5)
            6-> mTem10.setImageResource(R.mipmap.a_6)
            7-> mTem10.setImageResource(R.mipmap.a_7)
            8-> mTem10.setImageResource(R.mipmap.a_8)
            9-> mTem10.setImageResource(R.mipmap.a_9)

        }
        when(tem1){
            0f-> mTem1.setImageResource(R.mipmap.a_0)
            1f-> mTem1.setImageResource(R.mipmap.a_1)
            2f-> mTem1.setImageResource(R.mipmap.a_2)
            3f-> mTem1.setImageResource(R.mipmap.a_3)
            4f-> mTem1.setImageResource(R.mipmap.a_4)
            5f-> mTem1.setImageResource(R.mipmap.a_5)
            6f-> mTem1.setImageResource(R.mipmap.a_6)
            7f-> mTem1.setImageResource(R.mipmap.a_7)
            8f-> mTem1.setImageResource(R.mipmap.a_8)
            9f-> mTem1.setImageResource(R.mipmap.a_9)

        }

     var type = bean.typeMap.get("1")
        mType.text = type
        when(type){
           getString(R.string.type_qing)-> mTypePic.setImageResource(R.mipmap.type_qing)
            getString(R.string.type_yu)-> mTypePic.setImageResource(R.mipmap.type_yu)
            getString(R.string.type_yin)-> mTypePic.setImageResource(R.mipmap.type_yin)
        }
   //
 stopAnimation()
    }
    var isAnimation :Boolean = false
    lateinit var operatingAnim : Animation
    fun startAnimation(){
         operatingAnim = AnimationUtils.loadAnimation(mContext,R.anim.rote)
        val lin = LinearInterpolator()
        operatingAnim.setInterpolator(lin)
        mFreash.startAnimation(operatingAnim)
        isAnimation =true
    }
    fun stopAnimation(){
        if(isAnimation){
            mFreash.clearAnimation()
            isAnimation = false
        }
    }
}