package com.example.bookexchange

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

class edit_profile : AppCompatActivity() {
    lateinit var imageView: ImageView
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    val firestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        intent = intent
        val user = intent.getStringExtra("user").toString()

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


//      edit
        firestore.collection("users").document(user).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    if (document.get("profile pic")!=null) {
                        val imgUrl: String = document.get("profile pic").toString()
                        Glide.with(this@edit_profile).load(imgUrl).into(edit_v_dp)
                    }
                    else
                    {
//                        pro_v_dp.setBackgroundResource(R.drawable.profile_pic)
                    }
                    edit_name.setText(document.get("Name").toString())
                    edit_bio.setText(document.get("Bio").toString())
                }

//   Cancel botton
                edit_cancel.setOnClickListener(View.OnClickListener {
                    var intent = Intent(this@edit_profile, profile::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                })

//   Save Botton
                edit_save.setOnClickListener(View.OnClickListener {
//            firebase update name and bio
                    firestore.collection("users").document(user)
                        .update(
                            "Name", edit_name.text.toString(),
                            "Bio", edit_bio.text.toString()
                        )
                        .addOnSuccessListener {
                            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                        }
//            upload image

                    var intent = Intent(this@edit_profile, profile::class.java)
                    intent.putExtra("user", user)
                    uploadImage()
                    startActivity(intent)
                    finish()
                })

//   Set profile picture
                imageView = findViewById(R.id.edit_v_dp)
                edit_profile_pic.setOnClickListener {
                    launchGallery()
                }
            }
    }
//    firebase upload and save image
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                edit_v_dp.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String){
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["profile pic"] = uri
        intent = intent
        val user = intent.getStringExtra("user").toString()

        db.collection("users").document(user).update(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadImage(){
        if(filePath != null){
            val ref=  storageReference?.child("profile/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }
}