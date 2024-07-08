package com.code.bongpaldev.yahtzeee

data class Dice(
    private var value: Int = 1,
    private var isActive: Boolean = true
) {
    fun roll() {
        value = (1..6).random()
    }

    fun hold() {
        isActive = !isActive
    }

    fun reset() {
        value = 1
        isActive = true
    }

    fun getValue() = value

    fun isActive() = isActive
}
