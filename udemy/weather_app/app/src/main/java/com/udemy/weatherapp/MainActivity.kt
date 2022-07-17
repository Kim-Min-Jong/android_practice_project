package com.udemy.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.udemy.weatherapp.databinding.ActivityMainBinding
import com.udemy.weatherapp.models.WeatherResponse
import com.udemy.weatherapp.network.WeatherService
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private var key: String? = null
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
            if (latitude != null && longitude != null) {
                    getLocationWeatherDetails(latitude, longitude)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try{
            val jsonString = assets.open("key.json").reader().readText()
            val jsonObject = JSONObject(jsonString)
            key = jsonObject.getString("key")

        } catch(e: Exception){
            e.printStackTrace()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)

        // 위치 권한이 꺼져있으면 권한 요청
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

    // retrofit 연결 후 날씨 데이터 가져오기
    private fun getLocationWeatherDetails(latitude: Double, longitude: Double){
        if(Constants.isNetWorkAvailable(this)){
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service: WeatherService = retrofit.create(WeatherService::class.java)

            val listCall: Call<WeatherResponse> = service.getWeather(
                latitude, longitude, Constants.METRIC_UNIT, key
            )

            // 콜백
            listCall.enqueue(object: Callback<WeatherResponse>{
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if(response.isSuccessful){
                        val weatherList = response.body()
                        Log.i("response result", "$weatherList")
                    }else{
                        val rc = response.code()
                        when(rc){
                            400 -> Log.e("Error 400","Bad Connection")
                            404 -> Log.e("Error 404","Not Found")
                            else -> Log.e("Error","Generic Error")
                        }
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.e("Error",t.message.toString())
                }

            })
        } else{

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