package com.example.contact_nb12.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentContactDetailBinding


class ContactDetailFragment : Fragment() {

    private val binding get() =_binding!!
    private var _binding: FragmentContactDetailBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentContactDetailBinding.inflate(inflater, container,false)

        var a = "안녕"

        binding.Name.text = a
        binding.numberText.text = "010-1234-5678"
        binding.EmailText.text = a
        binding.BirthDateText.text = a
        binding.NickNameText.text = a


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
    }
}