package com.example.contact_nb12.main

import ContactListFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.contact_nb12.R
import com.example.contact_nb12.mypage.MyPageFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val list = ArrayList<TabModel>()

    init {
        list.add(
            TabModel(
                ContactListFragment.newInstacne(),
                R.string.tab_contactlist
            )
        )
        list.add(
            TabModel(
                MyPageFragment(),
                R.string.tab_mypage
            )
        )
    }

    // 타이틀 가져오기
    fun getTitle(position: Int): Int = list[position].titleRes
    fun getContactListFragment() : ContactListFragment = list.find { it.fragment is ContactListFragment }?.fragment as ContactListFragment
    fun getMyPageFragment() : MyPageFragment = list.find { it.fragment is MyPageFragment }?.fragment as MyPageFragment

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position].fragment
    }
}