package com.icang.tsukapark.data.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class WebSocketManager {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()


    fun connect(url: String, listener: WebSocketListener) {
        try {
            val request = Request.Builder().url(url).build()
            webSocket = client.newWebSocket(request, listener)
            Log.d("Websocket","Connected successfully")
        }catch (e: Exception) {
            Log.d("Websocket", "Failed to connected")
        }
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }


    fun disconnect() {
        webSocket?.close(1000, "Closing")
    }
}