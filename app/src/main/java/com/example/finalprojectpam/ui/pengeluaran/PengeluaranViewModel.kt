package com.example.finalprojectpam.ui.pengeluaran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.PengeluaranRepository
import com.example.finalprojectpam.model.Pendapatan
import com.example.finalprojectpam.model.Pengeluaran
import com.example.finalprojectpam.ui.pendapatan.DapatUiState
import kotlinx.coroutines.launch

sealed class KeluarUiState {
    data class Success(val keluar: List<Pengeluaran>) : KeluarUiState()
    data class Error(val message: String) : KeluarUiState()
    object Loading : KeluarUiState()
}

class PengeluaranViewModel (private val pengeluaranRepository: PengeluaranRepository) : ViewModel() {

    var keluarUiState: KeluarUiState by mutableStateOf(KeluarUiState.Loading)
        private set

    init {
        getPengeluaran()
    }

    fun getPengeluaran() {
        viewModelScope.launch {
            keluarUiState = KeluarUiState.Loading
            keluarUiState = try {
                val keluarList = pengeluaranRepository.getPengeluaran()
                KeluarUiState.Success(keluarList)
            } catch (e: Exception) {
                KeluarUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }


    fun deletePendapatan(id_pengeluaran: String) {
        viewModelScope.launch {
            try {
                pengeluaranRepository.deletePengeluaran(id_pengeluaran)
                getPengeluaran() // Refresh data
            } catch (e: Exception) {
                keluarUiState = KeluarUiState.Error(e.localizedMessage ?: "Failed to delete asset")
            }
        }
    }
}