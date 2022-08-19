package com.pourya.spy_game.service

import com.example.psnews.network.Status
import com.pourya.spy_game.model.Response


class ApiResponse<T> private constructor(status: Status, data: Response<T>?, error: String?) {

    var status: Status

    var data: Response<T>? = null

    var message: String? = null

    init {
        this.message = error
        this.data = data
        this.status = status
    }
    companion object{
        fun<T> loading(): ApiResponse<T> {
            return ApiResponse(Status.LOADING, null, null)
        }

        fun <T> success(data: Response<T>): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS, data, null)
        }

        fun<T> error(error: String): ApiResponse<T> {
            return ApiResponse(Status.ERROR, null ,error)
        }
    }
}