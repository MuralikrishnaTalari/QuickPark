package uk.ac.tees.mad.univid.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ac.tees.mad.univid.models.ParkingSpot
import uk.ac.tees.mad.univid.models.UserData
import uk.ac.tees.mad.univid.repository.QuickParkRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: QuickParkRepository,
) : ViewModel() {

    val signed = mutableStateOf(false)
    val userDetails = mutableStateOf(UserData())
    val loading = mutableStateOf(false)
    val parkingSpots = mutableStateOf<List<ParkingSpot>>(emptyList())

    init {
        viewModelScope.launch {
            signed.value = repository.isSignedIn()
            if (signed.value){
                getUserDetailsFromRepo()
                fetchParkingSpot()
            }
        }
    }

    fun signUP(context: Context, name: String, email: String, password: String) {
        loading.value = true
        repository.signUP(context, name, email, password, onSuccess = {
            signed.value = true
            loading.value = false
            getUserDetailsFromRepo()
            fetchParkingSpot()
        })
    }

    fun signIn(context: Context, email: String, password: String) {
        loading.value = true
        repository.signIn(context, email, password, onSuccess = {
            signed.value = true
            loading.value = false
            getUserDetailsFromRepo()
            fetchParkingSpot()
        })
    }

    fun getUserDetailsFromRepo() {
        viewModelScope.launch {
            userDetails.value = repository.getUserDetails()
        }
    }

    fun fetchParkingSpot() {
        viewModelScope.launch {
            parkingSpots.value = repository.fetchParkingSpots()
        }
    }

    fun uploadProfilePhoto(photo: Uri) {
        loading.value = true
        repository.uploadProfilePhoto(photo,onSuccess={
            loading.value = false
            getUserDetailsFromRepo()
        }, onFailure ={
            loading.value = false
        })
    }

    fun updateUserDetails(name: String) {
        loading.value = true
        repository.updateUserDetails(name, onSuccess = {
            loading.value = false
            getUserDetailsFromRepo()
        }, onFailed ={
            loading.value = false
        })
    }

    fun signOut() {
        repository.signOut()
        signed.value = false
        userDetails.value = UserData()
        parkingSpots.value = emptyList()
    }
}