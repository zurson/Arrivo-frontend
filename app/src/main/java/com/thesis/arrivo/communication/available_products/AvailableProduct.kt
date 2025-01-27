package com.thesis.arrivo.communication.available_products

data class AvailableProduct(
    val name: String
) {
    companion object {
        fun emptyProduct(): AvailableProduct = AvailableProduct("")
    }
}