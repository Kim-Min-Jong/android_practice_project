package com.udemy.weatherapp

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import com.udemy.weatherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if(!isLocationEnabled()){
            Toast.makeText(this,"Location provider is turned off. please turn on GPS", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else{
            Toast.makeText(this,"Location provider is turned on.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isLocationEnabled(): Boolean{
        val locationMgr:LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}