package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_password.*

class forgetPassword : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        mAuth = FirebaseAuth.getInstance()

        fp_reset.setOnClickListener {
            val email = fp_userid.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Enter your email!", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                            fp_userid.setError("Entered Email id is not Valid")
                        }
                    }
            }
        }

        fp_btn_back.setOnClickListener {
            val intent = Intent(this@forgetPassword,login::class.java)
            startActivity(intent)
            finish()
        }
    }
}