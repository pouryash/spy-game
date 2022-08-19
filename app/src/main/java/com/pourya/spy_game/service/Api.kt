package com.pourya.spy_game.service

import com.pourya.spy_game.model.Category
import com.pourya.spy_game.model.Response
import com.pourya.spy_game.model.Word
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface Api {

    @GET("/v1/spy/categories")
    fun getCategories(): Observable<Response<ArrayList<Category>>>

    @GET("/v1/spy/categories/{id}")
    fun getRandomWordWithCategory(@Path("id") categoryId: Int): Observable<Response<Word>>

}