package com.code.bongpaldev.yahtzeee.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.code.bongpaldev.yahtzeee.Dice
import com.code.bongpaldev.yahtzeee.Score
import com.code.bongpaldev.yahtzeee.model.ScoreType

class GameViewModel : ViewModel() {
    private val diceManager = DiceManager()
    private val scoreManager = ScoreManager()

    private var _dices = MutableLiveData(List(5) { Dice() })
    val dices: LiveData<List<Dice>> get() = _dices

    private var _scoreSheet = MutableLiveData(
        listOf(
            Score(ScoreType.Aces),
            Score(ScoreType.Twos),
            Score(ScoreType.Threes),
            Score(ScoreType.Fours),
            Score(ScoreType.Fives),
            Score(ScoreType.Sixes),
            Score(ScoreType.ThreeOfAKind),
            Score(ScoreType.FourOfAKind),
            Score(ScoreType.FullHouse),
            Score(ScoreType.SmallStraight),
            Score(ScoreType.LargeStraight),
            Score(ScoreType.Yahtzee),
            Score(ScoreType.Chance)
        )
    )
    val scoreSheet: LiveData<List<Score>> get() = _scoreSheet

    private var _currentSelected = MutableLiveData<Int?>(null)
    val currentSelected: LiveData<Int?> get() = _currentSelected

    private val _isNewTurn = MutableLiveData(true)
    private var _totalScore = MutableLiveData(0)

    private var _canRoll = MutableLiveData(true)
    val isNewTurn: LiveData<Boolean> get() = _isNewTurn
    val totalScore: LiveData<Int> get() = _totalScore
    val canRoll: LiveData<Boolean> get() = _canRoll

    private var _isGameOver = MutableLiveData(false)
    val isGameOver: LiveData<Boolean> get() = _isGameOver


    private var rollCount = 0

    fun rollDices() {
        _isNewTurn.value = false
        updateDice()
        updateScore()

        if (rollCount == 3) _canRoll.value = false
    }

    fun holdDice(index: Int) {
        _dices.value = diceManager.holdDice(dices.value, index)
    }

    private fun updateDice() {
        _dices.value = diceManager.rollDice(dices.value)
//        rollCount++
    }

    private fun updateScore() {
        _scoreSheet.value = scoreManager.checkScore(dices.value, scoreSheet.value)
    }


    fun selectScore(type: ScoreType) {
        val selectedScore = scoreSheet.value?.find { it.scoreType == type }

        _currentSelected.value = scoreSheet.value?.indexOf(selectedScore)
        _scoreSheet.value = scoreManager.selectScore(scoreSheet.value, currentSelected.value)
    }

    fun pickScore() {
        _scoreSheet.value = scoreManager.pickScore(scoreSheet.value, currentSelected.value)
        _totalScore.value = scoreSheet.value?.fold(0) { acc, score -> acc + score.score }
        resetState()
        checkEnd()
    }

    private fun checkEnd() {
        if(scoreSheet.value?.all { it.isPick } == true) {
            _isGameOver.value = true
        }
    }

    private fun resetState() {
        _isNewTurn.value = true
        _canRoll.value = true
        _currentSelected.value = null
        rollCount = 0
        _dices.value = diceManager.resetDice(dices.value)
    }
}