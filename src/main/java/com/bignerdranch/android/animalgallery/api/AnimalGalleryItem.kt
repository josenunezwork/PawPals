package com.bignerdranch.android.animalgallery.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimalGalleryItem (
    val name: String,
    val id: String,
    val url: String,
)