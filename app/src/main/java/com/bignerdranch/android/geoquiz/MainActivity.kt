package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // ViewBinding for accessing UI elements

    // Initialize ViewModel using the viewModels delegate
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel") // Log the ViewModel instance

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for True/False buttons
        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }

        binding.cheatButton.setOnClickListener {
            // Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        // Set click listener for the next button
        val nextQuestionListener = View.OnClickListener {
            quizViewModel.moveToNext() // Move to the next question in the ViewModel
            updateQuestion() // Update the UI to show the new question
        }

        // Set click listener for the previous button
        val previousQuestionListener = View.OnClickListener {
            quizViewModel.moveToPrevious() // Move to the previous question in the ViewModel
            updateQuestion() // Update the UI to show the previous question
        }

        // Attach listeners to the buttons and text view
        binding.nextButton.setOnClickListener(nextQuestionListener)
        binding.previousButton.setOnClickListener(previousQuestionListener)
        binding.questionTextView.setOnClickListener(nextQuestionListener)

        // Update the question on the screen when the activity starts
        updateQuestion()

        // Makes the cheat button blurred, however requires newer APK. Annotation inserted.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }

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

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            10.0f,
            10.0f,
            Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)

    }
    // Method to check if the user's answer is correct
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer // Get the correct answer from the ViewModel

        // Choose the correct or incorrect message based on the user's answer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        // Show a Toast message with the result
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}
