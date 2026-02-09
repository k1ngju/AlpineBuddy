package com.example.alpinebuddy.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("AlpineBuddyPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
    }

    /**
     * Shrani JWT žeton v SharedPreferences.
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Pridobi shranjen JWT žeton.
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    /**
     * Izbriše žeton (odjava).
     */
    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }
}
