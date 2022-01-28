package com.example.bookexchange

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class acceptAdopter(private val context: Context,
                          private val list: ArrayList<acceptDetails>) : RecyclerView.Adapter<acceptAdopter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): acceptAdopter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.accept_request_display, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var acceptList = list.get(position)
        holder.notifcation.text = acceptList.rid +" accept your request for " + acceptList.bookName

//       delete notification
        val firestore = FirebaseFirestore.getInstance()
        holder.delete.setOnClickListener(View.OnClickListener {
            firestore.collection("accept").document(acceptList.rid.toString())
                .collection(acceptList.sid.toString()).document(acceptList.bookName.toString()).delete()
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notifcation: TextView = itemView.findViewById(R.id.acpt_notify)
        var delete: Button = itemView.findViewById(R.id.acpt_delete)
    }

}
