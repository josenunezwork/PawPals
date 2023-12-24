package com.bignerdranch.android.animalgallery.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimalTypesResponse (
    @Json(name = "types")
    val animalTypes: List<AnimalType>,
)