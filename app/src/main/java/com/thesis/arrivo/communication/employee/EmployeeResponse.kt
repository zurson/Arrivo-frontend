package com.thesis.arrivo.communication.employee

data class EmployeeResponse(
    val id: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    var status: EmployeeStatus,
)