package com.learn.demo.memory.utils

import com.learn.demo.memory.models.BoardSize

class MemoryGame(private val boardSize: BoardSize) {

    val memoryCards: List<MemoryCard>
    var numPairsFound = 0
    private var selectedIndexPosition: Int? = null
    private var moves = 0

    init {
        val choosenNumPairs = DEFAULT_ICON.shuffled().take(boardSize.getNumPairs())
        val randomNumPairs = (choosenNumPairs + choosenNumPairs).shuffled()
        memoryCards = randomNumPairs.map { MemoryCard(it) }
    }

    fun flip(position: Int): Boolean {
        moves++
        var isMatched = false
        var memoryCard: MemoryCard = memoryCards[position]

        // 最多2张卡片已经被反面:
        // 0张卡片已经被翻面 -> 翻转新卡片
        // 2张卡片已经被翻面 -> 重置之前2张翻面的卡, 翻转新卡片
        // -- 合并: 0张 或 2张卡片已经被翻面 -> 重置之前2张翻面的卡, 翻转新卡片
        // -- 1张卡片已经被翻面 -> 翻转新卡片, 比较新卡片和已经翻面的卡片

        if (selectedIndexPosition == null) {
            resetCards()
            selectedIndexPosition = position
        } else {
            isMatched = compareNewCard(selectedIndexPosition!!, position)
            selectedIndexPosition = null
        }
        memoryCard.isFaceUp = !memoryCard.isFaceUp
        return isMatched
    }

    private fun compareNewCard(selectedIndexPosition: Int, position: Int): Boolean {
        if (memoryCards[selectedIndexPosition].identifier != memoryCards[position].identifier) {
            return false
        }
        memoryCards[selectedIndexPosition].isMatch = true
        memoryCards[position].isMatch = true
        numPairsFound++
        return true
    }

    private fun resetCards() {
        for (memoryCard in memoryCards) {
            if (!memoryCard.isMatch) {
                memoryCard.isFaceUp = false
            }
        }
    }

    fun hasWin(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isFaceUp(position: Int): Boolean {
        return memoryCards[position].isFaceUp
    }

    fun getMoves(): Int {
        return moves / 2
    }
}