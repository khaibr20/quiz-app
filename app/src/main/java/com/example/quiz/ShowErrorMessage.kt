package com.example.quiz

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView

object ShowErrorMessage{
    fun showErrorMessage(errorMessage: TextView, message: String) {
        if (message == errorMessage.context.getString(R.string.auth_error_massage)) {
            errorMessage.visibility = View.VISIBLE
        } else {
            errorMessage.text = errorMessage.context.getString(R.string.empty_field)
            errorMessage.visibility = View.VISIBLE
        }

        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            errorMessage.visibility = View.GONE
        }

        handler.postDelayed(runnable, 3000)
    }
}