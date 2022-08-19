package com.pourya.spy_game.repository

import com.pourya.spy_game.model.Category
import com.pourya.spy_game.model.Response
import com.pourya.spy_game.service.Api
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import rx.Observable

class CategoryRepository : KoinComponent {

    private val retrofit: Api by inject()

    fun getCategories(): Observable<Response<ArrayList<Category>>> {
        return retrofit.getCategories()
    }
}