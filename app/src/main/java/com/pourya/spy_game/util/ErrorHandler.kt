package com.pourya.spy_game.util

import android.content.Context
import com.pourya.spy_game.App
import com.pourya.spy_game.R
import org.json.JSONObject
import retrofit2.adapter.rxjava.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ErrorHandler {
    fun handleThrowable(e: Throwable?, context: Context): String {
        return if (e is IOException) {
            context.getString(R.string.user_not_connected_to_internet)
        } else if (e is HttpException) {
            e as HttpException
            try {
                return JSONObject(e.response()!!.errorBody()!!.string()).getString("error_msg")
            } catch (e: Exception) {
                return context.getString(R.string.connection_error)
            }
        } else if (e is SocketTimeoutException) {
            context.getString(R.string.user_not_connected_to_internet)
        } else {
            context.getString(R.string.connection_error)
        }
    }
}