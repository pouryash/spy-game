package com.pourya.spy_game.viewmodel

import android.content.Context
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pourya.spy_game.adaptor.CategoryAdapter
import com.pourya.spy_game.model.Category
import com.pourya.spy_game.repository.CategoryRepository
import com.pourya.spy_game.service.ApiResponse
import com.pourya.spy_game.util.ErrorHandler.handleThrowable
import com.pourya.spy_game.util.SharedPreferenceManager
import com.pourya.spy_game.util.ViewModelObservable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class CategoryViewModel(val context: Context) : ViewModelObservable(), KoinComponent {

    private val repository: CategoryRepository by inject()
    val sharedPreferenceManager: SharedPreferenceManager by inject()
    val categoryResponseLiveData = MutableLiveData<ApiResponse<ArrayList<Category>>>()
    val categoryLiveData = MutableLiveData<ArrayList<Category>>()
    val shouldCloseLiveData: MutableLiveData<Boolean> = MutableLiveData()

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

    companion object {

        @JvmStatic
        @BindingAdapter("bind:context", "bind:sharedPreferenceManager", "bind:shouldCloseLiveData", "bind:categoryList")
        fun getRecyclerBinder(
            recyclerView: RecyclerView,
            context: Context,
            sharedPreferenceManager: SharedPreferenceManager,
            shouldCloseLiveData:  MutableLiveData<Boolean>,
            categoryList: MutableLiveData<ArrayList<Category>>
        ) {
            val categories = ArrayList<Category>()
            val commentAdapter = CategoryAdapter(categories, context) {
                sharedPreferenceManager.saveCategory(it)
                shouldCloseLiveData.postValue(true)
            }
            val layoutManager =
                GridLayoutManager(recyclerView.context, 3, GridLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = commentAdapter
            recyclerView.setHasFixedSize(true)

            categoryList.observe(recyclerView.context as LifecycleOwner, Observer {
                categories.clear()
                categories.addAll(it)
                commentAdapter.notifyDataSetChanged()
            })


        }
    }

    fun getCategories() {

        repository.getCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { categoryResponseLiveData.postValue(ApiResponse.loading()) }
            .subscribe(
                { response ->
                    categoryResponseLiveData.postValue(
                        ApiResponse.success(response)
                    )
                },
                { error ->

                    categoryResponseLiveData.postValue(
                        ApiResponse.error(
                            handleThrowable(error, context)
                        )
                    )
                })
    }
}