package com.learn.demo.memory

import android.animation.ArgbEvaluator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.learn.demo.memory.models.BoardSize
import com.learn.demo.memory.utils.MemoryGame

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    private lateinit var mainView: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var moveNumbers: TextView
    private lateinit var pairedNumbers: TextView


    private val boardSize: BoardSize = BoardSize.EASY

    private lateinit var memoryGame : MemoryGame
    private lateinit var adapter: MemoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainView = findViewById(R.id.mainView)
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
        if (memoryGame.hasWin()) {
            Snackbar.make(mainView, "have win", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (memoryGame.isFaceUp(position)) {
            Snackbar.make(mainView, "faced up", Snackbar.LENGTH_SHORT).show()
            return
        }
        if (memoryGame.flip(position)) {
            Log.d(TAG, "flip $position")
            var color = ArgbEvaluator().evaluate(
                memoryGame.numPairsFound.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.color_none),
                ContextCompat.getColor(this, R.color.color_full),
            ) as Int
            pairedNumbers.setTextColor(color)
            pairedNumbers.setText("Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}")
            if (memoryGame.hasWin()) {
                Snackbar.make(mainView, "have win", Snackbar.LENGTH_SHORT).show()
            }
        }
        moveNumbers.setText("Move: ${memoryGame.getMoves()}")
        adapter.notifyDataSetChanged()


    }
}