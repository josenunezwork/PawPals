package com.bignerdranch.android.animalgallery

import android.util.Log
import com.bignerdranch.android.animalgallery.api.ApiKeys
import com.bignerdranch.android.animalgallery.api.PetfinderApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class PetRepository {

    private val petFinderApi: PetfinderApi

    //creating an interceptor to log stuff.
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("OkHttpLogging", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //more logging things
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    init {
        //create retrofit
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.petfinder.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        petFinderApi = retrofit.create(PetfinderApi::class.java)
    }

    suspend fun fetchToken(): String =
        petFinderApi
            .getToken("client_credentials", ApiKeys.API_KEY, ApiKeys.API_SECRET)
            .access_token

/**
 * Retrieves a list of animals from the Petfinder API based on the specified criteria.
 *
 * @param token The authorization header with the access token.
 * @param type The type of animals to retrieve (e.g., "dog", "cat").
 * Possible values may be looked via fetchTypes()
 * @param breed The breed of animals to retrieve. Accepts multiple values
 * (e.g., breed=pug,samoyed). Possible values may be looked up via fetchBreeds()
 * @param size The size of animals to retrieve (e.g., "small", "medium").
 * Possible values: small, medium, large, xlarge. Accepts multiple values.
 * (e.g., size=large,xlarge).
 * @param gender The gender of animals to retrieve (e.g., "male", "female").
 * Possible values: male, female, unknown. Accepts multiple values.
 * @param age The age of animals to retrieve (e.g., "baby", "adult").
 * Possible values: baby, young, adult, senior. Accepts multiple values.
 * @param color The color of animals to retrieve.
 * @param coat The coat of animals to retrieve (e.g., "short", "medium").
 * Possible values: short, medium, long, wire, hairless, curly. Accepts multiple values.
 * @param status The adoption status of animals to retrieve (e.g., "adoptable").
 * @param name The name of animals to retrieve.
 * @param organization The ID of the organization associated with the animals.
 * @param kidFriendly Whether the animals are good with children.
 * @param dogFriendly Whether the animals are good with dogs.
 * @param catFriendly Whether the animals are good with cats.
 * @param houseTrained Whether the animals are house trained.
 * @param declawed Whether the animals are declawed.
 * @param specialNeeds Whether the animals have special needs.
 * @param location The location to filter the results by.
 * @param distance The distance from the specified location.
 * @param before Retrieve results published before this date/time.
 * @param after Retrieve results published after this date/time.
 * @param sort The attribute to sort the results by.
 * @param page The page of results to return.
 * @param limit The maximum number of results per page.
 */
    //TODO: This is awful. Maybe look in to creating a filter options map?
    suspend fun fetchAnimals(token: String,
                             type: String? = null,
                             breed: String? = null,
                             size: String? = null,
                             gender: String? = null,
                             age: String? = null,
                             color: String? = null,
                             coat: String? = null,
                             status: String? = null,
                             name: String? = null,
                             organization: String? = null,
                             kidFriendly: Boolean? = null,
                             dogFriendly: Boolean? = null,
                             catFriendly: Boolean? = null,
                             houseTrained: Boolean? = null,
                             declawed: Boolean? = null,
                             specialNeeds: Boolean? = null,
                             location: String? = null,
                             distance: Int? = null,
                             before: String? = null,
                             after: String? = null,
                             sort: String? = null,
                             page: Int? = null,
                             limit: Int? = null) =
        petFinderApi.getAnimals(" Bearer $token", type, breed, size, gender, age,
            color, coat, status, name, organization, kidFriendly, dogFriendly, catFriendly,
            houseTrained, declawed, specialNeeds, location, distance, before, after, sort,
            page, limit).animals

    suspend fun fetchTypes(token: String): MutableList<String> {
        //gets the animalTypes data objects
        val animalTypes = petFinderApi.getAnimalTypes(" Bearer $token").animalTypes
        val types = mutableListOf<String>()
        //gets all the "name" property (which is animal type) of all animalTypes objects
        for(i in animalTypes){
            types.add(i.name)
        }
        return types
    }

    suspend fun fetchBreeds(token: String, typeName:String): MutableList<String>{
        val animalBreeds = petFinderApi.getBreeds(" Bearer $token", typeName).breeds
        val breeds = mutableListOf<String>()
        for(i in animalBreeds){
            breeds.add(i.name)
        }
        return breeds
    }




    suspend fun fetchColors(token: String, typeName: String) =
        petFinderApi.getTypeInfo(" Bearer $token", typeName).colors

    suspend fun fetchOrganization(token: String, id: String) =
        petFinderApi.getOrganizationInfo(" Bearer $token", id)

    suspend fun findOrganizations(token: String,
                                  name: String? = null,
                                  location: String? = null,
                                  distance: Int? = null,
                                  state: String? = null,
                                  country: String? = null,
                                  query: String? = null,
                                  sort: String? = null,
                                  limit: Int? = null,
                                  page: Int? = null,) =
        petFinderApi.getOrganizations(" Bearer $token", name, location, distance,
            state, country, query, sort, limit, page).organizations
}
