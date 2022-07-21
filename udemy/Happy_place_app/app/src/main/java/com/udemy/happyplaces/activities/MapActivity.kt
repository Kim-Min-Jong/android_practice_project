package com.udemy.happyplaces.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.udemy.happyplaces.databinding.ActivityMapBinding
import com.udemy.happyplaces.models.HappyPlaceModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity() {
    private var binding: ActivityMapBinding? = null
    private var happyPlaceDetailModel: HappyPlaceModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            happyPlaceDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)
        }
        setSupportActionBar(binding?.toolbarMap)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = happyPlaceDetailModel?.title

        binding?.toolbarMap?.setNavigationOnClickListener {
            onBackPressed()
        }
        Log.e("data",happyPlaceDetailModel?.location!!)

        initMap()
    }

    private fun initMap(){
        val mapView = MapView(this)
        val mapViewContainer = binding?.mapView
        val marker = MapPOIItem()
        marker.itemName = "happy place"
        marker.tag = 0
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(happyPlaceDetailModel!!.latitude, happyPlaceDetailModel!!.longitude)
        marker.markerType = MapPOIItem.MarkerType.RedPin
        marker.selectedMarkerType = MapPOIItem.MarkerType.YellowPin

        mapView.setMapCenterPoint(happyPlaceDetailModel?.latitude?.let { latitude ->
            happyPlaceDetailModel?.longitude?.let { longitude ->
                MapPoint.mapPointWithGeoCoord(
                    latitude, longitude
                )
            }
        },true)
        mapView.addPOIItem(marker)
        mapView.setZoomLevel(1, true)
        mapViewContainer?.addView(mapView)
    }
}