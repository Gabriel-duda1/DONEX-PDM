package com.example.donex.model

import com.google.android.gms.maps.model.LatLng

data class DonationPoint(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)