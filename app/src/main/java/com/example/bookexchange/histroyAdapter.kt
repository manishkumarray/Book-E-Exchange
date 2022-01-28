package com.example.bookexchange

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class historyAdapter(private val context: Context,
                    private val list: ArrayList<historydetails>) : RecyclerView.Adapter<historyAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): historyAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.display_book_histroy, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: historyAdapter.ViewHolder, position: Int) {
        var historyList = list.get(position)
        holder.bookName.text = historyList.bookName
        holder.authorName.text = historyList.authorName
        holder.language.text = historyList.language
        holder.date.text = historyList.date
        Glide.with(context).load(historyList.bookImage).into(holder.bookImage)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookName: TextView = itemView.findViewById(R.id.history_book_name)
        var authorName: TextView = itemView.findViewById(R.id.history_book_author)
        var language: TextView = itemView.findViewById(R.id.history_book_lng)
        var date: TextView = itemView.findViewById(R.id.history_book_date)
        var bookImage: ImageView = itemView.findViewById(R.id.history_book_image)
    }
}
