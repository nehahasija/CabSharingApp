package com.yuvapps.cabsharing.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yuvapps.cabsharing.data.model.CabRentalResponse
import com.yuvapps.cabsharing.data.model.CabResponse
import com.yuvapps.cabsharing.data.model.LocationModel
import com.yuvapps.cabsharing.data.model.NetworkResponse
import com.yuvapps.cabsharing.data.repository.CabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: CabRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _nearbyCabsResponse: MutableLiveData<NetworkResponse<List<CabResponse>>> =
        MutableLiveData()

    private val _cabResponse: MutableLiveData<NetworkResponse<CabResponse>> = MutableLiveData()
    val cabResponse: LiveData<NetworkResponse<CabResponse>> = _cabResponse

    private val _cabRental: MutableLiveData<NetworkResponse<CabRentalResponse>> = MutableLiveData()
    val cabRental: LiveData<NetworkResponse<CabRentalResponse>> = _cabRental

    private val _locationModel = MutableLiveData<LocationModel>()
    val locationModel: LiveData<LocationModel> = _locationModel

    private val _navigation =
        SingleLiveEvent<Navigation>()
    val navigation: LiveData<Navigation> = _navigation

    init {
        fetchNearbyCabsResponse()
    }

    fun fetchNearbyCabsResponse() = viewModelScope.launch {
        repository.getNearbyCabs().collect { values ->
            _nearbyCabsResponse.value = values
        }
    }

    fun fetchCabById(carId: Int) = viewModelScope.launch {
        repository.getCab(carId).collect { values ->
            _cabResponse.value = values
        }
    }

    fun bookQuickRental(cabResponse: CabResponse) = viewModelScope.launch {
        repository.bookRentalCab(cabResponse).collect { values ->
            _cabRental.value = values
        }
    }

    // When the context from which the Flow was called is closed, the Flow is also closed.
    // The awaitClose block is then called and our location update callback is removed.
    fun onLocationPermissionGranted() {
        viewModelScope.launch {
            repository.getLocation().collect { values ->
                _locationModel.value = values
            }
        }
    }

    fun onLocationPermissionDenied() {
        _navigation.postValue(Navigation.Finish)
    }

    fun getNearbyCabs(): LiveData<NetworkResponse<List<CabResponse>>> {
        return _nearbyCabsResponse
    }
    sealed class Navigation {
        object Finish : Navigation()
    }
}
