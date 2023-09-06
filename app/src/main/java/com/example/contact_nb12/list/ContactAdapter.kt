package com.example.contact_nb12.list

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.contact_nb12.R
import com.example.contact_nb12.databinding.ItemViewtypeBookmarkBinding
import com.example.contact_nb12.databinding.ItemViewtypeNormalBinding
import com.example.contact_nb12.models.Contact
import java.util.Collections

class ContactAdapter(private val Items: MutableList<Contact>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ItemTouchHelperCallback.OnItemMoveListener {

    private lateinit var dragListener: OnStartDragListener
    private lateinit var context: Context

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
        context = parent.context // Context 얻기
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { view ->
            itemClick?.onClick(view, position)
        }

        val contact = Items[position]
        when (holder) {
            is ContactBookmarkViewHolder -> {
                holder.bind(contact)
                holder.binding.BookmarkBtn.setOnClickListener {
                    // BookmarkBtn을 눌렀을 때 Contact 객체의 isMark 토글
                    contact.isMark = !contact.isMark
                    notifyItemChanged(position)
                }
                holder.itemView.setOnTouchListener { view, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(holder)
                    }
                    return@setOnTouchListener false
                }
            }
            is ContactNormalViewHolder -> {
                holder.bind(contact)
                holder.binding.BookmarkBtn.setOnClickListener {
                    // BookmarkBtn을 눌렀을 때 Contact 객체의 isMark 토글
                    contact.isMark = !contact.isMark
                    notifyItemChanged(position)
                }
                holder.itemView.setOnTouchListener { view, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(holder)
                    }
                    return@setOnTouchListener false
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return Items.size
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    fun startDrag(listener: OnStartDragListener) {
        this.dragListener = listener
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(Items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwiped(position: Int) {
        // 스와이프한 아이템의 전화 번호 가져오기
        val phoneNumber = Items[position].phonenumber
        // 전화 걸기 인텐트 생성
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        // 전화 걸기 인텐트 실행
        context.startActivity(callIntent)
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

    inner class ContactNormalViewHolder(val binding: ItemViewtypeNormalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact) {
            binding.listImg.setImageResource(contact.Img)
            binding.listName.text = contact.name
            binding.listNumber.text = contact.phonenumber
            binding.BookmarkBtn.setImageResource(R.drawable.star)
        }
    }
}
