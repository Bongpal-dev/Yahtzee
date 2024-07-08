package com.code.bongpaldev.yahtzeee

data class Score(
    var scoreType: ScoreType,
    var score: Int = 0,
    var selected: Boolean = false,
    var pick: Boolean = false
) {
    fun updateScore(score: Int) {
        this.score = score
    }

    fun getScorePoint() = this.score

    fun selectScore() {
        this.selected = !this.selected
    }

    fun resetScore() {
        this.score = 0
        this.selected = false
    }

    fun pickScore() {
        this.pick = true
    }

    fun isSelect(): Boolean {
        return this.selected
    }

    fun isPick(): Boolean {
        return this.pick
    }
}
