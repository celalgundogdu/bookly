package com.example.bookly.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookly.model.Book
import com.example.bookly.model.UserBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore
    private var user: MutableLiveData<FirebaseUser> = MutableLiveData()
    //private val userBookList: MutableLiveData<List<UserBook>> = MutableLiveData()

    companion object {
        val userBookList: MutableLiveData<List<UserBook>> = MutableLiveData()
    }

    fun updateUser(newName: String, newPassword: String) {
        val u = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = newName
        }
        u!!.updateProfile(profileUpdates).addOnCompleteListener {
            u!!.updatePassword(newPassword).addOnCompleteListener {
                user.value = u!!
            }
        }
    }

    fun getUserBookList(): MutableLiveData<List<UserBook>> {
        return userBookList
    }

    fun getAllUserBooks() {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("user_books")
            .get()
            .addOnSuccessListener { documents ->
                val tempList: ArrayList<UserBook> = ArrayList()
                for(document in documents) {
                    val bookId = document.get("bookId") as String
                    val imageUrl = document.get("imageUrl") as String
                    val page = (document.get("page")).toString().toInt()
                    val pageRead = (document.get("pageRead")).toString().toInt()
                    val userBook = UserBook(bookId, imageUrl, page, pageRead)
                    tempList.add(userBook)
                }
                userBookList.value = tempList
            }
    }

    fun addUserBook(userBook: UserBook) {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("user_books")
            .document(userBook.bookId)
            .set(userBook)
            .addOnSuccessListener {
                getAllUserBooks()
            }
    }

    suspend fun checkIfBookExistsInUserList(bookId: String): Boolean {
        var exists = false
        GlobalScope.launch(Dispatchers.IO) {
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .collection("user_books")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if(document.get("bookId")?.equals(bookId) == true) {
                            exists = true
                            break
                        }
                    }
                }
        }
        delay(1000)
        return exists
    }

    fun updateUserBook(bookId: String, readPage: Int) {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("user_books")
            .document(bookId)
            .update("pageRead", readPage)
            .addOnCompleteListener {
                getAllUserBooks()
            }
    }

    fun deleteUserBook(bookId: String) {
        db.collection("users")
            .document(auth.currentUser!!.uid)
            .collection("user_books")
            .document(bookId)
            .delete()
            .addOnSuccessListener{
                getAllUserBooks()
            }
    }

    fun getUser(): MutableLiveData<FirebaseUser> {
        user.value = auth.currentUser
        return user
    }
}