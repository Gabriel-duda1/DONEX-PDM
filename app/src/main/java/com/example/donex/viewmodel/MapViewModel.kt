package com.example.donex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.donex.model.DonationPoint
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    // Localização inicial: Marco Zero, Recife
    private val recifeCenter = LatLng(-8.0631, -34.8711)

    // Estado da câmera do mapa
    private val _cameraPositionState = MutableStateFlow(
        CameraPositionState(
            position = CameraPosition.fromLatLngZoom(recifeCenter, 12f)
        )
    )
    val cameraPositionState = _cameraPositionState.asStateFlow()

    // Lista de pontos de doação
    private val _points = MutableStateFlow<List<DonationPoint>>(emptyList())
    val points = _points.asStateFlow()

    init {
        loadDonationPoints()
    }

    private fun loadDonationPoints() {
        viewModelScope.launch {
            // No futuro, você pode buscar isso do Firebase usando um MapRepository.
            // Por enquanto, vamos criar pontos fixos em Recife para teste:
            val mockPoints = listOf(
                DonationPoint("1", "ONG CatLovers", "Doação de ração", -8.0476, -34.8770),
                DonationPoint("2", "Ponto de Coleta - Derby", "Roupas e agasalhos", -8.0542, -34.8813),
                DonationPoint("3", "Banco de Alimentos", "Alimentos não perecíveis", -8.0667, -34.8912)
            )
            _points.value = mockPoints
        }
    }

    // Função para mover a câmera para um ponto específico (ex: quando clicar num post)
    fun moveToLocation(latLng: LatLng) {
        _cameraPositionState.value.position = CameraPosition.fromLatLngZoom(latLng, 15f)
    }
}