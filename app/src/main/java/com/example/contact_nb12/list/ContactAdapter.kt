package com.example.contact_nb12.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.ItemViewtypeBookmarkBinding
import com.example.contact_nb12.databinding.ItemViewtypeNormalBinding
import com.example.contact_nb12.models.Contact

class ContactAdapter(val Items: MutableList<Contact>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_BOOKMARK = 0
        private const val VIEW_TYPE_NORMAL = 1
    }

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_BOOKMARK -> {
                val binding = ItemViewtypeBookmarkBinding.inflate(inflater, parent, false)
                ContactBookmarkViewHolder(binding)
            }
            else -> {
                val binding = ItemViewtypeNormalBinding.inflate(inflater, parent, false)
                ContactNormalViewHolder(binding)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {  //클릭이벤트추가부분
            itemClick?.onClick(it, position)
        }

            val contact = Items[position]
            when (holder) {
                is ContactBookmarkViewHolder -> {
                    holder.bind(contact)
                    holder.itemView.setOnClickListener {
                        // 아이템 클릭 이벤트 처리
                        contact.isMark = !contact.isMark
                        notifyItemChanged(position)
                    }
                }
                is ContactNormalViewHolder -> {
                    holder.bind(contact)
                    holder.itemView.setOnClickListener {
                        // 아이템 클릭 이벤트 처리
                        contact.isMark = !contact.isMark
                        notifyItemChanged(position)
                    }
                }
            }
        }

    override fun getItemCount(): Int {
        return Items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (Items[position].isMark) VIEW_TYPE_BOOKMARK else VIEW_TYPE_NORMAL
    }
    inner class ContactBookmarkViewHolder(val binding: ItemViewtypeBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.listImg.setImageResource(contact.Img)
            binding.listName.text = contact.name
            binding.listNumber.text = contact.phonenumber
            binding.BookmarkBtn.setImageResource(R.drawable.full_star)

        }
    }

    inner class ContactNormalViewHolder(private val binding: ItemViewtypeNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.listImg.setImageResource(contact.Img)
            binding.listName.text = contact.name
            binding.listNumber.text = contact.phonenumber
            binding.BookmarkBtn.setImageResource(R.drawable.star)
        }
    }

}