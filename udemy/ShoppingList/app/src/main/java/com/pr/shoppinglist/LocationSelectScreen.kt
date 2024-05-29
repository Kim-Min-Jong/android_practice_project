package com.pr.shoppinglist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.pr.shoppinglist.data.LocationData
import kotlin.reflect.KProperty

@Composable
fun LocationSelectionScreen(
    locationData: LocationData,
    onLocationSelected: (LocationData) -> Unit
) {
    // 현재 사용자 위치
    val userLocation by remember {
        mutableStateOf(LatLng(locationData.latitude, locationData.longitude))
    }

    // 카메라 위치를 담고있는 변수 (지도에 포커싱을 할 변수)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 10f)
    }
}
