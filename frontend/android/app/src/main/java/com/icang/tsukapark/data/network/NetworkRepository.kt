package com.icang.tsukapark.data.network

import com.google.gson.Gson
import com.icang.tsukapark.data.model.AuthRequest
import com.icang.tsukapark.data.model.AuthResponse
import com.icang.tsukapark.data.model.OrderResponse
import com.icang.tsukapark.data.model.ParkResponse

interface ApiRepository{
    suspend fun auth(data: AuthRequest): AuthResponse
    var wsUrl: String
    fun parsingJsonPark(text: String): ParkResponse
    fun parsingJsonOrder(text: String): OrderResponse
}
class NetworkRepository(
    private val apiService: ApiService,
) : ApiRepository {
    override suspend fun auth(data: AuthRequest): AuthResponse {
        return try {
            apiService.login(data)
        } catch (e: Exception){
            AuthResponse(
                status = "failed",
                data = null
            )
        }
    }

    override var wsUrl: String = "ws://192.168.137.1:3000"
    override fun parsingJsonPark(text: String): ParkResponse {
        val gson = Gson()
        return gson.fromJson(text, ParkResponse::class.java)
    }

    override fun parsingJsonOrder(text: String): OrderResponse {
        val gson = Gson()
        return gson.fromJson(text, OrderResponse::class.java)
    }
}

sealed class UiState<out T> {
    data object Success : UiState<Nothing>()
    data object Error : UiState<Nothing>()
    data object Initial : UiState<Nothing>()
}

data class Slot(val label: String, var isFilled: Boolean, val location: String)

sealed class ParkUiState<out T> {
    data class Success<T>(val data: T) : ParkUiState<T>()
    data object Initial : ParkUiState<Nothing>()
    data object Error : ParkUiState<Nothing>()
}