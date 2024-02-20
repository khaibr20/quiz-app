package com.example.quiz

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spanned
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.example.quiz.model.Question

class QuizPage : AppCompatActivity() {
    private lateinit var questions: ArrayList<Question>
    private var currentQuestionIndex: Int = 0
    private lateinit var radioButtons: List<RadioButton>
    private var userAnswerCorrect: Int = 0
    private var currentProgress: Int = 0
    private var currentQuizNumber: Int = 0

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_page)

        val closeBtn: ImageButton = findViewById(R.id.close_btn)
        val nextBtn: Button = findViewById(R.id.next_btn)
        val checkedRadioGroup: RadioGroup = findViewById(R.id.radio_group)

        // Progress bar
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val quizNumber: TextView = findViewById(R.id.quiz_number)

        radioButtons = listOf(
            findViewById(R.id.r_1),
            findViewById(R.id.r_2),
            findViewById(R.id.r_3),
            findViewById(R.id.r_4)
        )

        questions = intent.getParcelableArrayListExtra("Questions") ?: ArrayList()

        // update the questions
        updateQuestion(currentQuestionIndex, nextBtn)

        // disable the button
        nextBtn.isEnabled = false

        checkedRadioGroup.setOnCheckedChangeListener {group, id ->
            changeAppearance(group, id)
            // enable the button if the user clicks one of the radio groups
           nextBtn.isEnabled = checkedRadioGroup.checkedRadioButtonId != -1
        }

        nextBtn.setOnClickListener {
            // check if the button is enabled
            if(nextBtn.isEnabled){
                currentQuestionIndex++
                if (currentQuestionIndex < questions.size) {
                    updateQuestion(currentQuestionIndex, nextBtn)

                    // increase the the number of questions each time the question updates
                    currentQuizNumber++
                    // add 10 to the progress bar when the button is pressed
                    currentProgress += 10
                    progressBar.progress = currentProgress
                    progressBar.max = 100
                    quizNumber.text = "$currentQuizNumber/10"

                } else {
                    // Handle quiz completion
                    val intent = Intent(this, ActivityResult::class.java)
                    intent.putExtra("userAnswerCorrect", userAnswerCorrect)
                    startActivity(intent)
                    finish()
                }
            }
        }

        closeBtn.setOnClickListener{finish()}
    }

    private fun updateQuestion(index: Int, nextBtn: Button) {
        val question = questions[index]
        val quizCard: TextView = findViewById(R.id.quiz_card)

        val decodedQuestion: Spanned = HtmlCompat.fromHtml(question.question.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)

        quizCard.text = decodedQuestion.toString()

        nextBtn.isEnabled = false

        val choices = mutableListOf<String>()
        choices.add(question.correct_answer!!)
        choices.addAll(question.incorrect_answers!!)
        choices.shuffle()

        for (i in choices.indices) {
            radioButtons[i].text = choices[i]
        }

        for (radioButton in radioButtons) {
            radioButton.setBackgroundResource(R.drawable.question_card)
        }

        // Set the click listener for each RadioButton
        for (i in radioButtons.indices) {
            val radioButton = radioButtons[i]
            radioButton.setOnClickListener {
                if (radioButton.text == question.correct_answer) {
                    userAnswerCorrect++
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun changeAppearance(group: RadioGroup?, checkedId: Int) {
        val radioButton = findViewById<RadioButton>(checkedId)

        group?.apply {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (child is RadioButton && child.id != checkedId) {
                    // Deselect other radio buttons
                    child.isChecked = false
                    child.setBackgroundResource(R.drawable.question_card)
                }
            }
        }

        // Update the background and appearance of the selected radio button
        if (radioButton.isChecked){
            radioButton.setBackgroundResource(R.drawable.question_card)
            radioButton.setBackgroundColor(R.color.neutral_color)
        }
        else radioButton.setBackgroundResource(R.drawable.color_stroke)
    }
}