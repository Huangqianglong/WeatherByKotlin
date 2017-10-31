package com.hql.wheather.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by HuangQiangLong on 2017/10/28 0028.
 */
class MyUtil{
    companion object {
        fun getNumFromString(msg:String): Float {

            var patter: Pattern = Pattern.compile("[0-9]|\\.|\\-")
            var matcher: Matcher = patter.matcher(msg)
            var sb = StringBuffer()
            while (matcher.find()){
                sb.append(msg.substring(matcher.start(),matcher.end()))
            }
           // MyLog.d(this,"---wwww------------"+sb.toString().toFloat())
            return sb.toString().toFloat()
        }
    }
}