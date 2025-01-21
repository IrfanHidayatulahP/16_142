package com.example.finalprojectpam.ui.asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.AsetRepository
import com.example.finalprojectpam.model.Aset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    data class Success(val aset: List<Aset>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    object Loading : HomeUiState()
}

class AssetViewModel(private val asetRepository: AsetRepository) : ViewModel() {
    private val _asetUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val asetUiState: StateFlow<HomeUiState> = _asetUiState.asStateFlow()

    init {
        getAset()
    }

    fun getAset() {
        viewModelScope.launch {
            _asetUiState.value = HomeUiState.Loading
            _asetUiState.value = try {
                HomeUiState.Success(asetRepository.getAset())
            } catch (e: Exception) {
                HomeUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun deleteAset(idAset: String) {
        viewModelScope.launch {
            try {
                asetRepository.deleteAset(idAset)
                getAset() // Refresh data
            } catch (e: Exception) {
                _asetUiState.value = HomeUiState.Error(e.localizedMessage ?: "Failed to delete asset")
            }
        }
    }
}
