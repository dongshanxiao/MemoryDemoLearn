package com.learn.demo.memory.utils

import com.learn.demo.memory.models.BoardSize

class MemoryGame(private val boardSize: BoardSize) {

    val memoryCards: List<MemoryCard>
    val numPairsFound = 0

    init {
        val choosenNumPairs = DEFAULT_ICON.shuffled().take(boardSize.getNumPairs())
        val randomNumPairs = (choosenNumPairs + choosenNumPairs).shuffled()
        memoryCards = randomNumPairs.map { MemoryCard(it) }
    }

    fun flip(position: Int) {
        var memoryCard: MemoryCard = memoryCards[position]
        memoryCard.isFaceUp = !memoryCard.isFaceUp
    }
}