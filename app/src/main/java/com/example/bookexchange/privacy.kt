package com.example.bookexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password__set.*
import kotlinx.android.synthetic.main.activity_privacy.*
import java.util.regex.Pattern

class privacy : AppCompatActivity() {

    private val pswPattern: Pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
    val firebaseauth = FirebaseAuth.getInstance()
    private lateinit var cpsw:String
    private lateinit var psw:String
    private lateinit var rpsw:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy)

        intent=intent
        val user = intent.getStringExtra("user").toString()

//      reset password
        fun validpsw():Boolean
        {
            psw=cp_change_password.text.toString()
            rpsw=cp_reenter_password.text.toString()
            if(psw.isEmpty() and rpsw.isEmpty())
            {
                cp_change_password.setError("Field can't be empty")
                cp_reenter_password.setError("Field can't be empty")
                return false
            }
            else if (!pswPattern.matcher(psw).matches())
            {
                cp_change_password.setError("Invalid Password")
                return false
            }
            else
            {
                if(rpsw.isEmpty())
                {
                    psw_et_cpsw.setError("Field can't be empty")
                    return false
                }
                else if (!rpsw.equals(psw))
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


//      Cancel botton
        cp_btn_cancel.setOnClickListener(View.OnClickListener {
            var intent= Intent(this@privacy,profile::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
            finish()
        })

//      Send Botton
        cp_btn_save.setOnClickListener(View.OnClickListener {
            var intent= Intent(this@privacy,profile::class.java)
            intent.putExtra("user", user)
            Toast.makeText(applicationContext, "Your password has been changed.", Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        })
    }
}