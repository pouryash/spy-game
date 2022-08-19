package com.pourya.spy_game.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.psnews.network.Status
import com.pourya.spy_game.R
import com.pourya.spy_game.databinding.ActivityCategoryBinding
import com.pourya.spy_game.databinding.ActivityMainBinding
import com.pourya.spy_game.util.Constants
import com.pourya.spy_game.util.SharedPreferenceManager
import com.pourya.spy_game.viewmodel.CategoryViewModel
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private val categoryViewModel: CategoryViewModel by inject(parameters = {
        parametersOf(
            this@CategoryActivity
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.categoryViewModel = categoryViewModel

        setupViews()
    }

    private fun setupViews() {
        categoryViewModel.getCategories()

        categoryViewModel.shouldCloseLiveData.observe(this){
            if (it){
                finish()
            }
        }

        categoryViewModel.categoryResponseLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { data ->
                        categoryViewModel.categoryLiveData.postValue(data.data)
                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        }
    }

    override fun onBackPressed() {
        categoryViewModel.sharedPreferenceManager.clearKey(Constants.KEY_CATEGORY_MODEL)
        super.onBackPressed()
    }
}