package com.bignerdranch.android.animalgallery.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Organization (
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: Address,
    val hours: Hours?,
    val url: String,
    val website: String?,
    val mission_statement: String?,
    val adoption: AdoptionPolicy?,
    val social_media: SocialMedia?,
    val photo: Photo,
    val distance: Int?
)
@JsonClass(generateAdapter = true)
data class AdoptionPolicy(
    val policy: String?,
    val url: String?,
)
