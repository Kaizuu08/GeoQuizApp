package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {

    // Declare Button variables to hold references to the True and False buttons
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    // Override the onCreate method to initialize the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display to make the app take advantage of the entire screen area
        enableEdgeToEdge()

        // Set the layout for this activity
        setContentView(R.layout.activity_main)

        // Initialize the True and False buttons by finding them in the layout
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)

        // Set an onClick listener for the True button
        trueButton.setOnClickListener { view: View ->
            // Create and show a Snackbar with the message from the correct_toast string resource
            val snackbar = Snackbar.make(view, R.string.correct_toast, Snackbar.LENGTH_SHORT)
            // Customize the Snackbar
            customizeSnackbar(snackbar)
            snackbar.show() // Show the Snackbar
        }

        // Set an onClick listener for the False button
        falseButton.setOnClickListener { view: View ->
            // Create and show a Snackbar with the message from the incorrect_toast string resource
            val snackbar = Snackbar.make(view, R.string.incorrect_toast, Snackbar.LENGTH_SHORT)
            // Customize the Snackbar
            customizeSnackbar(snackbar)
            snackbar.show() // Show the Snackbar
        }

        // Set an onApplyWindowInsetsListener to adjust the padding of the main view based on system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Get the system bars insets (status bar, navigation bar, etc.)
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as padding to the view
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            // Return the insets to indicate they have been handled
            insets
        }
    }

    // Method to customize the Snackbar
    private fun customizeSnackbar(snackbar: Snackbar) {
        val snackbarView = snackbar.view
        val params = snackbarView.layoutParams as FrameLayout.LayoutParams

        // Set margins
        params.setMargins(50, 250, 50, 50) // Adjust margins as needed
        snackbarView.layoutParams = params

        // Increase height
        snackbarView.minimumHeight = 100 // Adjust height as needed

        // Set the Snackbar to appear at the top
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        snackbarView.layoutParams = params

    }
}
