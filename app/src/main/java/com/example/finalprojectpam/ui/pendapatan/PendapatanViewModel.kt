package com.example.finalprojectpam.ui.pendapatan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PendapatanRepository
import com.example.finalprojectpam.model.Pendapatan
import kotlinx.coroutines.launch

sealed class DapatUiState {
    data class Success(val dapat: List<Pendapatan>) : DapatUiState()
    data class Error(val message: String) : DapatUiState()
    object Loading : DapatUiState()
}

class PendapatanViewModel (private val pendapatanRepository: PendapatanRepository) : ViewModel() {

    var dapatUiState: DapatUiState by mutableStateOf(DapatUiState.Loading)
        private set

    init {
        getPendapatan()
    }

    fun getPendapatan() {
        viewModelScope.launch {
            dapatUiState = DapatUiState.Loading
            dapatUiState = try {
                val dapatList = pendapatanRepository.getPendapatan()
                DapatUiState.Success(dapatList)
            } catch (e: Exception) {
                DapatUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }


    fun deletePendapatan(id_pendapatan: String) {
        viewModelScope.launch {
            try {
                pendapatanRepository.deletePendapatan(id_pendapatan)
                getPendapatan() // Refresh data
            } catch (e: Exception) {
                dapatUiState = DapatUiState.Error(e.localizedMessage ?: "Failed to delete asset")
            }
        }
    }
}