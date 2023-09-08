package com.example.contact_nb12.list

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentContactAddDialogBinding
import com.example.contact_nb12.models.Contact
import com.example.contact_nb12.mypage.MyPageFragment.Companion.REQUEST_CODE_ADD_CONTACT


class ContactAddDialog : DialogFragment() {
    private lateinit var selectedImageView: ImageView
    private lateinit var binding: FragmentContactAddDialogBinding
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    interface OnContactAddedListener {
        fun onContactAdded(contact: Contact)
    }

    private var contactAddedListener: OnContactAddedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       binding = FragmentContactAddDialogBinding.inflate(inflater,container,false)
        selectedImageView = binding.addImg
        val saveButton = binding.addSaveBtn
        selectedImageUri = null
        saveButton.setOnClickListener {
            val name = binding.addName.text.toString()
            val phoneNumber = binding.addEditTel.text.toString()
            val birthDay = binding.addBirthEdittext.text.toString()
            val email = binding.addEmailEdittext.text.toString()
            val nickName = binding.addNickName.text.toString()
            val newNickname = nickName

            val bundle = Bundle()
            bundle.putString("newName",name)
            bundle.putString("newPhonenum",phoneNumber)
            bundle.putString("newbirth",birthDay)
            bundle.putString("newnick",newNickname)

            selectedImageUri?.let { imageUri ->
                bundle.putString("newImageUri", imageUri.toString())
            }

            targetFragment?.onActivityResult(
                REQUEST_CODE_ADD_CONTACT,
                Activity.RESULT_OK,
                Intent().putExtras(bundle)
            )
            val newContact = Contact(
                Img = selectedImageUri,
                name = name,
                phonenumber = phoneNumber,
                email =  email,
                birth = birthDay,
                nickname = nickName
            )
            contactAddedListener?.onContactAdded(newContact)
            dialog?.dismiss()
        }

        binding.addCancelBtn.setOnClickListener {
            dismiss()
        }

        binding.addImg.setOnClickListener {
            openGallery()
        }

        return binding.root

    }
    fun setOnContactAddedListener(listener: OnContactAddedListener) {
        contactAddedListener = listener
    }
    private fun openGallery() {
        val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), 0)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MyPageFragment", "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { imageUri ->
                selectedImageUri = imageUri
                binding.addImg.setImageURI(imageUri.toString())
            }
        }
    }
    private fun ImageView.setImageURI(newImageUriString: String?) {
        if(newImageUriString != null){
            val uri = Uri.parse(newImageUriString)
            setImageURI(uri)
        }else{
            setImageResource(R.drawable.default_profile)
        }
    }
}