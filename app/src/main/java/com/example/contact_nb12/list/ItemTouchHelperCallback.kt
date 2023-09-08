package com.example.contact_nb12.list

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.contact_nb12.R
import com.example.contact_nb12.models.Contact

class ItemTouchHelperCallback(
    private val itemMoveListener: OnItemMoveListener
) : ItemTouchHelper.Callback() {

    interface OnItemMoveListener {
        fun onItemMoved(fromPosition: Int, toPosition: Int)
        fun onItemSwiped(position: Int)
        fun addItem(contact: Contact)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        // 오른쪽 방향으로 스와이프만 허용
        val dragFlags = 0
        val swipeFlags = ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // 아이템 이동 기능은 비활성화
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // 오른쪽 방향으로 스와이프 시에만 동작
        if (direction == ItemTouchHelper.END) {
            itemMoveListener.onItemSwiped(viewHolder.adapterPosition)
        }
    }

    override fun isLongPressDragEnabled(): Boolean {
        // 아이템을 롱 프레스하여 드래그하는 기능 비활성화
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val context = recyclerView.context

        // 배경 색상을 초록색으로 설정
        val background = RectF(
            itemView.right + dX,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat()
        )

        val paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.primary)
        c.drawRect(background, paint)

        // 스와이프 버튼의 배경 색상을 초록색으로 설정
        val swipeButton = RectF(
            itemView.left.toFloat(),
            itemView.top.toFloat(),
            itemView.left + dX,
            itemView.bottom.toFloat()
        )

        val buttonPaint = Paint()
        buttonPaint.color = ContextCompat.getColor(context, R.color.primary)
        c.drawRect(swipeButton, buttonPaint)

        // "Call" 텍스트를 그림
        val textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 40f
        c.drawText(
            "Call",
            swipeButton.centerX() - textPaint.measureText("Call") / 2,
            swipeButton.centerY() + textPaint.textSize / 2,
            textPaint
        )

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}