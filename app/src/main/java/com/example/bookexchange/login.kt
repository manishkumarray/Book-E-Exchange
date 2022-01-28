package com.example.bookexchange

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_login.*


class login : AppCompatActivity() {
    //    variable declearation
    private lateinit var userid: String
    private lateinit var psw: String
    private lateinit var auth: FirebaseAuth
    private var lastLocation: Location? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    var latitude:String? = null
    var longitude:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//       receive location

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                latitude= (lastLocation)!!.latitude.toString()
                longitude= (lastLocation)!!.longitude.toString()

            } else {
            }
        }

        intent=intent
//        val latitude=intent.getStringExtra("latitude").toString()
//        val longitude=intent.getStringExtra("longitude").toString()


//      redirect to profile after login
        lgn_btn_login.setOnClickListener(View.OnClickListener {
//          Validation of user id and password
            userid = lgn_et_id.text.toString().trim()
            psw = lgn_et_psw.text.toString()
            if (userid.isEmpty() && psw.isEmpty()) {
                lgn_et_id.setError("Field can't be empty")
                lgn_et_psw.setError("Field can't be empty")
            } else if (psw.isEmpty()) {
                lgn_et_psw.setError("Field can't be empty")
            } else if (userid.isEmpty()) {
                lgn_et_id.setError("Field can't be empty")
            } else {
                auth.signInWithEmailAndPassword(userid, psw)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            val user = auth.currentUser
                            var intent1 = Intent(this@login, profile::class.java)
                            intent1.putExtra("user", userid)
                            startActivity(intent1)
                            finish()
                            Toast.makeText(
                                baseContext,
                                "Authentication successful.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.exception)
                            val builder = AlertDialog.Builder(this)
                            builder.setMessage("Invalid User id and Password")
                            builder.setTitle("Error")
                            builder.setCancelable(false)
                            builder.setNegativeButton("OK") { dialog, which ->
                                dialog.cancel()
                            }
                            val alertDialog = builder.create()
                            alertDialog.show()
                            Toast.makeText(
                                baseContext,
                                "Authentication Failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }
        })

//        Forget Password
        lgn_txt_fgtpsw.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@login, forgetPassword::class.java)
            startActivity(intent)
            finish()
        })

//        redirect to registration after click on sing up
        lgn_txt_signup.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@login, password_Set::class.java)

//           send loction

            intent.putExtra("latitude", latitude)
            intent.putExtra("longitude", longitude)

            startActivity(intent)
        })
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            FirebaseAuth.getInstance()
            var intent = Intent(this@login, profile::class.java)
            intent.putExtra("user", currentUser.email)
            startActivity(intent)
        }
    }
//    override fun onDestroy() {
//        super.onDestroy()
//        Firebase.auth.signOut()
//        finishAffinity()
//    }
}