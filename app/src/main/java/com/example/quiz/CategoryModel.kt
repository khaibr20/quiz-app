package com.example.quiz

import java.io.Serializable

data class CategoryModel(
    var imageResource: Int,
    var categoryName: String?,
    var quizNumber: Int
) : Serializable