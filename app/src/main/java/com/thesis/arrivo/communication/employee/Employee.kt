package com.thesis.arrivo.communication.employee

import com.thesis.arrivo.utilities.location.Location

data class Employee(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val status: EmployeeStatus,
    val role: Role,
    val company: Company
) {

    companion object {
        fun emptyEmployee(): Employee {
            return Employee(
                id = -1,
                firstName = "",
                lastName = "",
                email = "",
                phoneNumber = "",
                status = EmployeeStatus.DELETED,
                role = Role.USER,
                company = Company(
                    id = -1,
                    location = Location(),
                    name = "",
                    phoneNumber = ""
                )
            )
        }
    }
}