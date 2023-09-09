package com.example.contact_nb12.mypage

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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentAddContactDialogBinding
import com.example.contact_nb12.detail.ContactDetailFragment
import com.example.contact_nb12.detail.DetailActivity
import com.example.contact_nb12.main.MainActivity
import com.example.contact_nb12.models.Contact

class EditContactDialogFragment(val item: Contact, val position: Int) : DialogFragment() {
    private var _binding: FragmentAddContactDialogBinding? = null
    private val binding get() = _binding!!
    private var mItem: Contact = item
    private val registerDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        }

    // 기기이미지 가져오기
    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imgUri = result.data?.data
            imgUri?.let {
                mItem = mItem.copy(Img = it)
                binding.dialogImg.setImageURI(it)
            }
        }
    }

    companion object {
        const val REQ_CODE_IMAGE = 0
        const val BUNDLE_POSITION = "bundle_position"
        const val BUNDLE_IMAGE = "bundle_image"
        const val BUNDLE_NAME = "bundle_name"
        const val BUNDLE_NUMBER = "bundle_number"
        const val BUNDLE_EMAIL = "bundle_email"
        const val BUNDLE_BIRTH = "bundle_birth"
        const val BUNDLE_NICKNAME = "bundle_nickname"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }


    private fun initView() = with(binding) {
        // 기기 이미지 가져오기
        dialogImg.setOnClickListener {
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
                    arrayOf(permission),
                    REQ_CODE_IMAGE
                )
            }
        }

        dialogSaveBtn.setOnClickListener {
            val item = packagingData()
            val bundle = Bundle()
            bundle.let {
                item.let { it2 ->
                    it.putParcelable(BUNDLE_IMAGE, it2.Img)
                    it.putString(BUNDLE_NAME, it2.name)
                    it.putString(BUNDLE_NUMBER, it2.phonenumber)
                    it.putString(BUNDLE_BIRTH, it2.birth)
                    it.putString(BUNDLE_EMAIL, it2.email)
                    it.putString(BUNDLE_NICKNAME, it2.nickname)
                    it.putInt(BUNDLE_POSITION, position)
                }
            }
            when(position) {
                // MyPage
                MyPageFragment.ENTRY_POINT_MYPAGE -> {
                    dismiss()
                    val fragment = MyPageFragment.newInstace()
                    fragment.arguments = bundle
                    MainActivity.instance.getMyPageFragment().updateData(item)
                }
                // Detail
                else -> {
                    dismiss()
                    val intent = DetailActivity.editIntent(requireContext(), item, position)
                    registerDetailLauncher.launch(intent)

                    if (position != -1) {
                        MainActivity.instance.getListFragment().modifyContactItem(position, item)
                    }
                }
            }
        }

        dialogCancelBtn.setOnClickListener {
            dismiss()
        }

    }

    private fun initData() = with(binding) {
        mItem.let {
            dialogImg.setImageURI(it.Img ?: getDefaultImgUri())
            dialogName.setText(it.name)
            dialogPhone.setText(it.phonenumber)
            dialogBirth.setText(it.birth)
            dialogEmail.setText(it.email)
            dialogNickName.setText(it.nickname)
        }
    }

    private fun packagingData(): Contact = with(binding) {
        return Contact(
            Img = mItem.Img,
            name = dialogName.text.toString(),
            phonenumber = dialogPhone.text.toString(),
            birth = dialogBirth.text.toString(),
            email = dialogEmail.text.toString(),
            nickname = dialogNickName.text.toString()
        )
    }

    // 기본프로필이미지 Uri
    private fun getDefaultImgUri(): Uri {
        val resId = R.drawable.dialog_profile
        val imgUri =
            "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${resources.getResourcePackageName(resId)}/${
                resources.getResourceTypeName(resId)
            }/${resources.getResourceEntryName(resId)}"
        return Uri.parse(imgUri)!!
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CODE_IMAGE) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imageLauncher.launch(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}