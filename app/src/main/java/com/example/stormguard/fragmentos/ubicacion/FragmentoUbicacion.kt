package com.example.stormguard.fragmentos.ubicacion

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.stormguard.data.UbicacionRemota
import com.example.stormguard.databinding.FragmentoUbicacionBinding
import com.example.stormguard.fragmentos.Home.FragmentoHome
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.http.Query

class FragmentoUbicacion : Fragment() {

    private var _binding: FragmentoUbicacionBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val locationModelView: UbicacionVistaModelo by viewModel()

    private val AdaptadorUbicaciones = AdaptadorUbicaciones(
        onLocationClicked = {ubicacionRemota ->
            setLocation(ubicacionRemota)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentoUbicacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setupLocationsRecycleView()
        setObservers()
    }

    private fun setupLocationsRecycleView() {
        with(binding.locationRecycleView) {
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
            adapter = AdaptadorUbicaciones
        }
    }

    private fun setListeners() {
        binding.imageClose.setOnClickListener { findNavController().popBackStack() }
        binding.inputSearch.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard()
                val query = binding.inputSearch.editText?.text
                if (query.isNullOrBlank()) return@setOnEditorActionListener true
                searchLocation(query.toString())
            }
            return@setOnEditorActionListener true
        }
    }

    private fun setLocation(ubicacionRemota: UbicacionRemota) {
        with(ubicacionRemota) {
            val locationText = "$name, $region, $country"
            setFragmentResult(
                requestKey = FragmentoHome.REQUEST_KEY_MANUAL_LOCATION_SEARCH,
                result = bundleOf(
                    FragmentoHome.KEY_LOCATION_TEXT to locationText,
                    FragmentoHome.KEY_LATITUDE to lat,
                    FragmentoHome.KEY_LONGITUDE to lon
                )
            )
            findNavController().popBackStack()
        }

    }

    private fun setObservers() {
        locationModelView.searchResult.observe(viewLifecycleOwner) {
            val searchResultDataBase = it ?: return@observe
            if (searchResultDataBase.isLoading) {
                binding.locationRecycleView.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
            searchResultDataBase.locations?.let { remoteLocations ->
               binding.locationRecycleView.visibility = View.VISIBLE
                AdaptadorUbicaciones.setData(remoteLocations)
            }
           searchResultDataBase.error?.let { error ->
               Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
           }
        }
    }


    private fun searchLocation(query: String) {
        locationModelView.searchLocation(query)
    }

    private fun hideSoftKeyboard () {
        val inputManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            binding.inputSearch.editText?.windowToken,0
        )
    }

}