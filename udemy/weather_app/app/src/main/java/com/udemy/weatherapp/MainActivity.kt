package com.udemy.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.udemy.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    // 받을 위치 콜백
    private val mLocationCallback = object: LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            val mLastLocation: Location? = p0.lastLocation
            val latitude = mLastLocation?.latitude
            val longitude = mLastLocation?.longitude
            Log.e("latitude","latitude: $latitude")
            Log.e("longitude","longitude: $longitude")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)

        if(!isLocationEnabled()){
            Toast.makeText(this,"Location provider is turned off. please turn on GPS", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else{
            Dexter.withActivity(this).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object: MultiplePermissionsListener {
                // 권한이 확인되면
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()){

                        requestLocationData()

                    }
                    if(report!!.isAnyPermissionPermanentlyDenied){
                        Toast.makeText(this@MainActivity, "you have denied location permission. Please all enable permission", Toast.LENGTH_SHORT).show()
                    }
                }
                // 권한이 필요한지 보여줘야함
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                    showRationaleDialogForPermissions()
                }
            }).onSameThread().check()
        }
    }

    private fun isLocationEnabled(): Boolean{
        val locationMgr:LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // 권한 없을 시 왜 필요한 지 보여주며 설정으로
    private fun showRationaleDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permission required for this feature. It can be enabled under the Application Settings")
            .setPositiveButton("GO TO SETTINGS"){ _, _ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    // 현재 위치 요청
    @SuppressLint("MissingPermission")
    fun requestLocationData() {
        val mLocationRequest = LocationRequest.create()?.apply{
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback , Looper.myLooper()
        )
    }

}