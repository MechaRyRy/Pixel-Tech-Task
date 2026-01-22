package com.meowmakers.pixel.data.data_sources.persistence

import android.content.SharedPreferences

private const val sharedPreferencesKey = "id_list"

class SharedPreferencesPersistence(val sharedPreferences: SharedPreferences) : Persistence {

    val favorites: MutableList<String> = mutableListOf()

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

    private fun persistChanges() {
        sharedPreferences.edit()
            .putString(sharedPreferencesKey, favorites.joinToString(separator = ","))
            .apply()
    }

    override fun getFavorites(): List<String> {
        return favorites
    }
}