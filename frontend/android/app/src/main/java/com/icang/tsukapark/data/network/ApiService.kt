package com.icang.tsukapark.data.network

import com.icang.tsukapark.data.model.AuthRequest
import com.icang.tsukapark.data.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService{
//    @Headers(
//        "X-RapidAPI-Host: 192.168.137.1"
//    )
    @POST("login")
    suspend fun login(
        @Body data: AuthRequest
    ): AuthResponse
}