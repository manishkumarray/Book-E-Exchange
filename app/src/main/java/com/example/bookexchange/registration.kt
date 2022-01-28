package com.example.bookexchange

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.regex.Pattern


class registration : AppCompatActivity() {
//    variables declearation
    private lateinit var name:String
    private lateinit var phone:String
    private lateinit var dob:String
    private val namePattern: Pattern= Pattern.compile("[a-zA-Z][a-zA-Z ]{2,}")
    private val phonePattern: Pattern = Pattern.compile("[0-9]{10}")
    private val dobPattern: Pattern = Pattern.compile("^(0?[1-9]|[12][0-9]|3[01])[\\.\\/\\-](0?[1-9]|1[012])[\\.\\/\\-]\\d{4}\$")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

//        intent=intent
        val latitude=intent.getStringExtra("latitude").toString()
        val longitude=intent.getStringExtra("longitude").toString()

//      redirect to otp verification after click on next
        reg_btn_next.setOnClickListener(View.OnClickListener {
//          Validation
            fun validName():Boolean
            {
                name=reg_et_name.text.toString().trim()
                if(name.isEmpty())
                {
                    reg_et_name.setError("Field can't be empty")
                    return false
                }
                else if (!namePattern.matcher(name).matches()) {
                    reg_et_phone.setError("Invalid Name")
                    return false
                }
                else
                {
                    reg_et_name.setError(null)
                    return true
                }
            }

            fun validPhone():Boolean
            {
                phone=reg_et_phone.text.toString().trim()
                if(phone.isEmpty())
                {
                    reg_et_phone.setError("Field can't be empty")
                    return false
                }
                else if (!phonePattern.matcher(phone).matches())
                {
                    reg_et_phone.setError("Invalid Mobile Number")
                    return false
                }
                else
                {
                    reg_et_phone.setError(null)
                    return true
                }
            }
            fun validDob():Boolean
            {
                dob=reg_et_dob.text.toString().trim()
                if(dob.isEmpty())
                {
                    reg_et_dob.setError("Field can't be empty")
                    return false
                }
                else if (!dobPattern.matcher(dob).matches())
                {
                    reg_et_dob.setError("Invalid Date of Birth")
                    return false
                }
                else
                {
                    reg_et_dob.setError(null)
                    return true
                }
            }
            fun validCheck():Boolean
            {
                if(!reg_ckbox_1.isChecked())
                {
                    val builder =
                    AlertDialog.Builder(this)
                    builder.setMessage("Terms and Conditions must be accepted")
                    builder.setTitle("Error")
                    builder.setCancelable(false)
                    builder.setNegativeButton("OK") { dialog, which ->
                        dialog.cancel()
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                    return false
                }
                else
                {
                    reg_ckbox_1.setError(null)
                    return true
                }
            }

////      store data to firebase

                if (validName() and validPhone() and validDob() and validCheck()) {
                  intent=intent
                    val user=intent.getStringExtra("email").toString()

                    val data = HashMap<String, Any>()
                    data.put("Name", name)
                    data.put("Mobile number", phone)
                    data.put("DOB", dob)
                    data.put("latitude",latitude)
                    data.put("logitude",longitude)

                    val firestore = FirebaseFirestore.getInstance()
                    firestore.collection("users").document(user).set(data).addOnSuccessListener {
                    }.addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    val intent = Intent(this, profile::class.java)
                    intent.putExtra("user",user)
                    startActivity(intent)
                    finish()
                }
        })

//      redirect to login after click on login
        reg_txt_login.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@registration, login::class.java)
            startActivity(intent)
            finish()
        })

//      terms and conditions
        reg_tc.setOnClickListener(View.OnClickListener {
            val builder =
                AlertDialog.Builder(this)
                builder.setMessage("" +
                    "Terms of Use\n" +
                    "By using the application service you are agreeing to be bound by the following terms and conditions (\"Terms of Use\").\n" +
                    "\n" +
                    "Basic Terms\n" +
                    "You must be 14 years or older to use this site.\n" +
                    "You may not post nude, partially nude, or sexually suggestive photos.\n" +
                    "You are responsible for any activity that occurs under your screen name.\n" +
                    "You are responsible for keeping your password secure.\n" +
                    "You must not abuse, harass, threaten, impersonate or intimidate other users.\n" +
                    "You may not use the application service for any illegal or unauthorized purpose. International users agree to comply with all local laws regarding online conduct and acceptable content.\n" +
                    "You are solely responsible for your conduct and any data, text, information, screen names, graphics, photos, profiles, links (\"Content\") that you submit, post, and display on the application service.\n" +
                    "You must not modify, adapt or hack application or modify another website so as to falsely imply that it is associated with application.\n" +
                    "You must not crawl, scrape, or otherwise cache any content from Book E-Exchange including but not limited to user profiles and photos.\n" +
                    "You must not create or submit unwanted email or comments to any application's members (\"Spam\").\n" +
                    "You must not transmit any worms or viruses or any code of a destructive nature.\n" +
                    "You must not, in the use of application, violate any laws in your jurisdiction (including but not limited to copyright laws).\n" +
                    "Violation of any of these agreements will result in the termination of your account. While application prohibits such conduct and content on its site, you understand and agree that app cannot be responsible for the Content posted on it and you nonetheless may be exposed to such materials and that you use the book-e-exchange service at your own risk.\n" +
                    "\n" +
                    "\n" +
                    "General Conditions\n" +
                    "We reserve the right to modify or terminate the book-e-exchange service for any reason, without notice at any time.\n" +
                    "We reserve the right to refuse service to anyone for any reason at any time.\n" +
                    "We reserve the right to force forfeiture of any username that becomes inactive, violates trademark, or may mislead other users.\n" +
                    "We reserve the right to reclaim usernames on behalf of businesses or individuals that hold legal claim or trademark on those usernames.")
            builder.setTitle("Terms and Conditions")
            builder.setCancelable(false)
            builder.setNegativeButton("OK") { dialog, which ->
                dialog.cancel()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        })
    }
}