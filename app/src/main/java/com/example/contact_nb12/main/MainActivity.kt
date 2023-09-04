package com.example.contact_nb12.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contact_nb12.databinding.MainActivityBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val viewPagerAdapter : ViewPagerAdapter by lazy{
        ViewPagerAdapter(this@MainActivity)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        // ViewPager + TabLayout
        mainViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(mainTabLayout, mainViewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()
    }
}