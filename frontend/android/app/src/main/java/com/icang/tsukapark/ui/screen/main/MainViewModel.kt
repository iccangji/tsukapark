package com.icang.tsukapark.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.icang.tsukapark.TsukaparkApplication
import com.icang.tsukapark.data.local.LocalRepository
import com.icang.tsukapark.data.network.NetworkRepository
import com.icang.tsukapark.data.network.ParkUiState
import com.icang.tsukapark.data.network.Slot
import com.icang.tsukapark.data.network.WebSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MainViewModel(
    private val localRepository: LocalRepository,
    private val repository: NetworkRepository
) : ViewModel() {
    private val _parkUiState = MutableStateFlow<ParkUiState<List<Slot>>>(ParkUiState.Initial)
    val parkUiState = _parkUiState.asStateFlow()

    private val _checkinUiState = MutableStateFlow(0)
    val checkinUiState = _checkinUiState.asStateFlow()

    private val _isConnectedState = MutableStateFlow(false)
    val isConnectedState = _isConnectedState.asStateFlow()

    private val _emailState = MutableStateFlow("")

    private val orderList = listOf(
        "park1",
        "park4",
        "park2",
        "park5",
        "park3",
        "park6",
    )

    private val webSocket = WebSocketManager()
    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("Websocket", "Connected")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            try {
                _isConnectedState.value = true
                Log.d("Websocket", "Message has received!")
                val result = repository.parsingJsonOrder(text)
                when (result.topic) {
                    "sensor/data" -> {
                        _parkUiState.value = ParkUiState.Success(
                            listOf(
                                Slot("L1", result.data.park1.toBoolean(), "Barat Lobi Blok L No. 1"),
                                Slot("R1", result.data.park2.toBoolean(), "Timur Lobi Blok R No. 1"),
                                Slot("L2", result.data.park3.toBoolean(), "Barat Lobi Blok L No. 2"),
                                Slot("R2", result.data.park4.toBoolean(), "Timur Lobi Blok R No. 2"),
                                Slot("L3", result.data.park5.toBoolean(), "Barat Lobi Blok L No. 3"),
                                Slot("R3", result.data.park6.toBoolean(), "Timur Lobi Blok R No. 3"),
                            )
                        )
                    }

                    "order/data" -> {
                        val order = listOf(
                            result.data.park1 == _emailState.value,
                            result.data.park2 == _emailState.value,
                            result.data.park3 == _emailState.value,
                            result.data.park4 == _emailState.value,
                            result.data.park5 == _emailState.value,
                            result.data.park6 == _emailState.value,
                        )
                        if(order.any{it}){
                            val index = order.indexOfFirst { it } + 1
                            _checkinUiState.value = index
                        }else{
                            _checkinUiState.value = 0
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("Websocket Error", e.toString())
            }

        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//            super.onMessage(webSocket, bytes)
//            println("Received message (bytes): ${bytes.hex()}")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//            super.onFailure(webSocket, t, response)
//            println("Error occurred: ${t.message}")
        }
    }

    fun logout() {
        viewModelScope.launch {
            localRepository.removeEmail()
        }
    }

    private suspend fun getEmail(): String {
        return try {
            withTimeout(5000) {
                localRepository.showEmail.stateIn(
                    scope = CoroutineScope(Dispatchers.IO),
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = ""
                ).first { it.isNotEmpty() }
            }
        } catch (e: TimeoutCancellationException) {
            "Timeout"
        }
    }

    fun setPark(label: Int, checkin: Boolean) {
        viewModelScope.launch {
            val email = getEmail()
            webSocket.sendMessage(
                """
                    {
                        "park": "${orderList[label]}",
                        "email": "$email",
                        "checkin": $checkin
                    }
                """.trimIndent()
            )
        }
    }

    init {
        Log.d("ViewModel", "ViewModel init")
        webSocket.connect(repository.wsUrl, listener)
        viewModelScope.launch {
            _emailState.value = getEmail()
            Log.d("current email: ", _emailState.value)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TsukaparkApplication)
                MainViewModel(
                    application.container.localRepository,
                    application.container.networkRepository
                )
            }
        }
    }
}