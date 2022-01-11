package com.sielee.edvora.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sielee.edvora.R
import com.sielee.edvora.adapters.ProductCategoryAdapter
import com.sielee.edvora.data.models.Product
import com.sielee.edvora.data.models.ProductCategory
import com.sielee.edvora.databinding.FragmentHomepageBinding
import com.sielee.edvora.viewmodels.HomepageViewModel
import java.util.*

class Homepage : Fragment() {

    lateinit var binding: FragmentHomepageBinding
    lateinit var viewModel: HomepageViewModel
    lateinit var adapter:ProductCategoryAdapter
    private val TAG = "HomePage"
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomepageBinding.inflate(inflater,container,false)
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductCategoryAdapter()
        binding.rvProducts.adapter = adapter

        sharedPreferences = requireActivity().getSharedPreferences("preference",Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(this).get(HomepageViewModel::class.java)
        viewModel.productsList.observe(viewLifecycleOwner,{productList ->
            Log.d(TAG, "$productList")
            val productsList = productList.groupBy(Product::product_name).entries.map {
                ProductCategory(it.key,it.value)
            }
            Log.d(TAG, "$productsList")
            if (adapter.currentList.isEmpty()){
                adapter.submitList(productsList)
            }
            if (adapter.currentList.isNotEmpty()) binding.pbLoading.visibility = View.GONE
        })
        binding.btnFilter.setOnClickListener {
            showFilterDialog()

        }
        binding.btnClearFilters.setOnClickListener {
            viewModel.productsList.observe(viewLifecycleOwner,{productList ->
                Log.d(TAG, "$productList")
                val productsList = productList.groupBy(Product::product_name).entries.map {
                    ProductCategory(it.key,it.value)
                }
                Log.d(TAG, "$productsList")

                adapter.submitList(productsList)
                if (adapter.currentList.isNotEmpty()) binding.pbLoading.visibility = View.GONE
            })
        }


        return binding.root
    }


    private fun showFilterDialog(){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.filter_dialog,null)

        val filterDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
       val alertDialog =  filterDialog.show()

        viewModel.productsList.observe(viewLifecycleOwner,{products->
           val productArray= mutableListOf<String>()
           val statesArray= mutableListOf<String>()
           val citiesArray= mutableListOf<String>()

           for (product in products){
               productArray.add(product.product_name)
           }
        val adapterProducts = ArrayAdapter(requireContext(),R.layout.list_item, productArray.distinct())
        val aProducts = dialogView.findViewById<AutoCompleteTextView>(R.id.aedProducts)
        val aCities =  dialogView.findViewById<AutoCompleteTextView>(R.id.aedCities)
        val aStates = dialogView.findViewById<AutoCompleteTextView>(R.id.aedStates)
           val btnApply = dialogView.findViewById<Button>(R.id.applyFilter)
            aProducts.setAdapter(adapterProducts)


           aProducts.setOnItemClickListener { parent, view, position, id ->
               val selectedProduct = parent.getItemAtPosition(position).toString()
               //set state based on selected product

                   for (prod in products) {
                       if (aProducts.text.isNotEmpty()) {
                       if (prod.product_name.contains(selectedProduct)) {
                           statesArray.add(prod.address.state)
                       }
                   }else{
                           statesArray.add(prod.address.state)
                       }
               }
               val adapterStates = ArrayAdapter(requireContext(),R.layout.list_item, statesArray.distinct())
               aStates.setAdapter(adapterStates)
           }
           aStates.setOnItemClickListener { parent, view, position, id ->
               val selectedState = parent.getItemAtPosition(position).toString()
               //set cities based on selected state

               for (prod in products){
                   if (aStates.text.isNotEmpty()) {
                       if (prod.address.state.contains(selectedState)) {
                           citiesArray.add(prod.address.city)
                       }
                   }else{
                       citiesArray.add(prod.address.city)
                   }
               }
               val adapterCities = ArrayAdapter(requireContext(),R.layout.list_item, citiesArray.distinct())
               aCities.setAdapter(adapterCities)


           }

            //apply filter button
         btnApply.setOnClickListener {
             alertDialog.dismiss()
             Toast.makeText(requireContext(),"${aProducts.text}, ${aStates.text}, ${aCities.text}",Toast.LENGTH_SHORT).show()
             filter(aProducts.text.toString(), aStates.text.toString(), aCities.text.toString())
         }
       })
    }
    private fun filter(productName: String?,productState: String?, productCity: String?) {
        viewModel.productsList.observe(viewLifecycleOwner,{productList ->
            val productsList = productList.groupBy(Product::product_name).entries.map {
                ProductCategory(it.key,it.value)
            }
            Log.d(TAG, "UnfilteredList size: ${productsList.size} ")
            val filteredList = mutableListOf<ProductCategory>()

            for (category in productsList) {
                for (product in category.products!!) {
                    if (category.productCategory.lowercase(Locale.getDefault())
                            .contains(productName!!.lowercase(Locale.getDefault())) ||
                        product.product_name.lowercase(Locale.getDefault())
                            .contains(productName.lowercase(Locale.getDefault())) ||
                        product.address.state.lowercase(Locale.getDefault())
                            .contains(productState!!.lowercase(Locale.getDefault())) ||
                        product.address.city.lowercase(Locale.getDefault())
                            .contains(productCity!!.lowercase(Locale.getDefault()))
                    ) {
                        filteredList.add(category)
                        Log.d(TAG, "FilteredList size::: ${filteredList.size}")
                    }
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(requireContext(), "No match found", Toast.LENGTH_SHORT).show()
                adapter.submitList(filteredList.distinct())
                Log.d(TAG, "FilteredCategories: ${filteredList.size}")
            } else {
                adapter.submitList(filteredList.distinct())
                Log.d(TAG, "Filtered List: ${adapter.currentList.size}")
            }
        })

    }


}