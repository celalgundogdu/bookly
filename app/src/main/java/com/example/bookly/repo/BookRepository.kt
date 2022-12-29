package com.example.bookly.repo

import androidx.lifecycle.MutableLiveData
import com.example.bookly.model.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BookRepository {

    private val db: FirebaseFirestore = Firebase.firestore
    private val bookList: MutableLiveData<List<Book>> = MutableLiveData()

    fun getBookList(): MutableLiveData<List<Book>> {
        return bookList
    }

    fun getAllBooks() {
        db.collection("books")
            .get()
            .addOnSuccessListener { documents ->
                val tempList: ArrayList<Book> = ArrayList()
                for (document in documents) {
                    val name = document.get("bookName") as String
                    val rating = document.get("bookRating") as Double
                    val imageUrl = document.get("imageUrl") as String
                    val description = document.get("description") as String
                    val page = (document.get("page") as Long).toString().toInt()
                    val book = Book(document.id, name, rating.toFloat(), imageUrl, description, page)
                    tempList.add(book)
                }
                bookList.value = tempList
            }
    }

    fun searchBook(query: String) {
        db.collection("books").get()
            .addOnSuccessListener { documents ->
                val tempList: ArrayList<Book> = ArrayList()
                for (document in documents) {
                    val name = document.get("bookName") as String
                    val rating = document.get("bookRating") as Double
                    val imageUrl = document.get("imageUrl") as String
                    val description = document.get("description") as String
                    val page = (document.get("page") as Long).toString().toInt()
                    val book = Book(document.id, name, rating.toFloat(), imageUrl, description, page)
                    tempList.add(book)
                }
                val resultList = tempList.filter{book -> book.bookName.lowercase().contains(query.lowercase())}
                bookList.value = resultList
            }
    }
}