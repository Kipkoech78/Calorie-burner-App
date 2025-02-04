package com.fitnessapp.data.repository.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fitnessapp.domain.manager.LocalUserManager
import com.fitnessapp.utils.Constants
import com.fitnessapp.utils.Constants.GENDER
import com.fitnessapp.utils.Constants.USER_SETTINGS
import com.fitnessapp.utils.Constants.U_PREF
import com.fitnessapp.utils.Constants.WEIGHT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserManagerImpl(private val context: Context) : LocalUserManager {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(U_PREF, Context.MODE_PRIVATE)
    override suspend fun saveAppEntry() {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.APP_ENTRY] = true
        }
    }
    override fun readAppEntry(): Flow<Boolean> {
        return context.dataStore.data.map {  preferences ->
            preferences[PreferenceKeys.APP_ENTRY]?: false
        }
    }

    override suspend fun saveGender(gender: String) {
            context.dataStore.edit { preferences ->
                preferences[PreferenceKeys.U_GENDER] = gender
            }

    }

    override fun readGender(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.U_GENDER]
        }
    }

    override suspend fun saveWeight(weight: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.U_WEIGHT] = weight
        }
    }

    override fun readWeight(): Flow<Float?> {
        return context.dataStore.data.map { prev ->
            prev[PreferenceKeys.U_WEIGHT]
        }
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_SETTINGS)
//saving user preference key
private object PreferenceKeys{
    val APP_ENTRY = booleanPreferencesKey(name = Constants.APP_ENTRY)
    val U_GENDER = stringPreferencesKey(name = GENDER)
    val U_WEIGHT = floatPreferencesKey(name = WEIGHT)
}