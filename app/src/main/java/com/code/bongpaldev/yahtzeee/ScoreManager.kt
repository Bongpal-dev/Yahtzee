package com.code.bongpaldev.yahtzeee

import android.util.Log
import com.code.bongpaldev.yahtzeee.ScoreType.*
import kotlin.math.log

class ScoreManager {
    private val scores = listOf(
        Score(Aces),
        Score(Twos),
        Score(Threes),
        Score(Fours),
        Score(Fives),
        Score(Sixes),
        Score(ThreeOfAKind),
        Score(FourOfAKind),
        Score(FullHouse),
        Score(SmallStraight),
        Score(LargeStraight),
        Score(Yahtzee),
        Score(Chance)
    )
    private var prev: ScoreType? = null

    fun checkScore(dices: List<Dice>) {
        scores.forEach { rule ->
            if (rule.isPick().not()) {
                rule.getScorePoint()
                rule.updateScore(rule.calculateScore(dices))
            }
        }
    }

    fun getCurrentScores() = scores.map { it.copy() }

    fun selectScore(type: ScoreType) {
        scores.forEach {
            if (it.scoreType == type) it.selectScore()

            if (it.scoreType == prev) it.selectScore()
        }
        prev = type
        logScore()
    }

    fun resetSelect() {
        scores.filterNot { it.isPick() }.forEach {rule ->
            rule.resetScore()
        }
        prev = null
        logScore()
    }

    fun pickScore() {
        scores.find { it.scoreType == prev }?.pickScore()
        logScore()
    }

    private fun logScore(){
        scores.forEach {
            Log.d(TAG, "${it.scoreType}: ${it.isPick()}: ${it.score}")
        }
    }
    private fun Score.calculateScore(currentDice: List<Dice>): Int {
        val numbers = currentDice.map { it.getValue() }
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

    fun getTotalScore() = scores.sumOf { it.score }

    fun addScore(score: Int) {

    }
//        val upperScore = upper.fold(0) { acc, rule -> acc + rule.score }
//        val bonus = if (upperScore >= 63) 35 else 0
//        totalScore += score + bonus
//        totalScore += lower.fold(0) { acc, rule -> acc + rule.score }
//    }

//    fun selectScore(rule: ScoreType) {
//        score.find { it == rule }?.let {
//            it.complete = true
//            it.score = checkScore(rule)
//        }
//        score.forEach {
//            Log.d(TAG, "${it.name}: ${it.complete}, ${it.score}")
//        }
//    }

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