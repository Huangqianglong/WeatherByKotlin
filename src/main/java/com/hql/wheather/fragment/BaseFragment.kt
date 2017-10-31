package com.hql.wheather.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hql.wheather.beans.WheatherBean
import com.hql.wheather.utils.MyLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by HuangQiangLong on 2017/10/11 0011.
 */
abstract  class BaseFragment:Fragment() {
    lateinit var mContext:Context
    lateinit var mActivity :Activity

    abstract fun getLayoutID():Int
    abstract fun initView(view:View,savedInstanceState: Bundle?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view :View = inflater!!.inflate(getLayoutID(),container,false)
        initView(view,savedInstanceState)
        return view
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        if(null != activity){
            mActivity = activity
            mContext = activity.applicationContext
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(null !=context){
            mContext = context
        }

    }



}