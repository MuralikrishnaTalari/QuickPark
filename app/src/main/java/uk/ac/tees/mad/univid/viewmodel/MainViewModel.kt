package uk.ac.tees.mad.univid.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    init {
        viewModelScope.launch {
            signed.value = repository.isSignedIn()
            if (signed.value){
                getUserDetailsFromRepo()
            }
        }
    }

    fun signUP(context: Context, name: String, email: String, password: String) {
        loading.value = true
        repository.signUP(context, name, email, password, onSuccess = {
            signed.value = true
            loading.value = false
            getUserDetailsFromRepo()
        })
    }

    fun signIn(context: Context, email: String, password: String) {
        loading.value = true
        repository.signIn(context, email, password, onSuccess = {
            signed.value = true
            loading.value = false
            getUserDetailsFromRepo()
        })
    }

    fun getUserDetailsFromRepo() {
        viewModelScope.launch {
            userDetails.value = repository.getUserDetails()
        }
    }
}