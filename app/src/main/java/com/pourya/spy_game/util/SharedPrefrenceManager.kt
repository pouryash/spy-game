package com.pourya.spy_game.util

import android.content.Context
import android.content.SharedPreferences
import com.pourya.spy_game.model.Category
import org.json.JSONObject

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

    fun saveCategory(category: Category) {

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val jsonObject = JSONObject()

        jsonObject.put("id", category.id)
        jsonObject.put("name", category.name)
        jsonObject.put("icon", category.image)

        editor.putString(Constants.KEY_CATEGORY_MODEL, jsonObject.toString())
        editor.apply()
    }

    fun getCategory(): Category {
        val category = Category()
        val jsonModel = sharedPreferences.getString(Constants.KEY_CATEGORY_MODEL, null)

        if (!jsonModel.isNullOrEmpty()) {
            val jsonObject = JSONObject(jsonModel)
            category.id = jsonObject.getInt("id")
            category.name = jsonObject.getString("name")
            category.image = jsonObject.getString("icon")
        }

        return category
    }

}