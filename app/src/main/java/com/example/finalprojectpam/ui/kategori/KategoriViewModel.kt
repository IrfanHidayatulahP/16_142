package com.example.finalprojectpam.ui.kategori

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalprojectpam.Repository.KategoriRepository
import com.example.finalprojectpam.model.Kategori
import kotlinx.coroutines.launch

sealed class KatUiState {
    data class Success(val kategori: List<Kategori>) : KatUiState()
    data class Error(val message: String) : KatUiState()
    object Loading : KatUiState()
}

class KategoriViewModel (private val kategoriRepository: KategoriRepository) : ViewModel() {
    var katUiState: KatUiState by mutableStateOf(KatUiState.Loading)
        private set

    init {
        getKategori()
    }

    fun getKategori() {
        viewModelScope.launch {
            katUiState = KatUiState.Loading
            katUiState = try {
                val katList = kategoriRepository.getKategori()
                KatUiState.Success(katList)
            } catch (e: Exception) {
                KatUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }


    fun deleteKategori(id_kategori: String) {
        viewModelScope.launch {
            try {
                kategoriRepository.deleteKategori(id_kategori)
                getKategori() // Refresh data
            } catch (e: Exception) {
                katUiState = KatUiState.Error(e.localizedMessage ?: "Failed to delete asset")
            }
        }
    }
}