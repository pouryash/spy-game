package com.pourya.spy_game.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.pourya.spy_game.R
import com.pourya.spy_game.databinding.CategoryRowBinding
import com.pourya.spy_game.model.Category
import com.pourya.spy_game.viewmodel.CategoryViewModel

class CategoryAdapter(val categoryList: ArrayList<Category>, val context: Context, val onCategoryClicked : (category: Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryVH>() {

    lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        layoutInflater = LayoutInflater.from(context)
        val newsRowBinding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.category_row, parent, false)

        return CategoryVH(newsRowBinding, onCategoryClicked)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val viewModel = CategoryViewModel(categoryList[position], context)
        holder.bindNews(viewModel)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class CategoryVH(itemView: ViewDataBinding, val onCategoryClicked : (category: Category) -> Unit ) : RecyclerView.ViewHolder(itemView.root) {

        var binding: CategoryRowBinding = (itemView as CategoryRowBinding)

        fun bindNews(viewModel: CategoryViewModel) {
            binding.categoryViewModel = viewModel
            //TODO remove comment when add multiple selection
//            binding.root.setOnClickListener {
//                binding.checkboxCategory.isChecked = !binding.checkboxCategory.isChecked
//            }
            binding.root.setOnClickListener {
                onCategoryClicked(categoryList[adapterPosition])
            }
            binding.executePendingBindings()
        }
    }

    public fun insertCategory(category: Category) {
        categoryList.add(0, category)
        notifyItemInserted(0)
    }
}
