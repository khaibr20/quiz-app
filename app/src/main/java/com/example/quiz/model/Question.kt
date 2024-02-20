package com.example.quiz.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class Question(
    val category: String?,
    val correct_answer: String?,
    val difficulty: String?,
    val incorrect_answers: ArrayList<String>?,
    val question: String?,
    val type: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(correct_answer)
        parcel.writeString(difficulty)
        parcel.writeStringList(incorrect_answers)
        parcel.writeString(question)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}