package com.hql.wheather.services

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClientOption
import com.hql.wheather.activity.WheatherApplication
import com.hql.wheather.baidu.services.LocationService
import com.hql.wheather.beans.ActionBean
import com.hql.wheather.beans.WheatherBean
import com.hql.wheather.utils.MyLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import java.util.jar.Manifest
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by HuangQiangLong on 2017/10/17 0017.
 */
class WheatherService : Service() {
    final var STOP_FLASH :Int = 1
    lateinit var mContext:Context
    val WHEATHER_INTERFACE: String = "http://www.sojson.com/open/api/weather/json.shtml?city="
    var mSearchCity = "北京"
    lateinit var mRequestQueue: RequestQueue
    val myBinder: WheatherServiceBinder = WheatherServiceBinder()
     var mDataCallBack: DataCallBack? = null
    lateinit var mBaiduService : LocationService
    inner class WheatherServiceBinder : Binder() {
        fun getWheatherService(): WheatherService {
            return this@WheatherService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }

    override fun onCreate() {
        super.onCreate()
        MyLog.d(this, "onCreate");
        mContext = this
        initEventBus()
        initVolley()
    }

    fun initEventBus(){
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    fun test() {
        MyLog.d(this, "WheatherService   ============================test")
    }

    fun initVolley() {
        mRequestQueue = Volley.newRequestQueue(applicationContext)
    }
    fun updateWheather(){
        getWheatherData(mSearchCity)
    }
    fun getWheatherData(city: String) {
        var request = JsonObjectRequest(WHEATHER_INTERFACE + city,
                null,
                Response.Listener { response ->
                    MyLog.d(this, response.toString())
                    handleWheatherData(response)
                },
                Response.ErrorListener { error ->
                    MyLog.d(this, error.toString())
                    handleErroAction(STOP_FLASH)
                }
        )
        mRequestQueue.add(request)

    }
    fun handleErroAction(actionSize : Int){
        var action:ActionBean = ActionBean()
        action.action=actionSize
        EventBus.getDefault().post(action)
    }

    fun handleWheatherData(data :Any ){
        var wb :WheatherBean = WheatherBean()


        var jsonAll = JSONObject(data.toString())
        var status = jsonAll.getString("message")
        if(!status.equals("Success !")){
            MyLog.d(this," erro!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+status)
            ToastMessge(" erro!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+status)
            return
        }
        MyLog.d(this," wb.shidu================"+ jsonAll.toString())
        wb.city = jsonAll.getString("city")
        var jsonData = jsonAll.getJSONObject("data")
        if(!jsonAll.getString("message").equals("Success !")){
            ToastMessge("get data fail")
           handleErroAction(STOP_FLASH)
            return
        }
       // ToastMessge("get data fail")
        wb.shidu = jsonData.getString("shidu")
        wb.pm25 = jsonData.getString("pm25")
        wb.pm10 = jsonData.getString("pm10")
        wb.wendu = jsonData.getString("wendu")
        wb.ganmao = jsonData.getString("ganmao")



        var jsonYesterday=jsonData.getJSONObject("yesterday")
        MyLog.d(this," jsonYesterday================"+ jsonYesterday.toString())
        var flag = 0
        wb.dateMap.put(""+flag,jsonYesterday.getString("date")) //"date": "17日星期二",
        wb.sunriseMap.put(""+flag,jsonYesterday.getString("sunrise"))// "sunrise": "06:20",
        wb.highMap.put(""+flag, jsonYesterday.getString("high")) //"high": "高温 29.0℃",
        wb.lowMap.put(""+flag, jsonYesterday.getString("low"))//"low": "低温 22.0℃",
        wb.sunsetMap.put(""+flag, jsonYesterday.getString("sunset"))//"sunset": "17:57",
        wb.aqiMap.put(""+flag, jsonYesterday.getString("aqi"))//"aqi": 33.0,
        wb.fxMap.put(""+flag,jsonYesterday.getString("fx"))// "fx": "无持续风向",
        wb.flMap.put(""+flag, jsonYesterday.getString("fl"))// "fl": "<3级",
        wb.typeMap.put(""+flag, jsonYesterday.getString("type"))//"type": "多云",
        wb.noticeMap.put(""+flag, jsonYesterday.getString("notice"))// "notice": "悠悠的云里有淡淡的诗"

        wb.maxTem = getNumFromString(jsonYesterday.getString("high"))
        wb.minTem = getNumFromString(jsonYesterday.getString("low"))

        var jsonForecast = jsonData.getJSONArray("forecast")

        for (dataFlag in  0..jsonForecast.length()-1){
            var jsonData = jsonForecast[dataFlag].toString()
            var data = JSONObject(jsonData)
            wb.dateMap.put(""+(dataFlag+1),data.getString("date")) //"date": "17日星期二",
            wb.sunriseMap.put(""+(dataFlag+1),data.getString("sunrise"))// "sunrise": "06:20",
            wb.highMap.put(""+(dataFlag+1), data.getString("high")) //"high": "高温 29.0℃",
            getNumFromString(data.getString("high"))
            wb.lowMap.put(""+(dataFlag+1), data.getString("low"))//"low": "低温 22.0℃",
            wb.sunsetMap.put(""+(dataFlag+1), data.getString("sunset"))//"sunset": "17:57",
            wb.aqiMap.put(""+(dataFlag+1), data.getString("aqi"))//"aqi": 33.0,
            wb.fxMap.put(""+(dataFlag+1),data.getString("fx"))// "fx": "无持续风向",
            wb.flMap.put(""+(dataFlag+1), data.getString("fl"))// "fl": "<3级",
            wb.typeMap.put(""+(dataFlag+1), data.getString("type"))//"type": "多云",
            wb.noticeMap.put(""+(dataFlag+1), data.getString("notice"))// "notice": "悠悠的云里有淡淡的诗"
            if(getNumFromString(data.getString("high"))>wb.maxTem ){
               wb.maxTem = getNumFromString(data.getString("high").toString())


            }
            if( getNumFromString(jsonYesterday.getString("low"))< wb.minTem){
                wb.minTem = getNumFromString(jsonYesterday.getString("low").toString())
            }
        }

        EventBus.getDefault().post(wb)
       // if(null!= mDataCallBack){
      //      mDataCallBack?.getWheatherInfoCallBack(wb)
       // }
    }
    open interface DataCallBack{
        fun getWheatherInfoCallBack(bean:WheatherBean)

    }
    fun setDataCallBackListener(listener:DataCallBack){
        mDataCallBack = listener
    }
    var mToast:Toast?= null
    fun ToastMessge(msg:String){
        if (null == mToast){
            mToast = Toast.makeText(this,"", Toast.LENGTH_SHORT)
        }
        mToast?.setText(msg)

        mToast?.show()
    }
   /* @Subscribe
    public fun onEventMainThread(bean: WheatherBean) {
        MyLog.d(this,"onEventMainThread======services=="+bean.toString())
    }*/
    fun getNumFromString(msg:String): Float {

       var patter:Pattern = Pattern.compile("[0-9]|\\.|\\-")
       var matcher:Matcher = patter.matcher(msg)
       var sb = StringBuffer()
       while (matcher.find()){
           sb.append(msg.substring(matcher.start(),matcher.end()))
       }
      // MyLog.d(this,"---wwww------------"+sb.toString().toFloat())
       return sb.toString().toFloat()
   }
    fun getLocation(){
        MyLog.d(this,"============mLocation getLocation")
        if(ContextCompat.checkSelfPermission(mContext,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
           // ActivityCompat.requestPermissions(getApplication() as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            MyLog.d(this,"============requestPermissions")
        }
        else{
            startLocation()
        }

    }
    fun startLocation(){
        MyLog.d(this,"============mLocation startLocation")

        mBaiduService = (application as WheatherApplication).mLocationServices
        mBaiduService.registerListener(mLocationListener)
        mBaiduService.setLocationOption(mBaiduService.defaultLocationClientOption)
        mBaiduService.start()
    }
    fun stopLocation(){
        MyLog.d(this,"============mLocation stopLocation")
        mBaiduService.unregisterListener(mLocationListener)
        mBaiduService.stop()
    }
    val mLocationListener :BDAbstractLocationListener = object :BDAbstractLocationListener(){
        override fun onReceiveLocation(p0: BDLocation?) {
            MyLog.d(this,"============mLocation onReceiveLocation:"+p0!!.city.toString())
           mSearchCity = p0!!.city.toString()
            updateWheather()
            MyLog.d(this,"============mLocation updateWheather")
            stopLocation()
            MyLog.d(this,"============mLocation onReceiveLocation  end")
        }

        override fun onLocDiagnosticMessage(p0: Int, p1: Int, p2: String?) {
            super.onLocDiagnosticMessage(p0, p1, p2)
            MyLog.d(this,"============mLocation onLocDiagnosticMessage")
        }

        override fun onConnectHotSpotMessage(p0: String?, p1: Int) {
            super.onConnectHotSpotMessage(p0, p1)
            MyLog.d(this,"============mLocation onConnectHotSpotMessage")
        }
    }
    @Subscribe
    public fun onEventMainThread(bean:ActionBean){
        MyLog.d(this,"============mLocation onEventMainThread:"+bean.action)

        when(bean.action){
            0->getLocation()
        }
    }
}
