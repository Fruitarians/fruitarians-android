package com.althaaf.fruitarians.core.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.althaaf.fruitarians.core.data.local.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                accessToken = preferences[ACCESS_TOKEN_KEY] ?: "",
                tokenType = preferences[TOKEN_TYPE_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                name = preferences[NAME_KEY] ?: "",
                role = preferences[ROLE_KEY] ?: ""
            )
        }
    }

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = user.accessToken
            preferences[TOKEN_TYPE_KEY] = user.tokenType
            preferences[EMAIL_KEY] = user.email
            preferences[NAME_KEY] = user.name
            preferences[ROLE_KEY] = user.role
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")
        private val TOKEN_TYPE_KEY = stringPreferencesKey("tokenType")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("name")
        private val ROLE_KEY = stringPreferencesKey("role")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}