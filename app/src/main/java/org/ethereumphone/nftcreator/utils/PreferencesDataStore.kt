package org.ethereumphone.nftcreator.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class UserStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("onboarding")
        private val USER_ONBOARDING = booleanPreferencesKey("onboarding_complete")
    }

    val getUserOnboarding: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[USER_ONBOARDING] ?: false
    }

    suspend fun saveUserOnboarding(complete: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_ONBOARDING] = complete
        }
    }
}