package com.thesis.arrivo.communication

data class ErrorResponse(
    val code: Int,
    val errors: List<String> = emptyList()
)