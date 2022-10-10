package com.learn.demo.memory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learn.demo.memory.models.BoardSize
import com.learn.demo.memory.utils.DEFAULT_ICON
import com.learn.demo.memory.utils.MemoryCard
import com.learn.demo.memory.utils.MemoryGame

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var moveNumbers: TextView
    private lateinit var pairedNumbers: TextView

    private val boardSize: BoardSize = BoardSize.MEDIUM

    private lateinit var memoryGame : MemoryGame
    private lateinit var adapter: MemoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.game_content)
        moveNumbers = findViewById(R.id.moveNumbers)
        pairedNumbers = findViewById(R.id.pairedNumbers)

        memoryGame = MemoryGame(boardSize)
        adapter = MemoryAdapter(this, boardSize, memoryGame.memoryCards, object : MemoryAdapter
        .OnClickedPosition {
            override fun onClickedPosition(position: Int) {
                Log.d(TAG, "onClickedPosition $position")
                updateMemoryCardsFlip(position)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, boardSize.getWidth())
    }

    private fun updateMemoryCardsFlip(position: Int) {
        memoryGame.flip(position)
        adapter.notifyDataSetChanged()
    }
}