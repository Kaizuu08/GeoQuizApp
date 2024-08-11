package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

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
    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0 // Retrieve the saved index or default to 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value) // Save the current index

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

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
