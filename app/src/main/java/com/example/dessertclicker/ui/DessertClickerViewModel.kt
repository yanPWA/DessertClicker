package com.example.dessertclicker.ui

import DessertClickerState
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertClickerViewModel : ViewModel() {
    private val desserts = Datasource.dessertList
    private val _uiState = MutableStateFlow(DessertClickerState())
    val uiState: StateFlow<DessertClickerState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(
                currentDessertPrice = desserts[uiState.value.currentDessertIndex].price,
                currentDessertImageId = desserts[uiState.value.currentDessertIndex].imageId
            )
        }
    }

    // 収益の更新
    fun updateRevenue() {
        val revenue = uiState.value.revenue.plus(uiState.value.currentDessertPrice)
        val dessertsSold = uiState.value.dessertsSold.inc()
        _uiState.update { currentState ->
            currentState.copy(
                revenue = revenue,
                dessertsSold = dessertsSold
            )
        }
    }

    // どのデザートを表示するか
    private fun determineDessertIndex(dessertsSold: Int): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                break
            }
        }
        return dessertToShow
    }

    // 次のデザートを表示
    fun showNextDessert() {
        val dessertToShow = determineDessertIndex(uiState.value.dessertsSold)

        _uiState.update { currentState ->
            currentState.copy(
                currentDessertImageId = dessertToShow.imageId,
                currentDessertPrice = dessertToShow.price
            )
        }
    }
}