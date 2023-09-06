package com.example.contact_nb12.main

import ContactListFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.contact_nb12.R
import com.example.contact_nb12.mypage.MyPageFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val list = ArrayList<TabModel>()

    // tab 추가
    fun addTabItem(item: TabModel) {
        val res = list.filter { it.fragment== item.fragment}.size
        if(res > 0) {
            list.remove(item)
            list.add(item)
        } else {
            list.add(item)
        }
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