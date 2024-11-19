package uk.ac.tees.mad.univid.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.ac.tees.mad.univid.repository.QuickParkRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: QuickParkRepository
) :ViewModel() {
}