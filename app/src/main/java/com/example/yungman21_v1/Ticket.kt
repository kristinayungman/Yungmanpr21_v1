package com.example.yungman21_v1

import kotlin.random.Random
class Ticket (
    var number: Int,
    val question: String,
    val correctAnswer: Boolean,   // true = "Да", false = "Нет"
    var userAnswer: Boolean? = null,  // null = не отвечал
) {
    val passed: Boolean
        get() = userAnswer != null

    val isCorrect: Boolean?
        get() = if (userAnswer != null) userAnswer == correctAnswer else null
}