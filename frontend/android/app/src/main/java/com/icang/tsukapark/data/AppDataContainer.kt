package com.icang.tsukapark.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.icang.tsukapark.data.local.LocalRepository
import com.icang.tsukapark.data.network.ApiService
import com.icang.tsukapark.data.network.NetworkRepository
import com.icang.tsukapark.data.network.WebSocketManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface DataContainer{
    val networkRepository : NetworkRepository
    val localRepository : LocalRepository
}
class AppDataContainer(
    private val dataStore: DataStore<Preferences>,
): DataContainer {
    private val apiUrl = "http://192.168.137.1:3000"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(apiUrl)
        .build()
    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    override val networkRepository: NetworkRepository by lazy {
        NetworkRepository(retrofitService)
    }
    override val localRepository: LocalRepository by lazy {
        LocalRepository(dataStore)
    }
}