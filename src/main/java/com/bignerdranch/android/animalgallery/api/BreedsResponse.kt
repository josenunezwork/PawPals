package com.bignerdranch.android.animalgallery.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BreedsResponse (
    val breeds: List<Breed>
)