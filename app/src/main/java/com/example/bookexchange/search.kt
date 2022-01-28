package com.example.bookexchange

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.pro_bottom_nav
import kotlinx.android.synthetic.main.activity_search.*

class search : AppCompatActivity() {
    var user:String?=null
    val firestore = FirebaseFirestore.getInstance()
    lateinit var recyclerView: RecyclerView
    lateinit var searchAdapter: searchAdapter
    private val bookList: ArrayList<bookdetails> = ArrayList<bookdetails>()

    var lat1:Double? = null
//    var lat2:Double? = null
    var lng1:Double? = null
//    var lng2:Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        user = intent.getStringExtra("user").toString()

        recyclerView = findViewById<RecyclerView>(R.id.list_view)
        recyclerView.layoutManager = LinearLayoutManager(this)


//         navigation
        pro_bottom_nav.selectedItemId=R.id.btn_nav_search
        pro_bottom_nav.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.btn_nav_home -> {
                        val intent = Intent(this@search, profile::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                        finish()
                    }
                    R.id.btn_nav_notification -> {
                        val intent = Intent(this@search, request_notification::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                        finish()
                    }
                    R.id.btn_nav_chatBox -> {
                        val intent = Intent(this@search, users::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                        finish()
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
        )

//     User location
        firestore.collection("users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.getResult()!!) {
                        if(document.id == user)
                        {
                            lat1 = document.get("latitude").toString().toDouble()
                            lng1 = document.get("logitude").toString().toDouble()
                        }
                    }
                }
            }


//      Adopter implimentation
        firestore.collection("users").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.getResult()!!) {
                        if (document.id != user) {
                            var rid=document.id

                            var lat2 = document.get("latitude").toString().toDouble()
                            var lng2 = document.get("logitude").toString().toDouble()

                            var d2r = Math.PI / 180
                            var dLong = (lng1 !! - lng2) * d2r
                            var dLat = (lat1!! - lat2) * d2r
                            var a = (Math.pow(Math.sin(dLat / 2.0), 2.0) + (Math.cos(lat2 * d2r)
                                    * Math.cos(lat1!! * d2r) * Math.pow(Math.sin(dLong / 2.0), 2.0)))
                            var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
                            var d = Math.round((6367000 * c)/1000)

                            firestore.collection("users").document(document.id!!).collection("Book Details").get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful()) {
                                        for (document in task.getResult()!!) {
                                            val list = bookdetails(document.get("Book Name").toString(), document.get("Author Name").toString(), document.get("Language").toString(), document.get("Book pic").toString(),
                                                user,rid,document.id,d.toString())
                                            bookList.add(list)
                                        }
                                        searchAdapter = searchAdapter(applicationContext, bookList)
                                        recyclerView.adapter = searchAdapter
                                    }
                                }
                        }
                        else
                        {
                        }
                    }
                }
            }

//      Search by book name
        srch_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d("onQueryTextChange", "query: " + query)
                searchAdapter?.filter?.filter(query)
                return true
            }
        })
    }

}