package com.thesis.arrivo.communication.available_products

import com.thesis.arrivo.communication.RetrofitInstance

class AvailableProductsRepository {

    private val availableProductsRepository = RetrofitInstance.availableProductsService

    suspend fun getAllAvailableProducts(): List<AvailableProduct> {
        return availableProductsRepository.getAllAvailableProducts()
    }

}