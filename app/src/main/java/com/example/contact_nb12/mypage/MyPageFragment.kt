package com.example.contact_nb12.mypage

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentMyPageBinding
import com.example.contact_nb12.detail.ContactDetailFragment
import com.example.contact_nb12.main.MainActivity
import com.example.contact_nb12.models.Contact

class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val editButton by lazy {
        MainActivity.instance.getEditButton()
    }

//    private val item by lazy {
//        arguments?.let{
//            Contact(
//                Img = Uri.parse(it.getString(ContactDetailFragment.BUNDLE_IMAGE) ?: getDefaultImgUri().toString()),
//                name = it.getString(ContactDetailFragment.BUNDLE_NAME) ?: "",
//                phonenumber = it.getString(ContactDetailFragment.BUNDLE_NUMBER) ?: "",
//                email = it.getString(ContactDetailFragment.BUNDLE_EMAIL) ?: "",
//                birth = it.getString(ContactDetailFragment.BUNDLE_BIRTH)?: "",
//                nickname = it.getString(ContactDetailFragment.BUNDLE_NICKNAME)?: "",
//            )
//        }
//    }

    companion object{
        const val ENTRY_POINT_MYPAGE = -99
        fun newInstace() = MyPageFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {

        editButton.setOnClickListener {
            val item = packagingData()
            val dialogFragment = EditContactDialogFragment(
               item = item,
                position = ENTRY_POINT_MYPAGE
            )
            dialogFragment.show(parentFragmentManager, "AddContactDialogFragment")
        }
    }

    fun updateData(item: Contact)= with(binding) {
        item.let{
            profileImage.setImageURI(it.Img)
            profileName.text = it.name
            profilePhone.text = it.phonenumber
            profileBirth.text = it.birth
            profileEmail.text = it.email
            profileNickname.text = it.nickname
        }
    }

    private fun initData()= with(binding){
//        item?.let {
//            profileImage.setImageURI(it.Img)
//            profileName.text = it.name
//            profilePhone.text = it.phonenumber
//            profileBirth.text = it.birth
//            profileEmail.text = it.email
//            profileNickname.text = it.nickname
//        }
    }

    private fun packagingData(): Contact = with(binding) {
        return Contact(
            Img = getDefaultImgUri(),
            name = profileName.text.toString(),
            phonenumber = profilePhone.text.toString(),
            birth = profileBirth.text.toString(),
            email = profileEmail.text.toString(),
            nickname = profileNickname.text.toString()
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

    override fun onPause() {
        super.onPause()
        editButton.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        editButton.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}