package com.example.contact_nb12.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contact_nb12.databinding.FragmentContactDetailBinding
import com.example.contact_nb12.main.MainActivity
import com.example.contact_nb12.mypage.AddContactDialogFragment
import com.example.contact_nb12.mypage.MyPageFragment.Companion.REQUEST_CODE_ADD_CONTACT


class ContactDetailFragment : Fragment() {

    private val binding get() =_binding!!
    private var _binding: FragmentContactDetailBinding? = null
    private var selectedImageUri: Uri? = null


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
        requireActivity().finish()
        }
        binding.Edit.setOnClickListener {
            val image = arguments?.getInt("image", 0)
            val dialogFragment = AddContactDialogFragment(
                binding.Name.text.toString(),
                binding.numberText.text.toString(),
                binding.BirthDateText.text.toString(),
                binding.EmailText.text.toString(),
                binding.NickNameText.text.toString(),
                image!!
            )



            selectedImageUri?.let { it1 -> dialogFragment.setImageUri(it1) } // 이미지 URI를 다이얼로그에 전달
            dialogFragment.setTargetFragment(this@ContactDetailFragment, REQUEST_CODE_ADD_CONTACT)
            dialogFragment.show(parentFragmentManager, "AddContactDialogFragment")

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MyPageFragment", "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")

            data?.let {
                val newName = it.getStringExtra("newName")
                val newPhoneNumber = it.getStringExtra("newPhoneNumber")
                val newEmail = it.getStringExtra("newEmail")
                val newBirthday = it.getStringExtra("newBirthday")
                val newNickname = it.getStringExtra("newNickName")

                // 업데이트된 데이터를 UI에 반영
                binding.Name.text = newName
                binding.numberText.text = newPhoneNumber
                binding.EmailText.text = newEmail
                binding.BirthDateText.text = newBirthday
                binding.NickNameText.text = newNickname

                val newImageUri = data?.extras?.getParcelable<Uri>("newImageUri")
                newImageUri?.let { imageUri ->
                    selectedImageUri = imageUri
                    binding.UserImage.setImageURI(imageUri)

            }
        }
    }

}