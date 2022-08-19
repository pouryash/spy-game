package com.pourya.spy_game.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pourya.spy_game.model.Category
import com.pourya.spy_game.model.Word
import com.pourya.spy_game.repository.WordRepository
import com.pourya.spy_game.service.ApiResponse
import com.pourya.spy_game.util.ErrorHandler.handleThrowable
import com.pourya.spy_game.util.SharedPreferenceManager
import com.pourya.spy_game.util.ViewModelObservable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class WordViewModel(val context: Context) : ViewModelObservable(), KoinComponent {

    private val repository: WordRepository by inject()
    val sharedPreferenceManager: SharedPreferenceManager by inject()
    val wordResponseLiveData = MutableLiveData<ApiResponse<Word>>()

    var id: Int? = null

    var name: String? = null
        set(value) {
            field = value
        }

    var icon: String? = null
        set(value) {
            field = value
        }

    constructor(user: Category, context: Context) : this(context) {
        this.name = user.name
        this.icon = user.image
        this.id = user.id
    }

    fun getRandomWordWithCat(catId: Int) {

        repository.getRandomWordWithCat(catId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { wordResponseLiveData.postValue(ApiResponse.loading()) }
            .subscribe(
                { response ->
                    wordResponseLiveData.postValue(
                        ApiResponse.success(response)
                    )
                },
                { error ->

                    wordResponseLiveData.postValue(
                        ApiResponse.error(
                            handleThrowable(error, context)
                        )
                    )
                })
    }
}