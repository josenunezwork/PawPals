package com.bignerdranch.android.animalgallery.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimalType (
    val name: String,
    val coats: List<String>?,
    val colors: List<String>?,
    val genders: List<String>?,
)
