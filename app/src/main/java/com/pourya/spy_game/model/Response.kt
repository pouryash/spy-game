package com.pourya.spy_game.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Response<T>(

    @Expose
    @SerializedName("data")
    val data: T,

    @Expose
    @SerializedName("message")
    val message: String,

    @Expose
    @SerializedName("status")
    val status: Boolean

) {
}