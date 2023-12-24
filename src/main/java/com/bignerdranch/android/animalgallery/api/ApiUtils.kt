package com.bignerdranch.android.animalgallery.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Hours(
    val monday: String?,
    val tuesday: String?,
    val wednesday: String?,
    val thursday: String?,
    val friday: String?,
    val saturday: String?,
    val sunday: String?,
)
@JsonClass(generateAdapter = true)
data class SocialMedia(
    val facebook: String?,
    val twitter: String?,
    val youtube: String?,
    val instagram: String?,
    val pinterest: String?,
)

@JsonClass(generateAdapter = true)
data class Self(
    val href: String?,
)

@JsonClass(generateAdapter = true)
data class Type(
    val href: String?,
)

@JsonClass(generateAdapter = true)
data class OrganizationLink(
    val href: String?,
)

@JsonClass(generateAdapter = true)
data class Links(
    val self: Self?,
    val type: Type?,
    val organization: OrganizationLink?,
)

@JsonClass(generateAdapter = true)
data class Address(
    val address1: String?,
    val address2: String?,
    val city: String?,
    val state: String?,
    val postcode: String?,
    val country: String?,
)


@JsonClass(generateAdapter = true)
data class Photo(
    val small: String?,
    val medium: String?,
    val large: String?,
    val full: String?,
)

@JsonClass(generateAdapter = true)
data class Video(
    val embed: String?,
)

@JsonClass(generateAdapter = true)
data class Contact(
    val email: String?,
    val phone: String?,
    val address: Address?,
)