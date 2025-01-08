package com.thesis.arrivo.communication.available_products

import retrofit2.http.GET

interface AvailableProductsService {

    @GET("available-products")
    suspend fun getAllAvailableProducts(): List<AvailableProduct>

}