package com.example.quiz

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class ActivityResult : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val congratsIcon: ImageView = findViewById(R.id.congrats_icon)
        val congrats: TextView = findViewById(R.id.congrats)
        val score: TextView = findViewById(R.id.score)
        val closeBtn: ImageButton = findViewById(R.id.close_btn)

        val userAnswers = intent.getIntExtra("userAnswerCorrect", 0)

        if (userAnswers >= 5) score.text = "$userAnswers/10"
        else{
            congratsIcon.setImageResource(R.drawable.fail_icon)
            congrats.text = getString(R.string.oops)
            score.text = "$userAnswers/10"
        }


        closeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}