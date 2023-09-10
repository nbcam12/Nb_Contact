package com.example.contact_nb12.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.contact_nb12.Util.Utils
import com.example.contact_nb12.Util.Utils.getDefaultImgUri
import com.example.contact_nb12.Util.Utils.getImageForUri
import com.example.contact_nb12.databinding.FragmentContactDetailBinding
import com.example.contact_nb12.models.Contact
import com.example.contact_nb12.mypage.EditContactDialogFragment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class ContactDetailFragment : Fragment() {

    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private val item by lazy {
        arguments?.let {
            Contact(
                Img = Uri.parse(it.getString(BUNDLE_IMAGE)),
                name = it.getString(BUNDLE_NAME) ?: "",
                phonenumber = it.getString(BUNDLE_NUMBER) ?: "",
                email = it.getString(BUNDLE_EMAIL) ?: "",
                birth = it.getString(BUNDLE_BIRTH) ?: "",
                nickname = it.getString(BUNDLE_NICKNAME) ?: "",
            )
        }
    }
    private var updatedItem: Contact? = null

    private val position by lazy {
        arguments?.getInt(BUNDLE_POSITION)
    }

    init {
        _instance = this
    }

    companion object {
        const val BUNDLE_POSITION = "bundle_position"
        const val BUNDLE_IMAGE = "bundle_image"
        const val BUNDLE_NAME = "bundle_name"
        const val BUNDLE_NUMBER = "bundle_number"
        const val BUNDLE_EMAIL = "bundle_email"
        const val BUNDLE_BIRTH = "bundle_birth"
        const val BUNDLE_NICKNAME = "bundle_nickname"

        private var _instance: ContactDetailFragment? = null
        val instance get() = _instance!!
//        fun newInstance() = ContactDetailFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView() = with(binding) {
        // 전화
        phone.setOnClickListener {
            var number = binding.detailPhone.text
            val myUri = Uri.parse("tel:${number}")
            val intent = Intent(Intent.ACTION_DIAL, myUri)
            startActivity(intent)
        }

        // 뒤로가기
        BackPoint.setOnClickListener {
            requireActivity().finish()
        }

        // 수정
        Edit.setOnClickListener {
            detailImage.clipToOutline = true
            val dialogFragment = if (updatedItem == null) {
                EditContactDialogFragment(item!!, position!!)
            } else {
                EditContactDialogFragment(updatedItem!!, position!!)
            }
            dialogFragment.show(parentFragmentManager, "EditContactDialogFragment")
        }
    }

    private fun initData() = with(binding) {
        // init
        item?.let {
            getImageForUri(
                requireContext(),
                it.Img ?: getDefaultImgUri(requireContext()),
                detailImage
            )
            detailImage.clipToOutline = true
            detailName.text = it.name
            detailPhone.text = it.phonenumber
            detailBirth.text = it.birth
            detailEmail.text = it.email
            detailNickName.text = it.nickname
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun updateData(item: Contact) = with(binding) {
        updatedItem = item
        item.let {
            getImageForUri(
                requireContext(),
                it.Img ?: getDefaultImgUri(requireContext()),
                detailImage
            )
            detailImage.clipToOutline = true
            detailName.text = it.name
            detailPhone.text = it.phonenumber
            detailBirth.text = it.birth
            detailEmail.text = it.email
            detailNickName.text = it.nickname
        }
    }
}