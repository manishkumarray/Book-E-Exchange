package com.example.bookexchange

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.ArrayList

class chat : AppCompatActivity() {

    var user: String? = null
    val firestore = FirebaseFirestore.getInstance()
    lateinit var recyclerView: RecyclerView
    lateinit var chatAdapter: ChatAdapter
    var chatList = ArrayList<chatDetails>()
    var topic = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

//       Set user name and profile pic
        intent = intent
        var userId = intent.getStringExtra("userId").toString()
        var user = intent.getStringExtra("SenderId").toString()
        chat_UserName.text = intent.getStringExtra("userName").toString()
        var dpUrl = intent.getStringExtra("profile pic").toString()
        Glide.with(this@chat).load(dpUrl).into(chat_Profile)

//        Back to user list
        chat_Back.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@chat,users::class.java)
            intent.putExtra("user",user)
            startActivity(intent)
            finish()
        })


        chat_SendMessage.setOnClickListener {
            var message: String = chat_Message.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                chat_Message.setText("")
            } else {
                sendMessage(user,userId, message)
                chat_Message.setText("")
                topic = "/topics/$userId"
            }
        }

        readMessage(user, userId)
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)

    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
                Log.d("myTag", " "+error.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(chatDetails::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                chatAdapter = ChatAdapter(this@chat, chatList)
                recyclerView.adapter = chatAdapter

            }
        })
    }

}