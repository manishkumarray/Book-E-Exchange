package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_password__set.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.regex.Pattern

class password_Set : AppCompatActivity() {
    private lateinit var emailid:String
    private lateinit var psw:String
    private lateinit var cpsw:String
    private val emailPattern: Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})")
    private val pswPattern: Pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
    val firebaseauth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password__set)

//        receive location
        intent=intent
        val latitude=intent.getStringExtra("latitude").toString()
        val longitude=intent.getStringExtra("longitude").toString()

//        redirect to otp verification after click on next
        psw_btn_next.setOnClickListener(View.OnClickListener {
            fun validEmail():Boolean
            {
                emailid=psw_et_id.text.toString().trim()
                if(emailid.isEmpty())
                {
                    psw_et_id.setError("Field can't be empty")
                    return false
                }
                else
                {
                    psw_et_id.setError(null)
                    return true
                }
            }
            fun validpsw():Boolean
            {
                cpsw=psw_et_cpsw.text.toString()
                psw=psw_et_psw.text.toString()
                if(psw.isEmpty() and cpsw.isEmpty())
                {
                    psw_et_psw.setError("Field can't be empty")
                    psw_et_cpsw.setError("Field can't be empty")
                    return false
                }
                else if (!pswPattern.matcher(psw).matches())
                {
                    psw_et_psw.setError("Invalid Password")
                    return false
                }
                else
                {
                    if(cpsw.isEmpty())
                    {
                        psw_et_cpsw.setError("Field can't be empty")
                        return false
                    }
                    else if (!cpsw.equals(psw))
                    {
                        psw_et_cpsw.setError("Password must be Same")
                        return false
                    }
                    else
                    {
                        psw_et_psw.setError(null)
                        psw_et_cpsw.setError(null)
                        return true
                    }
                }
            }
            if(validEmail() and validpsw() ) {
//                create user id on firebase
                firebaseauth.createUserWithEmailAndPassword(emailid, psw).addOnCompleteListener {
                    if (it.isSuccessful) {
//                        varification of email
                        val Cuser=firebaseauth.currentUser
                        Cuser.sendEmailVerification().addOnSuccessListener {
                            Toast.makeText(this,"Verification link has been sent",Toast.LENGTH_LONG).show()
                        }.addOnFailureListener{
                            Toast.makeText(this,"Your Entered email is not valid",Toast.LENGTH_LONG).show()
                        }
                        val intent = Intent(this@password_Set, registration::class.java)
                        intent.putExtra("email",emailid)

//                       send location
                        intent.putExtra("latitude",latitude)
                        intent.putExtra("longitude",longitude)

                        startActivity(intent)
                    }
                }.addOnFailureListener {
                    psw_et_id.setError("Already have account ")
                }
            }
        })

//        redirect to login after click on login
        psw_txt_login.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@password_Set, login::class.java)
            startActivity(intent)
        })
    }
}