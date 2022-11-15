package com.example.dela.ui.model.category

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Category(
    val id: Long = 0,
    val name: String,
    val color: Int
): Parcelable
