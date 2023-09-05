package com.example.contact_nb12.mypage

import android.content.Context
import android.os.Bundle
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
            val dialogFragment = AddContactDialogFragment() // 다이얼로그 프래그먼트 인스턴스 생성
            dialogFragment.show(parentFragmentManager, "AddContactDialogFragment") // 다이얼로그 띄우기
        }
    }
}