package com.groceryapplication

import com.groceryapplication.model.ProductResponseModel
import com.groceryapplication.network.RetrofitClient
import retrofit2.Response

class Repository {

    suspend fun getProductList(): Response<ProductResponseModel> {
        return RetrofitClient().getapi().getProductList()
    }
}