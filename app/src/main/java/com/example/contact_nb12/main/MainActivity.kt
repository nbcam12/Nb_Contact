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
    private val requestContactPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                initTabs(true)
            } else {
                initTabs(false)
            }
            initView()
        }

    companion object {
        const val PERMISSION_READ_CONTACTS_REQ = 110
        fun newIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initContactPermission()
    }

    private fun initContactPermission() {
        val checkPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            initTabs(true)
        } else {
            requestContactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }


    private fun initView() = with(binding) {
        // ViewPager + TabLayout
        mainViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(mainTabLayout, mainViewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

        mainFabAdd.setOnClickListener {
            AddContactDialogFragment(
                "", // 초기 전화번호 값
                "", // 초기 생일 값
                "", // 초기 이메일 값
                "",// 초기 닉네임 값
                R.drawable.dialog_profile//초기 사진 값
            ).show(
                supportFragmentManager, "AddContactDialogFragment"
            )
        }

        // NumberPad 이동
        mainFabNumberpad.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"))
            startActivity(intent)
        }
    }

    // 디바이스 주소록 가져오기
    private fun getContacts(): MutableList<Contact> {
        val contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val list = mutableListOf<Contact>()

        contacts?.let {
            if (contacts.count > 0) {
                while (it.moveToNext()) {
                    val name =
                        contacts.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber =
                        contacts.getInt(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val image =
                        contacts.getInt(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.PHOTO_ID))
                    val email =
                        contacts.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS))

                    val model = Contact(
                        Img = image,
                        name = name,
                        phonenumber = phoneNumber.toString(),
                        email = email,
                        birth = "",
                        nickname = "",
                    )
                    list.add(model)
                }
            }
        }

        return list
    }

    private fun initTabs(isGranted: Boolean) {
        when (isGranted) {
            true -> {
                val list = getContacts()
                viewPagerAdapter.addTabItem(
                    TabModel(
                        ContactListFragment.newDeviceContactsInstacne(list),
                        R.string.tab_contactlist
                    )
                )
            }

            else -> {
                viewPagerAdapter.addTabItem(
                    TabModel(
                        ContactListFragment.newDummyDataInstance(),
                        R.string.tab_contactlist
                    )
                )
            }
        }

        viewPagerAdapter.addTabItem(
            TabModel(
                MyPageFragment(),
                R.string.tab_mypage
            )
        )
    }

    // fragment에서 editButton 컨트롤용
    fun getEditButton(): ImageButton = binding.mainEdit
}