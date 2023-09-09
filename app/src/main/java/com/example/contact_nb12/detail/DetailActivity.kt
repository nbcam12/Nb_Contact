package com.example.contact_nb12.detail

import ContactListFragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contact_nb12.databinding.ActivityDetailBinding
import com.example.contact_nb12.models.Contact
import com.example.contact_nb12.mypage.EditContactDialogFragment


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val position by lazy {
        intent.getIntExtra(ContactListFragment.EXTRA_POSITION, -1)
    }

    private val item by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(ContactListFragment.EXTRA_CONTACT_MODEL, Contact::class.java)
        } else {
            intent.getParcelableExtra(ContactListFragment.EXTRA_CONTACT_MODEL)
        }
    }

    init {
        _instance = this
    }

    companion object {

        private var _instance : DetailActivity? = null
        val instance get() = _instance!!
        fun newIntent(context: Context): Intent = Intent(context, DetailActivity::class.java)
        fun editIntent(context: Context, item: Contact, position: Int): Intent =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(ContactListFragment.EXTRA_CONTACT_MODEL, item) // 객체를 intent에 추가
                putExtra(ContactListFragment.EXTRA_POSITION, position)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init() {
        val bundle = Bundle().apply {
            // Contact Model
            item?.let {
                putString(ContactDetailFragment.BUNDLE_IMAGE, it.Img?.toString())
                putString(ContactDetailFragment.BUNDLE_NAME, it.name)
                putString(ContactDetailFragment.BUNDLE_NUMBER, it.phonenumber)
                putString(ContactDetailFragment.BUNDLE_EMAIL, it.email)
                putString(ContactDetailFragment.BUNDLE_BIRTH, it.birth)
                putString(ContactDetailFragment.BUNDLE_NICKNAME, it.nickname)
            }

            // position
            if (position != -1) {
                putInt(ContactDetailFragment.BUNDLE_POSITION, position)
            }
        }

        val fragment = ContactDetailFragment.newInstacne()
        // fragment 이동
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(com.example.contact_nb12.R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }
}