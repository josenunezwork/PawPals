package com.bignerdranch.android.animalgallery.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Breed (
    val name: String,
)