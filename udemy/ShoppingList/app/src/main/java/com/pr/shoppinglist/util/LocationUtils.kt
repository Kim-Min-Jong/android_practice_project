package com.pr.shoppinglist.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.pr.shoppinglist.LocationViewModel
import com.pr.shoppinglist.data.LocationData
import java.util.Locale

class LocationUtils(
    private val context: Context
) {

    private val _fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // 위치를 가져오는 함수
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = LocationData(latitude = it.latitude, longitude = it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

        _fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    // 위치 권한에 액세스를 할 수 있는지 없는 지 확인
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    // 위경도에서 주소 변경 - 지오코딩
    fun reverseGeocodeLocation(location: LocationData): String {
        // 지오코딩을 도와주는 객체
        val geoCoder = Geocoder(context, Locale.getDefault())
        val coordinate = LatLng(location.latitude, location.longitude)
        // 위경도에 따른 주소가 여러개 있을 수 있으므로..?
        val address: MutableList<Address>? =
            geoCoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)
        // 주소가 있으면 반환 없으면 없다고 알리기
        return if (address?.isNotEmpty() == true) {
            address[0].getAddressLine(0)
        } else {
            "Address Not Found"
        }
    }
}
