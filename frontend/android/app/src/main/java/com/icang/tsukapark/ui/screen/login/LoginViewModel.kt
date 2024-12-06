package com.icang.tsukapark.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.icang.tsukapark.TsukaparkApplication
import com.icang.tsukapark.data.local.LocalRepository
import com.icang.tsukapark.data.model.AuthRequest
import com.icang.tsukapark.data.network.NetworkRepository
import com.icang.tsukapark.data.network.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val localRepository: LocalRepository,
    private val repository: NetworkRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Initial)
    val uiState = _uiState.asStateFlow()

    private fun setEmail(email: String){
        viewModelScope.launch {
            localRepository.saveEmail(email)
        }
    }

    fun auth(email: String, password: String){
        viewModelScope.launch {
            try {
                val result = repository.auth(AuthRequest(email, password))
                when (result.status) {
                    "success" -> {
                        result.data?.let { setEmail(it.email) }
                        _uiState.value = UiState.Success
                    }
                    else -> {
                        _uiState.value = UiState.Error
                    }
                }
            }
            catch (e: Exception) {
                _uiState.value = UiState.Error
//                Log.d("ViewModel Error", e.toString())
            }
        }
    }

    fun hideModal(){
        _uiState.value = UiState.Initial
    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TsukaparkApplication)
                LoginViewModel(application.container.localRepository, application.container.networkRepository)
            }
        }
    }
}