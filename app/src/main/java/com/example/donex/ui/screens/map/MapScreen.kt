package com.example.donex.ui.screens.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.donex.navigation.Screen
import com.example.donex.ui.components.AppDrawer
import com.example.donex.ui.components.BottomNavBar
import com.example.donex.viewmodel.MainViewModel
import com.example.donex.viewmodel.MapViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    mainViewModel: MainViewModel,
    mapViewModel: MapViewModel,
    navController: NavController
) {
    val posts by mainViewModel.posts.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val auth = Firebase.auth

    var hasLocationPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-8.0476, -34.8770), 12f)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            AppDrawer(
                onProfileClick = {
                    scope.launch {
                        drawerState.close()
                        val currentUid = auth.currentUser?.uid ?: ""
                        navController.navigate("profile/$currentUid")
                    }
                },
                onChatListClick = {
                    scope.launch {
                        drawerState.close()
                        navController.navigate("chat_list")
                    }
                },
                onLogoutClick = {
                    scope.launch {
                        auth.signOut()
                        drawerState.close()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }
                },
                onCloseClick = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Mapa de Doações") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = { BottomNavBar(navController) }
        ) { padding ->
            GoogleMap(
                modifier = Modifier.fillMaxSize().padding(padding),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission)
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
}