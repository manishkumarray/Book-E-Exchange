package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.activity_history.pro_bottom_nav
import kotlinx.android.synthetic.main.activity_profile.*

class history : AppCompatActivity() {
    val firestore = FirebaseFirestore.getInstance()
    lateinit var recyclerView: RecyclerView
    var historyAdapter: historyAdapter? = null
    private val historyList: ArrayList<historydetails> = ArrayList<historydetails>()
    var user:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        intent=intent
        user = intent.getStringExtra("user").toString()
        recyclerView = findViewById<RecyclerView>(R.id.history_list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firestore.collection("users").document(user!!).collection("Book History").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful()) {
                    for (document in task.getResult()!!) {
                        empty_history.setVisibility(View.INVISIBLE)
                        val list = historydetails(document.get("Book Name").toString(), document.get("Author Name").toString(), document.get("Language").toString(), document.get("Book pic").toString(),document.get("Date").toString())
                        historyList.add(list)
                        historyAdapter = historyAdapter(applicationContext, historyList)
                    }
                    recyclerView.adapter = historyAdapter
                }
                else
                {
                    empty_history.setVisibility(View.VISIBLE)
                    history_del.setVisibility(View.INVISIBLE)
                }
            }
//        Delete history
        history_del.setOnClickListener(View.OnClickListener {
            firestore.collection("users").document(user!!).collection("Book History").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful()) {
                        for (document in task.getResult()!!) {
                            firestore.collection("users").document(user!!).collection("Book History").document(document.id)
                                .delete()
                        }
                    }
                }.addOnFailureListener{

                }
        })
        //         navigation
        pro_bottom_nav.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.btn_nav_home -> {
                        val intent = Intent(this, profile::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                    R.id.btn_nav_search -> {
                        val intent = Intent(this, search::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
        )
        history_back.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, profile::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        })
    }
}