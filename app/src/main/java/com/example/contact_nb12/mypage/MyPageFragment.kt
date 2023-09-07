package com.example.contact_nb12.mypage

import android.app.Activity
import android.content.Context
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentMyPageBinding
import com.example.contact_nb12.main.MainActivity
import java.util.jar.Manifest

class MyPageFragment : Fragment() {
    private var mainActivity: MainActivity? = null
    private lateinit var binding: FragmentMyPageBinding
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        /*binding.profileAd.setOnClickListener {
            openGallery()
        }*/

        val editButton = mainActivity?.getEditButton()
        editButton?.setOnClickListener {
            val dialogFragment = AddContactDialogFragment(
                binding.telText.text.toString(),
                binding.birthText.text.toString(),
                binding.EmailText.text.toString(),
                binding.nickNamText.text.toString())

            selectedImageUri?.let { it1 -> dialogFragment.setImageUri(it1) } // 이미지 URI를 다이얼로그에 전달
            dialogFragment.setTargetFragment(this@MyPageFragment, REQUEST_CODE_ADD_CONTACT)
            dialogFragment.show(parentFragmentManager, "AddContactDialogFragment")
        }

        return binding.root
    }

    /*    private fun openGallery() {
            val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), 0)
            }
        }*/

    /*  override fun onRequestPermissionsResult(
          requestCode: Int,
          permissions: Array<String>,
          grantResults: IntArray
      ) {
          if (requestCode == 0) {
              if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  openGallery()
              } else {
                  // 권한이 거부되면 사용자에게 설명을 보여줄 수 있습니다.
              }
          }
      }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MyPageFragment", "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { imageUri ->
                selectedImageUri = imageUri
                binding.profileAd.setImageURI(imageUri)
            }
        } else if (requestCode == REQUEST_CODE_ADD_CONTACT && resultCode == Activity.RESULT_OK) {
            // 다이얼로그에서 업데이트한 데이터를 받아서 처리
            data?.let {
                val newPhoneNumber = it.getStringExtra("newPhoneNumber")
                val newEmail = it.getStringExtra("newEmail")
                val newBirthday = it.getStringExtra("newBirthday")
                val newNickname = it.getStringExtra("newNickName")

                // 업데이트된 데이터를 UI에 반영
                binding.telText.text = newPhoneNumber
                binding.EmailText.text = newEmail
                binding.birthText.text = newBirthday
                binding.nickNamText.text = newNickname

                val newImageUri = data?.extras?.getParcelable<Uri>("newImageUri")
                newImageUri?.let { imageUri ->
                    selectedImageUri = imageUri
                    binding.profileAd.setImageURI(imageUri)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    override fun onPause() {
        super.onPause()
        val editButton = mainActivity?.getEditButton()
        editButton?.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        val editButton = mainActivity?.getEditButton()
        editButton?.visibility = View.VISIBLE
    }

    companion object {
        const val REQUEST_CODE_ADD_CONTACT = 123
    }
}