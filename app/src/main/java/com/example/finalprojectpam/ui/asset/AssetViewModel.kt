package com.example.finalprojectpam.ui.asset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.AsetRepository
import com.example.finalprojectpam.model.Aset
import kotlinx.coroutines.launch

sealed class HomeUiState {
    data class Success(val aset: List<Aset>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    object Loading : HomeUiState()
}

class AssetViewModel(private val asetRepository: AsetRepository) : ViewModel() {
    var asetUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getAset()
    }

    fun getAset() {
        viewModelScope.launch {
            asetUiState = HomeUiState.Loading
            asetUiState = try {
                val asetList = asetRepository.getAset()
                HomeUiState.Success(asetList)
            } catch (e: Exception) {
                HomeUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }


    fun deleteAset(id_aset: String) {
        viewModelScope.launch {
            try {
                asetRepository.deleteAset(id_aset)
                getAset() // Refresh data
            } catch (e: Exception) {
                asetUiState = HomeUiState.Error(e.localizedMessage ?: "Failed to delete asset")
            }
        }
    }
}
