package com.example.contact_nb12.main

import ContactListFragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.contact_nb12.databinding.MainActivityBinding
import com.example.contact_nb12.list.ContactAddDialog
import com.example.contact_nb12.mypage.MyPageFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val viewPagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(this@MainActivity)
    }

    init{
        _instance = this
    }

    companion object {
        private var _instance: MainActivity? = null
        val instance get() = _instance!!
        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
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
            ContactAddDialog.newInstance().show(supportFragmentManager, "ContactAddDialog")
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
    fun getListFragment() : ContactListFragment = viewPagerAdapter.getContactListFragment()
    fun getMyPageFragment() : MyPageFragment = viewPagerAdapter.getMyPageFragment()
}