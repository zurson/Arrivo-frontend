package com.thesis.arrivo.communication.employee

import com.thesis.arrivo.utilities.location.Location

data class Company(
    val id: Long,
    val location: Location,
    val name: String,
    val phoneNumber: String
)
