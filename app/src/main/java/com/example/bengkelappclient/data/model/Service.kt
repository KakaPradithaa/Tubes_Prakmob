// data/model/Service.kt
package com.example.bengkelappclient.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Service(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val img: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    var isSelected: Boolean = false
) : Parcelable
