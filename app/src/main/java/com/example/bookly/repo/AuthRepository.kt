package com.example.bookly.repo

import com.example.bookly.model.MyResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthRepository {

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore

     suspend fun register(fullName: String, email: String, password: String): MyResult? {
         var myResult: MyResult? = null
         GlobalScope.launch(Dispatchers.IO) {
             auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     // verify email
                     val user = auth.currentUser
                     user!!.sendEmailVerification().addOnCompleteListener { verification ->
                         if (verification.isSuccessful) {
                             val userMap = hashMapOf(
                                 "imageUrl" to "https://firebasestorage.googleapis.com/v0/b/bookly-9d3e8.appspot.com/o/user_images%2Fdefault.png?alt=media&token=cb5bc559-1406-46b1-9bf3-00a2edf41996"
                             )
                             // add user into firestore
                             db.collection("users").document(user.uid).set(userMap)
                             // save authentication displayName
                             val profileUpdates = userProfileChangeRequest {
                                 displayName = fullName
                             }
                             user.updateProfile(profileUpdates)
                         }
                     }
                     myResult = MyResult(200, "Account created")
                 }
             }.addOnFailureListener { exception ->
                 myResult = MyResult(400, exception.localizedMessage)
             }
         }
         delay(2000)
         return myResult
    }

    suspend fun login(email: String, password: String): MyResult? {
        var myResult: MyResult? = null
        GlobalScope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser?.isEmailVerified == true) {
                        myResult = MyResult(200, "Verified user")
                    } else {
                        myResult = MyResult(400, "Please verify your email address")
                        auth.signOut()
                    }
                }
            }.addOnFailureListener { exception ->
                myResult = MyResult(400, exception.localizedMessage)
            }
        }
        delay(2000)
        return myResult
    }

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }
}