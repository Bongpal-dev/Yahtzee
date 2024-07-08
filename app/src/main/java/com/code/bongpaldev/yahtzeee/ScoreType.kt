package com.code.bongpaldev.yahtzeee

const val UPPER = 1
const val LOWER = 2

enum class ScoreType(val section: Int) {
    Aces(UPPER),
    Twos(UPPER),
    Threes(UPPER),
    Fours(UPPER),
    Fives(UPPER),
    Sixes(UPPER),
    ThreeOfAKind(LOWER),
    FourOfAKind(LOWER),
    FullHouse(LOWER),
    SmallStraight(LOWER),
    LargeStraight(LOWER),
    Chance(LOWER),
    Yahtzee(LOWER)
}