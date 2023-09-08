package com.example.contact_nb12.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.MainActivityBinding
import com.example.contact_nb12.models.Contact
import com.example.contact_nb12.mypage.AddContactDialogFragment
import com.example.contact_nb12.mypage.MyPageFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val viewPagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(this@MainActivity)
    }


    companion object {
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
    fun getFlotingButton(): ImageButton = binding.mainFabAdd
}