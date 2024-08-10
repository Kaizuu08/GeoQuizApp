package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // ViewBinding for accessing UI elements

    // Initialize ViewModel using the viewModels delegate
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel") // Log the ViewModel instance

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for True/False buttons
        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        // Set click listener for the next button
        val nextQuestionListener = View.OnClickListener {
            quizViewModel.moveToNext() // Move to the next question in the ViewModel
            updateQuestion() // Update the UI to show the new question
        }

        // Use the same listener for the nextButton and the questionTextView
        binding.nextButton.setOnClickListener(nextQuestionListener)
        binding.questionTextView.setOnClickListener(nextQuestionListener)

        // Update the question on the screen when the activity starts
        updateQuestion()

        // Adjust padding for edge-to-edge display (optional, for modern UI)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Method to update the question text on the screen
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText // Get the current question's text ID from the ViewModel
        binding.questionTextView.setText(questionTextResId) // Set the question text in the UI
    }

    // Method to check if the user's answer is correct
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer // Get the correct answer from the ViewModel

        // Choose the correct or incorrect message based on the user's answer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast // If the answer is correct
        } else {
            R.string.incorrect_toast // If the answer is incorrect
        }

        // Show a Toast message with the result
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}
