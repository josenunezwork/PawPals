package com.bignerdranch.android.animalgallery
import android.content.Context

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.animalgallery.databinding.FragmentAnimalGalleryBinding
import kotlinx.coroutines.launch

private const val TAG = "AnimalGalleryFragment"
class AnimalGalleryFragment : Fragment() {

    private val toggle
        get() = checkNotNull(_toggle){
            "Cannot access drawer toggle because it is null, is it visible?"
        }
    private var _toggle: ActionBarDrawerToggle?= null

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private var _binding: FragmentAnimalGalleryBinding? = null


    private val animalGalleryViewModel: AnimalGalleryViewModel by viewModels()

    private val animalTypeSpinner
        get() = checkNotNull(_animalTypeSpinner){
            "Cannot access spinner because it is null, is it visible? "
        }
    private var _animalTypeSpinner: Spinner? = null

    private val animalAmountSpinner
        get() = checkNotNull(_animalAmountSpinner){
            "Cannot access spinner because it is null, is it visible? "
        }
    private var _animalAmountSpinner: Spinner? = null
    private val locationSearchView
        get() = checkNotNull(_locationSearchView){
            "Cannot access location search view because it is null, is it visible?"
        }
    private var _locationSearchView: SearchView? = null


    private var _animalBreedSpinner: Spinner? = null
    private val animalBreedSpinner
        get() = checkNotNull(_animalBreedSpinner){
            "Cannot access breed spinner because it is null, is it visible?"
        }


    private val distanceSearchView
        get() = checkNotNull(_distanceSearchView){
            "Cannot access distance edit text because it is null, is it visible?"
        }
    private var _distanceSearchView: SearchView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentAnimalGalleryBinding.inflate(inflater, container, false)
        binding.photoGrid.layoutManager = GridLayoutManager(context, 2)

        _toggle = ActionBarDrawerToggle(activity, binding.drawerLayout, R.string.open, R.string.close)

        //makes the toggle as a button to open and close the drawer
        binding.drawerLayout.addDrawerListener(toggle)
        //sync's the drawer toggle button with the state of the drawer
        //when drawer opened, toggle is a back button, etc
        toggle.syncState()
        //enables the "up" or back button for the drawer
        //need this or else the hamburger and back arrow for drawer won't appear
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //gets various navigation drawer views
        _animalTypeSpinner = binding.navView.menu.findItem(R.id.menu_item_dropdown_animal_type)
            .actionView as Spinner
        _animalAmountSpinner = binding.navView.menu.findItem(R.id.menu_item_dropdown_animal_count)
            .actionView as Spinner
        _locationSearchView = binding.navView.menu.findItem(R.id.menu_item_location)
            .actionView as SearchView
        _distanceSearchView = binding.navView.menu.findItem(R.id.menu_item_distance)
            .actionView as SearchView
        _animalBreedSpinner = binding.navView.menu.findItem(R.id.menu_item_dropdown_animal_breed)
            .actionView as Spinner

        //makes the distance edit text view only accept numbers
        distanceSearchView.setInputType(InputType.TYPE_CLASS_NUMBER)

        //makes the location edit text view only accept numbers, in order for the user to enter a zip code
        locationSearchView.setInputType(InputType.TYPE_CLASS_NUMBER)

        if(animalGalleryViewModel.userInputs.location == null){
            binding.navView.menu.findItem(R.id.menu_item_distance).isVisible = false
        }

        if(animalGalleryViewModel.userInputs.getAnimalType() == null){
            binding.navView.menu.findItem(R.id.menu_item_dropdown_animal_breed).isVisible = false
        }

        //set up the animal count spinner
        val animalCountNumbers = listOf(20,40,60,80,100)
        val animalCountAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            animalCountNumbers)
        animalAmountSpinner.adapter = animalCountAdapter


        //gets animal types from viewmodel and puts it in Spinner (dropdown menu)
        viewLifecycleOwner.lifecycleScope.launch {
            //get animaltypes from viewmodel
            animalGalleryViewModel.animalTypes.collect{
                val animalTypes = it

                //makes sure animalTypes exists before giving it to adapter
                if(animalTypes.isNotEmpty()) {
                    //make and give adapter with animal types which connects data to Spinner
                    val spinnerAdapter = ArrayAdapter(
                        requireContext(),
                        //use default android layout for a spinner
                        android.R.layout.simple_spinner_dropdown_item,
                        animalTypes
                    )
                    Log.d(
                        TAG,
                        "Updated animal types spinner adapter"
                    )
                    animalTypeSpinner.adapter = spinnerAdapter
                }
            }
        }

        //gets animal breeds from viewmodel and puts it in Spinner (dropdown menu)
        viewLifecycleOwner.lifecycleScope.launch {
            //get animal breeds from viewmodel
            animalGalleryViewModel.animalBreeds.collect{
                val animalBreeds = it

                //makes sure animalTypes exists before giving it to adapter
                if(animalBreeds.isNotEmpty()) {
                    //make and give adapter with animal types which connects data to Spinner
                    val spinnerAdapter = ArrayAdapter(
                        requireContext(),
                        //use default android layout for a spinner
                        android.R.layout.simple_spinner_dropdown_item,
                        animalBreeds
                    )
                    Log.d(
                        TAG,
                        "Updated animal breed spinner adapter"
                    )
                    animalBreedSpinner.adapter = spinnerAdapter
                }
            }
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                animalGalleryViewModel.galleryItems.collect { items ->
                    binding.photoGrid.adapter = AnimalAdapter(items) { animalPageUri ->
                        findNavController().navigate(
                            AnimalGalleryFragmentDirections.showAnimal(animalPageUri)
                        )
                    }
                }
            }
        }

        //listener for animal type dropdown menu in navigation drawer
        animalTypeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selection = animalTypeSpinner.adapter.getItem(p2) as String
                Log.d(TAG, "Selected $selection from animal types spinner")
                animalGalleryViewModel.userInputs.setAnimalType(selection)
                //if user chooses "any type", make animal breed spinner invisible
                //and reset animal breed to "any breed"
                if(selection=="Any type"){
                    binding.navView.menu.findItem(R.id.menu_item_dropdown_animal_breed).isVisible = false
                    animalGalleryViewModel.userInputs.setAnimalBreed("Any breed")
                    animalGalleryViewModel.updateBreeds(selection)
                }
                //if user chooses an animal type, tell viewmodel to get the breeds of that type
                //which will update animalBreed StateFlow
                //and since when creating our spinner we observe that StateFlow, the spinner will update
                //finally make the animal breed spinner visible
                else{
                    animalGalleryViewModel.updateBreeds(selection)
                    binding.navView.menu.findItem(R.id.menu_item_dropdown_animal_breed).isVisible = true
                }
                animalGalleryViewModel.updateGalleryItems()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //nothing
            }
        }


        //listener for animal breed dropdown menu
        animalBreedSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selection = animalBreedSpinner.adapter.getItem(p2) as String
                Log.d(TAG, "Selected $selection from animal breeds spinner")
                animalGalleryViewModel.userInputs.setAnimalBreed(selection)
                animalGalleryViewModel.updateGalleryItems()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //nothing
            }

        }

        //listener for animal count spinner
        animalAmountSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selection = animalAmountSpinner.adapter.getItem(p2)
                Log.d(TAG, "Selected $selection from animal amount spinner")
                animalGalleryViewModel.userInputs.animalAmount = checkNotNull(selection) as Int
                animalGalleryViewModel.updateGalleryItems()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //nothing
            }
        }

        //listener for location search in navigation drawer
        locationSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query?.length == 5){
                    animalGalleryViewModel.userInputs.location = query
                    binding.navView.menu.findItem(R.id.menu_item_distance).isVisible = true
                    Log.d(TAG, "LocationQueryTextSubmit: $query")
                    Log.d(TAG, "distance menu item should be visible")
                }
                else{
                    animalGalleryViewModel.userInputs.distance = null
                    animalGalleryViewModel.userInputs.location = null
                    binding.navView.menu.findItem(R.id.menu_item_distance).isVisible = false
                    locationSearchView.setQuery("",false)
                    Toast.makeText(context,"Please input valid ZIP code",Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "distance menu item should not be visible")
                }
                animalGalleryViewModel.updateGalleryItems()
                //hide keyboard on submit
                val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(locationSearchView.windowToken, 0)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "LocationQueryTextChange: $newText")
                return false
            }
        })

        //listener for distance search in navigation drawer
        distanceSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "DistanceQueryTextSubmit: $query")
                animalGalleryViewModel.userInputs.distance = query?.toInt()
                animalGalleryViewModel.updateGalleryItems()
                val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(locationSearchView.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "DistanceQueryTextChange: $newText")
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //if nav drawer toggle is selected (checked by using its own onOptionsItemSelected)
        //toggle will handle the click on its own (through its own onOptionsItemSelected)
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toggle = null
        _animalTypeSpinner = null
        _distanceSearchView = null
        _locationSearchView = null
        _animalAmountSpinner = null
        _animalBreedSpinner = null
        //disables the "up" or back button for the drawer, so drawer only appears on this fragment
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
