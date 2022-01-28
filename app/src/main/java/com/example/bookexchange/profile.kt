package com.example.bookexchange

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.display_book.*
import kotlinx.android.synthetic.main.menuheader.*
import kotlinx.android.synthetic.main.menuheader.view.*

class profile : AppCompatActivity() {
    var nav: NavigationView? = null
    var toggle: ActionBarDrawerToggle? = null
    var drawerLayout: DrawerLayout? = null
    var imgUrl:String?=null
    var user:String?=null
    var count=0
    var i=0
    val firebaseauth = FirebaseAuth.getInstance()

    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        intent = intent

//        user id set
        user = intent.getStringExtra("user").toString()
        toolbar.title = user

//        check the emai verification
        val Cuser=firebaseauth.currentUser
        if (!Cuser.isEmailVerified)
        {
            verifymsg.setVisibility(View.VISIBLE)
            verifyEmail.setVisibility(View.VISIBLE)
//            toolbar.setVisibility(View.INVISIBLE)
            pro_v_dp.setVisibility(View.INVISIBLE)
            pro_name.setVisibility(View.INVISIBLE)
            pro_bio.setVisibility(View.INVISIBLE)
            pro_btn_editprofile.setVisibility(View.INVISIBLE)
            pro_bottom_nav.setVisibility(View.INVISIBLE)
            pro_tv_book.setVisibility(View.INVISIBLE)
            pro_tv_del.setVisibility(View.INVISIBLE)
            nav_menu.setVisibility(View.INVISIBLE)

            verifyEmail.setOnClickListener(View.OnClickListener {
                Cuser.sendEmailVerification().addOnSuccessListener {
                    Toast.makeText(this,"Verification link has been sent",Toast.LENGTH_LONG).show()
                }.addOnFailureListener{
                    Toast.makeText(this,"Your Entered email is not valid",Toast.LENGTH_LONG).show()
                }
            }
            )
        }
        else
        {

            verifymsg.setVisibility(View.INVISIBLE)
            verifyEmail.setVisibility(View.INVISIBLE)

        }

//        header navigation view
        val navigationView: NavigationView = findViewById(R.id.nav_menu)
        val header: View = navigationView.getHeaderView(0)
        val user_head: TextView = header.findViewById(R.id.header_userid)

//        del button visibility
        del_book1.setVisibility(View.INVISIBLE)
        del_book2.setVisibility(View.INVISIBLE)
        del_book3.setVisibility(View.INVISIBLE)

//      Edit Profile
        pro_btn_editprofile.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@profile, edit_profile::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        })

//        name,bio and dp fetching from firebase

        firestore.collection("users").document(user!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {

//                    fetch profile image
                    pro_name.text = document.get("Name").toString()
                    user_head.text = document.get("Name").toString()
                    if (document.get("profile pic") != null) {
                        val imgUrl: String = document.get("profile pic").toString()
                        Glide.with(this@profile).load(imgUrl).into(pro_v_dp)
                        Glide.with(this@profile).load(imgUrl).into(head_v_dp)
                    } else {
                        pro_v_dp.setBackgroundResource(R.drawable.profile_pic)
                    }
                    if (document.get("Bio") != null) {
                        pro_bio.text = document.get("Bio").toString()
                    } else {
                        pro_bio.text = " "
                    }

                } else {
                    Toast.makeText(this, "No such docment found", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }

//      Navigation Drawer
        val toolbar =
            findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        nav = findViewById<View>(R.id.nav_menu) as NavigationView
        drawerLayout = findViewById<View>(R.id.drawer) as DrawerLayout
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout, toolbar, R.string.open, R.string.close
        )
        drawerLayout!!.addDrawerListener(toggle!!)
        toggle!!.syncState()
        nav!!.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_nav_feedback -> {
                    var intent = Intent(this@profile, feedback::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_nav_history -> {
                    var intent = Intent(this@profile, history::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_nav_accept -> {
                    var intent = Intent(this@profile, request_accept::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_nav_delete -> {
                    val cuser = firebaseauth.currentUser!!
                    cuser.delete()
                    firestore.collection("users").document(user!!)
                        .delete()
                    Firebase.auth.signOut()
                    startActivity(Intent(this@profile,login::class.java))
                    finish()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_nav_privacy -> {
                    var intent = Intent(this@profile, privacy::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_nav_contact -> {
                    var intent = Intent(this@profile, contactus::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_nav_appinfo -> {
                    Toast.makeText(applicationContext, "AppInfo Panel is Open", Toast.LENGTH_LONG)
                        .show()
                    drawerLayout!!.closeDrawer(GravityCompat.START)
                }
            }
            true
        }

//        navigation
        pro_bottom_nav.selectedItemId=R.id.btn_nav_home
        pro_bottom_nav.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.btn_nav_search -> {
                        val intent = Intent(this@profile, search::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                        finish()
                    }
                    R.id.btn_nav_notification -> {
                        val intent = Intent(this@profile, request_notification::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                        finish()
                    }
                    R.id.btn_nav_chatBox ->
                    {
                        val intent = Intent(this@profile, users::class.java)
                        intent.putExtra("user",user)
                        startActivity(intent)
                        finish()
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
        )
//        Logout
        var logout: TextView = header.findViewById(R.id.btn_logout)
        logout.setOnClickListener(View.OnClickListener {
            Firebase.auth.signOut()
            var intent = Intent(this@profile, login::class.java)
            startActivity(intent)
            finish()
        })

//      add book and fetch
        firestore.collection("users").document(user!!).collection("Book Details").get()
            .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                override fun onComplete(@NonNull task: Task<QuerySnapshot>) {
                    if (task.isSuccessful()) {
//                      Display Book Image
                        for (document in task.getResult()!!) {
                            if (document.get("Book pic") != null) {
                                count++
                                val imgUrl: String = document.get("Book pic").toString()
                                if (count == 1) {
                                    Glide.with(this@profile).load(imgUrl).into(book1)
                                } else if (count == 2) {
                                    Glide.with(this@profile).load(imgUrl).into(book2)
                                } else {
                                    Glide.with(this@profile).load(imgUrl).into(book3)
                                }
                            }
                        }

                        if (count == 0) {
                            book2.setVisibility(View.INVISIBLE)
                            book3.setVisibility(View.INVISIBLE)
                            book1.setOnClickListener {
                                var intent = Intent(this@profile, upload_book::class.java)
                                intent.putExtra("user", user)
                                startActivity(intent)
                                finish()
                            }
                        } else if (count == 1) {
                            book3.setVisibility(View.INVISIBLE)
                            del_book1.setVisibility(View.VISIBLE)

                            book1.setOnClickListener(View.OnClickListener { null })
                            book2.setOnClickListener {
                                var intent = Intent(this@profile, upload_book::class.java)
                                intent.putExtra("user", user)
                                startActivity(intent)
                                finish()
                            }
                        } else if (count == 2) {
                            book1.setOnClickListener(View.OnClickListener { null })
                            book2.setOnClickListener(View.OnClickListener { null })

                            del_book1.setVisibility(View.VISIBLE)
                            del_book2.setVisibility(View.VISIBLE)

                            book3.setOnClickListener {
                                var intent = Intent(this@profile, upload_book::class.java)
                                intent.putExtra("user", user)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            del_book1.setVisibility(View.VISIBLE)
                            del_book2.setVisibility(View.VISIBLE)
                            del_book3.setVisibility(View.VISIBLE)

                            book1.setOnClickListener(View.OnClickListener { null })
                            book2.setOnClickListener(View.OnClickListener { null })
                            book3.setOnClickListener(View.OnClickListener { null })

                        }
//                        Delete book
                        for (document in task.getResult()!!) {
                            if (document != null) {
                                i++
                                if (i == 1) {
                                    del_book1.setOnClickListener(View.OnClickListener {
                                        firestore.collection("users").document(user!!)
                                            .collection("Book Details").document(document.id)
                                            .delete()
                                        recreate()
                                        finish()
                                    })
                                } else if (i == 2) {
                                    del_book2.setOnClickListener(View.OnClickListener {
                                        firestore.collection("users").document(user!!)
                                            .collection("Book Details").document(document.id)
                                            .delete()
                                        recreate()
                                        finish()
                                    })
                                } else {
                                    del_book3.setOnClickListener(View.OnClickListener {
                                        firestore.collection("users").document(user!!)
                                            .collection("Book Details").document(document.id)
                                            .delete()
                                        recreate()
                                        finish()
                                    })

                                }
                            }
                        }
                    }
                    else {
//                        Log.d(TAG, "Error getting documents: ", task.getException())
                    }
                }
            })
    }
        override fun onResume() {
            super.onResume()
            firestore.collection("users").document(user!!).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.get("profile pic") != null) {
                            imgUrl= document.get("profile pic").toString()
                        }
                    }
                }
        }

   }