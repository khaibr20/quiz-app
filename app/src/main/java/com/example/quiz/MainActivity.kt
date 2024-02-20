package com.example.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiz.model.Question
import com.example.quiz.network.fetchQuestionsForCategory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menuView: RelativeLayout = findViewById(R.id.menu_view)

        // Get Local data
        val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)

        val userInfo = intent.getStringExtra("fullName")

        val fullName = sharedPreferences.getString("Full Name", userInfo)

        if (!fullName.isNullOrBlank()) {
            initializeRecyclerView(fullName)
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Set up a click listener for the menu view
        menuView.setOnClickListener {
            showPopupMenu(menuView)
        }
    }

    private fun initializeRecyclerView(fullName: String) {
        val categories = mutableListOf(
            CategoryModel(R.drawable.spo_icon, "Sport", 10),
            CategoryModel(R.drawable.geo_icon, "Geography", 10),
            CategoryModel(R.drawable.math_icon, "Mathematics", 10),
            CategoryModel(R.drawable.com_icon, "Computer", 10),
            CategoryModel(R.drawable.his_icon, "History", 10),
            CategoryModel(R.drawable.art_icon, "Art", 10),
            CategoryModel(R.drawable.pol_icon, "Politics", 10),
            CategoryModel(R.drawable.ani_icon, "Animal", 10),
            CategoryModel(R.drawable.veh_icon, "Vehicle", 10),
            CategoryModel(R.drawable.sci_icon, "Science & Nature", 10)
        )

        val customAdapter = CategoryAdaptor(categories)

        val greetingText: TextView = findViewById(R.id.greeting_text)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        "Hi there, $fullName".also { greetingText.text = it }

        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        customAdapter.onItemClick = {
            goToEachQuestion(it)
        }
    }

    private fun goToEachQuestion(it: CategoryModel) {
        when (it.categoryName){
            "Science & Nature" -> generateQuiz(17)
            "Computer" -> generateQuiz(18)
            "Mathematics" -> generateQuiz(19)
            "Sport" -> generateQuiz(21)
            "Geography" -> generateQuiz(22)
            "History" -> generateQuiz(23)
            "Politics" -> generateQuiz(24)
            "Art" -> generateQuiz(25)
            "Animal" -> generateQuiz(27)
            "Vehicle" -> generateQuiz(28)
        }
    }

    private fun generateQuiz(categoryNumber: Int){
        fetchQuestionsForCategory(categoryNumber) { questionsResponse ->
            val questionsList = questionsResponse?.results ?: emptyList()
            val questionObjects = questionsList.map { result ->
                Question(
                    result.category,
                    result.correct_answer,
                    result.difficulty,
                    result.incorrect_answers,
                    result.question,
                    result.type
                )
            }
            val intent = Intent(this, QuizPage::class.java)
            intent.putParcelableArrayListExtra("Questions", ArrayList(questionObjects))
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return true
    }

    private fun showPopupMenu(anchorView: View) {
        val popup = PopupMenu(this, anchorView)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.logout, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.clear().apply()

                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }
                // Handle other menu items here if needed
                else -> false
            }
        }

        popup.show()
    }
}