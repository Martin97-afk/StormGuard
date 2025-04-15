package com.example.stormguard.fragmentos.Home

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.stormguard.R
import com.example.stormguard.almacenamiento.PreferenciasCompartidasManager
import com.example.stormguard.data.ClimaData
import com.example.stormguard.data.CurrentLocation
import com.example.stormguard.databinding.FragmentoHomeBinding
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentoHome : Fragment() {

    companion object {
        const val REQUEST_KEY_MANUAL_LOCATION_SEARCH = "busquedaUbicaciónManual"
        const val KEY_LOCATION_TEXT = "textoUbicación"
        const val KEY_LATITUDE = "latitud"
        const val KEY_LONGITUDE = "longitud"
    }

    private var _binding: FragmentoHomeBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val homeVistaModelo: HomeVistaModelo by viewModel()
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val geocoder by lazy { Geocoder(requireContext()) }



    private val ClimaDataAdapter = ClimaDataAdapter(
        onLocationClicked = {showLocationOptions() }
    )

    private val preferenciasCompartidasManager: PreferenciasCompartidasManager by inject()

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {isGranted ->
        if(isGranted) {
            getCurrentLocation()
        }else{
            Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show()
        }

    }

    private var isInitialLocationSet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentoHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClimaDataAdapter()
        setObservers()
        setListeners()
        if(!isInitialLocationSet) {
            setCurrentLocation(currentLocation = preferenciasCompartidasManager.getCurrentLocation())
            isInitialLocationSet = true
        }
    }

    private fun setListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            setCurrentLocation(preferenciasCompartidasManager.getCurrentLocation())
        }
    }

    private fun setObservers() {
        with(homeVistaModelo) {
            currentLocation.observe(viewLifecycleOwner) {
                val currentLocationDateState = it.getContentIfNotHandled() ?: return@observe
                if(currentLocationDateState.isLoading) {
                    showLoading()
                }
                currentLocationDateState.currentLocation?.let { currentLocation ->
                    hideLoading()
                    preferenciasCompartidasManager.saveCurrentLocation(currentLocation)
                    setCurrentLocation(currentLocation)
                }
                currentLocationDateState.error?.let { error ->
                    hideLoading()
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
            climaData.observe(viewLifecycleOwner) {
                val climaDataState = it.getContentIfNotHandled() ?: return@observe
                binding.swipeRefreshLayout.isRefreshing = climaDataState.isLoading
                climaDataState.currentClima?.let { currentClima ->
                   ClimaDataAdapter.setCurrentClima(currentClima)
                }
                climaDataState.forecast?.let { pronosticos ->
                    ClimaDataAdapter.setPronosticoData(pronosticos)
                }
                climaDataState.error?.let { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setClimaDataAdapter() {
        binding.ClimaDataRecyclerView.itemAnimator = null
        binding.ClimaDataRecyclerView.adapter = ClimaDataAdapter
    }


    private fun setCurrentLocation(currentLocation: CurrentLocation? = null) {
        ClimaDataAdapter.setCurrentLocation(currentLocation ?: CurrentLocation())
        currentLocation?.let { getClimaData(currentLocation = it) }
    }



    private fun getCurrentLocation() {
        homeVistaModelo.getCurrentLocation(fusedLocationProviderClient, geocoder)
    }

    private fun isLocationPermissionGranted(): Boolean{
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun proceedWithCurrentLocation() {
        if(isLocationPermissionGranted()) {
            getCurrentLocation()
        }else {
            requestLocationPermission()
        }
    }

    private fun showLocationOptions() {
        val options = arrayOf("Ubicación Actual","Busqueda Manual")
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Elegir El Método De Ubicación")
            setItems(options) { _, which ->
                when(which) {
                    0 -> proceedWithCurrentLocation()
                    1 -> startManualLocationSearch()
                }

            }
            show()
        }
    }

    private fun showLoading() {
        with(binding) {
            ClimaDataRecyclerView.visibility = View.GONE
            swipeRefreshLayout.isEnabled = false
            swipeRefreshLayout.isRefreshing= true
        }
    }

    private fun hideLoading() {
        with(binding) {
            ClimaDataRecyclerView.visibility = View.VISIBLE
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.isRefreshing= false
        }
    }

    private fun startManualLocationSearch() {
        startListeningManualLocationSelection()
        findNavController().navigate(R.id.action_home_fragment_to_location_fragment)
    }

    private fun startListeningManualLocationSelection() {
        setFragmentResultListener(REQUEST_KEY_MANUAL_LOCATION_SEARCH) { _, bundle ->
            stopListeningManualLocationSelection()
            val currentLocation = CurrentLocation(
                location = bundle.getString(KEY_LOCATION_TEXT) ?: "N/A",
                latitude = bundle.getDouble(KEY_LATITUDE),
                longitude = bundle.getDouble(KEY_LONGITUDE)

            )

            preferenciasCompartidasManager.saveCurrentLocation(currentLocation)
            setCurrentLocation(currentLocation)
        }
    }

    private fun stopListeningManualLocationSelection() {
        clearFragmentResultListener(REQUEST_KEY_MANUAL_LOCATION_SEARCH)
    }

    private fun getClimaData(currentLocation: CurrentLocation) {
        if(currentLocation.latitude !=null && currentLocation.longitude != null) {
            homeVistaModelo.getClimaData(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude
            )
        }
    }

}