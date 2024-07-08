package com.code.bongpaldev.yahtzeee.ui.game

import com.code.bongpaldev.yahtzeee.Dice
import com.code.bongpaldev.yahtzeee.Score
import com.code.bongpaldev.yahtzeee.model.ScoreType.*

class ScoreManager {
    fun checkScore(dices: List<Dice>?, scores: List<Score>?): List<Score> {
        if (dices == null || scores == null) throw IllegalArgumentException("dices or scores is null")

        return scores.map { rule ->
            if (rule.isPick.not()) {
                rule.copy(score = rule.calculateScore(dices), isSelected = false)
            } else {
                rule.copy(isSelected = false)
            }
        }
    }

    fun selectScore(scores: List<Score>?, index: Int?) = scores?.mapIndexed { i, score ->
        if (i == index) score.copy(isSelected = true) else score.copy(isSelected = false)
    } ?: throw IllegalArgumentException("scores is null")

    fun pickScore(scores: List<Score>?, index: Int?) = scores?.mapIndexed { i, s ->
        if (i == index) {
            s.copy(score = s.score, isSelected = false, isPick = true)
        } else {
            s.copy(
                score = if (s.isPick) s.score else 0,
                isSelected = false
            )
        }
    } ?: throw IllegalArgumentException("scores is null")

    private fun Score.calculateScore(currentDice: List<Dice>): Int {
        val numbers = currentDice.map { it.value }
        val group = numbers.groupingBy { it }.eachCount()
        val straightCount = countStraight(numbers)

        return when (this.scoreType) {
            //upper section
            Aces -> 1 * numbers.count { it == 1 }
            Twos -> 2 * numbers.count { it == 2 }
            Threes -> 3 * numbers.count { it == 3 }
            Fours -> 4 * numbers.count { it == 4 }
            Fives -> 5 * numbers.count { it == 5 }
            Sixes -> 6 * numbers.count { it == 6 }
            //lower section
            ThreeOfAKind -> if (group.any { it.value >= 3 }) numbers.sum() else 0
            FourOfAKind -> if (group.any { it.value >= 4 }) numbers.sum() else 0
            FullHouse -> if (group.any { it.value == 3 } && group.any { it.value == 2 }) 25 else 0
            SmallStraight -> if (straightCount >= 4) 30 else 0
            LargeStraight -> if (straightCount >= 5) 40 else 0
            Yahtzee -> if (group.any { it.value == 5 }) 50 else 0
            Chance -> numbers.sum()
        }
    }

    private fun countStraight(numbers: List<Int>): Int {
        val numberSet = numbers.toSortedSet()
        var prevNum = 0
        var count = 0
        var temp = 0

        for (i in numberSet) {
            if (i - prevNum == 1) temp++ else temp = 1

            count = maxOf(count, temp)
            prevNum = i
        }
        return count
    }
}