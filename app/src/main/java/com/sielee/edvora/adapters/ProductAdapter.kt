package com.sielee.edvora.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sielee.edvora.data.models.Product
import com.sielee.edvora.databinding.ProductItemBinding
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class ProductAdapter:ListAdapter<Product,ProductAdapter.ProductViewHolder>(DiffItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(private val binding: ProductItemBinding):RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(product: Product?) {
            val formatter = DateTimeFormatter.ofPattern("MM:dd:yyyy")
            val zoneDate:ZonedDateTime = ZonedDateTime.parse(product?.date)
            binding.tvProductName.text = product?.product_name
            binding.tvBrandName.text = product?.brand_name
            binding.tvProductCost.text = product?.price.toString()
            binding.tvProductDescription.text = product?.discription
            binding.tvProductDate.text = zoneDate.format(formatter)

            binding.tvProductLocation.text = "${ product?.address?.city }, ${ product?.address?.state }"
            Glide.with(binding.root.context).load(product?.image).into(binding.imgProduct)
        }

    }

    class DiffItemCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.product_name == newItem.product_name
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }

    }
}