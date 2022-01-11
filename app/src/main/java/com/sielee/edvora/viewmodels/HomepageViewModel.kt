package com.sielee.edvora.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sielee.edvora.data.api.ProductApi
import com.sielee.edvora.data.models.Product
import kotlinx.coroutines.launch

class HomepageViewModel:ViewModel() {
    private val _productsList = MutableLiveData<List<Product>>()
    val productsList:LiveData<List<Product>>
    get() = _productsList

    init {
        getProducts()
    }

    private fun getProducts(){
        viewModelScope.launch {
            _productsList.value = ProductApi.apiService.getProducts()
        }

    }
}