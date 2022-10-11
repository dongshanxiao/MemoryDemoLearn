package com.learn.demo.memory

import android.animation.ArgbEvaluator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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


    private var boardSize: BoardSize = BoardSize.EASY

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainView = findViewById(R.id.mainView)
        recyclerView = findViewById(R.id.game_content)
        moveNumbers = findViewById(R.id.moveNumbers)
        pairedNumbers = findViewById(R.id.pairedNumbers)
        pairedNumbers.setTextColor(ContextCompat.getColor(this, R.color.color_none))
        setupGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reset_menu -> {
                if (memoryGame.hasWin()) {
                    setupGame()
                } else {
                    showAlertDialog("Are you sure to start a new Game?", null, object : View.OnClickListener {
                        override fun onClick(v: View?) {
                            setupGame()
                        }
                    })
                }
                return true
            }
            R.id.set_size -> {
                showSizeSetDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSizeSetDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.set_size_layout, null)
        val radioGroup: RadioGroup = view.findViewById(R.id.radio_group)
        when (boardSize) {
            BoardSize.EASY -> radioGroup.check(R.id.size_easy)
            BoardSize.MEDIUM -> radioGroup.check(R.id.size_medium)
            BoardSize.HARD -> radioGroup.check(R.id.size_hard)
        }
        showAlertDialog("chose new game mode", view, View.OnClickListener {
            boardSize = when (radioGroup.checkedRadioButtonId) {
                R.id.size_easy -> BoardSize.EASY
                R.id.size_hard -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setupGame()
        })
    }

    private fun setupGame() {
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

    private fun showAlertDialog(title: String, view: View?, optionItemOnClickListener: View.OnClickListener) {
        AlertDialog.Builder(this).setTitle(title)
            .setView(view)
            .setNegativeButton("cancel", null)
            .setPositiveButton("ok") { _, _ ->
                optionItemOnClickListener.onClick(null)
            }
            .show()

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