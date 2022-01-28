package com.example.bookexchange

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class searchAdapter(private val context: Context,
    private val list: ArrayList<bookdetails>) : RecyclerView.Adapter<searchAdapter.ViewHolder>(), Filterable {

    lateinit var bookFilterList: ArrayList<bookdetails>

    init {
        bookFilterList = list
    }

    val firestore = FirebaseFirestore.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): searchAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.display_book, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return bookFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    bookFilterList = list
                }
                else {
                    val resultList = ArrayList<bookdetails>()
                    for (row in list) {
                        if (row.bookName?.toLowerCase()?.contains(constraint.toString().toLowerCase())!! ||
                            row.authorName?.toLowerCase()?.contains(constraint.toString().toLowerCase())!!) {
                            resultList.add(row)
                        }
                    }
                    bookFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = bookFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                bookFilterList = results?.values as ArrayList<bookdetails>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: searchAdapter.ViewHolder, position: Int) {
        var bookList = bookFilterList.get(position)
        holder.bookName.text = bookList.bookName
        holder.authorName.text = bookList.authorName
        holder.language.text = bookList.language
        holder.dist.text = bookList.dis + " Kms"
        Glide.with(context).load(bookList.bookImage).into(holder.bookImage)

//      request send
        holder.request.setOnClickListener(View.OnClickListener {
            holder.request.setVisibility(View.INVISIBLE)
            holder.requested.setVisibility(View.VISIBLE)

//          Create field in document
            val data1 = HashMap<String, Any>()
            data1.put("Sender Id",bookList.sid.toString())
            firestore.collection("request").document(bookList.bookid.toString()).set(data1)
                .addOnSuccessListener {
                }.addOnFailureListener {
                }


//          Store details for request
            val data = HashMap<String, Any>()
            data.put("Sender Id",bookList.sid.toString())
            data.put("Receiver Id",bookList.rid.toString())
            data.put("Book Name",bookList.bookName.toString())
            data.put("Request",1)

            firestore.collection("request").document(bookList.bookid.toString())
                .collection(bookList.rid.toString()).document(bookList.sid.toString()).set(data).addOnSuccessListener {
            }.addOnFailureListener {
            }

        })

//      Cancel request send
        holder.requested.setOnClickListener(View.OnClickListener {
            holder.request.setVisibility(View.VISIBLE)
            holder.requested.setVisibility(View.INVISIBLE)

//            Delete from database
            firestore.collection("request").document(bookList.bookid.toString())
                .collection(bookList.rid.toString()).document(bookList.sid.toString()).delete()
        })

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bookName: TextView = itemView.findViewById(R.id.book_name)
        var authorName: TextView = itemView.findViewById(R.id.book_author)
        var language: TextView = itemView.findViewById(R.id.book_lng)
        var bookImage: ImageView = itemView.findViewById(R.id.book_image)
        var request: Button = itemView.findViewById(R.id.book_btn_req)
        var requested: Button = itemView.findViewById(R.id.book_btn_reqt)
        var dist:TextView = itemView.findViewById(R.id.book_loc)
    }
}
