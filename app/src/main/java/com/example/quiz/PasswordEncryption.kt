package com.example.quiz


import org.mindrot.jbcrypt.BCrypt

object PasswordEncryption {

    private const val SALT_ROUNDS = 12

    fun hashPassword(password: String): String {
        val salt = BCrypt.gensalt(SALT_ROUNDS)
        return BCrypt.hashpw(password, salt)
    }

    fun verifyPassword(inputPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(inputPassword, hashedPassword)
    }
}

