package com.pr.locationapp

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.pr.locationapp.util.LocationUtils

@Composable
fun LocationScreen(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context,
    modifier: Modifier = Modifier
) {
    // 런타임 권한 실행을 위한 런처
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        // 결과로 실행할 람다
        onResult = { permission ->
            // 권한을 다시 체크하고
            if (permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
            ) {
                // 권한이 있으면 로직 실행
            } else {
                // rational permission
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )

                if (rationaleRequired) {
                    // 권한이 필요한 이유를 알려줌
                    Toast.makeText(
                        context,
                        "Location Permission is required for this feature to work",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // 권한을 직접 설저하라고 알려줌
                    Toast.makeText(
                        context,
                        "Location Permission is required, Please go to seting and on the permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Location not available")

        Button(onClick = {
            if (locationUtils.hasLocationPermission(context)) {
                // 권한이 있다면 위치 업데이트

            } else {
                // 권한 요청
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }) {
            Text(text = "Get Location")
        }
    }
}


@Composable
fun MyApp(
    viewModel: LocationViewModel
) {
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    LocationScreen(locationUtils = locationUtils, context = context, viewModel = viewModel)
}
