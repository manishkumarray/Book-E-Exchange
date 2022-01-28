package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.android.synthetic.main.activity_upload_book.*
import java.time.LocalDateTime

class feedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        val firestore = FirebaseFirestore.getInstance()

        intent = intent
        val user = intent.getStringExtra("user").toString()

//      Cancel botton
        fb_btn_cancel.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@feedback, profile::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        })

//      Send Botton
        fb_btn_save.setOnClickListener(View.OnClickListener {

//            Store data
            val data = HashMap<String, Any>()
            var suggestion = findViewById<RadioButton>(fb_radiogroup.checkedRadioButtonId)
            data.put("Suggestion", suggestion.text.toString())
            data.put("Rating",ratingBar.rating.toString())
            data.put("Feedback",fb_desc.text.toString())

//           Book details
            firestore.collection("Feedback").document(user).set(data)
                .addOnSuccessListener {
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

            var intent = Intent(this@feedback, profile::class.java)
            intent.putExtra("user", user)
            Toast.makeText(applicationContext, "Thank you for your feedback.", Toast.LENGTH_LONG)
                .show()
            startActivity(intent)
            finish()
        })
    }
}