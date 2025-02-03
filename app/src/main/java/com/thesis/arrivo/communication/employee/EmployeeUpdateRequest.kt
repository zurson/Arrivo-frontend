package com.thesis.arrivo.communication.employee

data class EmployeeUpdateRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val status: EmployeeStatus
)