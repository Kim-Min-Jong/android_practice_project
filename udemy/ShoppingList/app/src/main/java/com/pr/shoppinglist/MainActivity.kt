package com.pr.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.pr.shoppinglist.ui.theme.ShoppingListTheme
import com.pr.shoppinglist.util.LocationUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(navController = navController, startDestination = "shoppinglistscreen") {
        composable("shoppinglistscreen") {
            ShoppingListScreen(
                locationUtils = locationUtils,
                viewModel = viewModel,
                navController = navController,
                context = context,
                address = viewModel.address.value.firstOrNull()?.formattedAddress ?: "No Address"
            )
        }
        // 일반 화면이아닌 다이얼로그를 띄우는 navigation
        dialog("locationscreen") { backStack ->
            viewModel.location.value?.let {
                LocationSelectionScreen(locationData = it, onLocationSelected = { location ->
                    viewModel.fetchAddress("${location.latitude},${location.longitude}")
                    // 뒤로가기 버튼 누를 시 다이얼로그를 스택에서 지우기 위함
                    navController.popBackStack()
                })
            }
        }
    }
}

