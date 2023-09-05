package com.example.contact_nb12.mypage

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentAddContactDialogBinding
import com.example.contact_nb12.mypage.MyPageFragment.Companion.REQUEST_CODE_ADD_CONTACT

class AddContactDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentAddContactDialogBinding
    private var originalPhoneNum: String = ""
    private var originalBirth: String = ""
    private var originalEmail: String = ""
    private var originalNickname: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)

        binding.mypageNameEdittext.setText(originalPhoneNum)
        binding.mypageBirthEdittext.setText(originalBirth)
        binding.mypageEmailEdittext.setText(originalEmail)
        binding.mypageNickName.setText(originalNickname)



        binding.mypageSaveBtn.setOnClickListener {
            Log.d("AddContactDialogFragment", "Save button clicked")
            val newPhoneNum = binding.mypageNameEdittext.text.toString()
            val newbirth = binding.mypageBirthEdittext.text.toString()
            val newEmail = binding.mypageEmailEdittext.text.toString()
            val newNickname = binding.mypageNickName.text.toString()

            val bundle = Bundle()
            bundle.putString("newPhoneNumber", newPhoneNum)
            bundle.putString("newBirthday", newbirth)
            bundle.putString("newEmail", newEmail)
            bundle.putString("newNickName", newNickname)
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

    fun setOriginalInfo(phoneNum: String, birth: String, email: String, nickname: String) {
        originalPhoneNum = phoneNum
        originalBirth = birth
        originalEmail = email
        originalNickname = nickname
    }
}
