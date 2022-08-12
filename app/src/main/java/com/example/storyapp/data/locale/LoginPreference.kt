package com.example.storyapp.data.locale

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.storyapp.data.entity.UserModel
import kotlinx.coroutines.flow.map

class LoginPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val NAME = stringPreferencesKey("name")
    private val EMAIL = stringPreferencesKey("email")
    private val PASSWORD = stringPreferencesKey("password")
    private val IS_LOGIN = booleanPreferencesKey("is_login")
    private val TOKEN = stringPreferencesKey("token")

    fun getUser() = dataStore.data.map {
        UserModel(
            it[NAME] ?: "",
            it[EMAIL] ?: "",
            it[PASSWORD] ?: "",
            it[IS_LOGIN] ?: false,
            it[TOKEN] ?: ""
        )
    }

    suspend fun login(user: UserModel) {
        dataStore.edit {
            it[NAME] = user.name
            it[EMAIL] = user.email
            it[PASSWORD] = user.password
            it[IS_LOGIN] = user.isLogin
            it[TOKEN] = user.token
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it[IS_LOGIN] = false
            it[TOKEN] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}