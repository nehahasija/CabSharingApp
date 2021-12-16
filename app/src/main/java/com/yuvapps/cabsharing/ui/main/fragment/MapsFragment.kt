package com.yuvapps.cabsharing.ui.main.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.yuvapps.cabsharing.R
import com.yuvapps.cabsharing.data.model.LocationModel
import com.yuvapps.cabsharing.data.model.NetworkResponse
import com.yuvapps.cabsharing.databinding.FragmentMapsBinding
import com.yuvapps.cabsharing.ui.main.viewmodel.MapsViewModel
import com.yuvapps.cabsharing.utils.MapUtils
import com.yuvapps.cabsharing.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapsFragment : Fragment(),
    GoogleMap.OnMarkerClickListener {
    @Inject
    lateinit var permissionsManager: PermissionUtils

    private val viewModel by viewModels<MapsViewModel>()
    private lateinit var _binding: FragmentMapsBinding
    private lateinit var navController: NavController

    private val nearbyCabMarkerList = arrayListOf<Marker>()
    private lateinit var mMap: GoogleMap
    private lateinit var currentLatlng: LatLng


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.getUiSettings().setZoomControlsEnabled(true)
        mMap.setOnMarkerClickListener(this)
        setUpLocationUpdatesObserver()
    }


    private fun requestLocationPermission() {
        if (permissionsManager.isAccessFineLocationGranted(requireContext())) {
            viewModel.onLocationPermissionGranted()
        } else {
            val requestPermission =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { IsGranted ->
                    if (IsGranted) {
                        viewModel.onLocationPermissionGranted()
                    } else {
                        viewModel.onLocationPermissionDenied()
                    }

                }
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            _binding.progressBar.visibility = View.GONE

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.fetchNearbyCabsResponse()

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        setUpNavigationObserver()
        requestLocationPermission()

        _binding.swipeContainer.setOnRefreshListener {
            animateMapToFirstNearbyCab()
        }
    }

    private fun onLocationModel(locationModel: LocationModel) {
        val latLng = LatLng(locationModel.latitude, locationModel.longitude)
        currentLatlng = latLng
        enableMyLocationOnMap()
        moveCamera(latLng)
        animateCamera(latLng)
        setUpNearbyCabsObservers()
    }

    private fun setUpNavigationObserver() {
        viewModel.navigation.observe(viewLifecycleOwner) { navigation ->
            when (navigation) {
                MapsViewModel.Navigation.Finish -> activity?.onBackPressed()
            }

        }
    }

    private fun setUpLocationUpdatesObserver() {
        viewModel.locationModel.observe(viewLifecycleOwner) { locationModel ->
            onLocationModel(locationModel)
        }
    }

    private fun animateMapToFirstNearbyCab() {
        moveCamera(nearbyCabMarkerList[0].position)
        animateCamera(nearbyCabMarkerList[0].position)
        _binding.swipeContainer.isRefreshing=false
    }

    private fun setUpNearbyCabsObservers() {
        viewModel.getNearbyCabs().observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _binding.progressBar.visibility = View.GONE
                    val nearByCabLocations: HashMap<Int, Pair<String, LatLng>> = HashMap()
                    for (i in 0 until response.data!!.size) {

                        val lat = response.data.get(i).lat
                        val lng = response.data.get(i).lon
                        val carId = response.data.get(i).carId
                        val title = response.data.get(i).title

                        val latLng = LatLng(lat, lng)
                        if (!nearByCabLocations.containsKey(carId))
                            nearByCabLocations[carId] = Pair(title, latLng)
                    }
                    showNearbyCabs(nearByCabLocations)

                }
                is NetworkResponse.Error -> {
                    _binding.progressBar.visibility = View.GONE
                    showSnackBar(getString(R.string.error_msg))
                }
                is NetworkResponse.Loading -> {
                    _binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showNearbyCabs(latLngList: HashMap<Int, Pair<String, LatLng>>) {
        nearbyCabMarkerList.clear()
        mMap.clear()
        addCurrentLocMarkerAndGet(currentLatlng)
        var firstLatLng: LatLng? = null
        for (latLng in latLngList) {
            if (firstLatLng == null) {
                firstLatLng = latLng.value.second
            }
            val nearbyCabMarker =
                addCarMarkerAndGet(latLng.key, latLng.value.first, latLng.value.second)
            nearbyCabMarkerList.add(nearbyCabMarker)
            nearbyCabMarker.showInfoWindow()
        }

        moveCamera(firstLatLng!!)
        animateCamera(firstLatLng)

    }

    private fun enableMyLocationOnMap() {
        if (!::mMap.isInitialized) return
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            val requestPermission =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { IsGranted ->
                    if (IsGranted) {
                        mMap.isMyLocationEnabled = true
                    }
                }
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }

    private fun moveCamera(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(16f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun addCarMarkerAndGet(carID: Int, title: String, latLng: LatLng): Marker {
        val bitmapDescriptor =
            BitmapDescriptorFactory.fromBitmap(MapUtils.getCarBitmap(requireContext()))
        var marker: Marker? = null
        if (marker != null) {
            marker.remove()
        }

        if(title.isNullOrBlank())
            marker = mMap.addMarker(
                MarkerOptions().position(latLng).flat(true).
                icon(bitmapDescriptor).title(getString(R.string.error_no_title))
            )!!
        else
        marker = mMap.addMarker(
            MarkerOptions().position(latLng).flat(true).icon(bitmapDescriptor).title(title)
        )!!
        marker.tag = MarkerTag(carID, false)
        return marker

    }

    private fun addCurrentLocMarkerAndGet(latLng: LatLng) {
        mMap.addMarker(
            MarkerOptions().position(latLng).title(latLng.toString()
            )
        )?.showInfoWindow()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            requireView().findViewById(R.id.constraintLayout),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (marker.tag != null) {
            val markerTagInfo: MarkerTag = marker.tag as MarkerTag
            if (!markerTagInfo.isMarkerClicked) {
                marker.showInfoWindow()
                marker.tag = MarkerTag(markerTagInfo.id, true)
                removeAllOtherMarkers(marker)
            } else {
                val actionargs =
                    MapsFragmentDirections.actionMapsFragmentToCabDetailFragment(markerTagInfo.id)
                navController.navigate(actionargs)
            }
        }
        else{
            marker.showInfoWindow()
        }
        return true
    }

    private fun removeAllOtherMarkers(selectedMarker: Marker) {

        if (nearbyCabMarkerList.size>0) {
            for (marker in nearbyCabMarkerList) {
                if (marker.id != selectedMarker.id) {
                    marker.remove()
                }
            }
        }
    }


    private data class MarkerTag(val id: Int, var isMarkerClicked: Boolean)

}



