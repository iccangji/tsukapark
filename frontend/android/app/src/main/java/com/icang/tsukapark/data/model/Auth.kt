package com.icang.tsukapark.data.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @field:SerializedName("email")
    var email: String = "english",
    @field:SerializedName("password")
    var password: String
)

data class AuthResponse(
    val data: AuthData?,
    val status: String
)

data class AuthData(
    val firstname: String,
    val email: String,
    val token: String,
    val lastname: String
)