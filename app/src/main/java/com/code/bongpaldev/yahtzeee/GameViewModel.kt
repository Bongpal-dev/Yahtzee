package com.code.bongpaldev.yahtzeee

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private val diceManager = DiceManager()
    private val scoreManager = ScoreManager()

    private val _isNewTurn = MutableLiveData(true)
    private var _totalScore = MutableLiveData(0)
    private var _currentDices = MutableLiveData<List<Dice>>()
    private var _canRoll = MutableLiveData(true)
    private var _scores = MutableLiveData<List<Score>>(scoreManager.getCurrentScores())
    private var _selectedAny = MutableLiveData(false)

    val isNewTurn: LiveData<Boolean> get() = _isNewTurn
    val totalScore: LiveData<Int> get() = _totalScore
    val currentDices: LiveData<List<Dice>> get() = _currentDices
    val canRoll: LiveData<Boolean> get() = _canRoll
    val scores: LiveData<List<Score>> get() = _scores
    val selectedAny: LiveData<Boolean> get() = _selectedAny

    private var rollCount = 0

    fun rollDices() {
        _isNewTurn.value = false
        diceManager.rollDice()
        rollCount++
        updateDices()

        scoreManager.resetSelect()
        _selectedAny.value = false
        updateScores()


        if (rollCount == 3) _canRoll.value = false
    }

    fun holdDice(index: Int) {
        diceManager.holdDice(index)
        updateDices()
    }

    fun checkScore() {
        scoreManager.checkScore(currentDices.value!!)
        updateScores()
    }

    fun selectScore(type: ScoreType) {
        scoreManager.selectScore(type)
        _selectedAny.value = true
        updateScores()
    }

    fun pickScore() {
        _isNewTurn.value = true
        rollCount = 0
        _canRoll.value = true
        scoreManager.pickScore()
        scoreManager.resetSelect()
        diceManager.resetDices()
        updateDices()
        _selectedAny.value = false
        updateScores()
        _totalScore.value = scoreManager.getTotalScore()
    }

    private fun updateScores() {
        _scores.value = scoreManager.getCurrentScores()
    }

    private fun updateDices() {
        _currentDices.value = diceManager.currentDices()
    }

}