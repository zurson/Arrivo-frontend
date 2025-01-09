package com.thesis.arrivo.communication.employee

data class Employee(
    val id: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    var status: EmployeeStatus,
) {

    companion object {
        fun emptyEmployee(): Employee {
            return Employee(
                id = -1,
                firstName = "",
                lastName = "",
                email = "",
                phoneNumber = "",
                status = EmployeeStatus.DELETED
            )
        }
    }
}