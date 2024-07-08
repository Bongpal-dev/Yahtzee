package com.code.bongpaldev.yahtzeee

import com.code.bongpaldev.yahtzeee.model.ScoreType

data class Score(
    var scoreType: ScoreType,
    var score: Int = 0,
    var isSelected: Boolean = false,
    var isPick: Boolean = false
)