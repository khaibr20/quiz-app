package com.example.quiz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.quiz.PasswordEncryption.verifyPassword
import com.example.quiz.ShowErrorMessage.showErrorMessage
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailTextView: EditText = findViewById(R.id.email_view)
        val passwordTextView: EditText = findViewById(R.id.password_view)
        val loginBtn: Button = findViewById(R.id.login_btn)
        val errorMessage: TextView = findViewById(R.id.error)
        val regTv: TextView = findViewById(R.id.reg_tv)

        loginBtn.setOnClickListener {
            val email = emailTextView.text.toString().trim()
            val password = passwordTextView.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                userValidation(email, password, errorMessage)
            } else {
                showErrorMessage(errorMessage, getString(R.string.empty_field))
            }
        }

        regTv.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun userValidation(email: String, password: String, errorMessage: TextView) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("User")

        usersCollection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val fullName = document.getString("fullName")
                    val storedPassword = document.getString("password")

                    val decryptedPassword = verifyPassword(password, storedPassword.toString())
                    Log.d("Decrypted Password", "Decrypted Password: $decryptedPassword")

                    if (decryptedPassword) {
                        // Store data locally
                        val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("Full Name", fullName)
                        editor.apply()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("fullName", fullName)
                        startActivity(intent)
                        finish()
                        return@addOnSuccessListener
                    }
                }
                // Invalid credentials
                showErrorMessage(errorMessage, getString(R.string.auth_error_massage))
            }
            .addOnFailureListener { exception ->
                // Handle error
                Log.e("ERROR", "Message: ${exception.message}")
            }
    }
}
