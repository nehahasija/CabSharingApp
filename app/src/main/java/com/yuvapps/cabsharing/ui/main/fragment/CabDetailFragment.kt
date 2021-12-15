package com.yuvapps.cabsharing.ui.main.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.yuvapps.cabsharing.R
import com.yuvapps.cabsharing.data.model.CabResponse
import com.yuvapps.cabsharing.data.model.NetworkResponse
import com.yuvapps.cabsharing.databinding.FragmentCabDetailBinding
import com.yuvapps.cabsharing.ui.main.viewmodel.MapsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CabDetailFragment : Fragment() {
    private val args: CabDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<MapsViewModel>()
    private lateinit var navController: NavController
    private lateinit var _binding: FragmentCabDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCabDetailBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        setUpCabObserver()
    }


    private fun setUpCabObserver() {
        viewModel.fetchCabById(args.carId)
        viewModel.cabResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _binding.progressBar.visibility = View.GONE
                    loadUI(response)
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

    private fun setUpRentalObserver() {
        viewModel.cabRental.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    _binding.progressBar.visibility = View.GONE
                    showSnackBar(getString(R.string.cab_rental_successful_msg))
                }
                is NetworkResponse.Error -> {
                    _binding.progressBar.visibility = View.GONE
                    showSnackBar(getString(R.string.cab_rental_error_msg))
                }
                is NetworkResponse.Loading -> {
                    _binding.progressBar.visibility = View.VISIBLE

                }
            }
        }
    }

    private fun loadUI(
        response:
        NetworkResponse.Success<CabResponse>
    ) {
        _binding.cabImageView.load(
            response.data?.vehicleTypeImageUrl!!
        ){
            placeholder(R.mipmap.ic_launcher)
        }
        _binding.cabTypeValue.text = response.data.title
        _binding.licencePlateValue.text = response.data.licencePlate
        _binding.pricingTime.text = response.data.pricingTime
        _binding.parkingPrice.text = response.data.pricingParking
        _binding.fuelLevel.text = response.data.fuelLevel.toString()
        _binding.cabAddress.text = response.data.address.plus(", ").plus(response.data.zipCode)
        _binding.cabCity.text = response.data.city
        _binding.cabCleanStatus.text = response.data.isClean.toString()
        _binding.isCabDamaged.text = response.data.isDamaged.toString()
        _binding.cabDamageDescription.text = response.data.damageDescription
        _binding.cabIsActivated.text = response.data.isActivatedByHardware.toString()

        _binding.rentNowFab.setOnClickListener { view ->
            viewModel.bookQuickRental(response.data)
            setUpRentalObserver()
        }

    }


    private fun showSnackBar(message: String) {
        Snackbar.make(
            requireView().findViewById(R.id.top_constraintLayout),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
