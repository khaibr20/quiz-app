package com.example.quiz.network

import android.util.Log
import com.example.quiz.model.QuestionResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


fun fetchQuestionsForCategory(category: Int, callback: (questions: QuestionResponseModel?) -> Unit) {
    val api = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuestionApi::class.java)

    val amount = 10
    val type = "multiple"

    api.getQuestionsByCategory(amount, category, type)
        .enqueue(object : Callback<QuestionResponseModel> {

        override fun onResponse(
            call: Call<QuestionResponseModel>,
            response: Response<QuestionResponseModel>
        ) {
            if (response.isSuccessful) {
                val questionResponse = response.body()
                callback(questionResponse)
            }
        }
            override fun onFailure(call: Call<QuestionResponseModel>, t: Throwable) {
            Log.e("QUESTION RESPONSE", "ERROR MESSAGE: ${t.message}")
            val requestUrl = call.request().url()
            Log.d("API URL", requestUrl.toString())
        }
    })
}

interface QuestionApi {
    @GET("/api.php")
    fun getQuestionsByCategory(
        @Query("amount") amount: Int,
        @Query("category") category: Int,
        @Query("type") type: String,
    ): Call<QuestionResponseModel>
}