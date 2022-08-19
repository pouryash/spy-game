package com.pourya.spy_game.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Word(

    @Expose
    @SerializedName("id")
    val id: Int,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("image")
    val image: String,
) {
}