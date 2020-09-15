package com.fadilahone.githubuserapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val username: String?,
    val name: String?,
    val image :Int?,
    val location: String?,
    val repository: String?,
    val company: String?,
    val followers: String?,
    val following: String?
) :Parcelable