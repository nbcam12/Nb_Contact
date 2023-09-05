package com.example.contact_nb12.main

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.contact_nb12.databinding.MainActivityBinding
import com.example.contact_nb12.mypage.AddContactDialogFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val viewPagerAdapter : ViewPagerAdapter by lazy{
        ViewPagerAdapter(this@MainActivity)
    }

    init {
        _instance = this
    }

    companion object {
        private var _instance: MainActivity? = null
        fun instance() : MainActivity = _instance!!
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

        mainFabAdd.setOnClickListener {
            // FragmentDialog 구현후 호출
        }

        // NumberPad 이동
        mainFabNumberpad.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"))
            startActivity(intent)
        }
    }

    // fragment에서 editButton 컨트롤용
    fun getEditButton(): ImageButton = binding.mainEdit
}