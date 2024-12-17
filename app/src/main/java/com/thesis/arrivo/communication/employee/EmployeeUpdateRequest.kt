package com.thesis.arrivo.communication.employee

data class EmployeeUpdateRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val status: EmployeeStatus
)