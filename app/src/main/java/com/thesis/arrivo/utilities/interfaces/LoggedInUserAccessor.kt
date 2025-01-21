package com.thesis.arrivo.utilities.interfaces

import com.thesis.arrivo.communication.employee.Employee

interface LoggedInUserAccessor {

    fun getLoggedInUserDetails(): Employee
    fun isAdmin(): Boolean

}