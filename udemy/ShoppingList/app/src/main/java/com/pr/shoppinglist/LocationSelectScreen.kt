package com.pr.shoppinglist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.pr.shoppinglist.data.LocationData

@Composable
fun LocationSelectionScreen(
    locationData: LocationData,
    onLocationSelected: (LocationData) -> Unit
) {
    // 현재 사용자 위치
    var userLocation by remember {
        mutableStateOf(LatLng(locationData.latitude, locationData.longitude))
    }

    // 카메라 위치를 담고있는 변수 (지도에 포커싱을 할 변수)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 10f)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier
                .weight(1f)
                .padding(top = 16.dp),
            // 지도 카메라 위치
            cameraPositionState = cameraPositionState,
            // 지도 클릭시 불리는 콜백
            onMapClick = { userLocation = it }
        ) {
            // 지도에 마커찍기 (사용자에 위치에)
            Marker(
                state = MarkerState(position = userLocation)
            )
        }

        var newLocation: LocationData

        Button(onClick = {
            // 내 위치를 보여주는 로직 작성
            newLocation = LocationData(userLocation.latitude, userLocation.longitude)
            // 새로운 위치를 담고 람다 이벤트를 실행
            onLocationSelected(newLocation)
        }) {
            Text(text = "Set Location")
        }
    }
}
