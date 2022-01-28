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

class notificationAdapter(private val context: Context,
                          private val list: ArrayList<Notificationdetails>) : RecyclerView.Adapter<notificationAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notificationAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.display_request_notification, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: notificationAdapter.ViewHolder, position: Int) {
        var notificationList = list.get(position)
        holder.userName.text = notificationList.sid
        holder.bookName.text = " request for "+ notificationList.bookName

//       Accept the request
        val firestore = FirebaseFirestore.getInstance()
        holder.accept.setOnClickListener(View.OnClickListener {

            val data1 = HashMap<String, Any>()
            data1.put("Sender Id",notificationList.sid.toString())
            firestore.collection("accept").document(notificationList.rid.toString()).set(data1)
                .addOnSuccessListener {
            }.addOnFailureListener {
            }

//          Save the data for chatter list
            val data = HashMap<String, Any>()
            data.put("Sender Id",notificationList.sid.toString())
            data.put("Receiver Id",notificationList.rid.toString())
            data.put("Book Name" , notificationList.bookName.toString())
            firestore.collection("accept").document(notificationList.rid.toString()).collection(notificationList.sid.toString()).document(notificationList.bookName.toString()).set(data)
                .addOnSuccessListener {
                }.addOnFailureListener {
                }

            firestore.collection("request").document(notificationList.bookid.toString())
                .collection(notificationList.rid.toString()).document(notificationList.sid.toString()).delete()
        })

//      Delete the request
        holder.delete.setOnClickListener(View.OnClickListener {
            firestore.collection("request").document(notificationList.bookid.toString())
                .collection(notificationList.rid.toString()).document(notificationList.sid.toString()).delete()
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName: TextView = itemView.findViewById(R.id.req_userid)
        var bookName: TextView = itemView.findViewById(R.id.req_notify)
        var accept: Button = itemView.findViewById(R.id.req_accept)
        var delete: Button = itemView.findViewById(R.id.req_delete)
    }
}
