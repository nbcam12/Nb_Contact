package com.example.contact_nb12.mypage

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.contact_nb12.R

class AddContactDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("연락처 추가")
                .setMessage("여기에 연락처 추가 폼을 정의하세요.")
                .setPositiveButton("확인") { dialog, _ ->
                    // "확인" 버튼을 눌렀을 때의 동작을 여기에 추가
                    // 예를 들어, 입력된 데이터를 가져와서 연락처를 추가하는 작업을 수행합니다.
                    dialog.dismiss() // 다이얼로그 닫기
                }
                .setNegativeButton("취소") { dialog, _ ->
                    // "취소" 버튼을 눌렀을 때의 동작을 여기에 추가
                    dialog.dismiss() // 다이얼로그 닫기
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}






