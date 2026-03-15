package com.example.donex.ui.screens.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.donex.ui.components.BottomNavBar
import com.example.donex.viewmodel.MainViewModel
import com.example.donex.viewmodel.MapViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mainViewModel: MainViewModel,
    mapViewModel: MapViewModel,
    navController: NavController
) {
    val posts by mainViewModel.posts.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-8.0476, -34.8770), 12f)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mapa de Doações") }) },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        GoogleMap(
            modifier = Modifier.fillMaxSize().padding(padding),
            cameraPositionState = cameraPositionState
        ) {
            posts.forEach { post ->
                if (post.latitude != 0.0 && post.longitude != 0.0) {
                    Marker(
                        state = MarkerState(position = LatLng(post.latitude, post.longitude)),
                        title = post.userName,
                        snippet = post.conteudo
                    )
                }
            }
        }
    }
}