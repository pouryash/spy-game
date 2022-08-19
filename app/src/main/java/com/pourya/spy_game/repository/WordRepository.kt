package com.pourya.spy_game.repository

import com.pourya.spy_game.model.Category
import com.pourya.spy_game.model.Response
import com.pourya.spy_game.model.Word
import com.pourya.spy_game.service.Api
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import rx.Observable

class WordRepository : KoinComponent {

    private val retrofit: Api by inject()

    fun getRandomWordWithCat(catId: Int): Observable<Response<Word>> {
        return retrofit.getRandomWordWithCategory(catId)
    }
}