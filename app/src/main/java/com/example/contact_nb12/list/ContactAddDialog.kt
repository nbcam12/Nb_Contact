package com.example.contact_nb12.list

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResultListener
import com.example.contact_nb12.R
import com.example.contact_nb12.Util.Utils.getDefaultImgUri
import com.example.contact_nb12.databinding.FragmentContactAddDialogBinding
import com.example.contact_nb12.detail.ContactDetailFragment
import com.example.contact_nb12.main.MainActivity
import com.example.contact_nb12.models.Contact
import com.example.contact_nb12.mypage.EditContactDialogFragment


class ContactAddDialog : DialogFragment() {
    private var _binding: FragmentContactAddDialogBinding? = null
    private val binding get() = _binding!!
    private var imageUri : Uri? = null

    // 기기이미지 가져오기
    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imgUri = result.data?.data
            imgUri?.let {
                imageUri = it
                binding.addImg.setImageURI(it)
            }
        }
    }

    companion object {
        const val REQ_CODE_IMAGE = 0

        fun newInstance() = ContactAddDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactAddDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        // 저장하기
        addSaveBtn.setOnClickListener {
            val item = packagingData()
            MainActivity.instance.getListFragment().addContactItem(item)
            dismiss()
        }

        // 취소
        addCancelBtn.setOnClickListener {
            dismiss()
        }

        // 기기 이미지 가져오기
        addImg.setOnClickListener {
            val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                imageLauncher.launch(intent)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQ_CODE_IMAGE
                )
            }
        }
    }

    private fun packagingData(): Contact= with(binding) {
            val name = addName.text.toString()
            val phoneNumber = addEditTel.text.toString()
            val birthDay = addBirthEdittext.text.toString()
            val email = addEmailEdittext.text.toString()
            val nickName = addNickName.text.toString()

        return Contact(
            Img = imageUri ?: getDefaultImgUri(requireContext()),
            name = name,
            phonenumber = phoneNumber,
            email = email,
            birth = birthDay,
            nickname = nickName
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EditContactDialogFragment.REQ_CODE_IMAGE) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imageLauncher.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}