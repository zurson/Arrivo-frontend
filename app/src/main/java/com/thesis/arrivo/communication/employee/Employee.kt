package com.thesis.arrivo.communication.employee

data class Employee(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val status: EmployeeStatus,
    val role: Role
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
                role = Role.USER
            )
        }
    }
}