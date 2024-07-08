package com.code.bongpaldev.yahtzeee

class DiceManager {
    private val dices = List(5) { Dice() }

    fun rollDice() {
            dices.forEach { if (it.isActive()) it.roll() }
    }

    fun holdDice(diceIndex: Int) {
        dices[diceIndex].hold()
    }

    fun currentDices() = dices.map { it.copy() }

    fun resetDices() {
        dices.forEach { it.reset() }
    }
}

