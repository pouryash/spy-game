package com.pourya.spy_game.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Category(

    @Expose
    @SerializedName("id")
    var id: Int,

    @Expose
    @SerializedName("name")
    var name: String,

    @Expose
    @SerializedName("image")
    var image: String,
) {
    constructor() : this(0, "", "")
}