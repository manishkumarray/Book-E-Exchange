package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_contactus.*

class contactus : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)

        intent=intent
        val user = intent.getStringExtra("user").toString()

//      Next Botton
        contact_btn_next.setOnClickListener(View.OnClickListener {

//         send feadback to database
            val firestore = FirebaseFirestore.getInstance()
            val data = HashMap<String, Any>()
            data.put("Problem",contact_txt.text.toString())

            firestore.collection("ContactUs").document(user).set(data)
                .addOnSuccessListener {
            }
                .addOnFailureListener {
            }


            var intent= Intent(this@contactus,profile::class.java)
            intent.putExtra("user",user)
            startActivity(intent)
            finish()
        })

//       Back button
        cu_back.setOnClickListener(View.OnClickListener {
            var intent= Intent(this@contactus,profile::class.java)
            intent.putExtra("user",user)
            startActivity(intent)
            finish()
        })
    }
}