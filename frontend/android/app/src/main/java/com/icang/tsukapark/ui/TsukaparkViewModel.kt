package com.icang.tsukapark.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.icang.tsukapark.TsukaparkApplication
import com.icang.tsukapark.data.local.LocalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val localRepository: LocalRepository
) : ViewModel() {

    val showEmail: StateFlow<String> =
        localRepository.showEmail.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    fun setEmail(email: String){
        viewModelScope.launch {
            localRepository.saveEmail(email)
        }
    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TsukaparkApplication)
                AppViewModel(application.container.localRepository)
            }
        }
    }

}