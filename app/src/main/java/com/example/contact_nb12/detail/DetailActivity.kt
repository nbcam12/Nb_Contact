package com.example.contact_nb12.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.MainActivityBinding
import com.example.contact_nb12.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(ContactDetailFragment())

    }
    private fun setFragment(frag : Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragmentContainerView, frag)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }

}