package com.icang.tsukapark.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

interface DataRepository{
    suspend fun saveEmail(email: String)
    suspend fun removeEmail()
    val showEmail: Flow<String>
}

class LocalRepository(
    private val dataStore: DataStore<Preferences>
): DataRepository {
    private companion object{
        val EMAIL = stringPreferencesKey("email")
        const val TAG = "UserPreferencesRepo"
    }

    override val showEmail: Flow<String> = dataStore.data
        .catch {
            if (it is IOException){
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[EMAIL] ?: "null"
        }

    override suspend fun saveEmail(email: String){
        dataStore.edit { preferences ->
            preferences[EMAIL] = email
        }
    }

    override suspend fun removeEmail() {
        dataStore.edit { preferences ->
            preferences[EMAIL] = "null"
        }
    }
}