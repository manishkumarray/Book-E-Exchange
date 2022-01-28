package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.pro_bottom_nav
import kotlinx.android.synthetic.main.activity_request_notification.*

class request_notification : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    var notificationAdapter: notificationAdapter? = null
    private val notificationList: ArrayList<Notificationdetails> = ArrayList<Notificationdetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_notification)
        val firestore = FirebaseFirestore.getInstance()
        intent = intent
        val user = intent.getStringExtra("user").toString()
        recyclerView = findViewById<RecyclerView>(R.id.req_list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

            firestore.collection("request").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            var bookId=document.id.toString()
                            firestore.collection("request").document(document.id).collection(user)
                                .get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful()) {
                                        for (document in task.getResult()!!) {
                                            val list = Notificationdetails(document.get("Book Name").toString(), document.id,user,bookId)
                                            notificationList.add(list)
                                        }
                                        notificationAdapter = notificationAdapter(applicationContext, notificationList)
                                        recyclerView.adapter = notificationAdapter
                                    }
                                }
                        }
                    }
                    else{
                        req_msg.setVisibility(View.VISIBLE)
                    }
                }


//        navigation
        pro_bottom_nav.selectedItemId=R.id.btn_nav_notification
        pro_bottom_nav.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.btn_nav_search -> {
                        val intent = Intent(this@request_notification, search::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                        finish()
                    }

                    R.id.btn_nav_home -> {
                        val intent = Intent(this@request_notification, profile::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                        finish()
                    }
                    R.id.btn_nav_chatBox -> {
                        val intent = Intent(this@request_notification, users::class.java)
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