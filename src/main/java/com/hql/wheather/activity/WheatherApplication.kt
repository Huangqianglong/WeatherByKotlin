package com.hql.wheather.activity

import android.app.Application
import android.app.Service
import android.os.Vibrator
import com.baidu.mapapi.SDKInitializer
import com.hql.wheather.baidu.services.LocationService


/**
 * Created by HuangQiangLong on 2017/10/11 0011.
 */
class WheatherApplication :Application() {
    lateinit var mLocationServices :LocationService
    lateinit var mVibrator:Vibrator

    override fun onCreate() {
        super.onCreate()
        mLocationServices = LocationService(applicationContext)
        mVibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        SDKInitializer.initialize(applicationContext)

    }
}