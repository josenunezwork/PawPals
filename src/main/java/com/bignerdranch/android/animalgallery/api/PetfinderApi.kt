package com.bignerdranch.android.animalgallery.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface PetfinderApi {

    @GET("v2/animals")
    suspend fun getAnimals(
        @Header("Authorization") authorization: String,
        @Query("type") type: String?,
        @Query("breed") breed: String?,
        @Query("size") size: String?,
        @Query("gender") gender: String?,
        @Query("age") age: String?,
        @Query("color") color: String?,
        @Query("coat") coat: String?,
        @Query("status") status: String?,
        @Query("name") name: String?,
        @Query("organization") organization: String?,
        @Query("good_with_children") good_with_children: Boolean?,
        @Query("good_with_dogs") good_with_dogs: Boolean?,
        @Query("good_with_cats") good_with_cats: Boolean?,
        @Query("house_trained") house_trained: Boolean?,
        @Query("declawed") declawed: Boolean?,
        @Query("special_needs") special_needs: Boolean?,
        @Query("location") location: String?,
        @Query("distance") distance: Int?,
        @Query("before") before: String?,
        @Query("after") after: String?,
        @Query("sort") sort: String?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
    ): AnimalResponse

    @GET("v2/types")
    suspend fun getAnimalTypes(
        @Header("Authorization") authorization: String
    ) : AnimalTypesResponse

    @GET("v2/types/{type}")
    suspend fun getTypeInfo(
        @Header("Authorization") authorization: String,
        @Path("type") typeName: String,
    ) : AnimalType

    @GET("v2/types/{type}/breeds")
    suspend fun getBreeds(
        @Header("Authorization") authorization: String,
        @Path("type") typeName: String,
    ) : BreedsResponse

    @GET("v2/organizations")
    suspend fun getOrganizations(
        @Header("Authorization") authorization: String,
        @Query("name") name: String?,
        @Query("location") location: String?,
        @Query("distance") distance: Int?,
        @Query("state") state: String?,
        @Query("country") country: String?,
        @Query("query") query: String?,
        @Query("sort") sort: String?,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?,
    ) : OrganizationResponse

    @GET("v2/organizations/{id}")
    suspend fun getOrganizationInfo(
        @Path("id") organizationId: String,
        @Header("Authorization") authorization: String
    ) : Organization

    @FormUrlEncoded
    @POST("v2/oauth2/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): TokenResponse
}