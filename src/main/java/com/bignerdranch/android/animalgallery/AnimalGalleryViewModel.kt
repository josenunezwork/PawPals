package com.bignerdranch.android.animalgallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.animalgallery.api.Animal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AnimalGalleryViewModel"

class AnimalGalleryViewModel : ViewModel() {

    private val petRepository = PetRepository()

    private lateinit var token: String


    private val _galleryItems: MutableStateFlow<List<Animal>> =
        MutableStateFlow(emptyList())
    val galleryItems: StateFlow<List<Animal>>
        get() = _galleryItems.asStateFlow()


    private val _animalTypes: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val animalTypes: StateFlow<List<String>>
        get() = _animalTypes.asStateFlow()


    private val _userInput = UserInput()
    val userInputs: UserInput
        get() = _userInput


    private val _animalBreeds: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val animalBreeds: StateFlow<List<String>>
        get() = _animalBreeds.asStateFlow()
    

    init {
        viewModelScope.launch {
            try {
                token = petRepository.fetchToken()
                Log.d(TAG, "Token received: $token")
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to secure token: ", ex)
            }

            try {
                val animals = petRepository.fetchAnimals(token, type = userInputs.getAnimalType())
                _galleryItems.value = animals
                Log.d(TAG, animals.toString())
            } catch (e: Exception) {
                Log.e(TAG, "error fetching animals: ", e)
            }

            try{
                val mutableTypes = petRepository.fetchTypes(token)
                mutableTypes.add(0,"Any type")
                val types = mutableTypes.toList()
                _animalTypes.value = types
                Log.d(TAG, "animal types are:$types")
            } catch (e: Exception){
                Log.e(TAG, "error fetching animal types", e)
            }
        }
    }

    fun updateGalleryItems(){
        viewModelScope.launch{
            try{
                val animals = petRepository.fetchAnimals(
                    token = token,
                    distance = userInputs.distance,
                    location = userInputs.location,
                    type = userInputs.getAnimalType(),
                    limit = userInputs.animalAmount,
                    breed = userInputs.getAnimalBreed()
                )
                Log.d(TAG, "update gallery items," +
                    "distance = ${userInputs.distance}, " +
                    "location = ${userInputs.location}, " +
                    "type = ${userInputs.getAnimalType()}"
                )
                _galleryItems.value = animals
            } catch (e: Exception){
                Log.e(TAG, "error fetching animal types through user inputs", e)
            }
        }
    }

    fun updateBreeds(animalType: String) {
        if (animalType == "Any type") {
            _animalBreeds.value = listOf()
        } else {
            viewModelScope.launch {
                try {
                    val mutableBreeds = petRepository.fetchBreeds(token, animalType)
                    mutableBreeds.add(0, "Any breed")
                    val breeds = mutableBreeds.toList()
                    _animalBreeds.value = breeds
                    Log.d(TAG, "animal breeds are:$breeds")
                } catch (e: Exception) {
                    Log.e(TAG, "error fetching animal breeds", e)
                }
            }
        }
    }
}

class UserInput(
    private var animalType: String? = null,
    private var animalBreed: String? = null,
    var distance: Int? = null,
    var location: String? = null,
    var animalAmount: Int? = null,
){
    fun setAnimalType(input: String){
        if(input=="Any type") animalType = null
        else animalType = input
    }
    fun getAnimalType() = animalType

    fun setAnimalBreed(input: String){
        if(input=="Any breed") animalBreed = null
        else animalBreed = input
    }
    fun getAnimalBreed() = animalBreed
}
