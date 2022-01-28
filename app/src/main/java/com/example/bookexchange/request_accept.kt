package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_request_accept.*
import kotlinx.android.synthetic.main.activity_request_notification.*

class request_accept : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    var acceptAdopter: acceptAdopter? = null
    private val acceptList: ArrayList<acceptDetails> = ArrayList<acceptDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_accept)
        val firestore = FirebaseFirestore.getInstance()
        intent = intent
        val user = intent.getStringExtra("user").toString()
        recyclerView = findViewById<RecyclerView>(R.id.acpt_list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firestore.collection("accept").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                            firestore.collection("accept").document(document.id)
                                .collection(user)
                                .get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful()) {
                                        for (document in task.getResult()!!) {
                                                val list = acceptDetails(
                                                    document.get("Book Name").toString(),
                                                    document.get("Sender Id").toString(),
                                                    document.get("Receiver Id").toString()
                                                )
                                                acceptList.add(list)
                                        }
                                        acceptAdopter = acceptAdopter(applicationContext, acceptList)
                                        recyclerView.adapter = acceptAdopter
                                    }
                                }
                        }
                }
            }

//        Back to profile
        acpt_back.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, login::class.java))
            finish()
        })

//        Refresh
        acpt_refresh.setOnClickListener(View.OnClickListener {
            acceptAdopter?.notifyDataSetChanged()
        })
    }
}
