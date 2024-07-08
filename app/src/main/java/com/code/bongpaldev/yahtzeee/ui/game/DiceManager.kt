package com.code.bongpaldev.yahtzeee.ui.game

import com.code.bongpaldev.yahtzeee.Dice

class DiceManager {
    fun rollDice(dices: List<Dice>?) = dices?.map {
        if (it.isActive) it.copy(value = (1..6).random()) else it
    } ?: throw IllegalArgumentException("Dice list cannot be null")


    fun holdDice(dices: List<Dice>?, index: Int): List<Dice> {
        return dices?.mapIndexed { i, dice ->
            if (i == index) dice.copy(value = dice.value, isActive = dice.isActive.not()) else dice
        } ?: throw IllegalArgumentException("Dice list cannot be null")
    }

    fun resetDice(dices: List<Dice>?) = dices?.map {
        it.copy(value = 1, isActive = true)
    } ?: throw IllegalArgumentException("Dice list cannot be null")
}