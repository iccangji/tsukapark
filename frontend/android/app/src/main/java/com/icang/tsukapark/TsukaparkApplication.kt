package com.icang.tsukapark

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.icang.tsukapark.data.AppDataContainer
import com.icang.tsukapark.data.DataContainer

private const val TOKEN_PREFERENCE_NAME = "token_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = TOKEN_PREFERENCE_NAME
)

class TsukaparkApplication: Application(){
    lateinit var container: DataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(dataStore)
    }
}