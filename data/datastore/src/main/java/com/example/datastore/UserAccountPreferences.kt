package com.example.datastore

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UserAccountPreferences @Inject constructor(
    @ApplicationContext
    private val context: Context
) : UserAccountPreferencesRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        USER_ACCOUNT_PREFERENCES_NAME
    )
    private val dataStore = context.dataStore
    private val _userAccountCredentials = MutableSharedFlow<Pair<String?, String?>>()
    val userAccountCredentials: SharedFlow<Pair<String?, String?>> =
        _userAccountCredentials.asSharedFlow()
    private val _token = MutableSharedFlow<String?>()
    val token: SharedFlow<String?> = _token.asSharedFlow()

    override suspend fun saveUserAccountCredential(id: String, password: String) {
        val encryptedPassword = encrypt(password = password)
        dataStore.edit { preferences ->
            preferences[STRING_KEY_ID] = id
            preferences[STRING_KEY_PASSWORD] = encryptedPassword
        }
        _userAccountCredentials.emit(id to password)
    }

    override fun getUserAccountCredentials(): Flow<Pair<String?, String?>> {
        return dataStore.data.map { preferences: Preferences ->
            Log.d(
                "UserAccountPreferences",
                "${preferences[STRING_KEY_ID]} && ${preferences[STRING_KEY_PASSWORD]}"
            )
            Pair(
                preferences[STRING_KEY_ID],
                decrypt(password = preferences[STRING_KEY_PASSWORD] ?: "")
            )
        }.onEach {
            _userAccountCredentials.emit(it)
        }
    }

    override suspend fun saveToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[STRING_KEY_TOKEN]
        }
        _token.emit(token)
    }

    override fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[STRING_KEY_TOKEN]
        }.onEach {
            _token.emit(it)
        }
    }

    override suspend fun signout() {
        dataStore.edit { preferences: MutablePreferences ->
            preferences.clear()
        }
    }

    fun encrypt(password: String?): String {
        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_user_account_shared_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val editor = sharedPreferences.edit()
        editor.putString("temp_key", password)
        editor.apply()

        return sharedPreferences.getString("temp_key", "") ?: ""
    }

    fun decrypt(password: String): String {
        val masterKeyAlias =
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context, "secure_user_account_shared_prefs", masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences.getString("temp_key", password) ?: ""
    }

    companion object {
        private const val USER_ACCOUNT_PREFERENCES_NAME = " user_account_preferences"
        private val STRING_KEY_ID = stringPreferencesKey("id")
        private val STRING_KEY_PASSWORD = stringPreferencesKey("password")
        private val STRING_KEY_TOKEN = stringPreferencesKey("jwt_token")
    }
}