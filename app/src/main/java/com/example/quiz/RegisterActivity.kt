package com.example.quiz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.quiz.PasswordEncryption.hashPassword
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val fullNameTextView: EditText = findViewById(R.id.full_name_view)
        val emailTextView: EditText = findViewById(R.id.email_view)
        val passwordTextView: EditText = findViewById(R.id.password_view)
        val regBtn: Button = findViewById(R.id.reg_btn)
        val errorMessage: TextView = findViewById(R.id.error)
        val loginTv: TextView = findViewById(R.id.login_tv)

        regBtn.setOnClickListener {
            val fullName = fullNameTextView.text.toString().trim()
            val email = emailTextView.text.toString().trim()
            val password = passwordTextView.text.toString().trim()

            val encryptedPassword = hashPassword(password)

            val user = User(fullName, email, encryptedPassword)

            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()){
                storeUserToDatabase(user)
            }else{
                ShowErrorMessage.showErrorMessage(
                    errorMessage,
                    getString(R.string.empty_field)
                )
            }
        }

        loginTv.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun storeUserToDatabase(user: User) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("User")

        usersCollection.add(user)
            .addOnSuccessListener {
                // Store data locally
                val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("Full Name", user.fullName)
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("fullName", user.fullName)
                startActivity(intent)
                finish()
                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                Log.e("FireStore", "Error adding document", exception)
            }
    }
}