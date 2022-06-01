package com.elevationsoft.passlocker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Long,
    val categoryName: String,
    val position: Int
) : Parcelable
