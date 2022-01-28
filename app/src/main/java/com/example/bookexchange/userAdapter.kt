package com.example.bookexchange

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class userAdapter(
    private val context: Context,
    private val list: ArrayList<userDetails>
) : RecyclerView.Adapter<userAdapter.ViewHolder>()  {

    val firestore = FirebaseFirestore.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.display_message, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: userAdapter.ViewHolder, position: Int) {
        var userList = list.get(position)
        holder.userName.text = userList.userName
        Glide.with(context).load(userList.profileImage).placeholder(R.drawable.profile_pic).into(holder.pic)

//        Open chat window
        holder.chat.setOnClickListener {
            val intent = Intent(context, chat::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("userId",userList.userId)
            intent.putExtra("SenderId",userList.senderId)
            intent.putExtra("profile pic",userList.profileImage)
            intent.putExtra("userName",userList.userName)
            context.startActivity(intent)
        }

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName: TextView = itemView.findViewById(R.id.msg_title)
        var pic: ImageView = itemView.findViewById(R.id.msg_image)
        val chat: CardView = itemView.findViewById(R.id.msg_parent)
        var lastMsg: TextView = itemView.findViewById(R.id.msg_last_text)

    }
}
