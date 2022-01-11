package com.sielee.edvora.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sielee.edvora.data.models.Product
import com.sielee.edvora.data.models.ProductCategory
import com.sielee.edvora.databinding.ProductCategoryItemBinding

class ProductCategoryAdapter:androidx.recyclerview.widget.ListAdapter<ProductCategory,ProductCategoryAdapter.CategoryViewHolder>(DiffCallback()) {
    class CategoryViewHolder(private val binding: ProductCategoryItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(category: ProductCategory?) {
            binding.tvProductCategory.text = category?.productCategory
            getData(binding.rvCategoryProducts,category?.products)
            binding.rvCategoryProducts.addItemDecoration(ItemScrollIndicator())

        }
        private fun getData(recyclerView: RecyclerView, products: List<Product>?) {
            val adapter = ProductAdapter()
            recyclerView.adapter = adapter
            products.let {
                adapter.submitList(it)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ProductCategoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    class DiffCallback:DiffUtil.ItemCallback<ProductCategory>() {
        override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean {
           return oldItem.productCategory == newItem.productCategory
        }

        override fun areContentsTheSame(
            oldItem: ProductCategory,
            newItem: ProductCategory
        ): Boolean {
            return oldItem == newItem
        }

    }
}