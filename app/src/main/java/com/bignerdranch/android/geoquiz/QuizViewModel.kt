package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    // List of questions in the quiz
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    // Index to track the current question
    private var currentIndex = 0

    // Computed property to get the answer of the current question
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    // Computed property to get the text resource ID of the current question
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResID

    // Function to move to the next question
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    // Function to move to the previous question (if needed)
    fun moveToPrevious() {
        currentIndex = if (currentIndex - 1 < 0) {
            questionBank.size - 1
        } else {
            currentIndex - 1
        }
    }

    // Log when the ViewModel instance is created
    init {
        Log.d(TAG, "ViewModel instance created")
    }

    // Log just before the ViewModel instance is destroyed
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}
