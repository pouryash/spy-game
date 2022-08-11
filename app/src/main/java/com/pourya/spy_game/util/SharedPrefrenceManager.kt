package com.pourya.spy_game.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.SPY_SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun saveString(key : String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key : String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun saveInteger(key : String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInteger(key : String): Int {
        return sharedPreferences.getInt(key, 0)
    }
}