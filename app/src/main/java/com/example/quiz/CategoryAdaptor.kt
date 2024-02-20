package com.example.quiz

import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdaptor(
    private var category: List<CategoryModel>
): RecyclerView.Adapter<CategoryAdaptor.CategoryViewHolder>() {

    var onItemClick : ((CategoryModel) -> Unit)? = null

    inner class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return category.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = category[position]
        val imageView = holder.itemView.findViewById<ImageView>(R.id.image_view)
        val categoryNameTextView = holder.itemView.findViewById<TextView>(R.id.category_name)
        val quizNumberTextView = holder.itemView.findViewById<TextView>(R.id.quiz_number)
        
        imageView.setImageResource(currentItem.imageResource)
        categoryNameTextView.text = currentItem.categoryName
        quizNumberTextView.text = "${currentItem.quizNumber} questions"

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(currentItem)
        }
    }
}