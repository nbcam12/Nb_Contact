package com.example.contact_nb12.mypage

import android.annotation.SuppressLint

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentAddContactDialogBinding
import com.example.contact_nb12.detail.ContactDetailFragment
import com.example.contact_nb12.models.Contact
import com.example.contact_nb12.mypage.MyPageFragment.Companion.REQUEST_CODE_ADD_CONTACT

class AddContactDialogFragment(private var originName:String,
                                private var originalPhoneNum: String,
                               private var originalBirth: String,
                               private var originalEmail: String,
                               private var originalNickname: String,
                               private var originImage:Int) : DialogFragment() {
    private val PICK_IMAGE_REQUEST = 1

    private lateinit var binding: FragmentAddContactDialogBinding

    private var selectedImageUri: Uri? = null


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)


        binding.diaImg.setOnClickListener {
            openGallery()
        }
        binding.mypageName.setText(originName)
        binding.mypageNameEdittext.setText(originalPhoneNum)
        binding.mypageBirthEdittext.setText(originalBirth)
        binding.mypageEmailEdittext.setText(originalEmail)
        binding.mypageNickName.setText(originalNickname)
        binding.diaImg.setImageResource(originImage)

        selectedImageUri?.let { imageUri ->
            binding.diaImg.setImageURI(imageUri)
        }

        binding.mypageSaveBtn.setOnClickListener {
            Log.d("AddContactDialogFragment", "Save button clicked")

            val newName= binding.mypageName.text.toString()
            val newPhoneNum = binding.mypageNameEdittext.text.toString()
            val newbirth = binding.mypageBirthEdittext.text.toString()
            val newEmail = binding.mypageEmailEdittext.text.toString()
            val newNickname = binding.mypageNickName.text.toString()


            val bundle = Bundle()
            bundle.putString("newName",newName)
            bundle.putString("newPhoneNumber", newPhoneNum)
            bundle.putString("newBirthday", newbirth)
            bundle.putString("newEmail", newEmail)
            bundle.putString("newNickName", newNickname)


            selectedImageUri?.let { imageUri ->
                bundle.putParcelable("newImageUri", imageUri)
            }
            Log.d("AddContactDialogFragment", "Data to send: $bundle")

            targetFragment?.onActivityResult(
                REQUEST_CODE_ADD_CONTACT,
                Activity.RESULT_OK,
                Intent().putExtras(bundle)
            )

            dismiss()
        }
        binding.mypageCancelBtn.setOnClickListener {
            dismiss()
        }
        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MyPageFragment", "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { imageUri ->
                selectedImageUri = imageUri
                binding.diaImg.setImageURI(imageUri)
            }
        }
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

    fun setOriginalInfo(phoneNum: String, birth: String, email: String, nickname: String) {
        originalPhoneNum = phoneNum
        originalBirth = birth
        originalEmail = email
        originalNickname = nickname
    }
    fun setImageUri(imageUri: Uri) {
        selectedImageUri = imageUri
    }

}