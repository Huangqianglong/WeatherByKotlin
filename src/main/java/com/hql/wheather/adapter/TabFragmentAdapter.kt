package com.hql.wheather.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by HuangQiangLong on 2017/10/11 0011.
 */
class TabFragmentAdapter(fm:FragmentManager,fragments: ArrayList<Fragment>,titles:ArrayList<String>):FragmentPagerAdapter(fm)  {
    lateinit var mFragments:ArrayList<Fragment>
    lateinit var mTabTitles:ArrayList<String>
    init {
        mFragments = fragments
        mTabTitles = titles
    }


    override fun getItem(position: Int): Fragment {
       return mFragments.get(position)
    }

    override fun getCount(): Int {
       return  mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTabTitles.get(position)
    }
}