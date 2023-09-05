package com.example.contact_nb12.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.FragmentContactListBinding
import com.example.contact_nb12.models.Contact

class ContactListFragment : Fragment() {
    lateinit var binding: FragmentContactListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentContactListBinding.inflate(inflater)

        // DataManager에서 데이터 가져오기
        val contacts = DataManager.getContacts()

        // RecyclerView에 어댑터 설정
        val adapter = ContactAdapter(contacts as MutableList<Contact>)
        binding.contactRecycler.adapter = adapter

        // RecyclerView 레이아웃 매니저 설정
        val layoutManager = LinearLayoutManager(requireContext())
        binding.contactRecycler.layoutManager = layoutManager

        return binding.root
    }
}

