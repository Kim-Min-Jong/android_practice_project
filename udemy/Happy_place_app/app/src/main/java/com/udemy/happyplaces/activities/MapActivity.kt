package com.udemy.happyplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.udemy.happyplaces.R
import com.udemy.happyplaces.databinding.ActivityMapBinding
import com.udemy.happyplaces.models.HappyPlaceModel
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity(), MapView.MapViewEventListener, MapView.POIItemEventListener {
    private var binding: ActivityMapBinding? = null
    private var happyPlaceDetailModel: HappyPlaceModel? = null
    private val markerList = mutableListOf<MapPOIItem>()
    private val marker = MapPOIItem()
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

        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            initMapMarked()
        }else{
            val coords = intent.getDoubleArrayExtra("coords")
            initMapNoMarked(coords!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(!intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            menuInflater.inflate(R.menu.menu_main, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_done -> {
                val newLatitude = marker.mapPoint.mapPointGeoCoord.latitude
                val newLongitude = marker.mapPoint.mapPointGeoCoord.longitude
                val intent = Intent(this@MapActivity, AddHappyPlaceActivity::class.java)
                println(newLatitude)
                println(newLongitude)
                intent.putExtra(NEW_POINT,doubleArrayOf(newLatitude,newLongitude))
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun initMapMarked(){
        val mapView = MapView(this)
        val mapViewContainer = binding?.mapView

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

    private fun initMapNoMarked(coords: DoubleArray){
        val mapView = MapView(this)
        val mapViewContainer = binding?.mapView

        marker.itemName = "happy place"
        marker.markerType = MapPOIItem.MarkerType.RedPin
        marker.selectedMarkerType = MapPOIItem.MarkerType.YellowPin
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(coords[0], coords[1]) ,true)
        mapView.setMapViewEventListener(this)
        mapView.setPOIItemEventListener(this)
        mapViewContainer?.addView(mapView)
    }

    override fun onMapViewInitialized(p0: MapView?) {
        Toast.makeText(this@MapActivity, "choose your happy place on map", Toast.LENGTH_LONG).show()
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        marker.mapPoint = p1!!
        if(markerList.isEmpty()) {
            p0?.addPOIItem(marker)
            markerList.add(marker)
        }
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
    }



    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        // 마커 클릭 시
    }
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
        // 말풍선 클릭 시 (Deprecated)
        // 이 함수도 작동하지만 그냥 아래 있는 함수에 작성하자
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        // 말풍선 클릭 시
        val builder = AlertDialog.Builder(this@MapActivity)
        val itemList = arrayOf("마커 삭제", "취소")
        builder.setTitle("${p1?.itemName}")
        builder.setItems(itemList) { dialog, which ->
            when(which) {
                0 -> {
                    p0?.removePOIItem(p1)
                    markerList.clear()
                }    // 마커 삭제
                1 -> dialog.dismiss()   // 대화상자 닫기
            }
        }
        builder.setCancelable(true)
        builder.show()
    }
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
    }

    companion object {
        var NEW_POINT = "new_point"
    }
}

