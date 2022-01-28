package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*

class users : AppCompatActivity() {

    var user: String? = null
    val firestore = FirebaseFirestore.getInstance()
    lateinit var recyclerView: RecyclerView
    lateinit var userAdapter: userAdapter
    private val userList: ArrayList<userDetails> = ArrayList<userDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        user = intent.getStringExtra("user").toString()

        recyclerView = findViewById<RecyclerView>(R.id.req_list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

//        firestore.collection("accept").get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    for (document in task.getResult()!!) {
//                        if (document.id == user ) {
//                            firestore.collection("accept").document(user.toString()).collection("Sender").get()
//                                .addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        for (document in task.getResult()!!) {
//                                            var uName=document.id.toString()
//                                            firestore.collection("users").get()
//                                                .addOnCompleteListener { task ->
//                                                    if (task.isSuccessful) {
//                                                        for (document in task.getResult()!!) {
//                                                            if (document.id == uName) {
//                                                                val list = User(
//                                                                    document.get("Name").toString(),
//                                                                    document.get("profile pic").toString()
//                                                                )
//                                                                userList.add(list)
//                                                            }
//                                                        }
//                                                        userAdapter = userAdapter(applicationContext, userList)
//                                                        recyclerView.adapter = userAdapter
//                                                    }
//                                                }
//
//                                            }
//                                        }
//                                    }
//                                }
//                    }
//                }
//            }


//      Show the all the registered users for chat

        firestore.collection("users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.getResult()!!) {
                        if (document.id != user) {
                            val list = userDetails(document.get("Name").toString(),document.get("profile pic").toString(),document.id,user)
                            userList.add(list)
                        }
                    }
                    userAdapter = userAdapter(applicationContext, userList)
                    recyclerView.adapter = userAdapter
                }
            }

        //        navigation
        pro_bottom_nav.selectedItemId=R.id.btn_nav_chatBox
        pro_bottom_nav.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.btn_nav_search -> {
                        val intent = Intent(this, search::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                        finish()
                    }
                    R.id.btn_nav_notification -> {
                        val intent = Intent(this, request_notification::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                        finish()
                    }
                    R.id.btn_nav_home ->
                    {
                        val intent = Intent(this, profile::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                        finish()
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
        )
    }
}