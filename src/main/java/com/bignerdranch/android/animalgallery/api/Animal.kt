package com.bignerdranch.android.animalgallery.api

import android.net.Uri
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
// !!IMPORTANT: For some reason, the PetFinder api does not have a standardized response. As such,
//              most of these fields need to be nullable. This means that we need to do a bunch of
//              null-safety checks when operating on data that we get from the api.

data class Animal (
        val id: Int,
        val organization_id: String?,
        val url: String,
        val species: String?,
        val breeds: Breeds?,
        val colors: Colors?,
        val age: String?,
        val gender: String?,
        val size: String?,
        val coat: String?,
        val attributes: Attributes?,
        val environment: Environment?,
        val tags: List<String>?,
        val name: String?,
        val description: String?,
        val photos: List<Photo>?,
        val videos: List<Video>?,
        val status: String?,
        val published_at: String?,
        val contact: Contact?,
        val _links: Links?,
) {
    val animalPageUri: Uri
        get() = Uri.parse(url)
}

@JsonClass(generateAdapter = true)
data class Breeds(
    val primary: String?,
    val secondary: String?,
    val mixed: Boolean?,
    val unknown: Boolean?,
)

@JsonClass(generateAdapter = true)
data class Colors(
    val primary: String?,
    val secondary: String?,
    val tertiary: String?,
)

@JsonClass(generateAdapter = true)
data class Attributes(
    val spayed_neutered: Boolean?,
    val house_trained: Boolean?,
    val declawed: Boolean?,
    val special_needs: Boolean?,
    val shots_current: Boolean?,
)

@JsonClass(generateAdapter = true)
data class Environment(
    val children: Boolean?,
    val dogs: Boolean?,
    val cats: Boolean?,
)




