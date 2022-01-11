package com.sielee.edvora.data.models

data class Product(
    val address: Address,
    val brand_name: String,
    val date: String,
    val discription: String,
    val image: String,
    val price: Int,
    val product_name: String,
    val time: String
)