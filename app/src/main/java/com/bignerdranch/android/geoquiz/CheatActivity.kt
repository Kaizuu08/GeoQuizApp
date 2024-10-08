package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bignerdranch.android.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
private const val KEY_IS_ANSWER_SHOWN = "is_answer_shown"

private var answerIsTrue = false

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding
    private var isAnswerShown: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the layout using View Binding
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        // Restore the saved state (if any)
        if (savedInstanceState != null) {
            isAnswerShown = savedInstanceState.getBoolean(KEY_IS_ANSWER_SHOWN, false)
            if (isAnswerShown) {
                setAnswerShownResult(true)
                val answerText = if (answerIsTrue) {
                    R.string.true_button
                } else {
                    R.string.false_button
                }
                binding.answerTextView.setText(answerText)
            }
        }

        binding.showAnswerButton.setOnClickListener {
            val answerText = if (answerIsTrue) {
                R.string.true_button
            } else {
                R.string.false_button
            }
            binding.answerTextView.setText(answerText)
            setAnswerShownResult(true)
            isAnswerShown = true
        }

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_ANSWER_SHOWN, isAnswerShown)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}
