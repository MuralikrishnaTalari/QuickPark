package uk.ac.tees.mad.univid.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.univid.data.local.ParkingDao
import uk.ac.tees.mad.univid.models.ParkingSpot
import uk.ac.tees.mad.univid.models.UserData
import javax.inject.Inject

class QuickParkRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val parkingDao: ParkingDao

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

    suspend fun fetchParkingSpots() : List<ParkingSpot> {
        val parkingSpots = mutableStateOf<List<ParkingSpot>>(emptyList())
        val result = firestore.collection("parking_spots").get().await()
        parkingSpots.value = result.map { it.toObject(ParkingSpot::class.java) }
        Log.d("ParkingSpots", parkingSpots.value.toString())
        return parkingSpots.value
    }

    fun uploadProfilePhoto(photo: Uri, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val storageRef = storage.reference
        val profileRef = storageRef.child("profile_pictures/${auth.currentUser!!.uid}")

        val uploadTask = profileRef.putFile(photo)

        uploadTask.addOnSuccessListener {
            profileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                firestore.collection("users")
                    .document(auth.currentUser!!.uid)
                    .update("profilePicture", downloadUrl.toString())
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onFailure()
                    }
            }.addOnFailureListener {
                onFailure()
            }
        }.addOnFailureListener {
            onFailure()
        }
    }

    fun updateUserDetails(name: String, onSuccess: () -> Unit, onFailed: () -> Unit) {
        firestore.collection("users").document(auth.currentUser!!.uid).update("name", name).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            onFailed()
        }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun insertParkingSpot(spot: ParkingSpot, onSuccess: () -> Unit) {
        val isSpotAlreadyAdded = parkingDao.getParkingSpotByName(spot.name)
        if (isSpotAlreadyAdded == null) {
            parkingDao.insertParkingSpot(spot)
            onSuccess()
        }
    }

    suspend fun getAllFromDatabase(): List<ParkingSpot> {
        val result = parkingDao.getAllParkingSpots()
        Log.d("ParkingSpots", result.toString())
        return result
    }
}