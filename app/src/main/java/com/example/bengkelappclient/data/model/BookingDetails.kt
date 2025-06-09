package com.example.bengkelappclient.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingDetails(
    val booking: Booking,
    val vehicle: Vehicle,
    val service: Service?
) : Parcelable