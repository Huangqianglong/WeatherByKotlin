package com.hql.wheather.utils

import android.util.Log
import java.util.*

/**
 * Created by HuangQiangLong on 2017/10/17 0017.
 */
class MyLog{

    companion object {
        val DEBUG:Boolean = true
        fun d(obj:Any,msg:String ){
            if(DEBUG)
                Log.d(obj.toString(),msg)
        }
    }

}