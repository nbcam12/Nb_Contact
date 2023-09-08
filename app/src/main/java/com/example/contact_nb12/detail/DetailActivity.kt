package com.example.contact_nb12.detail

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.MainActivityBinding
import com.example.contact_nb12.databinding.ActivityDetailBinding
import com.example.contact_nb12.list.DataManager
import com.example.contact_nb12.models.Contact
import com.example.contact_nb12.mypage.AddContactDialogFragment

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var selectedImageUri: Uri? = null
    private val imageUri: Uri? by lazy {
        intent.getStringExtra("selectedImageUri")?.let { Uri.parse(it) }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val position = intent.getIntExtra("position", -1)
        if (position != -1) {
            val selectedContact = DataManager.getContacts()[position]

            // 이미지 URI를 문자열로 전달
            val fragment = ContactDetailFragment()
            val bundle = Bundle()
            bundle.putString("name", selectedContact.name)
            bundle.putString("number", selectedContact.phonenumber)
            bundle.putString("email", selectedContact.email)
            bundle.putString("birth", selectedContact.birth)
            bundle.putString("nickName", selectedContact.nickname)
            imageUri?.let { bundle.putString("newImageUri", it.toString()) }

            fragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, fragment)
                .commit()
        }


}}