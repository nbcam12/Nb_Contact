package com.example.contact_nb12.detail

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

    public val item:Contact? by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra("selectedItem",Contact::class.java)
        }else{
            intent.getParcelableExtra<Contact>("selectedItem")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /// fragement로 값 넘기기
        var fragment = ContactDetailFragment()
        var bundle = Bundle()
        bundle.putInt("image",item!!.Img)
        bundle.putString("name",item!!.name)
        bundle.putString("number",item!!.phonenumber)
        bundle.putString("email",item!!.email)
        bundle.putString("birth",item!!.birth)
        bundle.putString("nickName",item!!.nickname)
        fragment.arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌
        supportFragmentManager!!.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()


    }


}