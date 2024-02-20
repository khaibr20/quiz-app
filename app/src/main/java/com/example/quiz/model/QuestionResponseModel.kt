package com.example.quiz.model

data class QuestionResponseModel(
    val response_code: Int,
    val results: List<Question>
)