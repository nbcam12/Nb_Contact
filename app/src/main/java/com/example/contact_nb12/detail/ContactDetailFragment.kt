package com.example.contact_nb12.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentContactDetailBinding
import com.example.contact_nb12.main.MainActivity


class ContactDetailFragment : Fragment() {

    private val binding get() =_binding!!
    private var _binding: FragmentContactDetailBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentContactDetailBinding.inflate(inflater, container,false)

        arguments?.getInt("image",0)?.let { binding.UserImage.setImageResource(it) }
        binding.Name.text = arguments?.getString("name")
        binding.numberText.text = arguments?.getString("number")
        binding.EmailText.text = arguments?.getString("email")
        binding.BirthDateText.text = arguments?.getString("birth")
        binding.NickNameText.text = arguments?.getString("nickName")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.phone.setOnClickListener {
            var number = binding.numberText.text
            val myUri = Uri.parse("tel:${number}")
            val intent = Intent(Intent.ACTION_DIAL, myUri)
            startActivity(intent)
        }

        binding.BackPoint.setOnClickListener {
            val mainintent = Intent(context, MainActivity::class.java)
            startActivity(mainintent)
        }
    }
}