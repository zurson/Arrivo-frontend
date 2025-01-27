package com.thesis.arrivo.utilities.interfaces

import com.thesis.arrivo.communication.employee.Employee

interface LoggedInUserAccessor {

    fun getLoggedInUser(): Employee
    fun isAdmin(): Boolean

}