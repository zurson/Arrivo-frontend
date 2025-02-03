package com.thesis.arrivo.communication.employee

data class EmployeeCreateAccountRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val companyId: Long
)