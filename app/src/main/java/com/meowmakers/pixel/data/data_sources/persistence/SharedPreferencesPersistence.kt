package com.meowmakers.pixel.data.data_sources.persistence

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val sharedPreferencesKey = "id_list"

class SharedPreferencesPersistence(val sharedPreferences: SharedPreferences) : Persistence {

    private val favorites = mutableListOf<String>()
    private val _idFlow = MutableStateFlow<List<String>>(emptyList())

    init {
        val savedString = sharedPreferences.getString(sharedPreferencesKey, "") ?: ""
        val idList = if (savedString.isNotEmpty()) savedString.split(",") else emptyList()
        favorites.addAll(idList)
        _idFlow.value = favorites.toList()
    }

    override suspend fun addFavorite(id: String) {
        if (!favorites.contains(id)) {
            favorites.add(id)
            persistChanges()
        }
    }

    override suspend fun removeFavorite(id: String) {
        if (favorites.remove(id)) {
            persistChanges()
        }
    }

    private fun persistChanges() {
        sharedPreferences.edit {
            putString(sharedPreferencesKey, favorites.joinToString(separator = ","))
        }
        _idFlow.value = favorites.toList()
    }

    override fun observeFavorites(): Flow<List<String>> = _idFlow.asStateFlow()
}