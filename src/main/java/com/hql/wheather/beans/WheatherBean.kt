package com.hql.wheather.beans

/**
 * Created by HuangQiangLong on 2017/10/17 0017.
 */
class WheatherBean {
   // init {
        var city = "深圳"
        var shidu =""//": "86%",
        var pm25 =""//": 18,
        var pm10=""//: 27,
        var quality=""//": "优",
        var wendu=""// "22",
        var ganmao=""// "各类人群可自由活动",

        var dateMap = HashMap<String, String>()//日期
        var sunriseMap = HashMap<String, String>()//日出
        var highMap = HashMap<String, String>()//最高温
        var lowMap = HashMap<String, String>()//最低温
        var sunsetMap = HashMap<String, String>()//日落
        var aqiMap = HashMap<String, String>()//空气质量
        var fxMap = HashMap<String, String>()//风向
        var flMap = HashMap<String, String>()//风力
        var typeMap = HashMap<String, String>()//": "晴",
        var noticeMap = HashMap<String, String>()//": "天气干燥，请适当增加室内湿度"

    var maxTem =0f
    var minTem = 0f
    //}

}