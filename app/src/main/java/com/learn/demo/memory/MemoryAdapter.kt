package com.learn.demo.memory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.learn.demo.memory.models.BoardSize
import com.learn.demo.memory.utils.MemoryCard

class MemoryAdapter(
    private val context: Context, private val boardSize: BoardSize, private val memoryCards:
    List<MemoryCard>, private val onClickedPosition: OnClickedPosition

) : RecyclerView.Adapter<MemoryAdapter
.ViewHolder>() {

    companion object {
        private val MARGIN_SIZE = 10
        private val TAG = "MemoryAdapter"
    }

    interface OnClickedPosition {
        fun onClickedPosition(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardWidth = parent.width / boardSize.getWidth() - 2 * MARGIN_SIZE
        val cardHeight = parent.height / boardSize.getHeight() - 2 * MARGIN_SIZE
        val cardSize = Math.min(cardWidth, cardHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        val layoutParams: ViewGroup.MarginLayoutParams = view.findViewById<CardView>(R.id.cardView).layoutParams as
                ViewGroup.MarginLayoutParams
        layoutParams.width = cardSize
        layoutParams.height = cardSize
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = boardSize.numCards

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)


        fun bind(position: Int) {
            var memoryCard: MemoryCard = memoryCards[position]
            imageButton.setImageResource(
                if (memoryCard.isFaceUp) memoryCard.identifier else R.drawable
                    .ic_launcher_background
            )
            imageButton.setOnClickListener {
                Log.d(TAG, "imageButton clicked $position")
                onClickedPosition.onClickedPosition(position)
            }
        }
    }

}
