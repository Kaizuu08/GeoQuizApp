package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Question data class
    data class Question(val textResId: Int, val answer: Boolean)

    // List of questions
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for buttons
        binding.trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        // Set click listener for the next button
        val nextQuestionListener = View.OnClickListener {
            // Move to the next question
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        // Set click listener for the next button
        val previousQuestionListener = View.OnClickListener {
            // Move to the previous question
            currentIndex = if (currentIndex - 1 < 0) {
                questionBank.size - 1
            } else {
                (currentIndex - 1) % questionBank.size
            }
            updateQuestion()
        }

        // Use the same listener for the nextButton and the questionTextView
        binding.nextButton.setOnClickListener(nextQuestionListener)
        binding.previousButton.setOnClickListener(previousQuestionListener)
        binding.questionTextView.setOnClickListener(nextQuestionListener)

        // Update the question at the start
        updateQuestion()

        // Adjust padding based on window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Method to update the question text
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}
