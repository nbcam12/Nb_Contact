package com.example.contact_nb12.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentMyPageBinding
import com.example.contact_nb12.main.MainActivity

class MyPageFragment : Fragment() {
    private var mainActivity: MainActivity? = null
    private lateinit var binding: FragmentMyPageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MyPageFragment", "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")
        if (requestCode == REQUEST_CODE_ADD_CONTACT && resultCode == Activity.RESULT_OK){
            data?.let{
                val newPhoneNumber = it.getStringExtra("newPhoneNumber")
                val newEail = it.getStringExtra("newEmail")
                val newBirthday = it.getStringExtra("newBirthday")
                val newNickname = it.getStringExtra("newNickName")
                Log.d("MyPageFragment", "Received data: newPhoneNumber=$newPhoneNumber, newEmail=$newEail, newBirthday=$newBirthday, newNickname=$newNickname")
                binding.telText.text = newPhoneNumber
                binding.EmailText.text = newEail
                binding.birthText.text = newBirthday
                binding.nickNamText.text = newNickname
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity){
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

        editButton?.setOnClickListener{
            val dialogFragment = AddContactDialogFragment()
            dialogFragment.setTargetFragment(this@MyPageFragment, REQUEST_CODE_ADD_CONTACT)
            dialogFragment.show(parentFragmentManager, "AddContactDialogFragment")
        }
    }
    companion object {
        const val REQUEST_CODE_ADD_CONTACT = 123
    }
}