package com.groceryapplication.network

import com.groceryapplication.model.ProductResponseModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("v2/5def7b172f000063008e0aa2")
    suspend fun getProductList(): Response<ProductResponseModel>
}