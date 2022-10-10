package com.learn.demo.memory.utils

import android.service.carrier.CarrierIdentifier

data class MemoryCard(
    val identifier: Int,
    var isFaceUp: Boolean = false,
    var isMatch: Boolean = false,
)