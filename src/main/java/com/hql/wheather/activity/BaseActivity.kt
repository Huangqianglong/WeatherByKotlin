package com.hql.wheather.activity

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.hql.wheather.fragment.BaseFragment
/**
 * Created by HuangQiangLong on 2017/10/11 0011.
 */
abstract class BaseActivity :AppCompatActivity(){

    abstract fun getContainerViewId():Int
    abstract fun getFragmentContainerViewId():Int
    fun addFragment(fragment:BaseFragment){
        if(null!=fragment){
            supportFragmentManager.beginTransaction().replace(getFragmentContainerViewId(),fragment as Fragment,fragment.javaClass.simpleName)
                    .addToBackStack(fragment.javaClass.simpleName)
                    .commit()
        }
    }
    fun removeFragment(){
        if (supportFragmentManager.backStackEntryCount> 1){
            supportFragmentManager.popBackStack()
        }else{
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContainerViewId())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (1 == supportFragmentManager.backStackEntryCount)
                finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}