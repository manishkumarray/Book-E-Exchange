package com.example.bookexchange

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_upload_book.*
import java.io.IOException
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap



class upload_book : AppCompatActivity() {
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71
    private var storageReference: StorageReference? = null

    val firestore = FirebaseFirestore.getInstance()

//    rendom book id
    val length = 5
    val bookId = getRandomString(length)

    //    Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_book)

    intent=intent
    val user = intent.getStringExtra("user").toString()

    val languages = resources.getStringArray(R.array.Languages)

    // access the spinner
    val spinner = findViewById<Spinner>(R.id.upd_spinner)
    if (spinner != null) {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, languages
        )
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            )
            {
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
//        Add book image
        upd_upload_book.setOnClickListener(View.OnClickListener {
            launchGallery()
        })

//            Upload book to firebase
        upd_btn_upload.setOnClickListener(View.OnClickListener {

            val data = HashMap<String, Any>()
            data.put("Book Id", bookId)
            data.put("Book Name", upd_et_bookname.text.toString())
            data.put("Author Name", upd_et_author.text.toString())
            data.put("Language", upd_spinner.selectedItem.toString())
            data.put("Date", (""+LocalDateTime.now().dayOfMonth+"/"+LocalDateTime.now().monthValue+"/"+LocalDateTime.now().year ))

//           Book details
            firestore.collection("users").document(user).collection("Book Details").document(bookId).set(data)
                .addOnSuccessListener {
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

//   req
//                    firestore.collection("request").document(bookId).set(
//
//                    )
//                        .addOnSuccessListener {
//                        }.addOnFailureListener {
//                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
//                        }

//          Book history
            firestore.collection("users").document(user).collection("Book History").document(bookId).set(data)
                .addOnSuccessListener {
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

            val intent = Intent(this, profile::class.java)
            intent.putExtra("user",user)
            uploadImage()
            startActivity(intent)
            finish()
        })
        }
    }

//    random book id
    fun getRandomString(length: Int) : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(length) { charset.random() }
            .joinToString("")
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
                upd_iv_1.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addUploadRecordToDb(uri: String){
        val db = FirebaseFirestore.getInstance()

        val data = HashMap<String, Any>()
        data["Book pic"] = uri
        intent = intent
        val user = intent.getStringExtra("user").toString()

//      book details
        db.collection("users").document(user).collection("Book Details").document(bookId).update(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }

//      Book history
        db.collection("users").document(user).collection("Book History").document(bookId).update(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadImage(){
        storageReference = FirebaseStorage.getInstance().reference
        if(filePath != null){
            val ref=  storageReference?.child("books/" + UUID.randomUUID().toString())
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
