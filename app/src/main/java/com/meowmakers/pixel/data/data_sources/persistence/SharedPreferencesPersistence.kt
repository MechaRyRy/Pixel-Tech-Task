package com.meowmakers.pixel.data.data_sources.persistence

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

private const val sharedPreferencesKey = "id_list"

class SharedPreferencesPersistence(val sharedPreferences: SharedPreferences) : Persistence {

    val favorites: MutableList<String> = mutableListOf()
    val _idFlow = MutableStateFlow<List<String>>(favorites)

    init {
        val savedString = sharedPreferences.getString("id_list", "") ?: ""
        val idList = if (savedString.isNotEmpty()) savedString.split(",") else emptyList()
        favorites.addAll(idList)
    }

    override suspend fun addFavorite(id: String) {
        favorites.add(id)
        persistChanges()
    }

    override suspend fun removeFavorite(id: String) {
        favorites.remove(id)
        persistChanges()
    }

    private suspend fun persistChanges() {
        sharedPreferences.edit {
            putString(sharedPreferencesKey, favorites.joinToString(separator = ","))
        }
        _idFlow.emit(favorites)
    }

    override fun observeFavorites(): Flow<List<String>> = _idFlow.asSharedFlow()
}