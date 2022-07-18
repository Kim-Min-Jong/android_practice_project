package com.udemy.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.udemy.weatherapp.databinding.ActivityMainBinding
import com.udemy.weatherapp.models.WeatherResponse
import com.udemy.weatherapp.network.WeatherService
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var customProgressDialog: Dialog
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
                    if(report.isAnyPermissionPermanentlyDenied){
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
            showProgressDialog()
            // 콜백
            listCall.enqueue(object: Callback<WeatherResponse>{
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if(response.isSuccessful){
                        cancelProgressDialog()
                        val weatherList = response.body()
                        setupUI(weatherList!!)
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
                    cancelProgressDialog()
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
        val mLocationRequest = LocationRequest.create().apply{
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback , Looper.myLooper()
        )
    }
    private fun showProgressDialog() {
        customProgressDialog = Dialog(this@MainActivity)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)

        //Start the dialog and display it on screen.
        customProgressDialog.show()
    }

    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    private fun cancelProgressDialog() {
        customProgressDialog.dismiss()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupUI(weatherList: WeatherResponse){
        for(i in weatherList.weather.indices){
            Log.i("Whether Name", weatherList.weather.toString())
            binding?.tvMain?.text = weatherList.weather[i].main
            binding?.tvMainDescription?.text = weatherList.weather[i].description
            binding?.tvTemp?.text = "%.2f".format(weatherList.main.temp-273.15) + getUnit(application.resources.configuration.locales.toString())

            binding?.tvSunriseTime?.text = unixTime(weatherList.sys.sunrise)
            binding?.tvSunsetTime?.text = unixTime(weatherList.sys.sunset)

            binding?.tvHumidity?.text = weatherList.main.humidity.toString() + "percent"
            binding?.tvMin?.text = "%.2f".format(weatherList.main.temp_min-273.15) + " min"
            binding?.tvMax?.text = "%.2f".format(weatherList.main.temp_max-273.15) + " max"
            binding?.tvSpeed?.text = weatherList.wind.speed.toString()
            binding?.tvName?.text = weatherList.name
            binding?.tvCountry?.text = weatherList.sys.country

            when(weatherList.weather[i].icon){
                "01d" -> binding?.ivMain?.setImageResource(R.drawable.sunny)
                "02d" -> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "03d" -> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "04d" -> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "04n" -> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "10d" -> binding?.ivMain?.setImageResource(R.drawable.rain)
                "11d" -> binding?.ivMain?.setImageResource(R.drawable.storm)
                "13d" -> binding?.ivMain?.setImageResource(R.drawable.snowflake)
                "01n" -> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "02n" -> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "03n" -> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "10n" -> binding?.ivMain?.setImageResource(R.drawable.cloud)
                "11n" -> binding?.ivMain?.setImageResource(R.drawable.rain)
                "13n" -> binding?.ivMain?.setImageResource(R.drawable.snowflake)
            }
        }
    }

    private fun getUnit(value: String):String{
        var values = "°C"
        if("US" == value || "LR" == value || "MM" == value){
            values = "°F"
        }
        return values
    }

    private fun unixTime(timex: Long):String?{
        val date = Date(timex* 1000L)
        val sdf = SimpleDateFormat("HH:mm", Locale.KOREA)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }
}