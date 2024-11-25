package uk.ac.tees.mad.univid.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.univid.models.UserData
import javax.inject.Inject

class QuickParkRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    fun signUP(context: Context, name: String, email: String, password: String, onSuccess:()->Unit) {
        val user = UserData(name = name, email = email, password = password)
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            firestore.collection("users").document(it.user!!.uid).set(user).addOnSuccessListener {
                Toast.makeText(context, "Sign up Successful", Toast.LENGTH_SHORT).show()
                onSuccess()
            }.addOnFailureListener { error->
                Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun getUserDetails() :UserData {
        val result = firestore.collection("users").document(auth.currentUser!!.uid).get().await()
        val response = result.toObject(UserData::class.java)
        return response!!
    }

    fun signIn(context: Context, email: String, password: String, onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            Toast.makeText(context, "Sign in Successful", Toast.LENGTH_SHORT).show()
            onSuccess()
        }.addOnFailureListener { error->
            Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

}